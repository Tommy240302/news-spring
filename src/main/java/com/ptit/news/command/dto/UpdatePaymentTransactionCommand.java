package com.ptit.news.command.dto;

import com.ptit.news.common.enums.PaymentsStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePaymentTransactionCommand {
    private Long id;
    private PaymentsStatus status;
}
