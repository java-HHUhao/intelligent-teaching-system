package cn.edu.hhu.its.user.service.common.enums;

import cn.edu.hhu.spring.boot.starter.common.enums.BaseErrorCode;

public enum UserErrorCode implements BaseErrorCode {
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
    USER_PASSWORD_ERROR("A000203", "用户名或密码错误");


    private final String code;
    private final String message;

    UserErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() { return code; }

    @Override
    public String getMessage() { return message; }
}