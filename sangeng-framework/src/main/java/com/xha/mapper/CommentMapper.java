package com.xha.mapper;

import com.xha.domain.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Tony贾维斯
* @description 针对表【sg_comment(评论表)】的数据库操作Mapper
* @createDate 2022-11-09 13:22:08
* @Entity com.xha.domain.entity.Comment
*/
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}




