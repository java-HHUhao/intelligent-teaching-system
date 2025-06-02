package cn.edu.hhu.spring.boot.starter.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface ILog {
    /**
     * 入参打印
     * @return 是否含有入参
     */
    boolean input() default true;

    /**
     * 出参打印
     * @return 是否含有出参
     */
    boolean output() default true;
}
