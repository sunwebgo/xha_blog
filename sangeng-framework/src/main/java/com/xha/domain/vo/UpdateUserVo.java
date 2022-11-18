package com.xha.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserVo {
    private List<Long> roleIds;
    private List<RoleVo> roles;
    private UpdateUserInfoVo user;
}
