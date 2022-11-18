package com.xha.service;

import com.xha.domain.entity.ArticleTag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Tony贾维斯
* @description 针对表【sg_article_tag(文章标签关联表)】的数据库操作Service
* @createDate 2022-11-13 10:55:51
*/
public interface ArticleTagService extends IService<ArticleTag> {
    public List<Long> getTagList(Long id);
}
