package cn.edu.hhu.spring.boot.starter.context.init;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class ApplicationInitializingEvent extends ApplicationEvent {

    public ApplicationInitializingEvent(Object source) {
        super(source);
    }
}
