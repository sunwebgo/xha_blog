package com.xha.service;

import com.xha.domain.ResponseResult;
import com.xha.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Tony贾维斯
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2022-11-08 09:21:53
*/

public interface UserService extends IService<User> {

    ResponseResult userLogin(User user);

    ResponseResult userLogout();

    ResponseResult getUserInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult userRegister(User user);
}
