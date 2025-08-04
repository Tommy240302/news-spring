package com.ptit.news.repository;

import com.ptit.news.entity.News;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByCategory_SlugIgnoreCase(String slug);

}
