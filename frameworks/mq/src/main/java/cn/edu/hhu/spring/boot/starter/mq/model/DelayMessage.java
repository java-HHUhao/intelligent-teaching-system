package cn.edu.hhu.spring.boot.starter.mq.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 延迟消息
 * 
 * @author intelligent-teaching-system
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DelayMessage extends MessageWrapper {

    /**
     * 延迟时间（毫秒）
     */
    private Long delayTime;

    /**
     * 延迟级别（RocketMQ支持18个级别）
     * 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     */
    private Integer delayLevel;

    /**
     * 期望执行时间
     */
    private LocalDateTime executeTime;

    /**
     * 是否为定时消息
     */
    private Boolean scheduled;

    public DelayMessage() {
        super();
        this.scheduled = false;
    }

    public DelayMessage(String topic, String tag, Object message, Long delayTime) {
        super(topic, tag, message);
        this.delayTime = delayTime;
        this.scheduled = false;
    }

    public DelayMessage(String topic, String tag, Object message, Integer delayLevel) {
        super(topic, tag, message);
        this.delayLevel = delayLevel;
        this.scheduled = false;
    }

    public DelayMessage(String topic, String tag, Object message, LocalDateTime executeTime) {
        super(topic, tag, message);
        this.executeTime = executeTime;
        this.scheduled = true;
    }
} 