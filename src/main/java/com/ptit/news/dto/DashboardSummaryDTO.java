package com.ptit.news.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSummaryDTO {
    private Long totalNewsCount;
    private Long totalUserCount;
    private Long newsCountToday; // Số lượng bài viết mới trong ngày
    private Long userCountToday; // Số lượng người dùng mới đăng ký trong ngày
    // Bạn có thể thêm các trường khác nếu muốn:
    // private Long newsCountLast7Days;
    // private Long userCountLast7Days;
    // private List<NewsViewDTO> top3News; // Lấy từ StatisticService
    // private List<AuthorRevenueDTO> top3Authors; // Lấy từ StatisticService
}