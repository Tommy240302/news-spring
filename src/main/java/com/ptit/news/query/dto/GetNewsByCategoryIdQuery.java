package com.ptit.news.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetNewsByCategoryIdQuery {
    private Long categoryId;
}
