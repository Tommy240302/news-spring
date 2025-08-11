package com.ptit.news.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.*;

@Table(name = "users", indexes = { @Index(name = "idx_email", columnList = "email") })
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails {
    @Column(length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 100)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    private Date dateOfBirth;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @Column(length = 20)
    private String phone;

    private String paymentNumber;

    private String avatar;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "email_user"), inverseJoinColumns = @JoinColumn(name = "name_role"))
    private Set<Role> roles;

    @Override
    @Transactional
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
        if (this.roles != null){
            this.roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())));
        }
        
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    public User(String email) {
        this.email = email;
    }

    public User(Long id) {
        this.setId(id);
    }
}