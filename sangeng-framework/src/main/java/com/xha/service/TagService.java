package com.xha.service;

import com.xha.domain.ResponseResult;
import com.xha.domain.dto.TagByIdVo;
import com.xha.domain.dto.TagDto;
import com.xha.domain.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xha.domain.vo.PageVo;

/**
* @author Tony贾维斯
* @description 针对表【sg_tag(标签)】的数据库操作Service
* @createDate 2022-11-10 21:43:53
*/
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> getTagList(Integer pageNum, Integer pageSize, TagDto tagDto);

    ResponseResult addTag(TagDto tagDto);

    ResponseResult deleteTag(Long id);

    ResponseResult getTagById(Long id);

    ResponseResult updateTag(TagByIdVo tagByIdVo);

    ResponseResult getNameTagList();
}
