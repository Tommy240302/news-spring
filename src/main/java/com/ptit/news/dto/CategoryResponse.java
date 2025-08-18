package com.ptit.news.dto;

import com.ptit.news.entity.Category;
import lombok.Data;

@Data
public class CategoryResponse {
    private Long id;
    private String content;
    private Boolean isDeleted;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.content = category.getContent();
        this.isDeleted = category.getIsDeleted();
    }
}
