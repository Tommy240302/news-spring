package com.ptit.news.controller;

import com.ptit.news.common.Response;
import com.ptit.news.dto.CommentResponse;
import com.ptit.news.dto.NewsResponse;
import com.ptit.news.query.dto.GetAllCommentApprovedByNewsId;
import com.ptit.news.query.dto.GetAllPostQuery;
import com.ptit.news.query.dto.GetPostByIdQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequiredArgsConstructor
public class NewsController extends AdvancedBaseController {
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
}
