package com.ptit.news.repository;

import com.ptit.news.entity.News;
import com.ptit.news.entity.User;
import com.ptit.news.dto.NewsViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;


@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    // Phương thức để lấy danh sách bài viết của một tác giả cụ thể (phục vụ chức năng thanh toán)
    List<News> findByAuthor(User author);

    // Phương thức từ nhánh Dai
    List<News> findByStatus(Boolean status);

    // Phương thức đã được sửa lỗi từ nhánh Dai
    List<News> findByStatusAndIsDeleted(Boolean status, boolean isDeleted);
    Page<News> findAllByIsDeletedFalse(Pageable pageable);

    Optional<News> findByIdAndIsDeletedFalse(Long id);

    // Phương thức tìm kiếm theo tiêu đề từ nhánh Dai
    Page<News> findByTitleContainingIgnoreCaseAndIsDeletedFalse(String title, Pageable pageable);

    // Truy vấn tùy chỉnh từ nhánh Dai
    @Query("SELECT new com.ptit.news.dto.NewsViewDTO(n.id, n.title, n.views) " +
            "FROM News n " +
            "WHERE n.isDeleted = false " +
            "ORDER BY n.views DESC")
    List<NewsViewDTO> findTopNewsByViews();
    long countByIsDeletedFalse();

    long countByCreatedAtAfterAndIsDeletedFalse(LocalDateTime createdAt);

    // Phương thức từ nhánh production
    List<News> findByCategory_Id(Long categoryId);

    List<News> findByCategory_IdIn(List<Long> categoryIds);

    @Query("SELECT c.id FROM Category c WHERE c.id = :parentId OR c.parent.id = :parentId")
    List<Long> findAllCategoryIdsByParent(@Param("parentId") Long parentId);

}
