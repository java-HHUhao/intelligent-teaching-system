package cn.edu.hhu.its.user.service.controller;

import cn.edu.hhu.its.user.service.model.dto.request.UserLoginReqDTO;
import cn.edu.hhu.its.user.service.model.dto.request.UserRegisterReqDTO;
import cn.edu.hhu.its.user.service.model.dto.response.UserLoginRespDTO;
import cn.edu.hhu.its.user.service.model.dto.response.UserRegisterRespDTO;
import cn.edu.hhu.its.user.service.service.UserLoginService;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import cn.edu.hhu.spring.boot.starter.common.utils.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/user/login")
@RequiredArgsConstructor
@RefreshScope
public class UserLoginController {
    private final UserLoginService userLoginService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO userLoginReqDTO) {
        return ResultUtil.success(userLoginService.login(userLoginReqDTO));
    }
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<UserRegisterRespDTO> register(@Validated @RequestBody UserRegisterReqDTO userRegisterReqDTO) {
        return ResultUtil.success(userLoginService.register(userRegisterReqDTO));
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        userLoginService.logout(request);
        return ResultUtil.success(null);
    }
}
