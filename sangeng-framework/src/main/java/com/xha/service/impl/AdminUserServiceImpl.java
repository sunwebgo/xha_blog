package com.xha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xha.domain.ResponseResult;
import com.xha.domain.dto.AdminUserDto;
import com.xha.domain.dto.UserInfoDto;
import com.xha.domain.entity.*;
import com.xha.domain.vo.*;
import com.xha.enums.AppHttpCodeEnum;
import com.xha.exception.SystemException;
import com.xha.mapper.MenuMapper;
import com.xha.mapper.RoleMapper;
import com.xha.mapper.UserMapper;
import com.xha.service.AdminUserService;
import com.xha.service.RoleService;
import com.xha.service.UserRoleService;
import com.xha.service.UserService;
import com.xha.utils.BeanCopyPropertiesUtils;
import com.xha.utils.JwtUtils;
import com.xha.utils.RedisCache;
import com.xha.utils.SecurityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.xha.constants.CommonConstants.*;
import static com.xha.constants.RedisConstants.ADMIN_USER_LOGIN;
import static com.xha.enums.AppHttpCodeEnum.*;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisCache redisCache;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserService userService;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private RoleService roleService;



    /**
     * 用户登录
     *
     * @return
     */
    @Override
    public ResponseResult userLogin(User user) {
//        1.根据username和password封装Authentication对象
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
//        2.AuthenticationManager调用authenticate方法，再调用UserDetailsService的loadUserByUsername方法
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
//        3.判断认证是否通过
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误！");
        }
//        4.获取到用户id
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtils.createJWT(userId);

//        5.将用户信息存入缓存
        redisCache.setCacheObject(ADMIN_USER_LOGIN + userId, loginUser);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
