package com.ptit.news.query.handler;

import com.ptit.news.dto.CommentResponse;
import com.ptit.news.entity.Comment;
import com.ptit.news.exception.InvalidRequestException;
import com.ptit.news.query.dto.GetAllCommentApprovedByNewsId;
import com.ptit.news.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GetAllCommentApprovedByNewsIdHandler {

    @Autowired
    private CommentRepository commentRepository;

    @QueryHandler
    public List<CommentResponse> handle(GetAllCommentApprovedByNewsId query) {
        try {
            return commentRepository.findByNewsId(query.getNewsId()).stream()
                    .filter(Comment::getIsApproved)
                    .map(CommentResponse::new)
                    .toList();
        } catch (Exception e) {
            log.error("Lỗi khi lấy comment: {}", e.getMessage(), e);
            throw new InvalidRequestException("Không thể lấy danh sách comment");
        }
    }
}
