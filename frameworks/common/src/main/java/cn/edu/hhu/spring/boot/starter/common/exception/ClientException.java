package cn.edu.hhu.spring.boot.starter.common.exception;

import cn.edu.hhu.spring.boot.starter.common.enums.BaseErrorCode;

public class ClientException extends AbstractException {
    public ClientException(BaseErrorCode errorCode,String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    public ClientException(BaseErrorCode errorCode,String message) {
        this(errorCode, message, null);
    }
    public ClientException(BaseErrorCode errorCode) {
        this(errorCode, null);
    }
    public ClientException(String message) {
        this(null,message);
    }
    public String toString(){
        return "ClientException{"+
                "code="+errorCode+",message="+errorMessage+"}";
    }
}
