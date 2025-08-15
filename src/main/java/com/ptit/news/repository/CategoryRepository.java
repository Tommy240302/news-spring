package com.ptit.news.repository;

import com.ptit.news.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Tìm kiếm category theo content và không bị xóa
    Optional<Category> findByContentAndIsDeletedFalse(String content);

    // Tìm tất cả các category không bị xóa, sắp xếp theo content
    List<Category> findAllByIsDeletedFalseOrderByContentAsc();

    // Tìm tất cả các category cấp cao nhất (không có parent) và không bị xóa
    List<Category> findByParentIsNullAndIsDeletedFalseOrderByContentAsc();

    // Kiểm tra sự tồn tại của category theo content và không bị xóa
    boolean existsByContentAndIsDeletedFalse(String content);

    // Tìm category theo ID và không bị xóa
    Optional<Category> findByIdAndIsDeletedFalse(Long id);

    // Phương thức MỚI: Tìm category theo ID và ĐÃ BỊ XÓA MỀM (cần cho chức năng khôi phục)
    Optional<Category> findByIdAndIsDeletedTrue(Long id); // <-- Đã thêm dòng này
}
