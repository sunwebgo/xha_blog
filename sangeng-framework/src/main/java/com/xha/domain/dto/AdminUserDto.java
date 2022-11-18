package com.xha.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserDto {
    private String userName;

    private String nickName;

    private String password;

    private String phonenumber;

    private String email;

    private String sex;

    private String status;

    private List<Long> roleIds;
}
