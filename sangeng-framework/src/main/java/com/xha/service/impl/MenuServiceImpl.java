package com.xha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xha.domain.ResponseResult;
import com.xha.domain.dto.MenuDto;
import com.xha.domain.entity.Menu;
import com.xha.domain.vo.MenuTreeVo;
import com.xha.domain.vo.MenuVo;
import com.xha.domain.vo.RoleMenuTreeSelectVo;
import com.xha.domain.vo.RoutersVo;
import com.xha.mapper.MenuMapper;
import com.xha.service.MenuService;
import com.xha.utils.BeanCopyPropertiesUtils;
import com.xha.utils.SecurityUtils;
import com.xha.utils.SystemConverter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.xha.constants.CommonConstants.MENU_PARENT_ID;
import static com.xha.enums.AppHttpCodeEnum.*;

/**
 * @author Tony贾维斯
 * @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
 * @createDate 2022-11-11 11:51:19
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
        implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Lazy
    @Resource
    private MenuService menuService;

    /**
     * 根据条件查询menu信息
     *
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult selectRouterMenu() {
//        1.获取到当前用户id
        Long userId = SecurityUtils.getUserId();
        List<Menu> menus = null;
//        2.判断当前用户是否是管理员
        if (SecurityUtils.isAdmin()) {
//            2.1如果是管理员就查询所有menu信息
            menus = menuMapper.selectAllRouterMenu();
        } else {
//            2.2不是管理员就查询对应的menu信息
            menus = menuMapper.selectRouterMenuByUserId(userId);
        }
//        3.构建menu树
        List<Menu> menuTree = buildMenuTree(menus, MENU_PARENT_ID);
        return ResponseResult.okResult(new RoutersVo(menuTree));
    }



    /**
     * 构建menu树(menu父子关系)
     *
     * @param menus
     * @return {@link List}<{@link Menu}>
     */
    private List<Menu> buildMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream().
//                按照parentId过滤，设置父menu的子menu
        filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildrenTree(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }


    /**
     * 在menus集合中查找到父menu的子menu
     *
     * @param menu  菜单
     * @param menus
     * @return {@link List}<{@link Menu}>
     */
    private List<Menu> getChildrenTree(Menu menu, List<Menu> menus) {
        List<Menu> childrenTree = menus.stream()
                .filter(menu1 -> menu1.getParentId().equals(menu.getId()))
//                递归：为子菜单设置子菜单
                .map(menu1 -> menu1.setChildren(getChildrenTree(menu1, menus)))
                .collect(Collectors.toList());
        return childrenTree;
    }

    /**
     * 查询菜单列表
     *
     * @param status   状态
     * @param menuName 菜单名称
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult getMenuList(String status, String menuName) {
//        1.根据menu状态和menuName查询
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(status), Menu::getStatus, status)
                .like(StringUtils.hasText(menuName), Menu::getMenuName, menuName)
                .orderByAsc(Menu::getParentId, Menu::getOrderNum);
        List<Menu> menus = list(queryWrapper);
//        2.将List<Menu>对象转换为List<MenuVo>对象
        List<MenuVo> menuVos = BeanCopyPropertiesUtils.copyBeanList(menus, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }


    /**
     * 添加菜单
     *
     * @param menuDto 菜单dto
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult addMenu(MenuDto menuDto) {
//        1.将MenuDto对象转换为Menu对象
        Menu menu = BeanCopyPropertiesUtils.copyBean(menuDto, Menu.class);
//        2.根据MenuName判断当前是否存在menu
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getMenuName, menu.getMenuName());
        Menu oneMenu = getOne(queryWrapper);
        if (!Objects.isNull(oneMenu)) {
            return ResponseResult.errorResult(ADD_MENU_FAIL);
        }
        save(menu);
        return ResponseResult.okResult();
    }

    /**
     * 通过id获取菜单
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult getMenuById(Long id) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getId, id);
        Menu menu = getOne(queryWrapper);
//        将Menu对象转换为MenuVo对象
        MenuVo menuVo = BeanCopyPropertiesUtils.copyBean(menu, MenuVo.class);
        return ResponseResult.okResult(menuVo);
    }

    /**
     * 更新菜单
     *
     * @param menuDto
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult updateMenu(MenuDto menuDto) {
        //        1.判断LinkDto对象值是否为空
        if (!StringUtils.hasText(menuDto.getMenuName()) ||
                !StringUtils.hasText(menuDto.getMenuType()) ||
                !StringUtils.hasText(String.valueOf(menuDto.getStatus())) ||
                !StringUtils.hasText(menuDto.getPath()) ||
                !StringUtils.hasText(String.valueOf(menuDto.getOrderNum())) ||
                !StringUtils.hasText(menuDto.getIcon())) {
            return ResponseResult.errorResult(CONTENT_IS_BLANK);
        }
//        1.将MenuDto对象转化为Menu对象
        Menu menu = BeanCopyPropertiesUtils.copyBean(menuDto, Menu.class);
        updateById(menu);
        return ResponseResult.okResult();
    }

    /**
     * 删除菜单
     *
     * @param id
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult deleteMenu(Long id) {
//        1.查询当前菜单是否有子菜单，如果有就不允许删除
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId, id);
        List<Menu> menus = menuMapper.selectList(queryWrapper);
        if (!Objects.isNull(menus) && menus.size() != 0) {
            return ResponseResult.errorResult(DELETE_MENU_REFUSE);
        }

        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public List<Menu> selectMenuList(Menu menu) {

        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        //menuName模糊查询
        queryWrapper.like(StringUtils.hasText(menu.getMenuName()),Menu::getMenuName,menu.getMenuName());
        queryWrapper.eq(StringUtils.hasText(menu.getStatus()),Menu::getStatus,menu.getStatus());
        //排序 parent_id和order_num
        queryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> menus = list(queryWrapper);;
        return menus;
    }


    /**
     * 查询菜单树
     *
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult getMenuTree() {
// 复用之前的selectMenuList方法。方法需要参数，参数可以用来进行条件查询，而这个方法不需要条件，所以直接new Menu()传入
        List<Menu> menus = menuService.selectMenuList(new Menu());
        List<MenuTreeVo> menuTreeVos =  SystemConverter.buildMenuSelectTree(menus);
        return ResponseResult.okResult(menuTreeVos);
    }

    /**
     * 角色菜单treeselect
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult roleMenuTreeselect(Long id) {
        List<Menu> menus = menuService.selectMenuList(new Menu());
        List<Long> checkedKeys = this.selectMenuListByRoleId(id);
        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        RoleMenuTreeSelectVo vo = new RoleMenuTreeSelectVo(checkedKeys,menuTreeVos);
        return ResponseResult.okResult(vo);
    }

    /**
     * 选择菜单通过角色id列表
     * @param roleId 角色id
     * @return {@link List}<{@link Long}>
     */
    public List<Long> selectMenuListByRoleId(Long roleId) {
        return getBaseMapper().selectMenuListByRoleId(roleId);
    }


}




