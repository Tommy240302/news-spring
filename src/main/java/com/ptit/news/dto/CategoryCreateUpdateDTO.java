package com.ptit.news.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateUpdateDTO {
    @NotBlank(message = "Category content cannot be empty")
    private String content;
    private Long parentId; // ID của category cha, có thể là null nếu là category gốc
}