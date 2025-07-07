package cn.edu.hhu.spring.boot.starter.mq.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 死信消息
 * 
 * @author intelligent-teaching-system
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeadLetterMessage extends MessageWrapper {

    /**
     * 原始主题
     */
    private String originalTopic;

    /**
     * 原始标签
     */
    private String originalTag;

    /**
     * 原始消费者组
     */
    private String originalConsumerGroup;

    /**
     * 失败原因
     */
    private String failureReason;

    /**
     * 死信时间
     */
    private LocalDateTime deadLetterTime;

    /**
     * 最后重试时间
     */
    private LocalDateTime lastRetryTime;

    /**
     * 消费失败次数
     */
    private Integer consumeFailedTimes;

    public DeadLetterMessage() {
        super();
        this.deadLetterTime = LocalDateTime.now();
        this.consumeFailedTimes = 0;
    }

    public DeadLetterMessage(MessageWrapper originalMessage, String failureReason) {
        this();
        this.originalTopic = originalMessage.getTopic();
        this.originalTag = originalMessage.getTag();
        this.originalConsumerGroup = originalMessage.getConsumerGroup();
        this.failureReason = failureReason;
        this.consumeFailedTimes = originalMessage.getRetryTimes();
        
        // 构建死信主题
        this.setTopic(originalMessage.getTopic() + "_DLQ");
        this.setTag(originalMessage.getTag());
        this.setMessage(originalMessage.getMessage());
        this.setMessageId(originalMessage.getMessageId());
        this.setConsumerGroup(originalMessage.getConsumerGroup());
    }
} 