package cn.edu.hhu.its.message.service.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 发送消息请求DTO
 * 
 * @author ITS项目组
 */
@Data
@Schema(description = "发送消息请求")
public class SendMessageRequestDTO {

    @Schema(description = "消息类型ID", example = "1")
    @NotNull(message = "消息类型ID不能为空")
    private Long messageTypeId;

    @Schema(description = "接收者ID", example = "12345")
    @NotNull(message = "接收者ID不能为空")
    private Long receiverId;

    @Schema(description = "消息标题", example = "系统通知")
    @NotBlank(message = "消息标题不能为空")
    private String title;

    @Schema(description = "消息内容", example = "这是一条系统通知消息")
    @NotBlank(message = "消息内容不能为空")
    private String content;

    @Schema(description = "消息优先级：1-普通，2-重要，3-紧急", example = "1")
    private Integer priority = 1;

    @Schema(description = "过期时间", example = "2024-12-31 23:59:59")
    private Date expiresAt;

    @Schema(description = "消息模板代码", example = "GROUP_JOIN_NOTICE")
    private String templateCode;

    @Schema(description = "模板参数")
    private Map<String, Object> templateParams;
} 