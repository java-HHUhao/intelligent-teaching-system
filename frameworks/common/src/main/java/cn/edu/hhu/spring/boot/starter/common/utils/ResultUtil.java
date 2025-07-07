package cn.edu.hhu.spring.boot.starter.common.utils;

import cn.edu.hhu.spring.boot.starter.common.enums.BaseErrorCode;
import cn.edu.hhu.spring.boot.starter.common.enums.CommonErrorCode;
import cn.edu.hhu.spring.boot.starter.common.exception.AbstractException;
import cn.edu.hhu.spring.boot.starter.common.result.Result;

import java.util.Optional;

/**
 * 统一返回结果工具类
 */
public class ResultUtil {
    public static <T> Result<T> success(T data) {
        return new Result<T>().setCode("0").setData(data);
    }
    
    public static <T> Result<T> success() {
        return new Result<T>().setCode("0");
    }
    
    public static <T> Result<T> fail(String message, String code) {
        return new Result<T>().setCode(code).setMessage(message);
    }
    
    public static <T> Result<T> fail(BaseErrorCode errorCode) {
        return new Result<T>().setCode(errorCode.getCode()).setMessage(errorCode.getMessage());
    }
    
    public static <T> Result<T> fail(AbstractException exception) {
        return new Result<T>()
                .setCode(Optional.ofNullable(exception.getErrorCode())
                        .orElse(CommonErrorCode.SERVER_SERVICE_ERROR.getCode()))
                .setMessage(Optional.ofNullable(exception.getMessage())
                        .orElse(exception.getMessage()));
    }
    
    public static <T> Result<T> fail() {
        return new Result<T>()
                .setCode(CommonErrorCode.SERVER_SERVICE_ERROR.getCode())
                .setMessage(CommonErrorCode.SERVER_SERVICE_ERROR.getMessage());
    }
    
    /**
     * 条件判断返回，如果条件为真则返回错误码
     *
     * @param condition 判断条件
     * @param errorCode 错误码
     * @return 如果条件为真返回错误结果，否则返回null
     */
    public static <T> Result<T> returnIf(boolean condition, BaseErrorCode errorCode) {
        return condition ? fail(errorCode) : null;
    }
    
    /**
     * 条件判断返回，如果条件为真则返回错误码和数据
     *
     * @param condition 判断条件
     * @param errorCode 错误码
     * @param data      返回数据
     * @return 如果条件为真返回错误结果和数据，否则返回null
     */
    public static <T> Result<T> returnIf(boolean condition, BaseErrorCode errorCode, T data) {
        return condition ? new Result<T>()
                .setCode(errorCode.getCode())
                .setMessage(errorCode.getMessage())
                .setData(data) : null;
    }
}