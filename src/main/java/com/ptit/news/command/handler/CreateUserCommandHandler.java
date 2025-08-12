package com.ptit.news.command.handler;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.axonframework.commandhandling.CommandHandler;
import com.ptit.news.command.dto.CreateUserCommand;
import com.ptit.news.entity.User;
import com.ptit.news.entity.Role;
import com.ptit.news.repository.UserRepository;
import com.ptit.news.repository.RoleRepository;
import com.ptit.news.common.Response;
import com.ptit.news.exception.DuplicateResourceException;
import com.ptit.news.common.enums.UserRole; // Import UserRole

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime; // Import LocalDateTime
import java.util.Set;
import java.util.Optional; // Import Optional

@Slf4j
@Component
public class CreateUserCommandHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @CommandHandler
    public Response<User> handle(CreateUserCommand command) {

        // Kiểm tra email đã tồn tại và không bị xóa mềm
        if (userRepository.findByEmailAndIsDeletedFalse(command.getEmail()).isPresent()) {
            throw new DuplicateResourceException("User", "email", command.getEmail());
        }

        // Tìm role mặc định
        Role defaultRole = roleRepository.findByName(UserRole.READER.getValue())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role READER"));

        // Tạo user mới
        User user = User.builder()
                .email(command.getEmail())
                .password(passwordEncoder.encode(command.getPassword()))
                .firstName(command.getFirstName())
                .lastName(command.getLastName())
                .phone(command.getPhone())
                .avatar(command.getAvatar())
                .isEnabled(true)
                .roles(Set.of(defaultRole))
                .build();
        user.setIsDeleted(false); // Đảm bảo đặt isDeleted
        user.setCreatedAt(LocalDateTime.now()); // Đảm bảo đặt createdAt

        User savedUser = userRepository.save(user);

        return Response.Success(savedUser, "Tạo tài khoản thành công");
    }
}