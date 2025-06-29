package cn.edu.hhu.spring.boot.starter.common.exception;

import cn.edu.hhu.spring.boot.starter.common.enums.BaseErrorCode;

public class ServerException extends AbstractException {
    public ServerException(BaseErrorCode errorCode, String message, Throwable cause) {
        super(errorCode,message,cause);
    }
    public ServerException(BaseErrorCode errorCode,String message) {
        this(errorCode,message,null);
    }
    public ServerException(BaseErrorCode errorCode) {
        this(errorCode,null);
    }
    public ServerException(String message) {
        this(null,message);
    }
    public String toString(){
        return "ServerException{"+
                "code="+errorCode+",message="+errorMessage+"}";
    }
}
