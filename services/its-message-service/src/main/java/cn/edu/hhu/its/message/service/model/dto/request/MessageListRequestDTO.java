package cn.edu.hhu.its.message.service.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 消息列表请求DTO
 * 
 * @author ITS项目组
 */
@Data
@Schema(description = "消息列表请求")
public class MessageListRequestDTO {

    @Schema(description = "当前页码", example = "1")
    private Integer current = 1;

    @Schema(description = "每页大小", example = "20")
    private Integer size = 20;

    @Schema(description = "消息类型ID", example = "1")
    private Long messageTypeId;

    @Schema(description = "是否已读：true-已读，false-未读，null-全部", example = "false")
    private Boolean isRead;

    @Schema(description = "消息优先级", example = "1")
    private Integer priority;

    @Schema(description = "关键词搜索", example = "通知")
    private String keyword;
} 