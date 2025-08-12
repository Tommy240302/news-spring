package com.ptit.news.dto;

import com.ptit.news.entity.Category;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class CategoryDTO {
    private Long id;
    private String content;
    private Long parentId; // ID của category cha
    private String parentContent; // Tên của category cha (nếu có)
    private List<CategoryDTO> children; // Danh sách category con
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;

    public static CategoryDTO fromEntity(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryDTO.builder()
                .id(category.getId())
                .content(category.getContent())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .parentContent(category.getParent() != null ? category.getParent().getContent() : null)
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .isDeleted(category.getIsDeleted())
                .build();
    }

    // Phương thức tĩnh để chuyển đổi một Category entity thành CategoryDTO, bao gồm cả children
    public static CategoryDTO fromEntityWithChildren(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDTO dto = fromEntity(category);
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            dto.setChildren(category.getChildren().stream()
                    .filter(child -> !child.getIsDeleted()) // Chỉ lấy các category con chưa bị xóa
                    .map(CategoryDTO::fromEntityWithChildren)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}