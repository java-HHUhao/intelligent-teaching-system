package cn.edu.hhu.spring.boot.starter.mq.model;

import cn.edu.hhu.spring.boot.starter.mq.enums.MessageStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 发送结果
 * 
 * @author intelligent-teaching-system
 */
@Data
@Accessors(chain = true)
public class SendResult implements Serializable {

    @Serial
    private static final long serialVersionUID = -4137972019525344851L;

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 消息状态
     */
    private MessageStatusEnum status;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 发送耗时（毫秒）
     */
    private Long costTime;

    /**
     * 主题
     */
    private String topic;

    /**
     * 标签
     */
    private String tag;

    /**
     * 消息键
     */
    private String messageKey;

    /**
     * 队列ID
     */
    private Integer queueId;

    /**
     * 队列偏移量
     */
    private Long queueOffset;

    /**
     * 事务ID
     */
    private String transactionId;

    /**
     * 发送时间
     */
    private Long sendTime;

    public SendResult() {
        this.sendTime = System.currentTimeMillis();
    }

    public SendResult(String messageId, MessageStatusEnum status) {
        this();
        this.messageId = messageId;
        this.status = status;
    }

    public static SendResult success(String messageId) {
        return new SendResult(messageId, MessageStatusEnum.SEND_OK);
    }

    public static SendResult failure(String messageId, String errorMessage) {
        SendResult result = new SendResult(messageId, MessageStatusEnum.SEND_FAILED);
        result.setErrorMessage(errorMessage);
        return result;
    }

    public boolean isSuccess() {
        return MessageStatusEnum.SEND_OK.equals(this.status);
    }
}
