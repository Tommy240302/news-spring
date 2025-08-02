package com.ptit.news.controller;

import com.ptit.news.common.Response;
import com.ptit.news.dto.CategoryResponse;
import com.ptit.news.dto.CommentResponse;
import com.ptit.news.dto.NewsResponse;
import com.ptit.news.query.dto.GetAllCategoryQuery;
import com.ptit.news.query.dto.GetAllCommentApprovedByNewsId;
import com.ptit.news.query.dto.GetPostByIdQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class PublicController extends AdvancedBaseController {

    @GetMapping("/categories")
    public Response<List<CategoryResponse>> getAllCategory() {
        List<CategoryResponse> categories = queryGateway.query(
                new GetAllCategoryQuery(),
                org.axonframework.messaging.responsetypes.ResponseTypes.multipleInstancesOf(CategoryResponse.class)
        ).join();
        return Response.Success(categories, "Lấy danh sách chuyên mục thành công.");
    }

    @GetMapping("/news/{newsId}")
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
