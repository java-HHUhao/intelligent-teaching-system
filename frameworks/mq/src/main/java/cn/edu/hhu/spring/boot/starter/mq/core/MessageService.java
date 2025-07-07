package cn.edu.hhu.spring.boot.starter.mq.core;

/**
 * 消息服务统一接口
 * 
 * @author intelligent-teaching-system
 */
public interface MessageService extends MessageProducer, MessageConsumer {

    /**
     * 获取生产者
     *
     * @return 消息生产者
     */
    MessageProducer getProducer();

    /**
     * 获取消费者
     *
     * @return 消息消费者
     */
    MessageConsumer getConsumer();

    /**
     * 初始化MQ服务
     */
    void initialize();

    /**
     * 销毁MQ服务
     */
    void destroy();
}
