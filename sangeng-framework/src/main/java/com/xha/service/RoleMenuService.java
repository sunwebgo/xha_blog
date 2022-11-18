package com.xha.service;

import com.xha.domain.entity.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Tony贾维斯
* @description 针对表【sys_role_menu(角色和菜单关联表)】的数据库操作Service
* @createDate 2022-11-11 11:57:52
*/
public interface RoleMenuService extends IService<RoleMenu> {
    public List<Long> getRoleMenuIdsById(Long roleId);
}
