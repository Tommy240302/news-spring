package com.ptit.news.dto;

import com.ptit.news.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private Long id;
    private String content;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.content =category.getContent();
    }
}
