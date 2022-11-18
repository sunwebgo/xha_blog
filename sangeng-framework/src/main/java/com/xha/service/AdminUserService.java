package com.xha.service;

import com.xha.domain.ResponseResult;
import com.xha.domain.dto.AdminUserDto;
import com.xha.domain.dto.UserInfoDto;
import com.xha.domain.entity.User;
import com.xha.domain.vo.UpdateUserInfoRoleIdVo;

public interface AdminUserService {
    ResponseResult userLogin(User user);

    ResponseResult getUserInfo();

    ResponseResult userLogout();

    ResponseResult getUserInfoList(Integer pageNum, Integer pageSize, UserInfoDto userInfoDto);

    ResponseResult updateUserStatus(Integer userId, Integer status);

    ResponseResult getUserInfoById(Long id);

    ResponseResult updateUserInfo(UpdateUserInfoRoleIdVo updateUserInfoVo);

    ResponseResult deleteUser(Long id);

    ResponseResult addUser(AdminUserDto adminUserDto);
}
