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
    private Long id;
    private String content;
    private boolean isApproved;
    private UserResponse user;
    private Long parentCommentId;
    private LocalDateTime createAt;

    public CommentResponse(Comment comment) {
        this.id  = comment.getId();
        this.content = comment.getContent();
        this.isApproved = comment.getIsApproved();
        this.user = new UserResponse(comment.getUser());
        this.parentCommentId = comment.getParent() == null?null:comment.getParent().getId();
        this.createAt = comment.getCreatedAt();
    }
}
