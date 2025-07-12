package cn.edu.hhu.its.gateway.service.filter;

import cn.edu.hhu.its.gateway.service.client.UserServiceClient;
import cn.edu.hhu.spring.boot.starter.common.dto.UserPermissionDTO;
import cn.edu.hhu.spring.boot.starter.common.cache.user.UserCacheExpire;
import cn.edu.hhu.spring.boot.starter.common.cache.user.UserCachePrefix;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Order(3)
@RequiredArgsConstructor
public class GlobalPermissionExistFilter implements GlobalFilter {
    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectProvider<UserServiceClient> userServiceClientProvider;
    private final ObjectMapper objectMapper;

    private static final int MAX_RETRIES = 3;
    private static final Duration INITIAL_BACKOFF = Duration.ofMillis(100);
    private static final Duration MAX_BACKOFF = Duration.ofSeconds(1);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Boolean isWhite = exchange.getAttribute("isWhite");
        if (Boolean.TRUE.equals(isWhite)) {
            // 是白名单，跳过 token 校验
            log.debug("白名单请求，跳过权限校验");
            return chain.filter(exchange);
        }
        
        // 1. 获取用户信息
        String userId = exchange.getRequest().getHeaders().getFirst("X-UserId");
        String username = exchange.getRequest().getHeaders().getFirst("X-Username");
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(username)) {
            log.warn("用户信息丢失，userId: {}, username: {}", userId, username);
            return unauthorized(exchange, "用户信息丢失");
        }

        log.debug("开始获取用户权限，userId: {}, username: {}", userId, username);

        // 2. 尝试从缓存中获取权限信息
        String cacheKey = UserCachePrefix.PERMISSIONS + userId;
        
        return Mono.fromCallable(() -> {
            Object cachedValue = redisTemplate.opsForValue().get(cacheKey);
            log.debug("从缓存获取权限信息，key: {}, value: {}", cacheKey, cachedValue);
            
            if (cachedValue != null) {
                try {
                    // 直接尝试类型转换
                    if (cachedValue instanceof UserPermissionDTO) {
                        log.debug("缓存数据直接转换成功");
                        return (UserPermissionDTO) cachedValue;
                    }
                    
                    // 处理GenericJackson2JsonRedisSerializer的类型化JSON格式
                    log.debug("尝试反序列化缓存数据，数据类型: {}", cachedValue.getClass().getName());
                    String jsonStr = objectMapper.writeValueAsString(cachedValue);
                    log.debug("序列化后的JSON: {}", jsonStr);
                    
                    JsonNode jsonNode = objectMapper.readTree(jsonStr);
                    if (jsonNode.isArray() && jsonNode.size() == 2) {
                        // 类型化JSON格式：[类型标识, 实际数据]
                        JsonNode dataNode = jsonNode.get(1);
                        log.debug("提取实际数据节点: {}", dataNode);
                        UserPermissionDTO result = objectMapper.treeToValue(dataNode, UserPermissionDTO.class);
                        log.debug("反序列化成功: {}", result);
                        return result;
                    } else {
                        // 尝试直接反序列化
                        UserPermissionDTO result = objectMapper.readValue(jsonStr, UserPermissionDTO.class);
                        log.debug("直接反序列化成功: {}", result);
                        return result;
                    }
                } catch (Exception e) {
                    log.error("权限数据反序列化失败，删除缓存重新获取: {}", e.getMessage(), e);
                    redisTemplate.delete(cacheKey);
                }
            }
            
            log.debug("缓存中没有权限信息或反序列化失败，从用户服务获取");
            // 从用户服务获取权限
            UserServiceClient userServiceClient = userServiceClientProvider.getIfAvailable();
            if (userServiceClient == null) {
                log.error("用户服务客户端不可用");
                throw new RuntimeException("用户服务不可用");
            }

            try {
                log.debug("调用用户服务获取权限，userId: {}, username: {}", userId, username);
                Result<UserPermissionDTO> result = userServiceClient.getUserPermissions(Long.valueOf(userId), username);
                log.debug("用户服务返回结果: {}", result);
                
                if (result == null || result.getData() == null) {
                    log.error("用户服务返回空数据");
                    throw new RuntimeException("获取用户权限失败");
                }

                UserPermissionDTO permissions = result.getData();
                log.debug("获取到权限数据: {}", permissions);
                
                // 将权限信息存入缓存
                redisTemplate.opsForValue().set(cacheKey, permissions, UserCacheExpire.USER_PERMISSION_EXPIRE, TimeUnit.SECONDS);
                log.debug("权限信息已写入缓存");
                
                return permissions;
            } catch (Exception e) {
                log.error("调用用户服务失败: {}", e.getMessage(), e);
                throw new RuntimeException("获取用户权限失败: " + e.getMessage());
            }
        })
        .subscribeOn(Schedulers.boundedElastic())
        .flatMap(permissions -> {
            try {
                // 确保权限数据被正确序列化
                String permissionsJson = objectMapper.writeValueAsString(permissions);
                log.debug("序列化后的权限信息：{}", permissionsJson);
                
                ServerHttpRequest newRequest = exchange.getRequest().mutate()
                        .header("X-Permissions", permissionsJson)
                        .build();
                
                return chain.filter(exchange.mutate().request(newRequest).build());
            } catch (JsonProcessingException e) {
                log.error("序列化权限信息失败", e);
                return unauthorized(exchange, "系统错误");
            }
        })
        .onErrorResume(e -> {
            log.error("权限处理过程中发生错误: {}", e.getMessage());
            return unauthorized(exchange, "权限处理失败: " + e.getMessage());
        });
    }

    private Mono<Void> processPermissions(ServerWebExchange exchange, GatewayFilterChain chain, UserPermissionDTO permissions) {
        try {
            // 4. 将权限信息序列化为JSON并放入请求头
            String permissionsJson = objectMapper.writeValueAsString(permissions);
            log.debug("序列化权限信息：{}", permissionsJson);
            
            ServerHttpRequest newRequest = exchange.getRequest().mutate()
                    .header("X-Permissions", permissionsJson)
                    .build();
            
            ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
            log.debug("添加权限头后继续处理请求");
            return chain.filter(newExchange);
        } catch (JsonProcessingException e) {
            log.error("序列化权限信息失败", e);
            return unauthorized(exchange, "系统错误");
        }
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String msg) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.valueOf("text/plain;charset=UTF-8"));
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
