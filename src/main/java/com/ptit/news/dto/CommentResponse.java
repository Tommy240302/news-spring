package com.ptit.news.dto;

import com.ptit.news.entity.Comment;
import com.ptit.news.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private String content;
    private boolean isApproved;
    private UserResponse user;
    private LocalDateTime createAt;

    public CommentResponse(Comment comment) {
        this.content = comment.getContent();
        this.isApproved = comment.getIsApproved();
        this.user = new UserResponse(comment.getUser());
        this.createAt = comment.getCreatedAt();
    }
}
