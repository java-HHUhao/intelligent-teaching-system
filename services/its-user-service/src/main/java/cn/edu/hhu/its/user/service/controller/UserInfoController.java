package cn.edu.hhu.its.user.service.controller;

import cn.edu.hhu.its.user.service.model.dto.request.UserAccountUpdateReqDTO;
import cn.edu.hhu.its.user.service.model.dto.request.UserProfileUpdateReqDTO;
import cn.edu.hhu.its.user.service.model.dto.request.UsernameUpdateReqDTO;
import cn.edu.hhu.its.user.service.model.dto.response.UserAccountUpdateRespDTO;
import cn.edu.hhu.its.user.service.model.dto.response.UserProfileRespDTO;
import cn.edu.hhu.its.user.service.model.dto.response.UserProfileUpdateRespDTO;
import cn.edu.hhu.its.user.service.model.dto.response.UsernameUpdateRespDTO;
import cn.edu.hhu.its.user.service.service.UserInfoService;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import cn.edu.hhu.spring.boot.starter.common.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/info")
@RequiredArgsConstructor
@RefreshScope
public class UserInfoController {
    private final UserInfoService userInfoService;

    /**
     * 获取用户个人资料
     */
    @GetMapping("/profile")
    public Result<UserProfileRespDTO> getProfile(@RequestParam("userId") Long userId) {
        return ResultUtil.success(userInfoService.getUserProfile(userId));
    }

    /**
     * 更新用户个人资料
     */
    @PutMapping("/profile")
    public Result<UserProfileUpdateRespDTO> updateProfile(
            @RequestParam("userId") Long userId,
            @Validated @RequestBody UserProfileUpdateReqDTO updateReq) {
        return ResultUtil.success(userInfoService.updateUserProfile(userId, updateReq));
    }

    /**
     * 更新用户账户信息
     */
    @PutMapping("/account")
    public Result<UserAccountUpdateRespDTO> updateAccount(
            @RequestParam("userId") Long userId,
            @Validated @RequestBody UserAccountUpdateReqDTO updateReq) {
        return ResultUtil.success(userInfoService.updateUserAccount(userId, updateReq));
    }

    /**
     * 修改用户用户名
     */
    @PutMapping("/username")
    public Result<UsernameUpdateRespDTO> updateUsername(
            @RequestParam("userId") Long userId,
            @Validated @RequestBody UsernameUpdateReqDTO updateReq) {
        return ResultUtil.success(userInfoService.updateUsername(userId, updateReq));
    }
}
