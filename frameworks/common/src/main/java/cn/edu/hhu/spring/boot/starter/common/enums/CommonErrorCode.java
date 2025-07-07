package cn.edu.hhu.spring.boot.starter.common.enums;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum CommonErrorCode implements BaseErrorCode {
    
    // 服务标识
    CLIENT_SERVICE_ERROR("A000000", "客户端错误"),
    SERVER_SERVICE_ERROR("B000000", "服务端错误"),
    REMOTE_SERVICE_ERROR("C000000", "第三方调用错误");
    
    private final String code;
    private final String message;
    
    @Override
    public String getCode() {
        return code;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
}
