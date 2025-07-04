package cn.edu.hhu.its.gateway.service.client;

import cn.edu.hhu.its.gateway.service.model.dto.UserPermissionDTO;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "its-user-service", path = "/user")
public interface UserServiceClient {
    /**
     * 获取用户权限信息
     */
    @GetMapping("/auth/permissions")
    Result<UserPermissionDTO> getUserPermissions(@RequestParam("userId") Long userId, @RequestParam("username") String username);
} 