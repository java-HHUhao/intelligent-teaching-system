package cn.edu.hhu.its.message.service.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 生成验证码请求DTO
 * 
 * @author ITS项目组
 */
@Data
@Schema(description = "生成验证码请求")
public class GenerateCodeRequestDTO {

    @Schema(description = "验证码类型：EMAIL、SMS、IMAGE", example = "EMAIL")
    @NotBlank(message = "验证码类型不能为空")
    private String codeType;

    @Schema(description = "目标（邮箱、手机号、sessionId等）", example = "user@example.com")
    @NotBlank(message = "目标不能为空")
    private String target;

    @Schema(description = "用户ID（可选）", example = "12345")
    private Long userId;

    @Schema(description = "验证码用途说明", example = "用户注册验证")
    private String purpose;
} 