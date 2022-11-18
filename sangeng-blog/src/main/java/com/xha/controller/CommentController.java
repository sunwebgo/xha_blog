package com.xha.controller;

import com.xha.domain.ResponseResult;
import com.xha.domain.dto.AddCommentDto;
import com.xha.domain.entity.Comment;
import com.xha.service.CommentService;
import com.xha.utils.BeanCopyPropertiesUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.xha.constants.CommonConstants.ARTICLE_COMMENT;
import static com.xha.constants.CommonConstants.LINK_COMMENT;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论", description = "评论相关接口")
public class CommentController {

    @Resource
    private CommentService commentService;

    /**
     * 查询文章评论
     *
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/commentList")
    @ApiOperation(value = "查询评论", notes = "获取文章评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId",value = "文章id"),
            @ApiImplicitParam(name = "pageNum",value = "当前页"),
            @ApiImplicitParam(name = "pageSize",value = "每页显示的条数")
        }
    )
    public ResponseResult getCommentList(Long articleId, Integer pageNum, Integer pageSize) {
        return commentService.getCommentList(ARTICLE_COMMENT, articleId, pageNum, pageSize);
    }

    /**
     * 添加评论
     *
     * @param addCommentDto
     * @return
     */
    @PostMapping("/addComment")
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto) {
//        将AddCommentDto类型转换为Comment类型
        Comment comment = BeanCopyPropertiesUtils.copyBean(addCommentDto, Comment.class);
        return commentService.addComment(comment);
    }

    /**
     * 查询友链评论
     *
     * @return
     */
    @GetMapping("/linkCommentList")
    public ResponseResult linkCommentList(Integer pageNum, Integer pageSize) {
        return commentService.getCommentList(LINK_COMMENT, null, pageNum, pageSize);
    }
}
