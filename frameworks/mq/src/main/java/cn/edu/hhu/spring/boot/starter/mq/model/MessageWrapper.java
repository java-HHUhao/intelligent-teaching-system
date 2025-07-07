package cn.edu.hhu.spring.boot.starter.mq.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * 消息包装器
 * 
 * @author intelligent-teaching-system
 */
@Data
@Accessors(chain = true)
public class MessageWrapper implements Serializable {

    @Serial
    private static final long serialVersionUID = -7834163315200549172L;

    /**
     * 主题
     */
    private String topic;

    /**
     * 标签
     */
    private String tag;

    /**
     * 消息内容
     */
    private Object message;

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 消息键
     */
    private String messageKey;

    /**
     * 消费者组
     */
    private String consumerGroup;

    /**
     * 自定义属性
     */
    private Map<String, String> properties;

    /**
     * 重试次数
     */
    private Integer retryTimes;

    /**
     * 最大重试次数
     */
    private Integer maxRetryTimes;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 超时时间（毫秒）
     */
    private Long timeout;

    /**
     * 是否为事务消息
     */
    private Boolean transactional;

    /**
     * 事务ID
     */
    private String transactionId;

    public MessageWrapper() {
        this.createTime = System.currentTimeMillis();
        this.retryTimes = 0;
        this.maxRetryTimes = 3;
        this.timeout = 3000L;
        this.transactional = false;
    }

    public MessageWrapper(String topic, String tag, Object message) {
        this();
        this.topic = topic;
        this.tag = tag;
        this.message = message;
    }

    public MessageWrapper(String topic, String tag, Object message, String consumerGroup) {
        this(topic, tag, message);
        this.consumerGroup = consumerGroup;
    }
} 