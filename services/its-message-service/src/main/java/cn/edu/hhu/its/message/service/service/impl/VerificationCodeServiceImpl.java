package cn.edu.hhu.its.message.service.service.impl;

import cn.edu.hhu.its.message.service.model.dto.request.GenerateCodeRequestDTO;
import cn.edu.hhu.its.message.service.model.dto.request.VerifyCodeRequestDTO;
import cn.edu.hhu.its.message.service.model.dto.response.VerificationCodeResponseDTO;
import cn.edu.hhu.its.message.service.service.VerificationCodeService;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {
    @Override
    public Result<VerificationCodeResponseDTO> generateCode(GenerateCodeRequestDTO requestDTO, String ipAddress) {
        return null;
    }

    @Override
    public Result<Boolean> verifyCode(VerifyCodeRequestDTO requestDTO, String ipAddress) {
        return null;
    }

    @Override
    public Result<VerificationCodeResponseDTO> generateImageCode(String sessionId, String ipAddress) {
        return null;
    }

    @Override
    public Result<Boolean> verifyImageCode(String sessionId, String code, String ipAddress) {
        return null;
    }

    @Override
    public Result<VerificationCodeResponseDTO> sendEmailCode(String email, Long userId, String purpose, String ipAddress) {
        return null;
    }

    @Override
    public Result<Boolean> verifyEmailCode(String email, String code, Long userId, String ipAddress) {
        return null;
    }

    @Override
    public Result<VerificationCodeResponseDTO> sendSmsCode(String phone, Long userId, String purpose, String ipAddress) {
        return null;
    }

    @Override
    public Result<Boolean> verifySmsCode(String phone, String code, Long userId, String ipAddress) {
        return null;
    }

    @Override
    public Result<Integer> cleanExpiredCodes() {
        return null;
    }

    @Override
    public Result<Boolean> checkSendLimit(String codeType, String target, String ipAddress) {
        return null;
    }
}
