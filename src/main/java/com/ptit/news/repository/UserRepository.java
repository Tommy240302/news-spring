package com.ptit.news.repository;

import com.ptit.news.entity.User;
import com.ptit.news.dto.AuthorRevenueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndIsDeletedFalse(Long id);
    Optional<User> findByEmailAndIsDeletedFalse(String email);
    Optional<User> findByEmail(String email);
    Page<User> findAllByIsDeletedFalse(Pageable pageable);

    // Phương thức MỚI: Tìm kiếm người dùng theo email, firstName, hoặc lastName
    // và đảm bảo người dùng chưa bị xóa mềm.
    // Sử dụng 'ContainingIgnoreCase' để tìm kiếm không phân biệt chữ hoa/thường
    // và 'AndIsDeletedFalse' để lọc người dùng chưa bị xóa mềm.
    Page<User> findByEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseAndIsDeletedFalse(
            String email, String firstName, String lastName, Pageable pageable); // <-- Thêm dòng này

    @Query("SELECT new com.ptit.news.dto.AuthorRevenueDTO(u.id, u.email, u.firstName, u.lastName, SUM(p.amount)) " +
            "FROM User u JOIN Payment p ON p.author.id = u.id " +
            "WHERE u.isDeleted = false " +
            "GROUP BY u.id, u.email, u.firstName, u.lastName " +
            "ORDER BY SUM(p.amount) DESC")
    List<AuthorRevenueDTO> findTopAuthorsByRevenue();

    // --- Các phương thức mới cho Dashboard ---

    // Đếm tổng số người dùng không bị xóa
    long countByIsDeletedFalse();

    // Đếm số người dùng được tạo sau một thời điểm nhất định và không bị xóa
    long countByCreatedAtAfterAndIsDeletedFalse(LocalDateTime createdAt);
    List<User> findByRoles_Name(String roleName);
}
