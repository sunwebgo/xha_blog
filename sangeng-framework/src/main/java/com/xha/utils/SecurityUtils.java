package com.xha.utils;

import com.xha.domain.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 从SecurityContextHolder中获取到用户信息
 */
public class SecurityUtils
{

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser()
    {
        return (LoginUser) getAuthentication().getPrincipal();
    }

    /**
     * 获取到用户id
     * @return
     */
    public static Long getUserId() {
        return getLoginUser().getUser().getId();
    }


    /**
     * 判断是不是管理员
     * @return {@link Boolean}
     */
    public static Boolean isAdmin(){
        Long id = getLoginUser().getUser().getId();
        return id != null && id.equals(1L);
    }

}