package cn.edu.hhu.its.message.service.service;

import cn.edu.hhu.its.message.service.model.dto.request.GenerateCodeRequestDTO;
import cn.edu.hhu.its.message.service.model.dto.request.VerifyCodeRequestDTO;
import cn.edu.hhu.its.message.service.model.dto.response.VerificationCodeResponseDTO;
import cn.edu.hhu.spring.boot.starter.common.result.Result;

/**
 * 验证码服务接口
 * 
 * @author ITS项目组
 */
public interface VerificationCodeService {

    /**
     * 生成验证码
     *
     * @param requestDTO 生成验证码请求
     * @param ipAddress 请求IP地址
     * @return 验证码响应
     */
    Result<VerificationCodeResponseDTO> generateCode(GenerateCodeRequestDTO requestDTO, String ipAddress);

    /**
     * 验证验证码
     *
     * @param requestDTO 验证验证码请求
     * @param ipAddress 请求IP地址
     * @return 验证结果
     */
    Result<Boolean> verifyCode(VerifyCodeRequestDTO requestDTO, String ipAddress);

    /**
     * 生成图片验证码
     *
     * @param sessionId 会话ID
     * @param ipAddress 请求IP地址
     * @return 图片验证码（base64编码）
     */
    Result<VerificationCodeResponseDTO> generateImageCode(String sessionId, String ipAddress);

    /**
     * 验证图片验证码
     *
     * @param sessionId 会话ID
     * @param code 验证码
     * @param ipAddress 请求IP地址
     * @return 验证结果
     */
    Result<Boolean> verifyImageCode(String sessionId, String code, String ipAddress);

    /**
     * 发送邮件验证码
     *
     * @param email 邮箱地址
     * @param userId 用户ID（可选）
     * @param purpose 用途
     * @param ipAddress 请求IP地址
     * @return 发送结果
     */
    Result<VerificationCodeResponseDTO> sendEmailCode(String email, Long userId, String purpose, String ipAddress);

    /**
     * 验证邮件验证码
     *
     * @param email 邮箱地址
     * @param code 验证码
     * @param userId 用户ID（可选）
     * @param ipAddress 请求IP地址
     * @return 验证结果
     */
    Result<Boolean> verifyEmailCode(String email, String code, Long userId, String ipAddress);

    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     * @param userId 用户ID（可选）
     * @param purpose 用途
     * @param ipAddress 请求IP地址
     * @return 发送结果
     */
    Result<VerificationCodeResponseDTO> sendSmsCode(String phone, Long userId, String purpose, String ipAddress);

    /**
     * 验证短信验证码
     *
     * @param phone 手机号
     * @param code 验证码
     * @param userId 用户ID（可选）
     * @param ipAddress 请求IP地址
     * @return 验证结果
     */
    Result<Boolean> verifySmsCode(String phone, String code, Long userId, String ipAddress);

    /**
     * 清理过期验证码
     *
     * @return 清理的验证码数量
     */
    Result<Integer> cleanExpiredCodes();

    /**
     * 检查发送频率限制
     *
     * @param codeType 验证码类型
     * @param target 目标
     * @param ipAddress IP地址
     * @return 是否可以发送
     */
    Result<Boolean> checkSendLimit(String codeType, String target, String ipAddress);
} 