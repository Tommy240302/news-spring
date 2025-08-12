package com.ptit.news.service;

import com.ptit.news.dto.DashboardSummaryDTO;
import com.ptit.news.repository.NewsRepository;
import com.ptit.news.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final NewsRepository newsRepository;
    private final UserRepository userRepository;

    /**
     * Lấy dữ liệu tổng quan cho Dashboard.
     *
     * @return DashboardSummaryDTO chứa các số liệu thống kê.
     */
    public DashboardSummaryDTO getDashboardSummary() {
        // Lấy tổng số bài viết không bị xóa
        long totalNewsCount = newsRepository.countByIsDeletedFalse();

        // Lấy tổng số người dùng không bị xóa
        long totalUserCount = userRepository.countByIsDeletedFalse();

        // Lấy thời điểm bắt đầu của ngày hôm nay (00:00:00)
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();

        // Lấy số lượng bài viết mới được tạo trong ngày hôm nay và không bị xóa
        long newsCountToday = newsRepository.countByCreatedAtAfterAndIsDeletedFalse(startOfDay);

        // Lấy số lượng người dùng mới được tạo trong ngày hôm nay và không bị xóa
        long userCountToday = userRepository.countByCreatedAtAfterAndIsDeletedFalse(startOfDay);

        // Xây dựng và trả về DashboardSummaryDTO
        return DashboardSummaryDTO.builder()
                .totalNewsCount(totalNewsCount)
                .totalUserCount(totalUserCount)
                .newsCountToday(newsCountToday)
                .userCountToday(userCountToday)
                .build();
    }
}