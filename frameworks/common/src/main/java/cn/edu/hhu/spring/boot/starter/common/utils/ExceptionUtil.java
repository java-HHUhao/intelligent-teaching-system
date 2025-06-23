package cn.edu.hhu.spring.boot.starter.common.utils;

import java.util.function.Supplier;

public class ExceptionUtil {
    /**
     * 如果 condition 为 true，抛出默认 RuntimeException
     */
    public static void throwIf(boolean condition, String message) {
        if (condition) {
            throw new RuntimeException(message);
        }
    }
    /**
     * 如果 condition 为 true，抛出指定异常（使用 Supplier 延迟创建）
     */
    public static void throwIf(boolean condition, Supplier<? extends Throwable> exceptionSupplier) {
        if (condition) {
            throwUnchecked(exceptionSupplier.get());
        }
    }

    /**
     * 捕获异常并返回兜底结果
     */
    public static <T> T catchOrDefault(Supplier<T> supplier, T defaultValue) {
        try{
            return supplier.get();
        }catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 抛出任意 Throwable，不受编译器检查限制
     */
    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void throwUnchecked(Throwable throwable) throws T {
        throw (T) throwable;
    }

}
