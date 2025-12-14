package com.ptit.news.config;

import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.http.HttpMethod;

import com.ptit.news.filter.JwtFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    @NonFinal
    private final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/**",
            "/api/otp/send",
            "/api/master-data/**",
            "/health",
            "/init",
            "/api/public/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/news/**",
            "/api/categories/**",     // Cho phép public categories (nếu muốn private thì bỏ dòng này)
            "/admin/author-requests",
    };

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(exceptionResolver);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // BẬT CORS và tắt CSRF (REST, stateless)
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)

            // Phân quyền
            .authorizeHttpRequests(auth -> auth
                // Quan trọng: cho phép tất cả OPTIONS (preflight) để CORS không bị chặn
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Các endpoint public
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()

                // Các endpoint yêu cầu đăng nhập
                .requestMatchers(HttpMethod.POST,"/api/author/create").hasRole("AUTHOR") // Chỉ author mới được đăng bài
                .requestMatchers("/api/users/me").authenticated()
                .requestMatchers("/api/**").authenticated()
                .requestMatchers("/api/users/change-password").authenticated()
                                // .requestMatchers("/admin/**").hasRole("ADMIN") // Chỉ admin mới được truy cập
                
                // Mặc định cho phép
                .anyRequest().permitAll()
            )

            // Stateless session
            .sessionManagement(ssm -> ssm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Provider + Filter
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Dùng allowedOriginPatterns để linh hoạt (nhiều port dev khác nhau)
        // Có thể thay bằng setAllowedOrigins(Arrays.asList("http://localhost:5173","http://localhost:5174"))
        config.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:*",
                "http://127.0.0.1:*"
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setExposedHeaders(Arrays.asList("Authorization", "Location")); // nếu cần đọc header phía client
        config.setAllowCredentials(true); // nếu gửi cookie/token qua CORS
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
