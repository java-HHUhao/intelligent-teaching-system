package cn.edu.hhu.spring.boot.starter.idempotent.common.enums;

/**
 * 幂等场景枚举
 */
public enum IdempotentSceneEnum {

    /**
     * 基于 RestAPI 场景验证
     */
    RESTAPI,

    /**
     * 基于 MQ 场景验证
     */
    MQ
}
