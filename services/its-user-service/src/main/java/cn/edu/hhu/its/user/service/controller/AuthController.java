package cn.edu.hhu.its.user.service.controller;

import cn.edu.hhu.spring.boot.starter.common.dto.UserPermissionDTO;
import cn.edu.hhu.its.user.service.service.AuthService;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import cn.edu.hhu.spring.boot.starter.common.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/auth")
@RequiredArgsConstructor
@RefreshScope
public class AuthController {
    private final AuthService authService;

    /**
     * 获取用户权限信息
     *
     * @param userId 用户ID
     * @param username 用户名
     * @return 用户权限信息
     */
    @GetMapping("/permissions")
    public Result<UserPermissionDTO> getUserPermissions(@RequestParam("userId") Long userId, @RequestParam("username") String username) {
        return ResultUtil.success(authService.getUserPermissions(userId, username));
    }
}
