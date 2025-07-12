package cn.edu.hhu.its.message.service.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 验证码响应DTO
 * 
 * @author ITS项目组
 */
@Data
@Schema(description = "验证码响应")
public class VerificationCodeResponseDTO {

    @Schema(description = "验证码类型", example = "EMAIL")
    private String codeType;

    @Schema(description = "目标", example = "user@example.com")
    private String target;

    @Schema(description = "验证码（图片验证码时返回base64编码的图片）", example = "123456")
    private String code;

    @Schema(description = "过期时间", example = "2024-07-08 10:10:00")
    private Date expiresAt;

    @Schema(description = "剩余有效时间（秒）", example = "300")
    private Long remainingSeconds;

    @Schema(description = "是否发送成功", example = "true")
    private Boolean success;

    @Schema(description = "提示信息", example = "验证码已发送到您的邮箱")
    private String message;
} 