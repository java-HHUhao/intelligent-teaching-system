package cn.edu.hhu.spring.boot.starter.idempotent.aop;

import cn.edu.hhu.spring.boot.starter.idempotent.aop.annotation.Idempotent;
import cn.edu.hhu.spring.boot.starter.idempotent.handler.IdempotentExecuteHandler;
import cn.edu.hhu.spring.boot.starter.idempotent.handler.context.IdempotentContext;
import cn.edu.hhu.spring.boot.starter.idempotent.handler.exception.RepeatConsumptionException;
import cn.edu.hhu.spring.boot.starter.idempotent.handler.factory.IdempotentExecuteHandlerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public class IdempotentAspect {
    @Around("@annotation(cn.edu.hhu.spring.boot.starter.idempotent.aop.annotation.Idempotent)")
    public Object idempotent(ProceedingJoinPoint joinPoint) throws Throwable {
        Idempotent annotation = getAnnotation(joinPoint);
        IdempotentExecuteHandler instance= IdempotentExecuteHandlerFactory.getInstance(annotation.scene(),annotation.type());
        Object result;
        try {
            instance.execute(joinPoint,annotation);
            result=joinPoint.proceed();
            instance.postProcessing();
        }catch (RepeatConsumptionException ex){
            /**
             * 触发幂等逻辑时可能有两种情况：
             *    * 1. 消息还在处理，但是不确定是否执行成功，那么需要返回错误，方便 RocketMQ 再次通过重试队列投递
             *    * 2. 消息处理成功了，该消息直接返回成功即可
             */
            if (!ex.getError()) {
                return null;
            }
            throw ex;
        }catch (Throwable ex){
            // 客户端消费存在异常，需要删除幂等标识方便下次 RocketMQ 再次通过重试队列投递
            instance.exceptionProcessing();
            throw ex;
        }finally {
            IdempotentContext.clean();
        }
        return result;
    }
    public static Idempotent getAnnotation(ProceedingJoinPoint joinPoint) throws NoSuchMethodException{
        MethodSignature methodSignature= (MethodSignature) joinPoint.getSignature();
        Method targetMethod= joinPoint.getTarget().getClass().getDeclaredMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        return targetMethod.getAnnotation(Idempotent.class);
    }
}
