package cn.edu.hhu.its.message.service.service.messageservice.strategy;

import cn.edu.hhu.its.message.service.model.dto.request.MultiChannelMessageRequestDTO;
import cn.edu.hhu.its.message.service.model.dto.response.MessageSendResultDTO;
import cn.edu.hhu.spring.boot.starter.designpattern.strategy.AbstractStrategyExecutor;

/**
 * 消息发送渠道策略接口
 * 
 * @author ITS项目组
 */
public interface ChannelSender extends AbstractStrategyExecutor<MultiChannelMessageRequestDTO, MessageSendResultDTO.ChannelResult> {

    /**
     * 获取渠道类型标识
     */
    @Override
    String mark();

    /**
     * 发送消息
     * 
     * @param request 消息发送请求
     * @return 发送结果
     */
    @Override
    MessageSendResultDTO.ChannelResult executeResp(MultiChannelMessageRequestDTO request);

    /**
     * 验证接收者信息是否有效
     * 
     * @param receiver 接收者信息
     * @return 是否有效
     */
    boolean validateReceiver(MultiChannelMessageRequestDTO.ReceiverInfo receiver);
}
