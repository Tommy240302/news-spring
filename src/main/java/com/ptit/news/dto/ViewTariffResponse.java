package com.ptit.news.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewTariffResponse {
    private Long id;
    private Long minView;
    private Long maxView;
    private Double pricePerView;
    private String description;
}
