package com.ptit.news.repository;

import com.ptit.news.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByContent(String content);
    
    Optional<Category> findBySlug(String slug); 
}
