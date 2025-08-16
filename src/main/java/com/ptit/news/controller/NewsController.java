package com.ptit.news.controller;

import com.ptit.news.command.dto.CountViewCommand;
import com.ptit.news.common.Response;
import com.ptit.news.dto.CommentResponse;
import com.ptit.news.dto.NewsResponse;
import com.ptit.news.entity.News;
import com.ptit.news.query.dto.GetAllCommentApprovedByNewsId;
import com.ptit.news.query.dto.GetAllPostQuery;
import com.ptit.news.query.dto.GetPostByIdQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ptit.news.repository.UserRepository;
import com.ptit.news.repository.NewsRepository;
import com.ptit.news.entity.User;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequiredArgsConstructor
public class NewsController extends AdvancedBaseController {

    private final UserRepository userRepository;
    private final NewsRepository newsRepository;
    
    @GetMapping
    public Response<List<NewsResponse>> getAllNews() {
        List<NewsResponse> news = queryGateway.query(
                new GetAllPostQuery(),
                org.axonframework.messaging.responsetypes.ResponseTypes.multipleInstancesOf(NewsResponse.class)
        ).join();
        return Response.Success(news, "Lấy danh sách bài báo thành công.");
    }

    @GetMapping("/{newsId}")
    public Response<NewsResponse> getNewsById(@PathVariable Long newsId) {
        NewsResponse newsResponse = queryGateway.query(
                GetPostByIdQuery.builder().id(newsId).build(),
                ResponseTypes.instanceOf(NewsResponse.class)
        ).join();
        return Response.Success(newsResponse, "Lấy tin tức thành công");
    }

    @GetMapping("/comment/{newsId}")
    public Response<List<CommentResponse>> getAllCommentApprovedByNewsId(@PathVariable Long newsId) {
        List<CommentResponse> commentResponses = queryGateway.query(
                new GetAllCommentApprovedByNewsId(newsId),
                ResponseTypes.multipleInstancesOf(CommentResponse.class)
        ).join();
        return Response.Success(commentResponses, "Lấy comment thành công");
    }

    @PatchMapping("/addView")
    public Response<String> addView(@RequestBody CountViewCommand command) {
        return executeCommand(command);
    }

    @GetMapping("/my-news")
    public Response<List<NewsResponse>> getMyNews(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        List<NewsResponse> myNews = newsRepository.findByAuthor(user).stream()
                .filter(news -> !news.getIsDeleted())
                .map(NewsResponse::new)
                .toList();
        return Response.Success(myNews, "Lấy bài viết của bạn thành công");
                
    }

}
