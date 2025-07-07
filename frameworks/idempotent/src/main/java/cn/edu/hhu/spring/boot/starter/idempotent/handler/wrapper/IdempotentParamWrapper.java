package cn.edu.hhu.spring.boot.starter.idempotent.handler.wrapper;

import cn.edu.hhu.spring.boot.starter.idempotent.aop.annotation.Idempotent;
import cn.edu.hhu.spring.boot.starter.idempotent.common.enums.IdempotentMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 米等参数包装器
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public final class IdempotentParamWrapper {

    /**
     * 幂等注解
     */
    private Idempotent idempotent;

    /**
     * AOP 处理连接点
     */
    private ProceedingJoinPoint joinPoint;

    /**
     * 锁标识，{@link IdempotentMethodEnum#PARAM}
     */
    private String lockKey;
}

