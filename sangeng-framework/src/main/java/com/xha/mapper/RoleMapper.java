package com.xha.mapper;

import com.xha.domain.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author Tony贾维斯
* @description 针对表【sys_role(角色信息表)】的数据库操作Mapper
* @createDate 2022-11-11 11:57:47
* @Entity com.xha.domain.entity.Role
*/
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long userId);
}




