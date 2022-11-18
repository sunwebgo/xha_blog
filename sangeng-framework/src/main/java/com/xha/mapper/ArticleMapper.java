package com.xha.mapper;

import com.xha.domain.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Tony贾维斯
* @description 针对表【sg_article(文章表)】的数据库操作Mapper
* @createDate 2022-11-05 09:21:27
* @Entity com.xha.domain.entity.Article
*/
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

}




