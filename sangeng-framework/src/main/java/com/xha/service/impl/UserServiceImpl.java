package com.xha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xha.domain.ResponseResult;
import com.xha.domain.entity.LoginUser;
import com.xha.domain.entity.User;
import com.xha.domain.vo.BlogUserLoginVo;
import com.xha.domain.vo.UserInfoVo;
import com.xha.enums.AppHttpCodeEnum;
import com.xha.exception.SystemException;
import com.xha.mapper.UserMapper;
import com.xha.service.UserService;
import com.xha.utils.BeanCopyPropertiesUtils;
import com.xha.utils.JwtUtils;
import com.xha.utils.RedisCache;
import com.xha.utils.SecurityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Objects;

import static com.xha.constants.RedisConstants.BLOG_USER_LOGIN;


/**
 * @author Tony贾维斯
 * @description 针对表【sys_user(用户表)】的数据库操作Service实现
 * @createDate 2022-11-08 09:21:53
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisCache redisCache;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 用户注册
     * @return
     * @param user
     */
    @Override
    public ResponseResult userRegister(User user) {
//        1.数据非空校验
        if (!StringUtils.hasText(user.getUserName())
                && !StringUtils.hasText(user.getPassword())
                && !StringUtils.hasText(user.getEmail())
                && !StringUtils.hasText(user.getNickName())
                && !StringUtils.hasText(user.getPhonenumber())){
            throw new SystemException(AppHttpCodeEnum.REGISTER_NOT_NULL);
        }
//        2.数据是否存在校验
        if (!judgeUsername(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (!judgeNickname(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (!judgeEmail(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
//        3.密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
//        4.添加用户
        return ResponseResult.okResult();
    }

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
//          4.1生成JWT
        String jwt = JwtUtils.createJWT(userId);

//        5.将用户信息存入缓存
        redisCache.setCacheObject(BLOG_USER_LOGIN + userId, loginUser);
//        6.将User对象转换为UserInfo对象
        UserInfoVo userInfoVo = BeanCopyPropertiesUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
//        7.将UserInfo对象封装为BlogUserLoginVo对象
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt, userInfoVo);
//        7.将token和用户信息封装返回
        return ResponseResult.okResult(blogUserLoginVo);
    }


    /**
     * 用户退出
     *
     * @return
     */
    public ResponseResult userLogout() {
//        1.获取到SecurityContextHolder中的LoginUser对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        2.获取到userId
        Long userId = loginUser.getUser().getId();
//        3.中redis中删除缓存
        redisCache.deleteObject(BLOG_USER_LOGIN + userId);
        return ResponseResult.okResult();
    }

    /**
     * 查询用户信息
     *
     * @return
     */
    @Override
    public ResponseResult getUserInfo() {
//        1.获取到当前用户信息
        Long userId = SecurityUtils.getUserId();
        User user = getById(userId);
//        2.将User对象封装为UserInfoVo对象
        UserInfoVo userInfo = BeanCopyPropertiesUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfo);
    }

    /**
     * 用户修改信息
     *
     * @param user
     * @return
     */
    @Override
    public ResponseResult updateUserInfo(User user) {
//        1.更新数据库
        boolean result = updateById(user);
        if (Objects.isNull(result)) {
            return ResponseResult.errorResult(408, "更新失败");
        }
//        2.删除缓存
        redisCache.deleteObject(BLOG_USER_LOGIN + user.getId());
//        3.查询新的用户信息
        User newUser = getById(user.getId());
        LoginUser loginUser = new LoginUser(newUser,null);
//        4.将用户信息存入缓存
        redisCache.setCacheObject(BLOG_USER_LOGIN + user.getId(), loginUser);
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
        User user = getOne(queryWrapper);
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
        User user = getOne(queryWrapper);
        if (Objects.isNull(user)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断昵称是否存在
     * @param nickname
     * @return
     */
    public boolean judgeNickname(String nickname){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getNickName, nickname);
        User user = getOne(queryWrapper);
        if (Objects.isNull(user)){
            return true;
        }else{
            return false;
        }
    }


}




