package cn.edu.hhu.its.gateway.service.filter;

import cn.edu.hhu.its.gateway.service.client.UserServiceClient;
import cn.edu.hhu.its.gateway.service.model.dto.UserPermissionDTO;
import cn.edu.hhu.its.user.service.common.cache.UserCacheExpire;
import cn.edu.hhu.its.user.service.common.cache.UserCachePrefix;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Order(3)
@RequiredArgsConstructor
public class GlobalPermissionExistFilter implements GlobalFilter {
    private final StringRedisTemplate redisTemplate;
    private final UserServiceClient userServiceClient;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 获取用户信息
        String userId = exchange.getRequest().getHeaders().getFirst("X-User-Id");
        String username = exchange.getRequest().getHeaders().getFirst("X-Username");
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(username)) {
            return unauthorized(exchange, "用户信息丢失");
        }

        // 2. 尝试从缓存中获取权限信息
        String cacheKey = UserCachePrefix.PERMISSIONS + userId;
        String permissionJson = redisTemplate.opsForValue().get(cacheKey);

        if (StringUtils.isBlank(permissionJson)) {
            try {
                // 3. 缓存中没有，远程调用用户服务获取权限
                Result<UserPermissionDTO> result = userServiceClient.getUserPermissions(Long.valueOf(userId), username);
                if (result.getData() == null) {
                    log.error("获取用户权限失败: userId={}, username={}, result={}", userId, username, result);
                    return forbidden(exchange, "获取用户权限失败");
                }

                // 4. 将权限信息序列化为JSON
                permissionJson = objectMapper.writeValueAsString(result.getData());

                // 5. 写入缓存
                redisTemplate.opsForValue().set(cacheKey, permissionJson, UserCacheExpire.USER_PERMISSION_EXPIRE, TimeUnit.SECONDS);
            } catch (JsonProcessingException e) {
                log.error("序列化用户权限信息失败: userId={}, username={}", userId, username, e);
                return forbidden(exchange, "系统错误");
            } catch (Exception e) {
                log.error("获取用户权限异常: userId={}, username={}", userId, username, e);
                return forbidden(exchange, "系统错误");
            }
        }

        // 6. 传入后续过滤器
        ServerHttpRequest newRequest = exchange.getRequest().mutate()
                .header("X-Permission-Info", permissionJson)
                .build();

        return chain.filter(exchange.mutate().request(newRequest).build());
    }

    private Mono<Void> forbidden(ServerWebExchange exchange, String msg) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String msg) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
