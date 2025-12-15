package com.ptit.news.dto;

import com.ptit.news.common.enums.DataType;
import com.ptit.news.entity.NewsCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewsCategoryDTO {

    private Long id;
    private CategoryResponse category;
    private DataType dataType;
    private boolean selected;

    public NewsCategoryDTO(NewsCategory newsCategory) {
        this.id = newsCategory.getId();
        this.category = new CategoryResponse(newsCategory.getCategory());
        this.dataType = newsCategory.getDataType();
        this.selected = newsCategory.isSelected();
    }
}
