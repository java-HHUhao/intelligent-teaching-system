package cn.edu.hhu.its.message.service.service.messageservice;

import cn.edu.hhu.its.message.service.model.dto.request.MultiChannelMessageRequestDTO;
import cn.edu.hhu.its.message.service.model.dto.response.MessageSendResultDTO;
import cn.edu.hhu.spring.boot.starter.common.result.Result;

/**
 * 消息服务接口
 * 统一消息发送入口，支持多渠道发送
 * 
 * @author ITS项目组
 */
public interface MessageService {

    /**
     * 多渠道发送消息
     * 
     * @param request 消息发送请求
     * @return 发送结果
     */
    Result<MessageSendResultDTO> sendMultiChannelMessage(MultiChannelMessageRequestDTO request);

    /**
     * 单渠道发送消息
     * 
     * @param channelType 渠道类型
     * @param request 消息发送请求
     * @return 发送结果
     */
    Result<MessageSendResultDTO.ChannelResult> sendSingleChannelMessage(String channelType, MultiChannelMessageRequestDTO request);

    /**
     * 使用模板发送多渠道消息
     * 
     * @param request 消息发送请求（包含模板代码和参数）
     * @return 发送结果
     */
    Result<MessageSendResultDTO> sendTemplateMessage(MultiChannelMessageRequestDTO request);
}
