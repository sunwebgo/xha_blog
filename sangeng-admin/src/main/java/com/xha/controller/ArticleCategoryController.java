package com.xha.controller;

import com.alibaba.excel.EasyExcel;
import com.xha.domain.ResponseResult;
import com.xha.domain.dto.CategoryDto;
import com.xha.domain.entity.Category;
import com.xha.domain.vo.CategoryTwoVo;
import com.xha.domain.vo.DownloadDataVo;
import com.xha.service.CategoryService;
import com.xha.utils.DownLoadExcelUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.xha.constants.CommonConstants.ARTICLE_CATEGORY;

@RestController
@RequestMapping("/content/category")
public class ArticleCategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 查询文章类别列表
     * @param pageNum     页面num
     * @param pageSize    页面大小
     * @param categoryDto 类别dto
     * @return {@link ResponseResult}
     */
    @GetMapping("/list")
    public ResponseResult getArticleCategoryList(Integer pageNum, Integer pageSize, CategoryDto categoryDto) {
        return categoryService.getArticleCategoryList(pageNum, pageSize, categoryDto);
    }

    /**
     * 添加类别
     *
     * @param categoryDto 类别dto
     * @return {@link ResponseResult}
     */
    @PostMapping
    public ResponseResult addCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.addCategory(categoryDto);
    }

    /**
     * 通过id查询分类
     * @param id id
     * @return {@link ResponseResult}
     */
    @GetMapping("/{id}")
    public ResponseResult getCategoryOneById(@PathVariable Long id){
        return categoryService.getCategoryOneById(id);
    }

    /**
     * 修改文章分类信息
     * @param categoryDto 类别dto
     * @return {@link ResponseResult}
     */
    @PutMapping
    public ResponseResult updateCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.updateCategory(categoryDto);
    }

    /**
     * 删除文章分类
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable Long id){
        return categoryService.deleteCategory(id);
    }

    /**
     * 查询文章类别列表
     *
     * @return {@link ResponseResult}
     */
    @GetMapping("/listAllCategory")
    public ResponseResult getNameArticleCategoryList(){
        return categoryService.getNameArticleCategoryList();
    }

    /**
     * 导出excel
     * @param response 响应
     * 使用自定义的权限校验方法
     */
    @PreAuthorize("@permission.hasAuthority('content:category:export')")
    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response){
        try {
            categoryService.getExcelData(ARTICLE_CATEGORY,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
