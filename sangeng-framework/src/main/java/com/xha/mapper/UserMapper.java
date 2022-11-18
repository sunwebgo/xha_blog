package com.xha.mapper;

import com.xha.domain.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Tony贾维斯
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2022-11-08 09:21:53
* @Entity com.xha.domain.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




