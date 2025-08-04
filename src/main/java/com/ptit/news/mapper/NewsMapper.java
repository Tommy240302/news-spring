package com.ptit.news.mapper;

import com.ptit.news.dto.NewsResponse;
import com.ptit.news.entity.News;
import org.springframework.stereotype.Component;

@Component
public class NewsMapper {

    public NewsResponse toResponse(News news) {
        if (news == null) return null;

        return NewsResponse.builder()
                .id(news.getId())
                .title(news.getTitle())
                .summary(news.getSummary())
                .image(news.getImage())
                .content(news.getContent())
                .status(news.isStatus())
                .view(news.getViews())
                .publishedAt(news.getPublishedAt())
                .authorName(getAuthorFullName(news))
                .build();
    }

    private String getAuthorFullName(News news) {
        if (news.getAuthor() == null) return null;

        String first = news.getAuthor().getFirstName() != null ? news.getAuthor().getFirstName() : "";
        String last = news.getAuthor().getLastName() != null ? news.getAuthor().getLastName() : "";
        return (first + " " + last).trim();
    }
}
