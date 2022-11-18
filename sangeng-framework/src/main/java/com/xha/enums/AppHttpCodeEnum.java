package com.xha.enums;


public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200, "操作成功"),
    // 登录
    NEED_LOGIN(401, "需要登录后操作"),
    NO_OPERATOR_AUTH(403, "无权限操作"),
    SYSTEM_ERROR(500, "出现错误"),
    EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    CONTENT_NOT_NULL(506, "评论内容不能为空"),
    FILE_TYPE_ERROR(507, "文件类型错误，请上传jpg文件"),
    REGISTER_NOT_NULL(508, "注册信息不完整"),
    USERNAME_EXIST(501, "用户名已存在"),
    PHONENUMBER_EXIST(502, "手机号已存在"),
    NICKNAME_EXIST(512, "昵称已存在"),
    LOGIN_ERROR(505, "用户名或密码错误"),
    COMMENT_NOT_NULL(506, "评论内容不能为空"),
    TAG_IS_EXIST(507, "该标签已经存在"),
    TAG_IS_NOEXIST(508, "该标签已经存在"),
    CONTENT_IS_BLANK(509, "内容不能为空"),
    LINK_IS_EXIST(510, "当前友链已经存在"),
    DELETE_LINK_FAIL(511, "删除友链失败"),
    CATEGORY_IS_EXIST(512, "当前分类已经存在"),
    DELETE_CATEGORY_FAIL(513, "删除文章分类失败"),
    DELETE_ARTICLE_FAIL(514, "删除文章失败"),
    ADD_MENU_FAIL(515, "当前菜单已存在"),
    DELETE_MENU_REFUSE(516, "当前菜单存在子菜单，不允许删除"),
    DELETE_USER_REFUSE(517, "不能删除当前已经登录的用户"),
    USER_INFO_EXIST(518, "用户信息已经存在"),
    ROLE_INFO_EXIST(518, "角色信息已经存在"),
    ROLEKEY_INFO_EXIST(518, "角色权限信息已经存在")
    ;
    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage) {
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
