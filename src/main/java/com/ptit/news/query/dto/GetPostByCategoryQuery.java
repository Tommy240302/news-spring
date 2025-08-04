package com.ptit.news.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetPostByCategoryQuery {
    private String slug;
}
