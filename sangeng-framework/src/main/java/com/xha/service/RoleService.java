package com.xha.service;

import com.xha.domain.ResponseResult;
import com.xha.domain.dto.RoleDto;
import com.xha.domain.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Tony贾维斯
* @description 针对表【sys_role(角色信息表)】的数据库操作Service
* @createDate 2022-11-11 11:57:47
*/
public interface RoleService extends IService<Role> {

    List<Role> getAllRoleList();

    ResponseResult getListAllRole();

    ResponseResult getAllRoleByPage(Integer pageNum, Integer pageSize, RoleDto roleDto);

    ResponseResult changeRoleStatus(Integer roleId, Integer status);

    ResponseResult addRole(RoleDto roleDto);

    ResponseResult getRoleInfoById(Long id);

    ResponseResult deleteRole(Long id);

    ResponseResult updateRoleInfo(RoleDto roleDto);
}
