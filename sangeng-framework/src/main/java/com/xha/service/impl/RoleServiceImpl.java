package com.xha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xha.domain.ResponseResult;
import com.xha.domain.dto.RoleDto;
import com.xha.domain.entity.Role;
import com.xha.domain.entity.RoleMenu;
import com.xha.domain.vo.AdminRoleVo;
import com.xha.domain.vo.RoleVo;
import com.xha.mapper.RoleMapper;
import com.xha.mapper.RoleMenuMapper;
import com.xha.service.RoleMenuService;
import com.xha.service.RoleService;
import com.xha.utils.BeanCopyPropertiesUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static com.xha.enums.AppHttpCodeEnum.*;

/**
 * @author Tony贾维斯
 * @description 针对表【sys_role(角色信息表)】的数据库操作Service实现
 * @createDate 2022-11-11 11:57:47
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
        implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Lazy
    @Resource
    private RoleService roleService;

    @Resource
    private RoleMenuService roleMenuService;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    /**
     * 得到所有角色列表
     *
     * @return {@link List}<{@link Role}>
     */
    @Override
    public List<Role> getAllRoleList() {
        List<Role> roles = roleMapper.selectList(null);
        return roles;
    }


    /**
     * 列出所有角色,并ResponseResult对象返回给前端
     *
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult getListAllRole() {
        List<Role> roles = roleMapper.selectList(null);
        return ResponseResult.okResult(roles);
    }

    /**
     * 分页查询所有角色
     *
     * @param pageNum  页面num
     * @param pageSize 页面大小
     * @param roleDto  角色dto
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult getAllRoleByPage(Integer pageNum, Integer pageSize, RoleDto roleDto) {
        //        1.根据友链名(模糊查询)和状态进行查询
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(roleDto.getStatus()), Role::getStatus, roleDto.getStatus());
        queryWrapper.like(StringUtils.hasText(roleDto.getRoleName()), Role::getRoleName, roleDto.getRoleName());

//        2.分页查询
        Page<Role> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

//        3.将当前页中的Role对象转换为RoleVo对象
        List<Role> roles = page.getRecords();
        List<RoleVo> roleVos = BeanCopyPropertiesUtils.copyBeanList(roles, RoleVo.class);
//        4.将LinkVo对象转换为LinkAdminVo对象
        AdminRoleVo adminRoleVo = new AdminRoleVo(roleVos, page.getTotal());
        return ResponseResult.okResult(adminRoleVo);
    }

    /**
     * 改变角色状态
     *
     * @param roleId 角色id
     * @param status 状态
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult changeRoleStatus(Integer roleId, Integer status) {
        LambdaUpdateWrapper<Role> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Role::getId, roleId)
                .set(Role::getStatus, status);
        roleService.update(null, updateWrapper);
        return ResponseResult.okResult();
    }

    /**
     * 添加角色
     *
     * @param roleDto 角色dto
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult addRole(RoleDto roleDto) {
//        1.根据角色名判断当前角色是否存在
        if (!this.judgeRole(roleDto.getRoleName())) {
            return ResponseResult.errorResult(ROLE_INFO_EXIST);
        }

//        1.根据权限名判断当前角色是否存在
        if (!this.judgeRoleKey(roleDto.getRoleKey())) {
            return ResponseResult.errorResult(ROLEKEY_INFO_EXIST);
        }

//        2.获取到当前角色的菜单权限列表
        List<Long> menuIds = roleDto.getMenuIds();

//        3.将RoleDto对象转换为Role对象
        Role role = BeanCopyPropertiesUtils.copyBean(roleDto, Role.class);
        save(role);

//        4.根据角色名获取到当前角色
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getRoleName, role.getRoleName());
        Role getRole = getOne(queryWrapper);

//        5.遍历menuIds，添加到sys_role_menu表中
        menuIds.stream()
                .map(menuId -> roleMenuService.save
                        (new RoleMenu(getRole.getId(), menuId)));
        return ResponseResult.okResult();
    }

    /**
     * 通过id获取角色信息
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult getRoleInfoById(Long id) {
//        1.根据用户id获取到当前用户信息
        Role role = roleService.getById(id);
//        2.将Role对象转换为RoleVo对象
        RoleVo roleVo = BeanCopyPropertiesUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    /**
     * 删除角色
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult deleteRole(Long id) {
//        1.首先移除当前角色所关联的sys_role_menu表数据
        roleMenuService.removeById(id);
//        2.根据id移除角色
        removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 更新角色信息
     *
     * @param roleDto
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult updateRoleInfo(RoleDto roleDto) {
        //        1.判断LinkDto对象值是否为空
        if (!StringUtils.hasText(roleDto.getRoleName()) ||
                !StringUtils.hasText(roleDto.getRoleKey()) ||
                !StringUtils.hasText(String.valueOf(roleDto.getStatus())) ||
                !StringUtils.hasText(roleDto.getRemark()) ||
                !StringUtils.hasText(String.valueOf(roleDto.getRoleSort()))) {
            return ResponseResult.errorResult(CONTENT_IS_BLANK);
        }

//        2.获取到当前角色的菜单权限列表
        List<Long> menuIds = roleDto.getMenuIds();

//        3.将RoleDto对象转换为Role对象
        Role role = BeanCopyPropertiesUtils.copyBean(roleDto, Role.class);
        updateById(role);

//        4.查询当前角色的menuIds列表
        List<Long> roleMenuIdsById = roleMenuService.getRoleMenuIdsById(role.getId());
//        5.根据roleId移除sys_role_menu表中的数据
        roleMenuService.removeById(role.getId());
//          2.2遍历修改后的用户的menuIds列表，添加到sys_role_menu表中
        for (Long menuId : menuIds) {
            roleMenuService.save(new RoleMenu(role.getId(), menuId));
        }
        return ResponseResult.okResult();
    }

    /**
     * 判断角色是否存在
     *
     * @param roleName
     * @return
     */
    public boolean judgeRole(String roleName) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Role::getRoleName, roleName);
        Role role = roleService.getOne(queryWrapper);
        if (Objects.isNull(role)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断权限是否存在
     *
     * @param roleKey
     * @return
     */
    public boolean judgeRoleKey(String roleKey) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Role::getRoleKey, roleKey);
        Role role = roleService.getOne(queryWrapper);
        if (Objects.isNull(role)) {
            return true;
        } else {
            return false;
        }
    }


}




