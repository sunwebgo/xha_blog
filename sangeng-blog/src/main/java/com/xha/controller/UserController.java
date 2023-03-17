package com.xha.controller;

import com.xha.domain.ResponseResult;
import com.xha.domain.entity.User;
import com.xha.service.UserService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @return
     */
    @PostMapping("/register")
    public ResponseResult userRegister(@RequestBody User user) {
        return userService.userRegister(user);
    }

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    @PostMapping("/login")
    public ResponseResult userLogin(@RequestBody User user) {
        return userService.userLogin(user);
    }


    /**
     * 用户退出
     *
     * @return
     */
    @PostMapping("/logout")
    public ResponseResult userLogout() {
        return userService.userLogout();
    }

    /**
     * 查询用户信息
     *
     * @return
     */
    @GetMapping("/userInfo")
    public ResponseResult getUserInfo() {
        return userService.getUserInfo();
    }


    /**
     * 用户信息修改
     *
     * @return
     */
    @PutMapping("/userInfo")
    public ResponseResult updateUserInfo(@RequestBody User user) {
        return userService.updateUserInfo(user);
    }


}
