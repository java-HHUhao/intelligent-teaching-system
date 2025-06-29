package cn.edu.hhu.its.user.service.controller;

import cn.edu.hhu.its.user.service.model.dto.req.UserLoginReqDTO;
import cn.edu.hhu.its.user.service.model.dto.req.UserRegisterReqDTO;
import cn.edu.hhu.its.user.service.model.dto.resp.UserLoginRespDTO;
import cn.edu.hhu.its.user.service.model.dto.resp.UserRegisterRespDTO;
import cn.edu.hhu.its.user.service.service.UserService;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import cn.edu.hhu.spring.boot.starter.common.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/common")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO userLoginReqDTO) {
        return ResultUtil.success(userService.login(userLoginReqDTO));
    }
    @PostMapping("/register")
    public Result<UserRegisterRespDTO> register(@Validated @RequestBody UserRegisterReqDTO userRegisterReqDTO) {
        return ResultUtil.success(userService.register(userRegisterReqDTO));
    }
}
