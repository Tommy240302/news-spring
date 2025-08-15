package com.ptit.news.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.ptit.news.entity.User;
import java.util.Set;
import java.util.HashSet;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Boolean isEnabled;
    private Set<String> roles;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
        this.isEnabled = user.getIsEnabled();
        this.roles = user.getRoles() != null
                ? user.getRoles().stream().map(role -> role.getName()).collect(java.util.stream.Collectors.toSet())
                : new HashSet<>();
    }
}