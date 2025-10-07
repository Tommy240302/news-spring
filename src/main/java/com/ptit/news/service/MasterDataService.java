package com.ptit.news.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import com.ptit.news.entity.Role;
import com.ptit.news.entity.User;
import com.ptit.news.repository.RoleRepository;
import com.ptit.news.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import com.ptit.news.common.enums.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.time.LocalDateTime;
import java.util.Date;
import com.ptit.news.repository.CategoryRepository;
import com.ptit.news.entity.Category;

@Slf4j
@Service
public class MasterDataService implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeUsers();
        initializeCategories();
    }

    /**
     * Khởi tạo các role cơ bản
     */
    private void initializeRoles() {
        log.info("Starting to initialize master data...");

        for (UserRole userRole : UserRole.values()) {
            String roleName = userRole.getValue();
            if (!roleRepository.existsByName(roleName)) {
                Role role = Role.builder()
                        .name(roleName)
                        .build();

                roleRepository.save(role);
                log.info("Created role: {}", roleName);
            } else {
                log.info("Role already exists: {}", roleName);
            }
        }

        log.info("Master data initialization completed!");
    }

    /**
     * Khởi tạo các tài khoản mặc định
     */
    private void initializeUsers() {
        log.info("Starting to initialize default users...");

        // Tài khoản 1: reader@gmail.com - chỉ có quyền READER
        if (userRepository.findByEmailAndIsDeletedFalse("reader@gmail.com").isEmpty()) {
            Role readerRole = roleRepository.findByName(UserRole.READER.getValue()).orElse(null);
            if (readerRole != null) {
                Set<Role> readerRoles = new HashSet<>();
                readerRoles.add(readerRole);

                User readerUser = User.builder()
                        .email("reader@gmail.com")
                        .password(passwordEncoder.encode("123456"))
                        .firstName("Reader")
                        .lastName("User")
                        .dateOfBirth(new Date())
                        .isEnabled(true)
                        .phone("111222333")
                        .avatar("reader_avatar.png")
                        .roles(readerRoles)
                        .build();
                readerUser.setIsDeleted(false); // Sửa lỗi: Gán isDeleted bằng setter
                readerUser.setCreatedAt(LocalDateTime.now()); // Sửa lỗi: Gán createdAt bằng setter

                userRepository.save(readerUser);
                log.info("Created reader user: reader@gmail.com");
            } else {
                log.error("READER role not found, cannot create reader user");
            }
        } else {
            log.info("Reader user already exists: reader@gmail.com");
        }

        // Tài khoản 2: writer@gmail.com - có quyền READER và WRITER
        if (userRepository.findByEmailAndIsDeletedFalse("writer@gmail.com").isEmpty()) {
            Role readerRole = roleRepository.findByName(UserRole.READER.getValue()).orElse(null);
            Role authorRole = roleRepository.findByName(UserRole.AUTHOR.getValue()).orElse(null);

            if (readerRole != null && authorRole != null) {
                Set<Role> authorRoles = new HashSet<>();
                authorRoles.add(readerRole);
                authorRoles.add(authorRole);

                User authorUser = User.builder()
                        .email("writer@gmail.com")
                        .password(passwordEncoder.encode("123456"))
                        .firstName("Writer")
                        .lastName("User")
                        .dateOfBirth(new Date())
                        .isEnabled(true)
                        .phone("444555666")
                        .avatar("writer_avatar.png")
                        .roles(authorRoles)
                        .build();
                authorUser.setIsDeleted(false); // Sửa lỗi: Gán isDeleted bằng setter
                authorUser.setCreatedAt(LocalDateTime.now()); // Sửa lỗi: Gán createdAt bằng setter

                userRepository.save(authorUser);
                log.info("Created writer user: writer@gmail.com");
            } else {
                log.error("Required roles not found, cannot create writer user");
            }
        } else {
            log.info("Writer user already exists: writer@gmail.com");
        }

        // Tài khoản 3: admin@gmail.com - có quyền READER và ADMIN
        if (userRepository.findByEmailAndIsDeletedFalse("admin1@gmail.com").isEmpty()) {
            Role readerRole = roleRepository.findByName(UserRole.READER.getValue()).orElse(null);
            Role adminRole = roleRepository.findByName(UserRole.ADMIN.getValue()).orElse(null);

            if (readerRole != null && adminRole != null) {
                Set<Role> adminRoles = new HashSet<>();
                adminRoles.add(readerRole);
                adminRoles.add(adminRole);

                User adminUser = User.builder()
                        .email("admin1@gmail.com")
                        .password(passwordEncoder.encode("123456"))
                        .firstName("Admin")
                        .lastName("User")
                        .dateOfBirth(new Date())
                        .isEnabled(true)
                        .phone("777888999")
                        .avatar("admin_avatar.png")
                        .roles(adminRoles)
                        .build();
                adminUser.setIsDeleted(false); // Sửa lỗi: Gán isDeleted bằng setter
                adminUser.setCreatedAt(LocalDateTime.now()); // Sửa lỗi: Gán createdAt bằng setter

                userRepository.save(adminUser);
                log.info("Created admin user: admin@gmail.com");
            } else {
                log.error("Required roles not found, cannot create admin user");
            }
        } else {
            log.info("Admin user already exists: admin@gmail.com");
        }

        log.info("Default users initialization completed!");
    }

    /**
     * Khởi tạo category mẫu tiếng Việt
     */
    private void initializeCategories() {
        log.info("Starting to initialize default categories...");
        String[] categories = {
                "Thời sự", "Thể thao", "Kinh tế", "Giải trí", "Giáo dục",
                "Công nghệ", "Sức khỏe", "Du lịch", "Pháp luật", "Văn hóa","Thế giới","Chính trị - Xã hội"
        };
        for (String cat : categories) {
            if (!categoryRepository.existsByContentAndIsDeletedFalse(cat)) {
                Category category = new Category();
                category.setContent(cat);
                category.setParent(null);
                category.setIsDeleted(false);
                category.setCreatedAt(LocalDateTime.now());
                categoryRepository.save(category);
                log.info("Created category: {}", cat);
            } else {
                log.info("Category already exists: {}", cat);
            }
        }
        log.info("Default categories initialization completed!");
    }

    /**
     * Khởi tạo role cụ thể
     */
    public void createRole(String roleName) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = Role.builder()
                    .name(roleName)
                    .build();

            roleRepository.save(role);
            log.info("Created role: {}", roleName);
        } else {
            log.warn("Role already exists: {}", roleName);
        }
    }

    /**
     * Lấy tất cả roles
     */
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * Kiểm tra role có tồn tại không
     */
    public boolean roleExists(String roleName) {
        return roleRepository.existsByName(roleName);
    }
}