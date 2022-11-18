package com.xha.service.impl;

import com.xha.domain.entity.LoginUser;
import com.xha.utils.SecurityUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("permission")
public class PermissionService {

    /**
     * 查询用户是否有权限
     * @param authority 权威
     * @return boolean
     */
    public boolean hasAuthority(String authority) {
//        1.如果是管理员就返回true
        if (SecurityUtils.isAdmin()){
            return true;
        }
//        2.如果不是管理员，就返回当前登录用户是否有此权限的判断
//          2.1获取当前用户的权限
//          2.2因为用户信息已经封装到SecurityContextHolder对象当中
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//          2.3获取到当前用户的权限列表
        List<String> permissions = loginUser.getPermissions();
//          2.4判断用户权限列表中是否含有方法级安全管理定义的authority
        return permissions.contains(authority);
    }
}
