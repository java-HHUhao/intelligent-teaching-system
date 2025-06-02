package cn.edu.hhu.spring.boot.starter.context.contextholder;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.util.Map;

public class ApplicationContextHolder implements ApplicationContextAware {
    public static ApplicationContext CONTEXT;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHolder.CONTEXT = applicationContext;
    }

    /**
     * 按照类型获取bean
     */
    public static <T> T getBean(Class<T> clazz){ return CONTEXT.getBean(clazz); }

    /**
     * 按照类名获取bean
     */
    public static Object getBean(String name){ return CONTEXT.getBean(name); }

    /**
     * 按照类型和名称获取bean
     */
    public static <T> T getBean(String name, Class<T> clazz){ return CONTEXT.getBean(name, clazz); }

    /**
     *获取带有相同注解的容器
     */
    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotation){ return CONTEXT.getBeansWithAnnotation(annotation); }

    /**
     *获取带有相同类型的容器
     */
    public static <T>Map<String,T> getBeansOfType(Class<T> classType){ return CONTEXT.getBeansOfType(classType); }

    public ApplicationContext getApplicationContext(){ return CONTEXT; }
}
