package com.ptit.news.query.handler;

import com.ptit.news.dto.NewsResponse;
import com.ptit.news.entity.News;
import com.ptit.news.query.dto.GetNewsByCategoryIdQuery;
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

    @QueryHandler
    public List<NewsResponse> handle(GetNewsByCategoryIdQuery query) {
        List<News> newsList = newsRepository.findByCategory_Id(query.getCategoryId());
        return newsList.stream()
                .map(NewsResponse::new) // Dùng constructor NewsResponse(News news)
                .collect(Collectors.toList());
    }
}
