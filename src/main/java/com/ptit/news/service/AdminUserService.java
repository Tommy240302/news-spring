package com.ptit.news.service;

import com.ptit.news.dto.UserDTO;
import com.ptit.news.repository.UserRepository;
import com.ptit.news.repository.RoleRepository; // Cần thêm RoleRepository
import com.ptit.news.entity.Role;
import com.ptit.news.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder; // Cần thêm PasswordEncoder
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils; // <-- Thêm import này

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository; // Inject RoleRepository
    @Autowired
    private PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    // Chuyển đổi Entity User sang UserDTO
    private UserDTO convertToDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setIsEnabled(user.getIsEnabled());
        dto.setPhone(user.getPhone());
        dto.setAvatar(user.getAvatar());
        dto.setIsDeleted(user.getIsDeleted());
        if (user.getRoles() != null) {
            dto.setRoles(user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet()));
        }
        return dto;
    }

    // Chuyển đổi UserCreateUpdateDTO sang Entity User (dùng cho tạo/cập nhật)
    private User convertToEntity(UserDTO.UserCreateUpdateDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        // Mã hóa mật khẩu khi tạo mới hoặc cập nhật mật khẩu
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setIsEnabled(dto.getIsEnabled() != null ? dto.getIsEnabled() : true); // Mặc định là enabled
        user.setPhone(dto.getPhone());
        user.setAvatar(dto.getAvatar());
        user.setIsDeleted(false); // Mặc định không bị xóa

        if (dto.getRoleNames() != null && !dto.getRoleNames().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : dto.getRoleNames()) {
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                roles.add(role);
            }
            user.setRoles(roles);
        } else {
            // Gán vai trò mặc định nếu không có vai trò nào được chỉ định, ví dụ 'USER'
            Role defaultRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Default 'USER' role not found. Please ensure it exists."));
            user.setRoles(Set.of(defaultRole));
        }

        return user;
    }

    // Đã sửa đổi phương thức này để chấp nhận searchTerm
    public Page<UserDTO> getAllUsers(String searchTerm, Pageable pageable) { // <-- Thêm tham số searchTerm
        Page<User> userPage;
        if (StringUtils.hasText(searchTerm)) {
            // Nếu có searchTerm, tìm kiếm theo email, firstName, hoặc lastName và chưa bị xóa mềm
            userPage = userRepository.findByEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseAndIsDeletedFalse(
                    searchTerm, searchTerm, searchTerm, pageable);
        } else {
            // Nếu không có searchTerm, lấy tất cả người dùng chưa bị xóa mềm
            userPage = userRepository.findAllByIsDeletedFalse(pageable);
        }
        return userPage.map(this::convertToDto);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("User not found or is deleted"));
        return convertToDto(user);
    }

    @Transactional
    public UserDTO createUser(UserDTO.UserCreateUpdateDTO userDTO) {
        // Kiểm tra xem email đã tồn tại chưa
        if (userRepository.findByEmailAndIsDeletedFalse(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + userDTO.getEmail());
        }

        User user = convertToEntity(userDTO);
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO.UserCreateUpdateDTO userDTO) {
        User existingUser = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("User not found or is deleted"));

        // Cập nhật các trường
        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        existingUser.setDateOfBirth(userDTO.getDateOfBirth());
        existingUser.setIsEnabled(userDTO.getIsEnabled());
        existingUser.setPhone(userDTO.getPhone());
        existingUser.setAvatar(userDTO.getAvatar());

        // Cập nhật mật khẩu nếu có
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        // Cập nhật vai trò
        if (userDTO.getRoleNames() != null) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : userDTO.getRoleNames()) {
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                roles.add(role);
            }
            existingUser.setRoles(roles);
        }

        User updatedUser = userRepository.save(existingUser);
        return convertToDto(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("User not found or is deleted"));
        user.setIsDeleted(true); // Soft delete
        userRepository.save(user);
    }
}
