package com.ptit.news.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewTariffRequest {

    @NotNull(message = "Minimum view is required")
    @Min(value = 0, message = "Minimum view must be at least 0")
    private Long minView;

    @NotNull(message = "Maximum view is required")
    @Min(value = 0, message = "Maximum view must be at least 0")
    private Long maxView;

    @NotNull(message = "Price per view is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double pricePerView;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
}
