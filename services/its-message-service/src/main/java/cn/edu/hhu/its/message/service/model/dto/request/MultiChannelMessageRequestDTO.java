package cn.edu.hhu.its.message.service.model.dto.request;

import cn.edu.hhu.its.message.service.common.enums.ChannelTypeEnum;
import cn.edu.hhu.its.message.service.common.enums.MessagePriorityEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 多渠道消息发送请求DTO
 * 
 * @author ITS项目组
 */
@Data
@Schema(description = "多渠道消息发送请求")
public class MultiChannelMessageRequestDTO {

    @Schema(description = "发送渠道列表", example = "[\"SMS\", \"EMAIL\", \"IN_APP\"]")
    @NotEmpty(message = "发送渠道不能为空")
    private List<ChannelTypeEnum> channels;

    @Schema(description = "消息类型代码", example = "VERIFICATION_CODE")
    @NotBlank(message = "消息类型不能为空")
    private String messageType;

    @Schema(description = "接收者信息")
    @NotNull(message = "接收者信息不能为空")
    private ReceiverInfo receiver;

    @Schema(description = "消息模板代码", example = "GROUP_JOIN_NOTICE")
    private String templateCode;

    @Schema(description = "消息标题", example = "系统通知")
    private String title;

    @Schema(description = "消息内容", example = "这是一条系统通知消息")
    private String content;

    @Schema(description = "模板参数")
    private Map<String, Object> templateParams;

    @Schema(description = "消息优先级", example = "NORMAL")
    private MessagePriorityEnum priority = MessagePriorityEnum.NORMAL;

    @Schema(description = "过期时间")
    private Date expiresAt;

    @Schema(description = "发送者ID")
    private Long senderId;

    /**
     * 接收者信息
     */
    @Data
    @Schema(description = "接收者信息")
    public static class ReceiverInfo {
        
        @Schema(description = "用户ID", example = "12345")
        private Long userId;
        
        @Schema(description = "手机号", example = "13800138000")
        private String phone;
        
        @Schema(description = "邮箱地址", example = "user@example.com")
        private String email;
        
        @Schema(description = "用户姓名", example = "张三")
        private String name;
    }
} 