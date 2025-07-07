package cn.edu.hhu.spring.boot.starter.mq.core;

import cn.edu.hhu.spring.boot.starter.mq.enums.ConsumerModeEnum;

/**
 * 消息消费者核心接口
 * 
 * @author intelligent-teaching-system
 */
public interface MessageConsumer {

    /**
     * 订阅主题
     *
     * @param topic 主题
     * @param tag 标签
     * @param consumerGroup 消费者组
     * @param listener 消息监听器
     * @param mode 消费模式（同步/异步）
     */
    void subscribe(String topic, String tag, String consumerGroup, MessageListener listener, ConsumerModeEnum mode);

    /**
     * 订阅主题（默认异步模式）
     *
     * @param topic 主题
     * @param tag 标签
     * @param consumerGroup 消费者组
     * @param listener 消息监听器
     */
    void subscribe(String topic, String tag, String consumerGroup, MessageListener listener);

    /**
     * 取消订阅
     *
     * @param topic 主题
     * @param consumerGroup 消费者组
     */
    void unsubscribe(String topic, String consumerGroup);

    /**
     * 启动消费者
     */
    void start();

    /**
     * 停止消费者
     */
    void shutdown();
}
