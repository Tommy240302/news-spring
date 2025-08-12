package com.ptit.news.dto;

import com.ptit.news.common.enums.RequestAuthorStatus;
import com.ptit.news.entity.RequestAuthor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestAuthorResponse {
    private Long id;
    private String userEmail; // <-- Đã hợp nhất
    private String profileUrl;
    private String sampleArticles;
    private String reason;
    private String paymentNumber;
    private RequestAuthorStatus status;

    public RequestAuthorResponse(RequestAuthor requestAuthor) {
        this.id = requestAuthor.getId();
        this.userEmail = requestAuthor.getUser() != null ? requestAuthor.getUser().getEmail() : null; // <-- Đã hợp nhất
        this.profileUrl = requestAuthor.getProfileUrl();
        this.sampleArticles = requestAuthor.getSampleArticles();
        this.reason = requestAuthor.getReason();
        this.status = requestAuthor.getStatus();
        this.paymentNumber = requestAuthor.getPaymentNumber();
    }
}