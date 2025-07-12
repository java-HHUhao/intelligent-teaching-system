package cn.edu.hhu.its.message.service.controller;

import cn.edu.hhu.its.message.service.model.dto.request.GenerateCodeRequestDTO;
import cn.edu.hhu.its.message.service.model.dto.request.VerifyCodeRequestDTO;
import cn.edu.hhu.its.message.service.model.dto.response.VerificationCodeResponseDTO;
import cn.edu.hhu.its.message.service.service.VerificationCodeService;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 验证码Controller
 * 
 * @author ITS项目组
 */
@RestController
@RequestMapping("/message/verification")
@RequiredArgsConstructor
@Tag(name = "验证码管理", description = "验证码生成、验证相关接口")
public class VerificationCodeController {

    private final VerificationCodeService verificationCodeService;

    @PostMapping("/generate")
    @Operation(summary = "生成验证码", description = "生成各种类型的验证码")
    public Result<VerificationCodeResponseDTO> generateCode(
            @RequestBody @Valid GenerateCodeRequestDTO requestDTO,
            HttpServletRequest request) {
        String ipAddress = getClientIpAddress(request);
        return verificationCodeService.generateCode(requestDTO, ipAddress);
    }

    @PostMapping("/verify")
    @Operation(summary = "验证验证码", description = "验证各种类型的验证码")
    public Result<Boolean> verifyCode(
            @RequestBody @Valid VerifyCodeRequestDTO requestDTO,
            HttpServletRequest request) {
        String ipAddress = getClientIpAddress(request);
        return verificationCodeService.verifyCode(requestDTO, ipAddress);
    }

    @GetMapping("/image")
    @Operation(summary = "生成图片验证码", description = "生成图片验证码，返回base64编码的图片")
    public Result<VerificationCodeResponseDTO> generateImageCode(
            @Parameter(description = "会话ID", example = "session123") @RequestParam String sessionId,
            HttpServletRequest request) {
        String ipAddress = getClientIpAddress(request);
        return verificationCodeService.generateImageCode(sessionId, ipAddress);
    }

    @PostMapping("/image/verify")
    @Operation(summary = "验证图片验证码", description = "验证图片验证码")
    public Result<Boolean> verifyImageCode(
            @Parameter(description = "会话ID", example = "session123") @RequestParam String sessionId,
            @Parameter(description = "验证码", example = "abc123") @RequestParam String code,
            HttpServletRequest request) {
        String ipAddress = getClientIpAddress(request);
        return verificationCodeService.verifyImageCode(sessionId, code, ipAddress);
    }

    @PostMapping("/email/send")
    @Operation(summary = "发送邮件验证码", description = "向指定邮箱发送验证码")
    public Result<VerificationCodeResponseDTO> sendEmailCode(
            @Parameter(description = "邮箱地址", example = "user@example.com") @RequestParam String email,
            @Parameter(description = "用户ID（可选）", example = "12345") @RequestParam(required = false) Long userId,
            @Parameter(description = "用途说明", example = "用户注册") @RequestParam(required = false) String purpose,
            HttpServletRequest request) {
        String ipAddress = getClientIpAddress(request);
        return verificationCodeService.sendEmailCode(email, userId, purpose, ipAddress);
    }

    @PostMapping("/email/verify")
    @Operation(summary = "验证邮件验证码", description = "验证邮箱验证码")
    public Result<Boolean> verifyEmailCode(
            @Parameter(description = "邮箱地址", example = "user@example.com") @RequestParam String email,
            @Parameter(description = "验证码", example = "123456") @RequestParam String code,
            @Parameter(description = "用户ID（可选）", example = "12345") @RequestParam(required = false) Long userId,
            HttpServletRequest request) {
        String ipAddress = getClientIpAddress(request);
        return verificationCodeService.verifyEmailCode(email, code, userId, ipAddress);
    }

    @PostMapping("/sms/send")
    @Operation(summary = "发送短信验证码", description = "向指定手机号发送验证码")
    public Result<VerificationCodeResponseDTO> sendSmsCode(
            @Parameter(description = "手机号", example = "13800138000") @RequestParam String phone,
            @Parameter(description = "用户ID（可选）", example = "12345") @RequestParam(required = false) Long userId,
            @Parameter(description = "用途说明", example = "用户登录") @RequestParam(required = false) String purpose,
            HttpServletRequest request) {
        String ipAddress = getClientIpAddress(request);
        return verificationCodeService.sendSmsCode(phone, userId, purpose, ipAddress);
    }

    @PostMapping("/sms/verify")
    @Operation(summary = "验证短信验证码", description = "验证短信验证码")
    public Result<Boolean> verifySmsCode(
            @Parameter(description = "手机号", example = "13800138000") @RequestParam String phone,
            @Parameter(description = "验证码", example = "123456") @RequestParam String code,
            @Parameter(description = "用户ID（可选）", example = "12345") @RequestParam(required = false) Long userId,
            HttpServletRequest request) {
        String ipAddress = getClientIpAddress(request);
        return verificationCodeService.verifySmsCode(phone, code, userId, ipAddress);
    }

    @GetMapping("/check-limit")
    @Operation(summary = "检查发送频率限制", description = "检查是否可以发送验证码")
    public Result<Boolean> checkSendLimit(
            @Parameter(description = "验证码类型", example = "EMAIL") @RequestParam String codeType,
            @Parameter(description = "目标", example = "user@example.com") @RequestParam String target,
            HttpServletRequest request) {
        String ipAddress = getClientIpAddress(request);
        return verificationCodeService.checkSendLimit(codeType, target, ipAddress);
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        // 对于多个IP的情况，取第一个IP
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0].trim();
        }
        return ipAddress;
    }
} 