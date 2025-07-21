package com.ptit.news.command.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewsCommand {
    private String title;
    private String summary;
    private String content;
    private boolean status;
    private String image;
    private Date publishedAt;
    private Long categoryId;
}
