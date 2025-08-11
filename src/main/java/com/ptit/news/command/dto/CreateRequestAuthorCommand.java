package com.ptit.news.command.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRequestAuthorCommand {
    private String profileUrl;
    private String sampleArticles;
    private String reason;
    private String paymentNumber;
}
