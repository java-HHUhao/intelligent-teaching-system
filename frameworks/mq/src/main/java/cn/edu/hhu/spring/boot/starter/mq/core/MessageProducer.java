package cn.edu.hhu.spring.boot.starter.mq.core;

import cn.edu.hhu.spring.boot.starter.mq.model.MessageWrapper;
import cn.edu.hhu.spring.boot.starter.mq.model.DelayMessage;
import cn.edu.hhu.spring.boot.starter.mq.model.SendResult;

import java.util.concurrent.CompletableFuture;

/**
 * 消息生产者核心接口
 * 
 * @author intelligent-teaching-system
 */
public interface MessageProducer {

    /**
     * 同步发送消息
     *
     * @param topic 主题
     * @param tag 标签
     * @param message 消息内容
     * @return 发送结果
     */
    SendResult sendSync(String topic, String tag, Object message);

    /**
     * 同步发送消息
     *
     * @param messageWrapper 消息包装器
     * @return 发送结果
     */
    SendResult sendSync(MessageWrapper messageWrapper);

    /**
     * 异步发送消息
     *
     * @param topic 主题
     * @param tag 标签
     * @param message 消息内容
     * @return 异步结果
     */
    CompletableFuture<SendResult> sendAsync(String topic, String tag, Object message);

    /**
     * 异步发送消息
     *
     * @param messageWrapper 消息包装器
     * @return 异步结果
     */
    CompletableFuture<SendResult> sendAsync(MessageWrapper messageWrapper);

    /**
     * 单向发送消息（不关心结果）
     *
     * @param topic 主题
     * @param tag 标签
     * @param message 消息内容
     */
    void sendOneWay(String topic, String tag, Object message);

    /**
     * 单向发送消息（不关心结果）
     *
     * @param messageWrapper 消息包装器
     */
    void sendOneWay(MessageWrapper messageWrapper);

    /**
     * 发送延迟消息
     *
     * @param delayMessage 延迟消息
     * @return 发送结果
     */
    SendResult sendDelayMessage(DelayMessage delayMessage);

    /**
     * 发送事务消息
     *
     * @param topic 主题
     * @param tag 标签
     * @param message 消息内容
     * @param transactionId 事务ID
     * @return 发送结果
     */
    SendResult sendTransactionMessage(String topic, String tag, Object message, String transactionId);
}
