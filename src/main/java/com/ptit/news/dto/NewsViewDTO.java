package com.ptit.news.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor // Lombok sẽ tạo constructor này
@Builder
public class NewsViewDTO {
    private Long newsId;
    private String newsTitle;
    private Integer views;

    // Xóa constructor thủ công này:
    // public NewsViewDTO(Long newsId, String newsTitle, Integer views) {
    //     this.newsId = newsId;
    //     this.newsTitle = newsTitle;
    //     this.views = views;
    // }
}