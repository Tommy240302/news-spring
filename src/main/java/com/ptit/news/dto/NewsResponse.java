package com.ptit.news.dto;

import com.ptit.news.entity.News;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsResponse {
    private Long id;
    private String title;
    private String summary;
    private String image;
    private Integer view;
    private boolean status;
    private String content;
    private Long authorId;
    private Date publishedAt;

    public NewsResponse(News news) {
        this.id = news.getId();
        this.title = news.getTitle();
        this.summary = news.getSummary();
        this.content = news.getContent();
        this.image = news.getImage();
        this.view = news.getViews();
        this.status = news.isStatus();
        this.authorId = news.getAuthor().getId();
        this.publishedAt = news.getPublishedAt();
    }

}
