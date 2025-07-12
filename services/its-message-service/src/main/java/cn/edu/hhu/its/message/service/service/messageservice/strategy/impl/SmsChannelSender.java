package cn.edu.hhu.its.message.service.service.messageservice.strategy.impl;

import cn.edu.hhu.its.message.service.common.enums.ChannelTypeEnum;
import cn.edu.hhu.its.message.service.model.dto.request.MultiChannelMessageRequestDTO;
import cn.edu.hhu.its.message.service.model.dto.response.MessageSendResultDTO;
import cn.edu.hhu.its.message.service.service.messageservice.strategy.ChannelSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 短信渠道发送策略实现
 * 
 * @author ITS项目组
 */
@Slf4j
@Component
public class SmsChannelSender implements ChannelSender {

    @Override
    public String mark() {
        return ChannelTypeEnum.SMS.name();
    }

    @Override
    public MessageSendResultDTO.ChannelResult executeResp(MultiChannelMessageRequestDTO request) {
        MessageSendResultDTO.ChannelResult result = new MessageSendResultDTO.ChannelResult();
        result.setChannel(ChannelTypeEnum.SMS);
        result.setSendTime(new Date());

        try {
            // 验证接收者信息
            if (!validateReceiver(request.getReceiver())) {
                result.setSuccess(false);
                result.setMessage("手机号码无效");
                result.setErrorCode("INVALID_PHONE");
                return result;
            }

            // 构建短信内容
            String smsContent = buildSmsContent(request);
            String phone = request.getReceiver().getPhone();

            log.info("发送短信到手机号: {}, 内容: {}", phone, smsContent);

            // TODO: 集成真实的短信服务提供商（阿里云短信、腾讯云短信等）
            // 这里模拟发送过程
            boolean sendSuccess = simulateSmsGateway(phone, smsContent);

            if (sendSuccess) {
                result.setSuccess(true);
                result.setMessage("短信发送成功");
                result.setExternalMessageId("SMS_" + System.currentTimeMillis());
                log.info("短信发送成功: {}", phone);
            } else {
                result.setSuccess(false);
                result.setMessage("短信发送失败");
                result.setErrorCode("SMS_GATEWAY_ERROR");
                log.error("短信发送失败: {}", phone);
            }

        } catch (Exception e) {
            log.error("短信发送异常: ", e);
            result.setSuccess(false);
            result.setMessage("短信发送异常: " + e.getMessage());
            result.setErrorCode("SMS_EXCEPTION");
        }

        return result;
    }

    @Override
    public boolean validateReceiver(MultiChannelMessageRequestDTO.ReceiverInfo receiver) {
        if (receiver == null || !StringUtils.hasText(receiver.getPhone())) {
            return false;
        }
        
        String phone = receiver.getPhone();
        // 简单的手机号验证（11位数字，以1开头）
        return phone.matches("^1[3-9]\\d{9}$");
    }

    /**
     * 构建短信内容
     */
    private String buildSmsContent(MultiChannelMessageRequestDTO request) {
        if (StringUtils.hasText(request.getContent())) {
            return request.getContent();
        }
        
        if (StringUtils.hasText(request.getTitle())) {
            return request.getTitle();
        }
        
        return "您有一条新消息，请注意查收。";
    }

    /**
     * 模拟短信网关发送
     * 实际使用时应替换为真实的短信服务提供商API调用
     */
    private boolean simulateSmsGateway(String phone, String content) {
        // 模拟网络延迟
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 模拟99%的成功率
        return Math.random() > 0.01;
    }
} 