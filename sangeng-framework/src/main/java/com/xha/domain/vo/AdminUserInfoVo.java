package com.xha.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserInfoVo {


    /**
     * 权限
     */
    private List<String> permissions;

    /**
     * 角色
     */
    private List<String> roles;

    /**
     * 用户信息
     */
    private UserInfoVo user;

}
