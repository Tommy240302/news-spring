package com.ptit.news.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
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

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    @JsonIgnore
    private PaymentsTransaction transactionId;


}
