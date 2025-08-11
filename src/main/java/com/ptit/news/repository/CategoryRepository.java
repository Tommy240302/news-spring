package com.ptit.news.repository;

import com.ptit.news.entity.Category;
import com.ptit.news.entity.News;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByContent(String content);
}