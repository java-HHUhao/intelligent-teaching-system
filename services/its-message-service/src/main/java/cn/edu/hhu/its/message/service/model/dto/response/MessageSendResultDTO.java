package cn.edu.hhu.its.message.service.model.dto.response;

import cn.edu.hhu.its.message.service.common.enums.ChannelTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 消息发送结果DTO
 * 
 * @author ITS项目组
 */
@Data
@Schema(description = "消息发送结果")
public class MessageSendResultDTO {

    @Schema(description = "总体发送状态", example = "true")
    private Boolean success;

    @Schema(description = "发送消息", example = "消息发送成功")
    private String message;

    @Schema(description = "各渠道发送结果")
    private List<ChannelResult> channelResults;

    @Schema(description = "发送时间")
    private Date sendTime;

    /**
     * 单个渠道发送结果
     */
    @Data
    @Schema(description = "渠道发送结果")
    public static class ChannelResult {
        
        @Schema(description = "发送渠道", example = "SMS")
        private ChannelTypeEnum channel;
        
        @Schema(description = "发送状态", example = "true")
        private Boolean success;
        
        @Schema(description = "发送消息", example = "短信发送成功")
        private String message;
        
        @Schema(description = "外部系统返回的消息ID")
        private String externalMessageId;
        
        @Schema(description = "发送时间")
        private Date sendTime;
        
        @Schema(description = "错误代码")
        private String errorCode;
    }
} 