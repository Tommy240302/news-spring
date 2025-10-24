package com.ptit.news.command.handler;

import com.ptit.news.command.dto.ChangePasswordCommand;
import com.ptit.news.common.Response;
import com.ptit.news.entity.User;
import com.ptit.news.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChangePasswordCommandHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @CommandHandler
    public Response<String> handle(ChangePasswordCommand command) {
        try {
            User user = userRepository.findByEmail(command.getEmail()).orElse(null);
            if (user == null) {
                return Response.Error("Không tìm thấy người dùng với email: " + command.getEmail());
            }

            // Kiểm tra mật khẩu cũ
            if (!passwordEncoder.matches(command.getOldPassword(), user.getPassword())) {
                return Response.Error("Mật khẩu cũ không chính xác!");
            }

            // Đặt mật khẩu mới
            user.setPassword(passwordEncoder.encode(command.getNewPassword()));
            userRepository.save(user);

            return Response.Success(null, "Đổi mật khẩu thành công!");
        } catch (Exception e) {
            log.error("Lỗi khi đổi mật khẩu: {}", e.getMessage(), e);
            return Response.Error("Có lỗi xảy ra khi đổi mật khẩu!");
        }
    }
}
