package cn.edu.hhu.its.message.service.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 验证验证码请求DTO
 * 
 * @author ITS项目组
 */
@Data
@Schema(description = "验证验证码请求")
public class VerifyCodeRequestDTO {

    @Schema(description = "验证码类型：EMAIL、SMS、IMAGE", example = "EMAIL")
    @NotBlank(message = "验证码类型不能为空")
    private String codeType;

    @Schema(description = "目标（邮箱、手机号、sessionId等）", example = "user@example.com")
    @NotBlank(message = "目标不能为空")
    private String target;

    @Schema(description = "验证码", example = "123456")
    @NotBlank(message = "验证码不能为空")
    private String code;

    @Schema(description = "用户ID（可选）", example = "12345")
    private Long userId;
} 