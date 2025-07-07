package cn.edu.hhu.spring.boot.starter.mq.core;

/**
 * 消息监听器接口
 * 
 * @author intelligent-teaching-system
 */
@FunctionalInterface
public interface MessageListener {

    /**
     * 处理接收到的消息
     *
     * @param topic 主题
     * @param tag 标签
     * @param message 消息内容
     * @param messageId 消息ID
     * @return 是否消费成功
     */
    boolean onMessage(String topic, String tag, String message, String messageId);
}
