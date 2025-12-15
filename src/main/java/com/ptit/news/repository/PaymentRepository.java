package com.ptit.news.repository;

import com.ptit.news.entity.Payment;
import com.ptit.news.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    List<Payment> findByAuthor(User author);

    
    Optional<Payment> findFirstByAuthorOrderByCreatedAtDesc(User author);

    @Query(value = """
            SELECT
                n.author_id,
                CONCAT(u.first_name, ' ', u.last_name) AS author_name,
                u.email,
                u.payment_number,
                SUM(n.views) AS total_views,
                COALESCE(p.total_view_current, 0) AS views_already_paid,
                SUM(n.views) - COALESCE(p.total_view_current, 0) AS unpaid_views
            FROM news n
            JOIN users u ON n.author_id = u.id
            LEFT JOIN (
                SELECT
                    user_id,
                    SUM(view_current) AS total_view_current
                FROM payments
                WHERE is_deleted = 0
                GROUP BY user_id
            ) p ON p.user_id = n.author_id
            WHERE n.is_deleted = 0
            GROUP BY n.author_id, u.first_name, u.last_name, u.email, p.total_view_current
            ORDER BY unpaid_views DESC;
            """, nativeQuery = true)
    List<Object[]> calculateSalary();

}
