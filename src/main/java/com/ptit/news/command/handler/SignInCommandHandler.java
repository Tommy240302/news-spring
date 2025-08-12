package com.ptit.news.command.handler;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.axonframework.commandhandling.CommandHandler;
import com.ptit.news.command.dto.SignInCommand;
import com.ptit.news.command.dto.AuthResponse; // Đảm bảo AuthResponse là một DTO phù hợp
import com.ptit.news.dto.UserResponse; // Đảm bảo UserResponse là một DTO phù hợp
import com.ptit.news.entity.User;
import com.ptit.news.entity.Token;
import com.ptit.news.repository.UserRepository;
import com.ptit.news.repository.TokenRepository;
import com.ptit.news.service.JwtService; // Giả định JwtService có generateToken
import com.ptit.news.common.Response; // Giả định Response.Success tồn tại
import org.springframework.security.authentication.BadCredentialsException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime; // Import LocalDateTime
import java.util.UUID; // Import UUID nếu sử dụng cho refresh token

@Slf4j
@Component
public class SignInCommandHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @CommandHandler
    public Response<AuthResponse> handle(SignInCommand command) {
        try {
            String email = command.getEmail();
            String password = command.getPassword();
            Boolean isRemember = command.getIsRemember() != null && "true".equalsIgnoreCase(command.getIsRemember());

            // Xác thực bằng AuthenticationManager
            // AuthenticationManager sẽ gọi UserDetailsService, nơi đã dùng findByEmailAndIsDeletedFalse
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

            // Lấy user sau khi xác thực thành công
            // Dùng findByEmailAndIsDeletedFalse để đảm bảo chỉ lấy user đang hoạt động
            User user = userRepository.findByEmailAndIsDeletedFalse(email) // Sửa lỗi ở đây
                    .orElseThrow(() -> new BadCredentialsException("User not found or account is disabled/deleted."));

            // Kiểm tra user có enabled không (mặc dù UserDetailsService đã kiểm tra isEnabled)
            if (!user.getIsEnabled()) {
                throw new BadCredentialsException("Tài khoản đã bị khóa.");
            }

            // Tạo JWT token
            String accessToken = jwtService.generateToken(user, isRemember);

            log.info("Generated access token: {}", accessToken);

            // Lưu token vào database
            saveToken(accessToken, user);

            AuthResponse authResponse = AuthResponse.builder()
                    .token(accessToken)
                    .refreshToken(null) // Nếu bạn không dùng refresh token, có thể bỏ qua
                    .user(new UserResponse(user)) // Đảm bảo UserResponse constructor nhận User entity
                    .build();

            return Response.Success(authResponse, "Đăng nhập thành công");

        } catch (BadCredentialsException e) {
            log.warn("Authentication failed for email {}: {}", command.getEmail(), e.getMessage());
            throw e; // Ném lại ngoại lệ BadCredentialsException
        } catch (Exception e) {
            log.error("Error during sign in for email {}: {}", command.getEmail(), e.getMessage(), e);
            throw new RuntimeException("Lỗi khi đăng nhập: " + e.getMessage()); // Ném RuntimeException chung
        }
    }

    private void saveToken(String code, User user) {
        Token token = Token.builder()
                .code(code)
                .isSignOut(false)
                .user(user)
                .build();
        token.setCreatedAt(LocalDateTime.now()); // Đảm bảo đặt createdAt cho Token

        try {
            tokenRepository.save(token);
            log.info("Token saved successfully for user: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Error saving token for user {}: {}", user.getEmail(), e.getMessage(), e);
        }
    }
}