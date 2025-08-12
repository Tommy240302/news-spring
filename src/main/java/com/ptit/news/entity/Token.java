package com.ptit.news.entity;

import lombok.*;
import jakarta.persistence.*;

@Table(name = "tokens")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true) // Rất quan trọng: Bao gồm các trường của BaseEntity
public class Token extends BaseEntity { // Quan trọng: Kế thừa BaseEntity

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private Boolean isSignOut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_user") // Đảm bảo rằng cột này tồn tại trong cơ sở dữ liệu của bạn
    private User user;
}