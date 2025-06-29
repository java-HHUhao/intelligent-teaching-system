package cn.edu.hhu.spring.boot.starter.common.utils;

import cn.edu.hhu.spring.boot.starter.common.enums.BaseErrorCode;
import cn.edu.hhu.spring.boot.starter.common.enums.CommonErrorCode;
import cn.edu.hhu.spring.boot.starter.common.exception.AbstractException;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import java.lang.Void;
import java.util.Optional;

public class ResultUtil {
    public static <T>Result<T> success(T data){
        return new Result<T>().setCode("0").setData(data);
    }
    public static Result<?> success(){
        return new Result<>().setCode("0");
    }
    public static Result<Void> fail(String message,String code){
        return new Result<Void>().setCode(code).setMessage(message);
    }
    public static Result<Void> fail(BaseErrorCode errorCode){
        return new Result<Void>().setCode(errorCode.getCode()).setMessage(errorCode.getMessage());
    }
    public static Result<Void> fail(AbstractException exception){
        return new Result<Void>().setCode(Optional.ofNullable(exception.getErrorCode()).orElse(CommonErrorCode.SERVICE_ERROR.getCode()))
                .setMessage(Optional.ofNullable(exception.getMessage()).orElse(exception.getMessage()));
    }
    public static Result<Void> fail(){
        return new Result<Void>().setCode(CommonErrorCode.SERVICE_ERROR.getCode()).setMessage(CommonErrorCode.SERVICE_ERROR.getMessage());
    }
}
