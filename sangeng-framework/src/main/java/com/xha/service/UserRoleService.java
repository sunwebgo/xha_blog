package com.xha.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xha.domain.entity.UserRole;

import java.util.List;

/**
 * @author Tony贾维斯
 * @description 针对表【sys_user_role(用户和角色关联表)】的数据库操作Service
 * @createDate 2022-11-11 11:57:58
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * 得到用户角色id
     *
     * @param id id
     * @return {@link List}<{@link Long}>
     */
    public List<Long> getUserRoleById(Long id);


}
