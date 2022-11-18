package com.xha.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminArticleDto {

    private Long categoryId;

    private String content;

    private Long id;

    private String isComment;

    private String isTop;

    private String status;

    private String summary;

    private List<Long> tags;

    private String thumbnail;

    private String title;

    private Long viewCount;
}
