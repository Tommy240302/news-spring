package com.ptit.news.query.handler;

import com.ptit.news.dto.NewsResponse;
import com.ptit.news.entity.News;
import com.ptit.news.exception.InvalidRequestException;
import com.ptit.news.query.dto.GetPostByIdQuery;
import com.ptit.news.repository.NewsRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class GetPostByIdHandler {

    @Autowired
    private NewsRepository newsRepository;

    @QueryHandler
    public NewsResponse handle(GetPostByIdQuery query) {
        Optional<News> news = newsRepository.findById(query.getId());
        if (news.isPresent() && news.get().isStatus()) {
            return new NewsResponse(news.get());
        }
        else {
            throw new InvalidRequestException("Không tìm thấy tin tức");
        }
    }
}
