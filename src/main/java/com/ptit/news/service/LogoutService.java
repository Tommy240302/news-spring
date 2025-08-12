package com.ptit.news.service;

import com.ptit.news.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders; // Import HttpHeaders

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        // Lấy token từ header Authorization
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return; // Không có token hoặc không đúng định dạng, không làm gì cả
        }

        jwt = authHeader.substring(7); // Bỏ qua "Bearer "

        // Tìm kiếm token trong cơ sở dữ liệu
        var storedToken = tokenRepository.findByCode(jwt);

        if (storedToken.isPresent()) {
            // Đánh dấu token là đã đăng xuất và lưu lại
            storedToken.get().setIsSignOut(true);
            tokenRepository.save(storedToken.get());
        }
    }
}