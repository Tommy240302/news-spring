package com.ptit.news.service;

import com.ptit.news.dto.NewsDTO;
import com.ptit.news.common.Response;
import com.ptit.news.common.enums.StatusResponse;
import com.ptit.news.entity.Category;
import com.ptit.news.entity.News;
import com.ptit.news.entity.User;
import com.ptit.news.repository.CategoryRepository;
import com.ptit.news.repository.NewsRepository;
import com.ptit.news.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminNewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private NewsDTO convertToDto(News news) {
        NewsDTO dto = new NewsDTO();
        dto.setId(news.getId());
        dto.setTitle(news.getTitle());
        dto.setSummary(news.getSummary());
        dto.setContent(news.getContent());
        dto.setImage(news.getImage());
        dto.setStatus(news.isStatus());
        dto.setViews(news.getViews());
        dto.setPublishedAt(news.getPublishedAt());
        dto.setDeleted(news.getIsDeleted());

        if (news.getAuthor() != null) {
            dto.setAuthorId(news.getAuthor().getId());
            dto.setAuthorName(news.getAuthor().getFirstName() + " " + news.getAuthor().getLastName());
        }

        if (news.getCategory() != null) {
            dto.setCategoryId(news.getCategory().getId());
            dto.setCategoryName(news.getCategory().getContent());
        }

        return dto;
    }

    /**
     * Lấy tất cả tin tức với phân trang, tìm kiếm theo tiêu đề (nếu có)
     */
    public Page<NewsDTO> getAllNews(String searchTerm, Pageable pageable) {
        Page<News> newsPage = StringUtils.hasText(searchTerm)
                ? newsRepository.findByTitleContainingIgnoreCaseAndIsDeletedFalse(searchTerm, pageable)
                : newsRepository.findAllByIsDeletedFalse(pageable);

        return newsPage.map(this::convertToDto);
    }

    /**
     * Lấy chi tiết tin tức theo ID
     */
    public NewsDTO getNewsById(Long id) {
        News news = newsRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("News not found or is deleted"));
        return convertToDto(news);
    }

    /**
     * Tạo mới một tin tức
     */
    @Transactional
    public NewsDTO createNews(NewsDTO newsDTO) {
        News news = new News();
        news.setTitle(newsDTO.getTitle());
        news.setSummary(newsDTO.getSummary());
        news.setContent(newsDTO.getContent());
        news.setImage(newsDTO.getImage());
        news.setStatus(false);
        news.setViews(0);
        news.setPublishedAt(null);
        news.setIsDeleted(false);

        User author = userRepository.findById(newsDTO.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found"));
        news.setAuthor(author);

        Category category = categoryRepository.findById(newsDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        news.setCategory(category);

        return convertToDto(newsRepository.save(news));
    }

    /**
     * Cập nhật tin tức theo ID
     */
    @Transactional
    public NewsDTO updateNews(Long id, NewsDTO newsDTO) {
        News existingNews = newsRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("News not found or is deleted"));

        existingNews.setTitle(newsDTO.getTitle());
        existingNews.setSummary(newsDTO.getSummary());
        existingNews.setContent(newsDTO.getContent());
        existingNews.setImage(newsDTO.getImage());
        existingNews.setStatus(newsDTO.isStatus());

        // Cập nhật tác giả nếu thay đổi
        if (newsDTO.getAuthorId() != null &&
                (existingNews.getAuthor() == null || !existingNews.getAuthor().getId().equals(newsDTO.getAuthorId()))) {

            User author = userRepository.findById(newsDTO.getAuthorId())
                    .orElseThrow(() -> new RuntimeException("Author not found"));
            existingNews.setAuthor(author);
        }

        // Cập nhật danh mục nếu thay đổi
        if (newsDTO.getCategoryId() != null &&
                (existingNews.getCategory() == null || !existingNews.getCategory().getId().equals(newsDTO.getCategoryId()))) {

            Category category = categoryRepository.findById(newsDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existingNews.setCategory(category);
        }

        // Cập nhật ngày xuất bản nếu thay đổi trạng thái
        if (!existingNews.isStatus() && newsDTO.isStatus()) {
            existingNews.setPublishedAt(new Date());
        } else if (existingNews.isStatus() && !newsDTO.isStatus()) {
            existingNews.setPublishedAt(null);
        }

        return convertToDto(newsRepository.save(existingNews));
    }

    /**
     * Xóa mềm tin tức
     */
    @Transactional
    public void deleteNews(Long id) {
        News news = newsRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("News not found or is deleted"));
        news.setIsDeleted(true);
        newsRepository.save(news);
    }

    /**
     * Lấy danh sách tin tức đang chờ duyệt
     */
    public Response<List<NewsDTO>> getPendingNews() {
        try {
            List<News> pendingNews = newsRepository.findByStatusAndIsDeleted(false, false);
            List<NewsDTO> newsDTOs = pendingNews.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            return Response.Success(newsDTOs, "Successfully retrieved pending news.");
        } catch (Exception e) {
            return Response.Error("Failed to retrieve pending news: " + e.getMessage());
        }
    }

    /**
     * Duyệt bài viết
     */
    @Transactional
    public Response<NewsDTO> approveNews(Long newsId) {
        try {
            Optional<News> optionalNews = newsRepository.findById(newsId);
            if (optionalNews.isEmpty()) {
                return Response.Error("News not found.");
            }

            News news = optionalNews.get();
            if (news.getIsDeleted()) {
                return Response.Error("Cannot approve a soft-deleted news article.");
            }

            news.setStatus(true);
            news.setPublishedAt(new Date());
            News updatedNews = newsRepository.save(news);

            return Response.Success(convertToDto(updatedNews), "News approved successfully.");
        } catch (Exception e) {
            return Response.Error("Failed to approve news: " + e.getMessage());
        }
    }

    /**
     * Từ chối bài viết (hủy duyệt)
     */
    @Transactional
    public Response<NewsDTO> rejectNews(Long newsId) {
        try {
            Optional<News> optionalNews = newsRepository.findById(newsId);
            if (optionalNews.isEmpty()) {
                return Response.Error("News not found.");
            }

            News news = optionalNews.get();
            if (news.getIsDeleted()) {
                return Response.Error("Cannot reject a soft-deleted news article.");
            }

            news.setStatus(false);
            news.setPublishedAt(null);
            News updatedNews = newsRepository.save(news);

            return Response.Success(convertToDto(updatedNews), "News rejected successfully.");
        } catch (Exception e) {
            return Response.Error("Failed to reject news: " + e.getMessage());
        }
    }
}
