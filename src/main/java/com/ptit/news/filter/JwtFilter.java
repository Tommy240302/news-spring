package com.ptit.news.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.ptit.news.service.JwtService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    private final HandlerExceptionResolver exceptionResolver;

    // Những path KHÔNG kiểm tra JWT (prefix match bằng startsWith)
    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/api/auth",            // /api/auth, /api/auth/...
            "/api/otp/send",
            "/api/master-data",
            "/swagger-ui",
            "/v3/api-docs",
            "/api/news/public",
            "/api/categories"       // /api/categories, /api/categories/...
    );

    private boolean isExcluded(String path) {
        // startsWith để match cả path gốc và các sub-path
        for (String prefix : EXCLUDED_PATHS) {
            if (path.startsWith(prefix)) return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            // BỎ QUA HOÀN TOÀN PRELIGHT CORS
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                filterChain.doFilter(request, response);
                return;
            }

            final String path = request.getServletPath();

            // Bỏ qua các path public
            if (isExcluded(path)) {
                filterChain.doFilter(request, response);
                return;
            }

            // Lấy token từ header
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                // Không có token → để Security xử lý rule (nếu endpoint yêu cầu auth thì sẽ bị chặn ở layer trên)
                filterChain.doFilter(request, response);
                return;
            }

            final String jwt = authHeader.substring(7);
            final String username = jwtService.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // Đẩy lỗi về GlobalExceptionHandler qua resolver
            exceptionResolver.resolveException(request, response, null, e);
        }
    }
}
