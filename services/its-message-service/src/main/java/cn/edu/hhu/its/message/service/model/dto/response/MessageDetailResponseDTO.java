package cn.edu.hhu.its.message.service.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 消息详情响应DTO
 * 
 * @author ITS项目组
 */
@Data
@Schema(description = "消息详情响应")
public class MessageDetailResponseDTO {

    @Schema(description = "消息ID", example = "12345")
    private Long id;

    @Schema(description = "消息类型ID", example = "1")
    private Long messageTypeId;

    @Schema(description = "消息类型名称", example = "系统通知")
    private String messageTypeName;

    @Schema(description = "发送者ID", example = "67890")
    private Long senderId;

    @Schema(description = "发送者用户名", example = "admin")
    private String senderUsername;

    @Schema(description = "接收者ID", example = "12345")
    private Long receiverId;

    @Schema(description = "消息标题", example = "系统通知")
    private String title;

    @Schema(description = "消息内容", example = "这是一条系统通知消息")
    private String content;

    @Schema(description = "是否已读", example = "false")
    private Boolean isRead;

    @Schema(description = "优先级：1-普通，2-重要，3-紧急", example = "1")
    private Integer priority;

    @Schema(description = "过期时间", example = "2024-12-31 23:59:59")
    private Date expiresAt;

    @Schema(description = "读取时间", example = "2024-07-08 10:30:00")
    private Date readAt;

    @Schema(description = "创建时间", example = "2024-07-08 10:00:00")
    private Date createdAt;

    @Schema(description = "是否已过期", example = "false")
    private Boolean expired;
} 