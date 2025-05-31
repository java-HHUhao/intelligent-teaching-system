package cn.edu.hhu.spring.boot.starter.common.exception;

import cn.edu.hhu.spring.boot.starter.common.enums.BaseErrorCode;
import lombok.Getter;

import java.util.Optional;

/**
 * 抽象异常类，子类为三类异常，客户端异常，服务端异常，远端调用异常
 */
@Getter
public abstract class AbstractException extends RuntimeException {
    protected final String errorCode;
    protected final String errorMessage;
    public AbstractException(BaseErrorCode errorCode,String errorMessage,Throwable throwable) {
        super(errorMessage,throwable);
        this.errorCode= errorCode.getCode();
        this.errorMessage=Optional.ofNullable(errorMessage).orElse(errorCode.getMessage());
    }
}
