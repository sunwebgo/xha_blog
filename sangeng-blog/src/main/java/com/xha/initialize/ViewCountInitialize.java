package com.xha.initialize;


import com.xha.domain.entity.Article;
import com.xha.mapper.ArticleMapper;
import com.xha.utils.RedisCache;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.xha.constants.RedisConstants.ARTICLE_VIEWCOUNT;

/**
 * 文章浏览量初始化
 * @author Tony贾维斯
 * @date 2022/11/10
 */
@Component
public class ViewCountInitialize implements CommandLineRunner {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private RedisCache redisCache;


    @Override
    public void run(String... args) throws Exception {
//        1.查询博客信息（id、viewCount）
        List<Article> articles = articleMapper.selectList(null);
//        2.获取到文章id和观看数量viewCount
        Map<String, Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> article.getViewCount().intValue()));
        redisCache.setCacheMap(ARTICLE_VIEWCOUNT,viewCountMap);
    }
}
