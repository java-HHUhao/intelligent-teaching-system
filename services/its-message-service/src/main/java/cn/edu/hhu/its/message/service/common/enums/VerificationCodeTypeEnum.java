package cn.edu.hhu.its.message.service.common.enums;

import lombok.Getter;

/**
 * 验证码类型枚举
 */
@Getter
public enum VerificationCodeTypeEnum {
    EMAIL("邮箱验证码"),
    SMS("短信验证码"),
    IMAGE("图片验证码"),
    TOTP("TOTP动态验证码");
    
    private final String description;
    
    VerificationCodeTypeEnum(String description) {
        this.description = description;
    }

    public String getCode() {
        return this.name();
    }
} 