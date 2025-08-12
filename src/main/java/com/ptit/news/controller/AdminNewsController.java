package com.ptit.news.controller;

import com.ptit.news.dto.NewsDTO;
import com.ptit.news.common.Response;
import com.ptit.news.service.AdminNewsService;
import com.ptit.news.common.enums.StatusResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/admin/news")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
// @PreAuthorize("hasRole('ADMIN')")
public class AdminNewsController {

    @Autowired
    private AdminNewsService adminNewsService;

    /**
     * API: Lấy tất cả bài viết (phân trang, sắp xếp, tìm kiếm theo tiêu đề)
     */
    @GetMapping
    public ResponseEntity<Page<NewsDTO>> getAllNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort,
            @RequestParam(required = false) String searchTerm) {

        Sort sorting = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sorting);
        Page<NewsDTO> newsPage = adminNewsService.getAllNews(searchTerm, pageable);

        return ResponseEntity.ok(newsPage);
    }

    /**
     * API: Lấy bài viết theo ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<NewsDTO> getNewsById(@PathVariable Long id) {
        NewsDTO news = adminNewsService.getNewsById(id);
        return ResponseEntity.ok(news);
    }

    /**
     * API: Lấy danh sách bài viết đang chờ duyệt
     */
    @GetMapping("/pending")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<List<NewsDTO>>> getPendingNews() {
        Response<List<NewsDTO>> response = adminNewsService.getPendingNews();

        return response.getStatus() == StatusResponse.Success
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * API: Duyệt bài viết
     */
    @PutMapping("/{newsId}/approve")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response<NewsDTO>> approveNews(@PathVariable Long newsId) {
        Response<NewsDTO> response = adminNewsService.approveNews(newsId);

        return response.getStatus() == StatusResponse.Success
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * API: Xóa mềm bài viết
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        adminNewsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }
}
