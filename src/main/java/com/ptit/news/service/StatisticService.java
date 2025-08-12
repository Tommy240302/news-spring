package com.ptit.news.service;

import com.ptit.news.dto.AuthorRevenueDTO;
import com.ptit.news.dto.NewsViewDTO;
import com.ptit.news.repository.UserRepository;
import com.ptit.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // Lombok sẽ tự tạo constructor với các final fields
public class StatisticService {

    private final UserRepository userRepository;
    private final NewsRepository newsRepository;

    /**
     * Lấy danh sách các tác giả có doanh thu cao nhất.
     *
     * @return Danh sách AuthorRevenueDTO.
     */
    public List<AuthorRevenueDTO> getTopAuthorsByRevenue() {
        return userRepository.findTopAuthorsByRevenue();
    }

    /**
     * Lấy danh sách các bài viết có lượt xem cao nhất.
     *
     * @return Danh sách NewsViewDTO.
     */
    public List<NewsViewDTO> getTopNewsByViews() {
        return newsRepository.findTopNewsByViews();
    }
}