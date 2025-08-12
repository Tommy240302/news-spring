package com.ptit.news.controller;

import com.ptit.news.common.Response;
import com.ptit.news.dto.AuthorRevenueDTO;
import com.ptit.news.dto.NewsViewDTO;
import com.ptit.news.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/statistics") // Endpoint cho thống kê, dành cho Admin
@RequiredArgsConstructor
public class StatisticController extends AdvancedBaseController { // Kế thừa AdvancedBaseController

    private final StatisticService statisticService;

    /**
     * Lấy danh sách các tác giả có doanh thu cao nhất.
     * Endpoint: GET /api/admin/statistics/top-authors-by-revenue
     *
     * @return ResponseEntity chứa danh sách AuthorRevenueDTO.
     */
    @GetMapping("/top-authors-by-revenue")
    public ResponseEntity<Response<List<AuthorRevenueDTO>>> getTopAuthorsByRevenue() {
        List<AuthorRevenueDTO> topAuthors = statisticService.getTopAuthorsByRevenue();
        return success(topAuthors, "Lấy danh sách tác giả có doanh thu cao nhất thành công.");
    }

    /**
     * Lấy danh sách các bài viết có lượt xem cao nhất.
     * Endpoint: GET /api/admin/statistics/top-news-by-views
     *
     * @return ResponseEntity chứa danh sách NewsViewDTO.
     */
    @GetMapping("/top-news-by-views")
    public ResponseEntity<Response<List<NewsViewDTO>>> getTopNewsByViews() {
        List<NewsViewDTO> topNews = statisticService.getTopNewsByViews();
        return success(topNews, "Lấy danh sách bài viết có lượt xem cao nhất thành công.");
    }
}