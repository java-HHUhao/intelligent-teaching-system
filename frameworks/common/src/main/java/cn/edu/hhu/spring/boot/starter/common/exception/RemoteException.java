package cn.edu.hhu.spring.boot.starter.common.exception;

import cn.edu.hhu.spring.boot.starter.common.enums.BaseErrorCode;

public class RemoteException extends AbstractException {
    public RemoteException(BaseErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    public RemoteException(BaseErrorCode errorCode, String message) {
        this(errorCode,message,null);
    }
    public RemoteException(BaseErrorCode errorCode) {
        this(errorCode,null);
    }
    public RemoteException(String message){
        this(null,message);
    }
    public String toString(){
        return "RemoteException{"+
                "code="+errorCode+",message="+errorMessage+"}";
    }
}
