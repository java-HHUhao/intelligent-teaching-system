package cn.edu.hhu.spring.boot.starter.common.enums;

public enum CommonErrorCode implements BaseErrorCode {
    CLIENT_ERROR("A000001", "用户端错误"),
    SERVICE_ERROR("B000001", "系统执行出错"),
    REMOTE_ERROR("C000001", "调用第三方服务出错");

    private final String code;
    private final String message;
    private CommonErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
