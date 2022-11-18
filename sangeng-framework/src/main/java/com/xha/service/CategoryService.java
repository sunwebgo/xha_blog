package com.xha.service;

import com.xha.domain.ResponseResult;
import com.xha.domain.dto.CategoryDto;
import com.xha.domain.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xha.domain.vo.CategoryTwoVo;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
* @author Tony贾维斯
* @description 针对表【sg_category(分类表)】的数据库操作Service
* @createDate 2022-11-05 13:05:46
*/

public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    ResponseResult getArticleCategoryList(Integer pageNum, Integer pageSize, CategoryDto categoryDto);

    ResponseResult addCategory(CategoryDto categoryDto);

    ResponseResult getCategoryOneById(Long id);

    ResponseResult updateCategory(CategoryDto categoryDto);

    ResponseResult deleteCategory(Long id);

    ResponseResult getNameArticleCategoryList();

    void getExcelData(String filename, HttpServletResponse response) throws IOException;

    String getCategoryName(Long id);
}
