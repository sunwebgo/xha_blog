package com.xha.mapper;

import com.xha.domain.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Tony贾维斯
* @description 针对表【sg_category(分类表)】的数据库操作Mapper
* @createDate 2022-11-05 13:05:46
* @Entity com.xha.domain.entity.Category
*/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}




