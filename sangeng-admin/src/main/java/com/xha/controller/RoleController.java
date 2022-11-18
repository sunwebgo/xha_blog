package com.xha.controller;

import com.xha.domain.ResponseResult;
import com.xha.domain.dto.RoleDto;
import com.xha.domain.entity.Role;
import com.xha.service.RoleService;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * 查询出所有角色
     * @return {@link ResponseResult}
     */
    @GetMapping("/listAllRole")
    public ResponseResult getListAllRole(){
        return roleService.getListAllRole();
    }


    /**
     * 得到所有角色页面
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @param roleDto  角色dto
     * @return {@link ResponseResult}
     */
    @GetMapping("/list")
    public ResponseResult getAllRoleByPage(Integer pageNum, Integer pageSize, RoleDto roleDto){
        return roleService.getAllRoleByPage(pageNum,pageSize,roleDto);
    }

    /**
     * 改变角色状态
     *
     * @param roleId 角色id
     * @param status 状态
     * @return {@link ResponseResult}
     */
    @PutMapping("/changeStatus")
    public ResponseResult changeRoleStatus(Integer roleId,Integer status){
        return roleService.changeRoleStatus(roleId,status);
    }

    /**
     * 添加角色
     *
     * @param roleDto 角色dto
     * @return {@link ResponseResult}
     */
    @PostMapping
    public ResponseResult addRole(@RequestBody RoleDto roleDto){
        return roleService.addRole(roleDto);
    }


    /**
     * 通过id获取角色信息
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @GetMapping("/{id}")
    public ResponseResult getRoleInfoById(@PathVariable Long id){
        return roleService.getRoleInfoById(id);
    }

    /**
     * 修改角色信息
     *
     * @return {@link ResponseResult}
     */
    @PutMapping
    public ResponseResult updateRoleInfo(@RequestBody RoleDto roleDto){
        return roleService.updateRoleInfo(roleDto);
    }

    /**
     * 删除角色
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable Long id){
        return roleService.deleteRole(id);
    }





}
