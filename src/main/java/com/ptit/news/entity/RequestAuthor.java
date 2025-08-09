package com.ptit.news.entity;

import com.ptit.news.common.enums.RequestAuthorStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "request_authors")
public class RequestAuthor extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String profileUrl;
    private String sampleArticles;
    private String reason;
    private String paymentNumber;

    @Enumerated(EnumType.STRING)
    private RequestAuthorStatus status;
}