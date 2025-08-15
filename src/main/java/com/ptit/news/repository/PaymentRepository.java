package com.ptit.news.repository;

import com.ptit.news.entity.Payment;
import com.ptit.news.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository để tương tác với bảng payments.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    /**
     * Tìm tất cả các giao dịch của một tác giả.
     * @param author Tác giả
     * @return Danh sách các giao dịch thanh toán
     */
    List<Payment> findByAuthor(User author);

    /**
     * Tìm giao dịch gần nhất của một tác giả, sắp xếp theo thời gian tạo giảm dần.
     * @param author Tác giả
     * @return Optional của giao dịch gần nhất
     */
    Optional<Payment> findFirstByAuthorOrderByCreatedAtDesc(User author);
}
