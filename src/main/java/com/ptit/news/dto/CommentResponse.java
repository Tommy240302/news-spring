package com.ptit.news.dto;

import com.ptit.news.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private String content;
    private boolean isApproved;

    public CommentResponse(Comment comment) {
        this.content = comment.getContent();
        this.isApproved = comment.getIsApproved();
    }
}
