package cn.edu.hhu.spring.boot.starter.designpattern.chain;

import org.springframework.core.Ordered;

public interface AbstractChainHandler<T> extends Ordered {
    /**
     * 处理
     *
     */
    void handle(T t);

    /**
     * 责任链标识
     *
     */
    String mark();
}
