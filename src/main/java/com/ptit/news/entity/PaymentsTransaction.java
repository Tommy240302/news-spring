package com.ptit.news.entity;

import com.ptit.news.common.enums.PaymentsStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payments_transaction")
public class PaymentsTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String jsonData;

    @CreationTimestamp
    private LocalDateTime createDate;

    @Enumerated(EnumType.STRING)
    private PaymentsStatus status;

    @OneToMany(mappedBy = "transactionId")
    private List<Payment> payments;

}
