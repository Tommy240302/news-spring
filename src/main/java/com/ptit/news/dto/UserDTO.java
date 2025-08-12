package com.ptit.news.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private Boolean isEnabled;
    private String phone;
    private String avatar;
    private Set<String> roles; // Tên vai trò, ví dụ: "ADMIN", "EDITOR", "USER"
    private Boolean isDeleted; // Thêm trường isDeleted để hiển thị trạng thái xóa mềm

    // DTO để nhận dữ liệu khi tạo/cập nhật người dùng (có thể bao gồm mật khẩu)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserCreateUpdateDTO {
        private String email;
        private String password; // Chỉ nên được set khi tạo hoặc đổi mật khẩu
        private String firstName;
        private String lastName;
        private Date dateOfBirth;
        private Boolean isEnabled;
        private String phone;
        private String avatar;
        private Set<String> roleNames; // Tên các vai trò để gán cho người dùng
    }
}