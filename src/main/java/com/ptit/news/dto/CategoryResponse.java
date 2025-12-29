package com.ptit.news.dto;

import com.ptit.news.entity.Category;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {
    private Long id;
    private String content;
    private Long parentId;
    private Boolean isDeleted;
    private List<CategoryResponse> children;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.content = category.getContent();
        this.isDeleted = category.getIsDeleted();
        this.parentId = category.getParent() != null ? category.getParent().getId() : null;
    }

    public static CategoryResponse fromEntityWithChildren(Category category) {
        if (category == null) return null;

        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setContent(category.getContent());
        response.setParentId(category.getParent() != null ? category.getParent().getId() : null);
        response.setIsDeleted(category.getIsDeleted());

        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            response.setChildren(category.getChildren().stream()
                    .filter(child -> !Boolean.TRUE.equals(child.getIsDeleted()))
                    .map(CategoryResponse::fromEntityWithChildren)
                    .toList());
        }

        return response;
    }
}