package cn.edu.hhu.its.user.service.common.enums;

import cn.edu.hhu.spring.boot.starter.common.enums.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户模块错误码
 */
@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode{
    //二级宏观错误码 用户注册错误
    USER_REGISTER_ERROR("A000100", "用户注册错误"),
    USER_NAME_VERIFY_ERROR("A000110", "用户名校验失败"),
    USER_NAME_EXIST_ERROR("A000111", "用户名已存在"),
    USER_NAME_SENSITIVE_ERROR("A000112", "用户名包含敏感词"),
    USER_NAME_SPECIAL_CHARACTER_ERROR("A000113", "用户名包含特殊字符"),

    PASSWORD_VERIFY_ERROR("A000120", "密码校验失败"),
    PASSWORD_SHORT_ERROR("A000121", "密码长度不够"),
    CONFIRM_PASSWORD_ERROR("A000122", "两次密码不一致"),

    PHONE_VERIFY_ERROR("A000151", "手机格式校验失败"),

    EMAIL_VERIFY_ERROR("A000161", "邮箱格式校验失败"),
    //二级宏观错误码 用户登录错误
    USER_LOGIN_ERROR("A000200", "用户登录错误"),
    USER_NOT_EXIST("A000201", "用户不存在"),
    USER_FORBIDDEN("A000202", "用户已被禁用"),
    USER_PASSWORD_ERROR("A000203", "用户名或密码错误"),
    //二级宏观错误码 鉴权错误
    USER_AUTH_ERROR("A000300", "用户鉴权错误"),
    TOKEN_EXPIRED("A000301", "TOKEN已过期"),
    TOKEN_INVALID("A000302", "TOKEN非法"),
    TOKEN_NOT_FOUND("A000303", "TOKEN不存在"),
    //二级宏观错误码 角色管理错误
    ROLE_ERROR("A000400", "角色管理错误"),
    ROLE_NAME_EXIST_ERROR("A000401", "角色名已存在"),
    USER_ALREADY_EXISTS("A000403", "用户已存在"),
    ROLE_NAME_EXISTS("A000404", "角色名已存在"),
    ROLE_NOT_FOUND("A000405", "角色不存在"),
    PERMISSION_NOT_FOUND("A000406", "权限不存在"),
    PERMISSION_HAS_CHILDREN("A000407", "该权限存在子权限，无法删除"),
    PERMISSION_CODE_EXIST("A000408", "权限编码已存在"),
    PERMISSION_PARENT_ERROR("A000409", "无效的父权限设置"),
    PROFILE_UPDATE_EMPTY("A000410", "个人资料更新字段不能全为空"),
    ACCOUNT_UPDATE_EMPTY("A000405", "账户信息更新字段不能全为空"),
    EMAIL_VERIFY_ERROR_USED("A000402", "邮箱已被使用"),
    PHONE_VERIFY_ERROR_USED("A000403", "手机号已被使用"),
    USERNAME_ALREADY_EXISTS("A000406", "用户名已被使用"),
    ;

    private final String code;
    private final String message;

    @Override
    public String getCode() { return code; }

    @Override
    public String getMessage() { return message; }
}