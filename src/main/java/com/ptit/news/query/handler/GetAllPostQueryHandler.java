package com.ptit.news.query.handler;

import com.ptit.news.dto.NewsResponse;
import com.ptit.news.query.dto.GetAllPostQuery;
import com.ptit.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllPostQueryHandler {
    private final NewsRepository newsRepository;

    @QueryHandler
    public List<NewsResponse> handle(GetAllPostQuery query) {
        return newsRepository.findAll().stream()
                .map(NewsResponse::new)
                .toList();
    }
}
