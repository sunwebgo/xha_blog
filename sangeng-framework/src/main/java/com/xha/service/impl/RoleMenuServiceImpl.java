package com.xha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xha.domain.entity.RoleMenu;
import com.xha.service.RoleMenuService;
import com.xha.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Tony贾维斯
* @description 针对表【sys_role_menu(角色和菜单关联表)】的数据库操作Service实现
* @createDate 2022-11-11 11:57:52
*/
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>
    implements RoleMenuService{

    @Resource
    private RoleMenuMapper roleMenuMapper;
    /**
     * 通过id获取角色菜单id
     *
     * @param roleId 角色id
     * @return {@link List}<{@link String}>
     */
    public List<Long> getRoleMenuIdsById(Long roleId){
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,roleId);
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(queryWrapper);
        List<Long> menuIds = roleMenus
                .stream()
                .map(roleMenu -> roleMenu.getMenuId()).collect(Collectors.toList());
        return menuIds;
    }
}




