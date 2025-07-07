package cn.edu.hhu.spring.boot.starter.common.enums;

/**
 * 错误码基础接口
 */
public interface BaseErrorCode {
    /**
     * 获取错误码
     *
     * @return 错误码
     */
    String getCode();
    
    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    String getMessage();
}
