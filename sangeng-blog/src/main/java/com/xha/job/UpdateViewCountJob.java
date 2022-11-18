package com.xha.job;


import com.xha.domain.entity.Article;
import com.xha.service.ArticleService;
import com.xha.utils.RedisCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.xha.constants.RedisConstants.ARTICLE_VIEWCOUNT;

/**
 * 定时更新文章浏览量
 *
 * @author Tony贾维斯
 * @date 2022/11/10
 */

@Component
public class UpdateViewCountJob {

    @Resource
    private RedisCache redisCache;

    @Resource
    private ArticleService articleService;

    /**
     * 从0秒开始，每10分钟从redis中获取到文章浏览量，更新数据库
     */
    @Scheduled(cron = "0 0/10 * * * ? ")
    public void updateViewCount() {
//        1.根据key从redis中获取到含有文章id和浏览量的Map集合
        Map<String, Integer> articles = redisCache.getCacheMap(ARTICLE_VIEWCOUNT);

        List<Article> articleList = articles.entrySet()
                .stream()
                .map(article -> new Article(Long.valueOf(article.getKey()), Long.valueOf(article.getValue())))
                .collect(Collectors.toList());

//        2.更新到数据库
        articleService.updateBatchById(articleList);
    }
}
