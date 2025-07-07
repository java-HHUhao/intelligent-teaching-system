package cn.edu.hhu.spring.boot.starter.idempotent.handler;

import cn.edu.hhu.spring.boot.starter.idempotent.aop.annotation.Idempotent;
import cn.edu.hhu.spring.boot.starter.idempotent.handler.wrapper.IdempotentParamWrapper;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 幂等处理器接口
 */
public interface IdempotentExecuteHandler {
    /**
     * 具体的幂等处理逻辑
     *
     * @param wrapper 幂等参数包装器
     */
    void handler(IdempotentParamWrapper wrapper);

    /**
     * 对aop暴露的执行幂等处理逻辑
     *
     * @param joinPoint  AOP 方法处理
     * @param idempotent 幂等注解
     */
    void execute(ProceedingJoinPoint joinPoint, Idempotent idempotent);

    /**
     * 异常流程处理
     */
    default void exceptionProcessing() {

    }

    /**
     * 后置处理
     */
    default void postProcessing() {

    }
}
