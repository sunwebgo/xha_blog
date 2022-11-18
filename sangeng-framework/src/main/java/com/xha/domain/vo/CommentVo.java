package com.xha.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVo {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 根评论id
     */
    private Long rootId;

    /**
     * 评论人名字
     */
    private String username;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 回复目标评论的用户id
     */
    private Long toCommentUserId;

    /**
     * 回复目标评论的评论id
     */
    private Long toCommentId;

    /**
     * 回复目标评论的用户名
     */
    private String toCommentUserName;

    /**
     *根评论的子评论
     */
    private List<CommentVo> children;

    /**
     *当前评论人的id
     */
    private Long createBy;

    /**
     *创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


}
