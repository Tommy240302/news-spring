package com.ptit.news.controller;

import com.ptit.news.common.Response;
import com.ptit.news.dto.NewsResponse;
import com.ptit.news.query.dto.GetAllPostQuery;
import lombok.RequiredArgsConstructor;
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
}
