package cn.edu.hhu.its.gateway.service.filter;

import cn.edu.hhu.its.gateway.service.util.JwtUtil;
import cn.edu.hhu.spring.boot.starter.common.cache.user.UserCachePrefix;
import io.jsonwebtoken.Claims;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 用于校验token是否缺失过期或格式非法
 */
@Component
@Order(2)
public class GlobalTokenValidationFilter implements GlobalFilter{
    @Autowired
    StringRedisTemplate redisTemplate;

    private static final String LOGIN_TOKEN_USER_ID = "userId";
    private static final String LOGIN_TOKEN_USER_NAME = "userName";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Boolean isWhite = exchange.getAttribute("isWhite");
        if (Boolean.TRUE.equals(isWhite)) {
            // 是白名单，跳过 token 校验
            return chain.filter(exchange);
        }
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        //判断是否携带token以及格式合法
        if (StringUtils.isBlank(token) || !token.startsWith("Bearer ")) {
            return unauthorized(exchange, "Token缺失或格式错误！");
        }
        token = token.replace("Bearer ", "");
        try {
            Claims claims = JwtUtil.parseToken(token);

            // 从claims中获取userId和username，与用户服务保持一致
            String userId = String.valueOf(claims.get(LOGIN_TOKEN_USER_ID));
            String username = String.valueOf(claims.get(LOGIN_TOKEN_USER_NAME));

            if (StringUtils.isBlank(userId) || StringUtils.isBlank(username)) {
                return unauthorized(exchange, "Token信息不完整: userId=" + userId + ", userName=" + username);
            }

            // Redis 比对
            String cachedToken = redisTemplate.opsForValue().get(UserCachePrefix.ACCESS_TOKEN + userId);
            if (StringUtils.isBlank(cachedToken) || !token.equals(cachedToken)) {
                return unauthorized(exchange, "Token 非法或已过期");
            }

            // 将 userId 和 username 放入 request 传给下游
            ServerHttpRequest newRequest = exchange.getRequest().mutate()
                    .header("X-UserId", userId)
                    .header("X-Username", username)
                    .build();

            return chain.filter(exchange.mutate().request(newRequest).build());

        } catch (Exception e) {
            return unauthorized(exchange, "Token 校验失败");
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


