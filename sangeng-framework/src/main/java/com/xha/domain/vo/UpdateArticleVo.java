package com.xha.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xha.domain.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateArticleVo {
    private Long id;

    private String title;

    private String summary;

    private String thumbnail;

    private Long viewCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss",timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss",timezone = "GMT+8")
    private Date updateTime;

    private String isComment;

    private String updateBy;

    private String createBy;

    private String isTop;

    private String status;

    private Long categoryId;

    private String content;

    private Integer delFlag;

    private List<Long> tags;

}