//        7.将token和用户信息封装返回
        return ResponseResult.okResult(map);
    }


    /**
     * 获取用户信息
     *
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult getUserInfo() {
//        1.获取到用户id
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long userId = loginUser.getUser().getId();
        List<String> perms = null;
        List<String> roles = null;
//        2.获取到用户的权限和角色信息
//          2.1当用户id为1的时候表示是管理员,查询权限表中的所有权限返回
        if (SecurityUtils.isAdmin()) {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
//          2.2菜单类型为C或F
            queryWrapper.in(Menu::getMenuType, MENU, BUTTON);
//          2.3权限状态正常
            queryWrapper.eq(Menu::getStatus, MENU_STATUS_NORMAL);
            List<Menu> menus = menuMapper.selectList(queryWrapper);
            perms = menus
                    .stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
//          2.3当用户id为1的时候表示是管理员,角色信息直接返回"admin"
            roles = new ArrayList<>();
            roles.add("admin");
        } else {
//          2.3当用户为普通用户的时候，查询当前用户所对应的权限
            perms = menuMapper.selectPermsByUserId(userId);
//          2.4查询普通用户的角色信息
            roles = roleMapper.selectRoleKeyByUserId(userId);
        }
//        4.将user对象封装为UserInfoVo对象
        UserInfoVo user = BeanCopyPropertiesUtils.copyBean(loginUser.getUser(), UserInfoVo.class);

//        4.将用户的角色信息和权限信息封装为AdminUserInfoVo对象返回
        AdminUserInfoVo adminUserInfo = new AdminUserInfoVo(perms, roles, user);
        return ResponseResult.okResult(adminUserInfo);
    }


    /**
     * 用户注销
     *
     * @return {@link ResponseResult}
     */
    public ResponseResult userLogout() {
//        1.根据当前用户id将redis中的用户信息删除
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject(ADMIN_USER_LOGIN + userId);
        return ResponseResult.okResult();
    }

    /**
     * 得到用户信息列表
     *
     * @param pageNum     页面num
     * @param pageSize    页面大小
     * @param userInfoDto 用户信息dto
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult getUserInfoList(Integer pageNum, Integer pageSize, UserInfoDto userInfoDto) {
//        1.根据用户名(模糊查询)和状态进行查询
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(userInfoDto.getStatus()), User::getStatus, userInfoDto.getStatus());
        queryWrapper.eq(StringUtils.hasText(userInfoDto.getPhonenumber()), User::getPhonenumber, userInfoDto.getPhonenumber());
        queryWrapper.like(StringUtils.hasText(userInfoDto.getUserName()), User::getUserName, userInfoDto.getUserName());

//        2.分页查询
        Page<User> page = new Page(pageNum, pageSize);
        userMapper.selectPage(page, queryWrapper);
        List<User> users = page.getRecords();

//        3.将当前页中的User对象转换为UserInfoVo对象
        List<UserInfoVo> userInfoVos = BeanCopyPropertiesUtils.copyBeanList(users, UserInfoVo.class);
////        4.将LinkVo对象转换为LinkAdminVo对象
        AdminUserVo adminUserVo = new AdminUserVo(userInfoVos, page.getTotal());
        return ResponseResult.okResult(adminUserVo);
    }

    /**
     * 更新用户状态
     *
     * @param userId 用户id
     * @param status 状态
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult updateUserStatus(Integer userId, Integer status) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, userId)
                .set(User::getStatus, status);
        userService.update(null, updateWrapper);
        return ResponseResult.okResult();
    }


    /**
     * 通过id获取用户信息
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult getUserInfoById(Long id) {
//        1.通过id查询用户信息
        User userById = userService.getById(id);
//          1.1将User对象转换为UpdateUserInfoVo对象
        UpdateUserInfoVo user = BeanCopyPropertiesUtils.copyBean(userById, UpdateUserInfoVo.class);
//        2.查询用户所具有的角色id
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, id);
        List<Long> userRoleId = userRoleService.getUserRoleById(id);
//        3.查询所有角色的列表
        List<Role> allRoleList = roleService.getAllRoleList();
//            3.1将List<Role>对象转换为List<RoleVo>对象
        List<RoleVo> roleVos = BeanCopyPropertiesUtils.copyBeanList(allRoleList, RoleVo.class);

        UpdateUserVo updateUserVo = new UpdateUserVo(userRoleId, roleVos, user);
        return ResponseResult.okResult(updateUserVo);
    }

    /**
     * 更新用户信息
     *
     * @param updateUserInfoVo 更新用户信息签证官
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult updateUserInfo(UpdateUserInfoRoleIdVo updateUserInfoVo) {
//        1.获取到修改后的用户的roleIds列表
        List<Long> roleIds = updateUserInfoVo.getRoleIds();
//        2.将UpdateUserInfoRoleIdVo对象转换为User对象
        User user = BeanCopyPropertiesUtils.copyBean(updateUserInfoVo, User.class);
//          2.1查询当前用户roleIds列表
        List<Long> userRoleById = userRoleService.getUserRoleById(user.getId());
//          2.2遍历修改后的用户的roleIds列表，如果有新增的就添加到sys_user_role表中
        for (Long roleId : roleIds) {
            if (!userRoleById.contains(roleId)) {
                userRoleService.save(new UserRole(user.getId(), roleId));
            }
        }

//        2.根据用户id修改用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, user.getId());
        userService.update(user, queryWrapper);
        return ResponseResult.okResult();
    }

    /**
     * 删除用户
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult deleteUser(Long id) {
//        1.从SecurityContextHolder当中获取到当前登录用户的id
        Long userId = SecurityUtils.getUserId();
        if (userId == id) {
//            1.1如果是当前登录的用户则不允许删除
            return ResponseResult.errorResult(DELETE_USER_REFUSE);
        }
//        2.删除用户所对应sys_user_role表中的角色信息
        userRoleService.removeById(id);
        userService.removeById(id);
        return ResponseResult.okResult();
    }


    /**
     * 添加用户
     *
     * @param adminUserDto 管理用户dto
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult addUser(AdminUserDto adminUserDto) {
//        1.获取到AdminUserDto对象当中的roleIds属性
        List<Long> roleIds = adminUserDto.getRoleIds();
//        2.将AdminUserDto对象转化为User对象
        User user = BeanCopyPropertiesUtils.copyBean(adminUserDto, User.class);
//        3.判断信息是否为空
        if (!StringUtils.hasText(user.getUserName()) ||
                !StringUtils.hasText(user.getNickName()) ||
                !StringUtils.hasText(user.getPassword()) ||
                !StringUtils.hasText(user.getEmail()) ||
                !StringUtils.hasText(user.getPhonenumber()) ||
                !StringUtils.hasText(user.getStatus()) ||
                !StringUtils.hasText(user.getSex())){
            throw new SystemException(CONTENT_IS_BLANK);
        }
//        4.判断信息是否存在
        if (!judgeUsername(user.getUserName())){
            throw new SystemException(USERNAME_EXIST);
        }
        if (!judgePhoneNumber(user.getPhonenumber())){
            throw new SystemException(PHONENUMBER_EXIST);
        }
        if (!judgeEmail(user.getEmail())){
            throw new SystemException(EMAIL_EXIST);
        }
//        5.保存用户
        userService.save(user);
//        6.获取到用户id
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,user.getUserName());
        User getUser = userService.getOne(queryWrapper);

//        7.向sys_user_role表中添加数据
        roleIds.stream()
                .map(roleId -> userRoleService.save
                        (new UserRole(getUser.getId(),roleId)));
        return ResponseResult.okResult();
    }



    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    public boolean judgeUsername(String username){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getUserName, username);
        User user = userService.getOne(queryWrapper);
        if (Objects.isNull(user)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断邮箱是否存在
     * @param email
     * @return
     */
    public boolean judgeEmail(String email){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getEmail, email);
        User user = userService.getOne(queryWrapper);
        if (Objects.isNull(user)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断手机号是否存在
     * @param phonenumber
     * @return
     */
    public boolean judgePhoneNumber(String phonenumber){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getPhonenumber, phonenumber);
        User user = userService.getOne(queryWrapper);
        if (Objects.isNull(user)){
            return true;
        }else{
            return false;
        }
    }


}
