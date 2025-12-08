package com.ptit.news.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("paymentNumber")
    private String paymentNumber;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("viewCurrent")
    private Integer viewCurrent;
}
