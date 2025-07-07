package cn.edu.hhu.spring.boot.starter.idempotent.aop.annotation;

import cn.edu.hhu.spring.boot.starter.idempotent.common.enums.IdempotentMethodEnum;
import cn.edu.hhu.spring.boot.starter.idempotent.common.enums.IdempotentSceneEnum;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {
    /**
     * 触发幂等失败逻辑时，返回的错误提示信息
     */
    String message() default "您操作太快，请稍后再试";
    /**
     * 验证幂等类型，支持多种幂等方式
     * RestAPI 建议使用 {@link IdempotentMethodEnum#TOKEN} 或 {@link IdempotentMethodEnum#PARAM}
     * 其它类型幂等验证，使用 {@link IdempotentMethodEnum#SPEL}
     */
    IdempotentMethodEnum type() default IdempotentMethodEnum.PARAM;
    /**
     * 验证幂等场景，支持多种 {@link IdempotentSceneEnum}
     */
    IdempotentSceneEnum scene() default IdempotentSceneEnum.RESTAPI;
    /**
     * 幂等Key，只有在 {@link Idempotent#type()} 为 {@link IdempotentMethodEnum#SPEL} 时生效
     */
    String key() default "";
    /**
     * 设置防重令牌 Key 前缀，MQ 幂等去重可选设置
     * {@link IdempotentSceneEnum#MQ} and {@link IdempotentMethodEnum#SPEL} 时生效
     */
    String uniqueKeyPrefix() default "";
    /**
     * 设置防重令牌 Key 过期时间，单位秒，默认 1 小时，MQ 幂等去重可选设置
     * {@link IdempotentSceneEnum#MQ} and {@link IdempotentMethodEnum#SPEL} 时生效
     */
    long keyTimeout() default 3600L;
}
