package cn.edu.hhu.its.message.service.service.messageservice.strategy.impl;

import cn.edu.hhu.its.message.service.common.enums.ChannelTypeEnum;
import cn.edu.hhu.its.message.service.model.dto.request.MultiChannelMessageRequestDTO;
import cn.edu.hhu.its.message.service.model.dto.response.MessageSendResultDTO;
import cn.edu.hhu.its.message.service.service.messageservice.strategy.ChannelSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * 邮件渠道发送策略实现
 * 
 * @author ITS项目组
 */
@Slf4j
@Component
public class EmailChannelSender implements ChannelSender {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );

    @Override
    public String mark() {
        return ChannelTypeEnum.EMAIL.name();
    }

    @Override
    public MessageSendResultDTO.ChannelResult executeResp(MultiChannelMessageRequestDTO request) {
        MessageSendResultDTO.ChannelResult result = new MessageSendResultDTO.ChannelResult();
        result.setChannel(ChannelTypeEnum.EMAIL);
        result.setSendTime(new Date());

        try {
            // 验证接收者信息
            if (!validateReceiver(request.getReceiver())) {
                result.setSuccess(false);
                result.setMessage("邮箱地址无效");
                result.setErrorCode("INVALID_EMAIL");
                return result;
            }

            String email = request.getReceiver().getEmail();
            String subject = StringUtils.hasText(request.getTitle()) ? request.getTitle() : "系统通知";
            String content = buildEmailContent(request);

            log.info("发送邮件到: {}, 主题: {}", email, subject);

            // TODO: 集成真实的邮件服务（JavaMail、第三方邮件服务等）
            // 这里模拟发送过程
            boolean sendSuccess = simulateEmailGateway(email, subject, content);

            if (sendSuccess) {
                result.setSuccess(true);
                result.setMessage("邮件发送成功");
                result.setExternalMessageId("EMAIL_" + System.currentTimeMillis());
                log.info("邮件发送成功: {}", email);
            } else {
                result.setSuccess(false);
                result.setMessage("邮件发送失败");
                result.setErrorCode("EMAIL_GATEWAY_ERROR");
                log.error("邮件发送失败: {}", email);
            }

        } catch (Exception e) {
            log.error("邮件发送异常: ", e);
            result.setSuccess(false);
            result.setMessage("邮件发送异常: " + e.getMessage());
            result.setErrorCode("EMAIL_EXCEPTION");
        }

        return result;
    }

    @Override
    public boolean validateReceiver(MultiChannelMessageRequestDTO.ReceiverInfo receiver) {
        if (receiver == null || !StringUtils.hasText(receiver.getEmail())) {
            return false;
        }
        
        return EMAIL_PATTERN.matcher(receiver.getEmail()).matches();
    }

    /**
     * 构建邮件内容
     */
    private String buildEmailContent(MultiChannelMessageRequestDTO request) {
        StringBuilder contentBuilder = new StringBuilder();
        
        // 添加称呼
        if (StringUtils.hasText(request.getReceiver().getName())) {
            contentBuilder.append("亲爱的 ").append(request.getReceiver().getName()).append("：\n\n");
        } else {
            contentBuilder.append("您好：\n\n");
        }
        
        // 添加正文
        if (StringUtils.hasText(request.getContent())) {
            contentBuilder.append(request.getContent());
        } else {
            contentBuilder.append("您有一条新的系统消息，请注意查收。");
        }
        
        // 添加签名
        contentBuilder.append("\n\n");
        contentBuilder.append("此致\n");
        contentBuilder.append("智能教学系统\n");
        contentBuilder.append("发送时间：").append(new Date());
        
        return contentBuilder.toString();
    }

    /**
     * 模拟邮件网关发送
     * 实际使用时应替换为真实的邮件服务API调用
     */
    private boolean simulateEmailGateway(String email, String subject, String content) {
        // 模拟网络延迟
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 模拟98%的成功率
        return Math.random() > 0.02;
    }
} 