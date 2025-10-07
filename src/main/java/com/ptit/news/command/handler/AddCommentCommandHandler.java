package com.ptit.news.command.handler;

import com.ptit.news.command.dto.AddCommentCommand;
import com.ptit.news.common.Response;
import com.ptit.news.dto.CommentResponse;
import com.ptit.news.entity.Comment;
import com.ptit.news.entity.News;
import com.ptit.news.entity.User;
import com.ptit.news.repository.CommentRepository;
import com.ptit.news.repository.NewsRepository;
import com.ptit.news.repository.UserRepository;
import lombok.extern.java.Log;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class AddCommentCommandHandler {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private RestTemplate restTemplate;

    @CommandHandler
    public Response<CommentResponse> handle(AddCommentCommand command) {
        String url = "http://localhost:8000/toxic-comment-detect";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElse(null);
        Comment parrentComment = new Comment();
        if (command.getParentCommentId()!=null) {
            parrentComment = commentRepository.findById(command.getParentCommentId()).orElse(null);
            if (parrentComment == null) {
                return Response.Error("Không tìm thấy Comment cha");
            }
        }
        else {
            parrentComment = null;
        }
        if (user == null) {
            return Response.Error("Không tìm thấy authencation");
        }

        News news = newsRepository.findById(command.getNewsId()).orElse(null);
        if (news == null) {
            return Response.Error("Không tìm thấy tin tức");
        }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("comment", command.getComment());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

        Map<String, Object> body = response.getBody();
        Boolean isToxic = (Boolean) body.get("is_toxic");
        if (isToxic) {
            Comment comment = Comment.builder()
                    .labelAI("Toxic")
                    .content(command.getComment())
                    .user(user)
                    .isApproved(false)
                    .news(news)
                    .parent(parrentComment)
                    .build();
            commentRepository.save(comment);
            return Response.Success(new CommentResponse(comment), "Đã ghi nhận comment");
        }
        Comment comment = Comment.builder()
                .labelAI("Normal")
                .content(command.getComment())
                .user(user)
                .isApproved(true)
                .news(news)
                .parent(parrentComment)
                .build();
        commentRepository.save(comment);
        return Response.Success(new CommentResponse(comment), "Đã ghi nhận comment");
    }
}


