package com.ptit.news.dto;

import com.ptit.news.entity.Payment;
import lombok.*;

import java.time.LocalDate;
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
            this.authorId = payment.getAuthor().getId();
            this.authorName = payment.getAuthor().getFirstName() + " "+ payment.getAuthor().getLastName();
            this.viewPaid = payment.getViewCurrent();
            this.transactionId = payment.getTransactionId().getId();
            this.paymentDate = payment.getCreatedAt();
        }
}
