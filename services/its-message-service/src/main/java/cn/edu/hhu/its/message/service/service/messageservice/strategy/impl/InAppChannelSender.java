package cn.edu.hhu.its.message.service.service.messageservice.strategy.impl;

import cn.edu.hhu.its.message.service.common.enums.ChannelTypeEnum;
import cn.edu.hhu.its.message.service.model.dto.request.MultiChannelMessageRequestDTO;

import cn.edu.hhu.its.message.service.model.dto.response.MessageSendResultDTO;
import cn.edu.hhu.its.message.service.service.messageservice.manager.SiteMessageManager;
import cn.edu.hhu.its.message.service.model.domain.SiteMessageDO;
import cn.edu.hhu.its.message.service.service.messageservice.strategy.ChannelSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 站内消息渠道发送策略实现
 * 
 * @author ITS项目组
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InAppChannelSender implements ChannelSender {

    private final SiteMessageManager siteMessageManager;

    @Override
    public String mark() {
        return ChannelTypeEnum.IN_APP.name();
    }

    @Override
    public MessageSendResultDTO.ChannelResult executeResp(MultiChannelMessageRequestDTO request) {
        MessageSendResultDTO.ChannelResult result = new MessageSendResultDTO.ChannelResult();
        result.setChannel(ChannelTypeEnum.IN_APP);
        result.setSendTime(new Date());

        try {
            // 验证接收者信息
            if (!validateReceiver(request.getReceiver())) {
                result.setSuccess(false);
                result.setMessage("用户ID无效");
                result.setErrorCode("INVALID_USER_ID");
                return result;
            }

            // 构建站内消息对象
            SiteMessageDO message = buildSiteMessage(request);

            log.info("发送站内消息到用户: {}, 标题: {}", request.getReceiver().getUserId(), message.getTitle());

            // 调用站内消息管理器保存消息
            cn.edu.hhu.spring.boot.starter.common.result.Result<Long> saveResult = 
                siteMessageManager.saveMessage(message);

            if (saveResult != null && saveResult.getCode() != null && saveResult.getCode().equals("0")) {
                result.setSuccess(true);
                result.setMessage("站内消息发送成功");
                result.setExternalMessageId("SITE_MSG_" + saveResult.getData());
                log.info("站内消息发送成功: 用户ID={}, 消息ID={}", request.getReceiver().getUserId(), saveResult.getData());
            } else {
                result.setSuccess(false);
                String errorMsg = saveResult != null ? saveResult.getMessage() : "未知错误";
                result.setMessage("站内消息发送失败: " + errorMsg);
                result.setErrorCode("SITE_MESSAGE_SAVE_FAILED");
                log.error("站内消息发送失败: 用户ID={}, 错误: {}", request.getReceiver().getUserId(), errorMsg);
            }

        } catch (Exception e) {
            log.error("站内消息发送异常: ", e);
            result.setSuccess(false);
            result.setMessage("站内消息发送异常: " + e.getMessage());
            result.setErrorCode("SITE_MESSAGE_EXCEPTION");
        }

        return result;
    }

    @Override
    public boolean validateReceiver(MultiChannelMessageRequestDTO.ReceiverInfo receiver) {
        return receiver != null && receiver.getUserId() != null && receiver.getUserId() > 0;
    }

    /**
     * 构建站内消息对象
     */
    private SiteMessageDO buildSiteMessage(MultiChannelMessageRequestDTO request) {
        SiteMessageDO message = new SiteMessageDO();
        
        message.setSenderId(request.getSenderId());
        message.setReceiverId(request.getReceiver().getUserId());
        message.setTitle(request.getTitle() != null ? request.getTitle() : "系统通知");
        message.setContent(request.getContent() != null ? request.getContent() : "您有一条新消息");
        
        // 设置消息类型ID（这里需要根据消息类型获取对应的ID）
        message.setMessageTypeId(1L); // 默认使用系统通知类型
        
        // 设置优先级
        if (request.getPriority() != null) {
            message.setPriority(request.getPriority().getCode());
        } else {
            message.setPriority(1); // 默认普通优先级
        }
        
        // 设置过期时间
        message.setExpiresAt(request.getExpiresAt());
        
        return message;
    }
} 