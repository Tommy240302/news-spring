package com.ptit.news.dto;

import com.ptit.news.entity.Payment;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {

    private Double amount;
    private Long authorId;
    private String authorName;
    private Integer viewPaid;
    private Long transactionId;
    private LocalDateTime paymentDate;

    public PaymentDTO(Payment payment) {
        this.amount = payment.getAmount();
        this.authorId = payment.getAuthor() != null ? payment.getAuthor().getId() : null;
        this.authorName = payment.getAuthor() != null
                ? payment.getAuthor().getFirstName() + " " + payment.getAuthor().getLastName()
                : "Không xác định";
        this.viewPaid = payment.getViewCurrent();

        // 👇 Fix NullPointerException tại đây
        this.transactionId = (payment.getTransactionId() != null)
                ? payment.getTransactionId().getId()
                : null;

        this.paymentDate = payment.getCreatedAt() != null
                ? payment.getCreatedAt()
                : LocalDateTime.now(); // fallback nếu null
    }
}
