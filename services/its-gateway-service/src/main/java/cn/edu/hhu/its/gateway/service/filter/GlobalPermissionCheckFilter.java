package cn.edu.hhu.its.gateway.service.filter;

import cn.hutool.core.text.AntPathMatcher;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.edu.hhu.its.gateway.service.model.dto.UserPermissionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
@Order(4)
@RequiredArgsConstructor
public class GlobalPermissionCheckFilter implements GlobalFilter {
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String method = request.getMethod().name();

        // 1. 获取权限信息
        String permissionJson = request.getHeaders().getFirst("X-Permission-Info");
        if (StringUtils.isBlank(permissionJson)) {
            return forbidden(exchange, "权限信息不存在");
        }

        try {
            // 2. 解析权限信息
            UserPermissionDTO userPermission = objectMapper.readValue(permissionJson, UserPermissionDTO.class);
            List<String> permissions = userPermission.getPermissions();
            
            if (permissions == null || permissions.isEmpty()) {
                log.warn("用户没有任何权限: userId={}, path={}", userPermission.getUserId(), path);
                return forbidden(exchange, "没有访问权限");
            }

            // 3. 构建权限标识
            // 示例：GET:/api/user/list -> user:list:view
            // 示例：POST:/api/user/create -> user:create:add
            String permissionCode = buildPermissionCode(path, method);
            log.debug("权限检查: userId={}, path={}, method={}, permissionCode={}", 
                    userPermission.getUserId(), path, method, permissionCode);

            // 4. 检查是否有权限
            boolean hasPermission = permissions.stream()
                    .anyMatch(permission -> pathMatcher.match(permission, permissionCode));

            if (!hasPermission) {
                log.warn("用户权限不足: userId={}, path={}, permissionCode={}, permissions={}", 
                        userPermission.getUserId(), path, permissionCode, permissions);
                return forbidden(exchange, "没有访问权限");
            }

            // 5. 有权限，继续后续过滤器
            return chain.filter(exchange);

        } catch (JsonProcessingException e) {
            log.error("解析权限信息失败: permissionJson={}", permissionJson, e);
            return forbidden(exchange, "系统错误");
        }
    }

    /**
     * 构建权限标识
     * 将请求路径转换为权限标识
     * 例如：
     * GET:/api/user/list -> user:list:view
     * POST:/api/user/create -> user:create:add
     * PUT:/api/user/update -> user:update:edit
     * DELETE:/api/user/delete -> user:delete:delete
     */
    private String buildPermissionCode(String path, String method) {
        // 1. 移除API前缀
        String processedPath = path.replaceFirst("^/api/", "");
        
        // 2. 分割路径
        String[] parts = processedPath.split("/");
        if (parts.length < 2) {
            return processedPath + ":" + getPermissionAction(method);
        }

        // 3. 构建权限标识
        // 使用前两段作为权限标识，例如：user/list -> user:list
        String resource = parts[0] + ":" + parts[1];
        
        // 4. 添加操作类型
        return resource + ":" + getPermissionAction(method);
    }

    /**
     * 根据HTTP方法获取权限操作类型
     */
    private String getPermissionAction(String method) {
        return switch (method.toUpperCase()) {
            case "GET" -> "view";
            case "POST" -> "add";
            case "PUT" -> "edit";
            case "DELETE" -> "delete";
            default -> "access";
        };
    }

    private Mono<Void> forbidden(ServerWebExchange exchange, String msg) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
