package com.xha.mapper;

import com.xha.domain.entity.Link;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Tony贾维斯
* @description 针对表【sg_link(友链)】的数据库操作Mapper
* @createDate 2022-11-05 22:21:27
* @Entity com.xha.domain.entity.Link
*/
@Mapper
public interface LinkMapper extends BaseMapper<Link> {

}




