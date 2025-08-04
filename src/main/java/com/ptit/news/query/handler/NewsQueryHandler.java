package com.ptit.news.query.handler;

import com.ptit.news.dto.NewsResponse;
import com.ptit.news.entity.News;
import com.ptit.news.mapper.NewsMapper;
import com.ptit.news.query.dto.GetAllPostQuery;
import com.ptit.news.query.dto.GetPostByCategoryQuery;
import com.ptit.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NewsQueryHandler {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    @QueryHandler
    public List<NewsResponse> handle(GetAllPostQuery query) {
        List<News> newsList = newsRepository.findAll();

        return newsList.stream()
                .filter(news -> !news.isDeleted())
                .map(newsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @QueryHandler
    public List<NewsResponse> handle(GetPostByCategoryQuery query) {
        List<News> newsList = newsRepository.findByCategory_SlugIgnoreCase(query.getSlug());

        return newsList.stream()
                .filter(news -> !news.isDeleted())
                .map(newsMapper::toResponse)
                .collect(Collectors.toList());
    }

}
