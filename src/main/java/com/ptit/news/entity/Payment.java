package com.ptit.news.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "payments")
public class Payment extends BaseEntity {
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;
    @Column(name = "image_pm")
    private String imagePm;

    @Column(name = "content_pm")
    private String contentPm;

    @Column(name = "view_current")
    private Integer viewCurrent;
}
