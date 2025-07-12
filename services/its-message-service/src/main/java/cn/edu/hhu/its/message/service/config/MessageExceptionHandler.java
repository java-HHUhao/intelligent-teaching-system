package cn.edu.hhu.its.message.service.config;

import cn.edu.hhu.its.message.service.common.enums.MessageErrorCode;
import cn.edu.hhu.spring.boot.starter.common.exception.AbstractException;
import cn.edu.hhu.spring.boot.starter.common.exception.ServerException;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import cn.edu.hhu.spring.boot.starter.common.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import java.util.concurrent.TimeoutException;

/**
 * 消息模块全局异常处理器
 * 
 * @author ITS项目组
 */
@Slf4j
@RestControllerAdvice
public class MessageExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(AbstractException.class)
    public Result<Void> handleAbstractException(AbstractException e) {
        log.error("业务异常: {}", e.getMessage(), e);
        return ResultUtil.fail(e);
    }

    /**
     * 处理服务异常
     */
    @ExceptionHandler(ServerException.class)
    public Result<Void> handleServerException(ServerException e) {
        log.error("服务异常: {}", e.getMessage(), e);
        return ResultUtil.fail(MessageErrorCode.SYSTEM_BUSY);
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("参数验证异常: {}", e.getMessage(), e);
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .findFirst()
                .orElse("参数验证失败");
        
        return new Result<Void>()
                .setCode(MessageErrorCode.PARAM_VALIDATION_ERROR.getCode())
                .setMessage(MessageErrorCode.PARAM_VALIDATION_ERROR.getMessage() + ": " + errorMessage);
    }

    /**
     * 处理约束验证异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("约束验证异常: {}", e.getMessage(), e);
        return new Result<Void>()
                .setCode(MessageErrorCode.PARAM_VALIDATION_ERROR.getCode())
                .setMessage(MessageErrorCode.PARAM_VALIDATION_ERROR.getMessage() + ": " + e.getMessage());
    }

    /**
     * 处理超时异常
     */
    @ExceptionHandler(TimeoutException.class)
    public Result<Void> handleTimeoutException(TimeoutException e) {
        log.error("消息发送超时: {}", e.getMessage(), e);
        return new Result<Void>()
                .setCode(MessageErrorCode.MESSAGE_SEND_ERROR.getCode())
                .setMessage("消息发送超时: " + e.getMessage());
    }

    /**
     * 处理权限异常
     */
    @ExceptionHandler(SecurityException.class)
    public Result<Void> handleSecurityException(SecurityException e) {
        log.error("权限异常: {}", e.getMessage(), e);
        return ResultUtil.fail(MessageErrorCode.PERMISSION_DENIED);
    }

    /**
     * 处理IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("参数异常: {}", e.getMessage(), e);
        return new Result<Void>()
                .setCode(MessageErrorCode.PARAM_VALIDATION_ERROR.getCode())
                .setMessage(MessageErrorCode.PARAM_VALIDATION_ERROR.getMessage() + ": " + e.getMessage());
    }

    /**
     * 处理其他运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<Void> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常: {}", e.getMessage(), e);
        return ResultUtil.fail(MessageErrorCode.SYSTEM_BUSY);
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return ResultUtil.fail(MessageErrorCode.SYSTEM_BUSY);
    }
} 