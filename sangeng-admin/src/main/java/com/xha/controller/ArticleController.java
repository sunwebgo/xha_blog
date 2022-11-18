package com.xha.controller;

import com.xha.domain.ResponseResult;
import com.xha.domain.dto.AddArticleDto;
import com.xha.domain.dto.AdminArticleDto;
import com.xha.domain.dto.ArticleDto;
import com.xha.domain.vo.ArticleDetailsVo;
import com.xha.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    /**
     * 查询博客列表
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param articleDto 文章dto
     * @return {@link ResponseResult}
     */
    @GetMapping("/list")
    public ResponseResult getAllArticleList(Integer pageNum, Integer pageSize, ArticleDto articleDto) {
        return articleService.getAllArticleList(pageNum, pageSize, articleDto);
    }

    /**
     * 通过id获取博客
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @GetMapping("/{id}")
    public ResponseResult getArticleById(@PathVariable Long id){
        return articleService.getArticleById(id);
    }




    /**
     * 删除博客
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable Long id){
        return articleService.deleteArticle(id);
    }

    /**
     * 添加博客
     *
     * @param articleDto 文章dto
     * @return {@link ResponseResult}
     */
    @PostMapping
    public ResponseResult addArticle(@RequestBody AddArticleDto articleDto){
        return articleService.addArticle(articleDto);
    }

    /**
     * 修改博客
     *
     * @param adminArticleDto 文章细节签证官
     * @return {@link ResponseResult}
     */
    @PutMapping
    public ResponseResult updateArticle(@RequestBody AdminArticleDto adminArticleDto){
        return articleService.updateArticle(adminArticleDto);
    }





}
