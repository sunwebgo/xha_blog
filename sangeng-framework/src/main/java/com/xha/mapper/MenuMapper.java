package com.xha.mapper;

import com.xha.domain.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author Tony贾维斯
* @description 针对表【sys_menu(菜单权限表)】的数据库操作Mapper
* @createDate 2022-11-11 11:51:19
* @Entity com.xha.domain.entity.Menu
*/
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 通过用户id查询对应的权限信息
     *
     * @param userId 用户id
     */
    List<String> selectPermsByUserId(Long userId);


    /**
     * 为管理员用户查询所有menu
     * @return
     */
    List<Menu> selectAllRouterMenu();

    /**
     * 为管理员用户查询所有menu
     * @return
     */
    List<Menu> selectRouterMenuByUserId(Long userId);

    List<Long> selectMenuListByRoleId(Long roleId);
}




