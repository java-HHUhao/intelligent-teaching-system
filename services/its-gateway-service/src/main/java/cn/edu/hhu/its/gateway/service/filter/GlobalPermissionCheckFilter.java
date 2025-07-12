package cn.edu.hhu.its.gateway.service.filter;

import cn.edu.hhu.spring.boot.starter.common.dto.UserPermissionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@Order(4)
@RequiredArgsConstructor
public class GlobalPermissionCheckFilter implements GlobalFilter {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Boolean isWhite = exchange.getAttribute("isWhite");
        if (Boolean.TRUE.equals(isWhite)) {
            // 是白名单，跳过权限校验
            log.debug("白名单请求，跳过权限校验");
            return chain.filter(exchange);
        }

        try {
            // 1. 获取用户权限信息
            String permissionsJson = exchange.getRequest().getHeaders().getFirst("X-Permissions");
            if (permissionsJson == null) {
                log.warn("权限信息丢失，请求路径: {}", exchange.getRequest().getPath().value());
                return unauthorized(exchange, "权限信息丢失");
            }

            log.debug("开始解析权限信息，JSON长度: {}", permissionsJson.length());
            log.debug("权限JSON内容: {}", permissionsJson);
            
            UserPermissionDTO permissions;
            try {
                permissions = objectMapper.readValue(permissionsJson, UserPermissionDTO.class);
                log.debug("权限解析成功: userId={}, username={}, permissions={}", 
                    permissions.getUserId(), permissions.getUsername(), permissions.getPermissions());
            } catch (Exception e) {
                log.error("权限JSON解析失败: {}", e.getMessage(), e);
                return unauthorized(exchange, "权限数据格式错误");
            }
            
            if (permissions == null || permissions.getPermissions() == null) {
                log.warn("用户权限为空，userId: {}", permissions != null ? permissions.getUserId() : "null");
                return unauthorized(exchange, "用户权限为空");
            }
            
            // 2. 获取请求路径
            String path = exchange.getRequest().getPath().value();
            String[] pathParts = path.split("/");
            log.debug("请求路径分析: path={}, parts={}", path, java.util.Arrays.toString(pathParts));
            
            // 对于admin接口，只要有任何admin权限就放行
            if (pathParts.length >= 3 && "admin".equals(pathParts[2])) {
                boolean hasAdminPermission = permissions.getPermissions().stream()
                    .anyMatch(permission -> permission.contains("admin") || 
                                         permission.endsWith(":*") || 
                                         permission.equals("*:*:*"));
                                         
                log.debug("Admin接口权限检查: hasAdminPermission={}, userPermissions={}", 
                    hasAdminPermission, permissions.getPermissions());
                    
                if (hasAdminPermission) {
                    log.debug("用户具有管理员权限，允许访问admin接口");
                    return chain.filter(exchange);
                }
                log.warn("用户无管理员权限，userId: {}, permissions: {}", 
                    permissions.getUserId(), permissions.getPermissions());
                return unauthorized(exchange, "无访问权限");
            }

            if (pathParts.length < 3) {
                log.warn("无效的请求路径：{}", path);
                return unauthorized(exchange, "无效的请求路径");
            }

            // 3. 构建权限编码
            String module = pathParts[1]; // 模块名
            String resource = pathParts[2]; // 资源名
            String operation = getOperationFromMethod(exchange.getRequest().getMethod().name());
            String requiredPermission = String.format("%s:%s:%s", module, resource, operation);
            
            log.debug("权限检查详情: module={}, resource={}, operation={}, requiredPermission={}", 
                module, resource, operation, requiredPermission);

            // 4. 检查权限
            boolean hasPermission = permissions.getPermissions().stream()
                    .anyMatch(permission -> {
                        boolean matches = permission.equals(requiredPermission) || 
                                        permission.equals(module + ":" + resource + ":*") ||
                                        permission.equals(module + ":*:*") ||
                                        permission.equals("*:*:*");
                        if (matches) {
                            log.debug("权限匹配成功，用户权限：{}，需要权限：{}", permission, requiredPermission);
                        }
                        return matches;
                    });

            if (!hasPermission) {
                log.warn("用户权限不足，userId: {}，需要权限：{}，拥有权限：{}", 
                    permissions.getUserId(), requiredPermission, permissions.getPermissions());
                return unauthorized(exchange, "无访问权限");
            }

            log.debug("权限校验通过，继续处理请求");
            return chain.filter(exchange);
        } catch (Exception e) {
            log.error("权限校验过程中发生错误", e);
            return unauthorized(exchange, "权限校验失败：" + e.getMessage());
        }
    }

    private String getOperationFromMethod(String method) {
        switch (method.toUpperCase()) {
            case "GET":
                return "read";
            case "POST":
                return "create";
            case "PUT":
            case "PATCH":
                return "update";
            case "DELETE":
                return "delete";
            default:
                return "unknown";
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
