package com.ptit.news.repository;

import com.ptit.news.entity.Payment;
import com.ptit.news.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    List<Payment> findByAuthor(User author);

    
    Optional<Payment> findFirstByAuthorOrderByCreatedAtDesc(User author);
}
