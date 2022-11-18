package com.xha.service;

import com.xha.domain.ResponseResult;
import com.xha.domain.dto.MenuDto;
import com.xha.domain.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Tony贾维斯
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Service
* @createDate 2022-11-11 11:51:19
*/
public interface MenuService extends IService<Menu> {


    /**
     * 根据条件查询menu信息
     * @return {@link ResponseResult}
     */
    ResponseResult selectRouterMenu();

    List<Menu> selectMenuList(Menu menu);

    ResponseResult getMenuList(String status, String menuName);

    ResponseResult addMenu(MenuDto menuDto);

    ResponseResult getMenuById(Long id);

    ResponseResult updateMenu(MenuDto menuDto);

    ResponseResult deleteMenu(Long menuId);

    ResponseResult getMenuTree();

    ResponseResult roleMenuTreeselect(Long id);


}
