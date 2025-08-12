package com.ptit.news.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {
    private Long id;
    private String title;
    private String summary;
    private String content;
    private String image;
    private boolean status;
    private int views;
    private Date publishedAt;
    private boolean isDeleted;
    private Date createdAt;
    private Date updatedAt;
    private Long authorId;
    private String authorName;
    private Long categoryId;
    private String categoryName;

    // Constructor để phù hợp với JPQL query trong AdminNewsService
    public NewsDTO(Long id, String title, String summary, String content, String image, int views, boolean status,
                   Date publishedAt, boolean isDeleted, Date createdAt, Date updatedAt,
                   Long authorId, String authorName, Long categoryId, String categoryName) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.image = image;
        this.views = views;
        this.status = status;
        this.publishedAt = publishedAt;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.authorId = authorId;
        this.authorName = authorName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
