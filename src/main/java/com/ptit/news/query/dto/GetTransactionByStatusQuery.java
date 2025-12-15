package com.ptit.news.query.dto;

import com.ptit.news.common.enums.PaymentsStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetTransactionByStatusQuery {
    private PaymentsStatus status;
}
