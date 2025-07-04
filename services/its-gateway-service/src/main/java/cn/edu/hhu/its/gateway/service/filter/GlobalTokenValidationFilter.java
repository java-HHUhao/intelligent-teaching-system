package cn.edu.hhu.its.gateway.service.filter;

import cn.edu.hhu.its.gateway.service.util.JwtUtil;
import cn.edu.hhu.its.user.service.common.cache.UserCachePrefix;
import io.jsonwebtoken.Claims;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * 用于校验token是否缺失过期或格式非法
 */
@Component
@Order(2)
public class GlobalTokenValidationFilter implements GlobalFilter{
    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        //判断是否携带token以及格式合法
        if (StringUtils.isBlank(token) || !token.startsWith("Bearer ")) {
            return unauthorized(exchange, "Token缺失或格式错误！");
        }
        token = token.replace("Bearer ", "");
        try {
            Claims claims = JwtUtil.parseToken(token);
            String userId = claims.getSubject();

            // Redis 比对
            String cachedToken = redisTemplate.opsForValue().get(UserCachePrefix.ACCESS_TOKEN + userId);
            if (!token.equals(cachedToken)) {
                return unauthorized(exchange, "Token 非法或已过期");
            }

            // 将 userId 放入 request 传给下游
            ServerHttpRequest newRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", userId)
                    .build();

            return chain.filter(exchange.mutate().request(newRequest).build());

        } catch (Exception e) {
            return unauthorized(exchange, "Token 校验失败");
        }
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String msg) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
