package cn.edu.hhu.its.gateway.service.filter;

import cn.edu.hhu.its.gateway.service.config.IgnoreUrlsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 用于检验白名单
 */
@Component
@Order(1)
public class GlobalWhiteListFilter implements GlobalFilter{
    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        boolean isWhite = ignoreUrlsConfig.getWhitePaths().stream()
                .anyMatch(path::startsWith);

        if (isWhite) {
            // 设置标记，供后续过滤器判断
            exchange.getAttributes().put("isWhite", true);
        }

        return chain.filter(exchange); // 所有请求都继续执行，但标记了白名单
    }
}
