package com.xha.controller;

import com.xha.domain.ResponseResult;
import com.xha.service.ArticleService;
import com.xha.utils.RedisCache;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.xha.constants.RedisConstants.ARTICLE_VIEWCOUNT;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    /**
     * 热门文章查询
     * @return
     */
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList() {
//        查询热门文章
        return articleService.hotArticleList();
    }

    /**
     *文章列表查询
     */
    @GetMapping("/articleList")
    public ResponseResult articleList(Long categoryId,Integer pageNum,Integer pageSize){
        return articleService.articleList(categoryId,pageNum,pageSize);
    }

    /**
     * 获取文章详情
     */
    @GetMapping("/{id}")
    public ResponseResult getArticleDetails(@PathVariable("id") Integer id ){
        return articleService.getArticleDetails(id);
    }

    /**
     * 更新文章数量
     * @param id
     * @return
     */
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable Long id){
        return articleService.updateViewCount(id);
    }
}
