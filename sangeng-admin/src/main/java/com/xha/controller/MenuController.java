package com.xha.controller;

import com.xha.domain.ResponseResult;
import com.xha.domain.dto.MenuDto;
import com.xha.service.MenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    /**
     * 查询菜单列表
     *
     * @param status   状态
     * @param menuName 菜单名称
     * @return {@link ResponseResult}
     */
    @GetMapping("/list")
    public ResponseResult getMenuList(String status,String menuName){
        return menuService.getMenuList(status,menuName);
    }

    /**
     * 添加菜单
     * @return {@link ResponseResult}
     */
    @PostMapping
    public ResponseResult addMenu(@RequestBody MenuDto menuDto){
        return menuService.addMenu(menuDto);
    }

    /**
     * 通过id获取菜单
     * @param id id
     * @return {@link ResponseResult}
     */
    @GetMapping("/{id}")
    public ResponseResult getMenuById(@PathVariable Long id){
        return menuService.getMenuById(id);
    }

    /**
     * 更新菜单
     *
     * @param menuDto
     * @return {@link ResponseResult}
     */
    @PutMapping
    public ResponseResult updateMenu(@RequestBody MenuDto menuDto){
        return menuService.updateMenu(menuDto);
    }

    /**
     * 删除菜单
     *
     * @return {@link ResponseResult}
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteMenu(@PathVariable Long id){
        return menuService.deleteMenu(id);
    }

    /**
     * 查询菜单树
     *
     * @return {@link ResponseResult}
     */
    @GetMapping("/treeselect")
    public ResponseResult getMenuTree(){
        return menuService.getMenuTree();
    }

    /**
     * 角色菜单treeselect
     * @param id id
     * @return {@link ResponseResult}
     */
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeselect(@PathVariable Long id){
        return menuService.roleMenuTreeselect(id);
    }

}
