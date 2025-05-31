package cn.edu.hhu.spring.boot.starter.web.globalexception;

import cn.edu.hhu.spring.boot.starter.common.exception.AbstractException;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import cn.edu.hhu.spring.boot.starter.common.utils.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 应用内异常
     */
    @ExceptionHandler(value = AbstractException.class)
    public Result<?> abstractException(HttpServletRequest request, AbstractException e) {
        log.error("[{}] {} [e] {}", request.getMethod(), request.getRequestURI(), e.toString());
        return ResultUtil.fail(e);
    }
    /**
     * 拦截未捕获异常
     */
    @ExceptionHandler(value = Throwable.class)
    public Result<?> defaultErrorHandler(HttpServletRequest request,Throwable e) {
        log.error("[{}] {}", request.getMethod(), request.getRequestURI(), e);
        return ResultUtil.fail();
    }
}
