package cn.edu.hhu.spring.boot.starter.idempotent.common.enums;

/**
 * 幂等解决方法枚举
 */
public enum IdempotentMethodEnum {
    /**
     * 基于 Token 方式验证
     */
    TOKEN,

    /**
     * 基于方法参数方式验证
     */
    PARAM,

    /**
     * 基于 SpEL 表达式方式验证
     */
    SPEL
}
