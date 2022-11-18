package com.xha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xha.domain.ResponseResult;
import com.xha.domain.dto.AddArticleDto;
import com.xha.domain.dto.AdminArticleDto;
import com.xha.domain.dto.ArticleDto;
import com.xha.domain.entity.*;
import com.xha.domain.vo.*;
import com.xha.mapper.ArticleMapper;
import com.xha.service.ArticleService;
import com.xha.service.ArticleTagService;
import com.xha.service.CategoryService;
import com.xha.utils.BeanCopyPropertiesUtils;
import com.xha.utils.RedisCache;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.xha.constants.CommonConstants.*;
import static com.xha.constants.RedisConstants.ARTICLE_VIEWCOUNT;
import static com.xha.enums.AppHttpCodeEnum.DELETE_ARTICLE_FAIL;
import static com.xha.enums.AppHttpCodeEnum.DELETE_LINK_FAIL;


/**
 * @author Tony贾维斯
 * @description 针对表【sg_article(文章表)】的数据库操作Service实现
 * @createDate 2022-11-05 09:21:27
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService {

    @Lazy
    @Resource
    private CategoryService categoryService;

    @Resource
    private RedisCache redisCache;

    @Resource
    private ArticleTagService articleTagService;

    @Resource
    private ArticleMapper articleMapper;

    /**
     * 热门博客查询
     *
     * @return
     */
    @Override
    public ResponseResult hotArticleList() {
//        1.查询热门文章
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        2.必须是发布的文章
        queryWrapper.eq(Article::getStatus, ARTICLE_STATUS_PUBLISH);
//        3。按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
//        4.查询10条数据
        Page<Article> page = new Page<>(CURRENT_PAGE, SHOW_NUMBERS);
        page(page, queryWrapper);
        List<Article> articles = page.getRecords();

//        5.根据文章id查询redis缓存获取到当前文章的viewCount并设置
        queryViewCount(articles);

//        使用Stream流进行Bean转换操作
        List<HotArticleVo> articleVos = BeanCopyPropertiesUtils
                .copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(articleVos);
    }

    /**
     * 博客列表查询
     */
    @Override
    public ResponseResult articleList(Long categoryId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();
//        1.判断categoryId是否存在，如果存在就添加
        queryWrapper.eq(Objects.nonNull(categoryId) && categoryId >= 0, Article::getCategoryId, categoryId);
//        3.规定状态为发布状态
        queryWrapper.eq(Article::getStatus, ARTICLE_STATUS_PUBLISH);
//        4.按照is_top属性进行降序排序
        queryWrapper.orderByDesc(Article::getIsTop);
//        5.分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

//        6.根据categoryId查询categoryName(因为article表中只有categoryId)
        List<Article> articles = page.getRecords();
        articles.stream()
                .map(article -> article.setCategoryName(
                        categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());

//        7.根据文章id查询redis缓存获取到当前文章的viewCount并设置
        queryViewCount(articles);

//        8.将当前页数据转为Vo对象
        List<ArticleListVo> articleListVos = BeanCopyPropertiesUtils.copyBeanList(page.getRecords(), ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 根据id获取博客详情
     *
     * @param id
     * @return
     */
    public ResponseResult getArticleDetails(Integer id) {

//        1.根据id查询文章
        Article article = getById(id);

//        2.从redis中获取到当前最新的文章的浏览量viewCount
        Integer viewCount = redisCache.getCacheMapValue(ARTICLE_VIEWCOUNT, id.toString());
        article.setViewCount(Long.valueOf(viewCount));

//        3.转换为Vo
        ArticleDetailsVo articleDetailsVo = BeanCopyPropertiesUtils.copyBean(article, ArticleDetailsVo.class);
//        4.根据分类id查询分类名

        String categoryId = articleDetailsVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if (!ObjectUtils.isEmpty(category)) {
            articleDetailsVo.setCategoryName(category.getName());
        }
//        5.封装响应对象
        return ResponseResult.okResult(articleDetailsVo);
    }

    /**
     * 更新博客数量
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue(ARTICLE_VIEWCOUNT, id.toString(), 1);
        return ResponseResult.okResult();
    }

    /**
     *根据文章id查询浏览量
     * @param articles 文章
     * @return {@link List}<{@link Article}>
     */
    public List<Article> queryViewCount(List<Article> articles){
        articles.
                stream()
                .map(article -> {
                    Integer viewCount = redisCache.getCacheMapValue(ARTICLE_VIEWCOUNT, article.getId().toString());
                    article.setViewCount(Long.valueOf(viewCount));
                    return article;
                }).collect(Collectors.toList());
        return articles;
    }


    /**
     * 查询博客列表
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param articleDto 文章dto
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult getAllArticleList(Integer pageNum, Integer pageSize, ArticleDto articleDto) {
//        1.根据文章标题(模糊查询)和摘要进行查询
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(articleDto.getTitle()), Article::getTitle, articleDto.getTitle());
        queryWrapper.like(StringUtils.hasText(articleDto.getSummary()), Article::getSummary, articleDto.getSummary());
//        2.规定文章是未发布状态不能显示
        queryWrapper.eq(Article::getStatus,ARTICLE_STATUS_PUBLISH);
//        3.分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

//        3.将当前页中的Article对象转换为ArticleDetailsVo对象
        List<Article> articles = page.getRecords();
        List<ArticleDetailsVo> articleDetailsVos = BeanCopyPropertiesUtils.copyBeanList(articles, ArticleDetailsVo.class);
//        4.将LinkVo对象转换为LinkAdminVo对象
        AdminArticleVo adminArticleVo = new AdminArticleVo(articleDetailsVos, page.getTotal());
        return ResponseResult.okResult(adminArticleVo);
    }

    /**
     * 通过id获取博客
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult getArticleById(Long id) {
//        1.根据id查询博客
        Article article = getById(id);
//        2.将Link对象封装为UpdateArticleVo对象
        UpdateArticleVo updateArticleVo = BeanCopyPropertiesUtils.copyBean(article, UpdateArticleVo.class);
//        3.根据文章id获取到文章标签列表
        List<Long> tagList = articleTagService.getTagList(id);
        updateArticleVo.setTags(tagList);
        return ResponseResult.okResult(updateArticleVo);
    }

    /**
     * 删除博客
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult deleteArticle(Long id) {
        boolean result = removeById(id);
        if (result == false){
            return ResponseResult.errorResult(DELETE_ARTICLE_FAIL);
        }
        return ResponseResult.okResult();
    }


    /**
     * 添加博客
     *
     * @param articleDto 文章dto
     * @return {@link ResponseResult}
     */
    @Override
    @Transactional
    public ResponseResult addArticle(AddArticleDto articleDto) {
//        1.将AddArticleDto对象转换为Article对象
        Article article = BeanCopyPropertiesUtils.copyBean(articleDto, Article.class);
//        2.保存Article对象
        save(article);

//        3.获取到AddArticleDto对象当中的tags属性，
//        使用Stream流遍历，操作ArticleTag对象，将当前Article和Tag的对应关系添加到sg_article_tag中
        List<ArticleTag> articleTags = articleDto
                .getTags()
                .stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    /**
     * 修改博客
     * @param adminArticleDto 文章细节签证官
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult updateArticle(AdminArticleDto adminArticleDto) {
//        1.将AdminArticleDto对象转换为Article对象
        Article article = BeanCopyPropertiesUtils.copyBean(adminArticleDto, Article.class);
//        2.将博客的标签信息存入标签表
//          2.1根据当前博客id获取到已有的标签列表
        List<Long> tagList = articleTagService.getTagList(article.getId());
//          2.2得到修改过后的标签列表
        List<Long> tags = article.getTags();
//          2.3遍历修改过后的标签列表，判断当前博客是否已经有此标签，没有则一条数据添加到sg_article_tag表中
        for (Long tag:tags){
            if (!tagList.contains(tag)){
                articleTagService.save(new ArticleTag(article.getId(), tag));
            }
        }
        updateById(article);
        return ResponseResult.okResult();
    }
}




