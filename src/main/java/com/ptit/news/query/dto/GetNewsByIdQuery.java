package com.ptit.news.query.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetNewsByIdQuery {
    private Long id;
}
