package cn.edu.hhu.spring.boot.starter.log.core;

import cn.edu.hhu.spring.boot.starter.log.annotation.ILog;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Optional;

@Aspect
public class LogAspect {
    @Around("@within(cn.edu.hhu.spring.boot.starter.log.annotation.ILog)|| @annotation(cn.edu.hhu.spring.boot.starter.log.annotation.ILog)")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        //获得代理对象 代理的方法签名
        MethodSignature methodSignature= (MethodSignature) joinPoint.getSignature();
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        Object result=null;

        String beginTime= DateUtil.now();
        try {
            result = joinPoint.proceed();
        }finally {
            //获得要被代理的方法
            Method targetMethod=joinPoint.getTarget().getClass().getDeclaredMethod(methodSignature.getName(),methodSignature.getParameterTypes());
            //获得被代理方法或者类上的注解
            ILog annotation= Optional.ofNullable(targetMethod.getAnnotation(ILog.class)).orElse(joinPoint.getTarget().getClass().getAnnotation(ILog.class));
            //如果有注解执行增强
            if(annotation!=null){
                LogDTO logDTO=new LogDTO();
                logDTO.setBeginTime(beginTime);
                //获得入参和出参
                if(annotation.input()){
                    logDTO.setInputParams(joinPoint.getArgs());
                }
                if (annotation.output()){
                    logDTO.setReturnValue(result);
                }
                //获取方法类型和url
                String methodType="",requestUrl="";
                try{
                    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    assert attributes != null;
                    methodType=attributes.getRequest().getMethod();
                    requestUrl=attributes.getRequest().getRequestURI();
                }catch (Exception ignore){}
                logger.info("[{}] {},executeTime:{}ms,info:{}",methodType,requestUrl,System.currentTimeMillis()-startTime, JSON.toJSONString(logDTO));
            }
        }
        return result;
    }
}
