package com.xha.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xha.domain.ResponseResult;
import com.xha.domain.dto.AddCommentDto;
import com.xha.domain.entity.Comment;
import org.springframework.stereotype.Service;

/**
 * @author Tony贾维斯
 * @description 针对表【sg_comment(评论表)】的数据库操作Service
 * @createDate 2022-11-09 13:22:08
 */

public interface CommentService extends IService<Comment> {

    ResponseResult getCommentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}
