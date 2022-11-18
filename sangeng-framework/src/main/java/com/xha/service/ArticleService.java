package com.xha.service;

import com.xha.domain.ResponseResult;
import com.xha.domain.dto.AddArticleDto;
import com.xha.domain.dto.AdminArticleDto;
import com.xha.domain.dto.ArticleDto;
import com.xha.domain.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xha.domain.vo.ArticleDetailsVo;
import org.springframework.stereotype.Service;

/**
* @author Tony贾维斯
* @description 针对表【sg_article(文章表)】的数据库操作Service
* @createDate 2022-11-05 09:21:27
*/

public interface ArticleService extends IService<Article> {
    /**
     * 热门文章查询
     * @return
     */
    ResponseResult hotArticleList();

    /**
     *文章列表查询
     */
    ResponseResult articleList(Long categoryId, Integer pageNum, Integer pageSize);

    /**
     * 根据id获取文章详情
     */
    ResponseResult getArticleDetails(Integer id);

    /**
     * 更新文章数量
     * @param id
     * @return
     */
    ResponseResult updateViewCount(Long id);

    /**
     * 查询文章列表
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param articleDto 文章dto
     * @return {@link ResponseResult}
     */
    ResponseResult getAllArticleList(Integer pageNum, Integer pageSize, ArticleDto articleDto);

    ResponseResult getArticleById(Long id);

    ResponseResult deleteArticle(Long id);

    ResponseResult addArticle(AddArticleDto articleDto);

    ResponseResult updateArticle(AdminArticleDto adminArticleDto);
}
