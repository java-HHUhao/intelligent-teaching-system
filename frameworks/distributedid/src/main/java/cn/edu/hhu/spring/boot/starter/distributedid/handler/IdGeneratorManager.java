package cn.edu.hhu.spring.boot.starter.distributedid.handler;

import cn.edu.hhu.spring.boot.starter.distributedid.core.IdGenerator;
import cn.edu.hhu.spring.boot.starter.distributedid.core.serviceid.DefaultServiceIdGenerator;
import cn.edu.hhu.spring.boot.starter.distributedid.core.serviceid.ServiceIdGenerator;
import lombok.NonNull;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class IdGeneratorManager {

    /**
     * ID 生成器管理容器
     */
    private static Map<String, IdGenerator> MANAGER = new ConcurrentHashMap<>();

    /**
     * 注册默认 ID 生成器
     */
    static {
        MANAGER.put("default", new DefaultServiceIdGenerator());
    }

    /**
     * 注册 ID 生成器
     */
    public static void registerIdGenerator(@NonNull String resource, @NonNull IdGenerator idGenerator) {
        IdGenerator actual = MANAGER.get(resource);
        if (actual != null) {
            return;
        }
        MANAGER.put(resource, idGenerator);
    }

    /**
     * 根据 {@param resource} 获取 ID 生成器
     */
    public static ServiceIdGenerator getIdGenerator(@NonNull String resource) {
        return Optional.ofNullable(MANAGER.get(resource)).map(each -> (ServiceIdGenerator) each).orElse(null);
    }

    /**
     * 获取默认 ID 生成器 {@link DefaultServiceIdGenerator}
     */
    public static ServiceIdGenerator getDefaultServiceIdGenerator() {
        return Optional.ofNullable(MANAGER.get("default")).map(each -> (ServiceIdGenerator) each).orElse(null);
    }
}

