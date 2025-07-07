package cn.edu.hhu.spring.boot.starter.mq.example;

import cn.edu.hhu.spring.boot.starter.mq.core.MessageListener;
import cn.edu.hhu.spring.boot.starter.mq.core.MessageService;
import cn.edu.hhu.spring.boot.starter.mq.enums.ConsumerModeEnum;
import cn.edu.hhu.spring.boot.starter.mq.model.DelayMessage;
import cn.edu.hhu.spring.boot.starter.mq.model.MessageWrapper;
import cn.edu.hhu.spring.boot.starter.mq.model.SendResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * MQ使用示例
 * 
 * @author intelligent-teaching-system
 */
@Slf4j
@Component
public class MQUsageExample {

    @Autowired
    private MessageService messageService;

    /**
     * 同步发送消息示例
     */
    public void syncSendExample() {
        // 方式1：简单发送
        SendResult result = messageService.sendSync("TEST_TOPIC", "TEST_TAG", "Hello World!");
        log.info("同步发送结果: {}", result);

        // 方式2：使用消息包装器
        MessageWrapper wrapper = new MessageWrapper("TEST_TOPIC", "TEST_TAG", "Hello World with wrapper!")
                .setMessageKey("msg_key_001")
                .setTimeout(5000L);
        SendResult result2 = messageService.sendSync(wrapper);
        log.info("同步发送结果（包装器）: {}", result2);
    }

    /**
     * 异步发送消息示例
     */
    public void asyncSendExample() {
        // 异步发送
        CompletableFuture<SendResult> future = messageService.sendAsync("TEST_TOPIC", "TEST_TAG", "Async Hello!");
        
        future.whenComplete((result, throwable) -> {
            if (throwable != null) {
                log.error("异步发送失败", throwable);
            } else {
                log.info("异步发送成功: {}", result);
            }
        });
    }

    /**
     * 单向发送消息示例
     */
    public void oneWaySendExample() {
        // 单向发送，不关心结果
        messageService.sendOneWay("TEST_TOPIC", "ONE_WAY_TAG", "One way message");
        log.info("单向消息发送完成");
    }

    /**
     * 延迟消息示例
     */
    public void delayMessageExample() {
        // 方式1：使用延迟时间（毫秒）
        DelayMessage delayMessage1 = new DelayMessage("DELAY_TOPIC", "DELAY_TAG", "5秒后执行", 5000L);
        SendResult result1 = messageService.sendDelayMessage(delayMessage1);
        log.info("延迟消息发送结果（5秒）: {}", result1);

        // 方式2：使用延迟级别
        DelayMessage delayMessage2 = new DelayMessage("DELAY_TOPIC", "DELAY_TAG", "1分钟后执行", 5); // 级别5=1分钟
        SendResult result2 = messageService.sendDelayMessage(delayMessage2);
        log.info("延迟消息发送结果（1分钟）: {}", result2);

        // 方式3：使用指定执行时间
        DelayMessage delayMessage3 = new DelayMessage("DELAY_TOPIC", "DELAY_TAG", "明天执行", LocalDateTime.now().plusDays(1));
        SendResult result3 = messageService.sendDelayMessage(delayMessage3);
        log.info("延迟消息发送结果（明天）: {}", result3);
    }

    /**
     * 事务消息示例
     */
    public void transactionMessageExample() {
        String transactionId = "tx_" + System.currentTimeMillis();
        SendResult result = messageService.sendTransactionMessage("TX_TOPIC", "TX_TAG", "事务消息内容", transactionId);
        log.info("事务消息发送结果: {}", result);
    }

    /**
     * 同步消费消息示例
     */
    public void syncConsumerExample() {
        messageService.subscribe("TEST_TOPIC", "*", "SYNC_CONSUMER_GROUP", new MessageListener() {
            @Override
            public boolean onMessage(String topic, String tag, String message, String messageId) {
                log.info("同步消费消息: topic={}, tag={}, message={}, messageId={}", topic, tag, message, messageId);
                
                try {
                    // 模拟业务处理
                    Thread.sleep(1000);
                    return true; // 消费成功
                } catch (Exception e) {
                    log.error("消费消息失败", e);
                    return false; // 消费失败，会重试
                }
            }
        }, ConsumerModeEnum.SYNC);
    }

    /**
     * 异步消费消息示例
     */
    public void asyncConsumerExample() {
        messageService.subscribe("TEST_TOPIC", "*", "ASYNC_CONSUMER_GROUP", new MessageListener() {
            @Override
            public boolean onMessage(String topic, String tag, String message, String messageId) {
                log.info("异步消费消息: topic={}, tag={}, message={}, messageId={}", topic, tag, message, messageId);
                
                // 异步处理业务逻辑
                CompletableFuture.runAsync(() -> {
                    try {
                        // 模拟异步业务处理
                        Thread.sleep(500);
                        log.info("异步业务处理完成: messageId={}", messageId);
                    } catch (Exception e) {
                        log.error("异步业务处理失败: messageId={}", messageId, e);
                    }
                });
                
                return true; // 立即返回消费成功
            }
        }, ConsumerModeEnum.ASYNC);
    }

    /**
     * 顺序消费示例
     */
    public void orderlyConsumerExample() {
        messageService.subscribe("ORDER_TOPIC", "*", "ORDERLY_CONSUMER_GROUP", new MessageListener() {
            @Override
            public boolean onMessage(String topic, String tag, String message, String messageId) {
                log.info("顺序消费消息: topic={}, tag={}, message={}, messageId={}", topic, tag, message, messageId);
                
                try {
                    // 顺序处理业务逻辑
                    Thread.sleep(100);
                    return true;
                } catch (Exception e) {
                    log.error("顺序消费失败", e);
                    return false;
                }
            }
        }, ConsumerModeEnum.ORDERLY);
    }

    /**
     * 死信队列消费示例
     */
    public void deadLetterConsumerExample() {
        // 消费死信队列
        messageService.subscribe("TEST_TOPIC_DLQ", "*", "DLQ_CONSUMER_GROUP", new MessageListener() {
            @Override
            public boolean onMessage(String topic, String tag, String message, String messageId) {
                log.error("收到死信消息: topic={}, tag={}, message={}, messageId={}", topic, tag, message, messageId);
                
                // 处理死信消息，比如记录到数据库、发送告警等
                try {
                    // 死信处理逻辑
                    processDLQMessage(message, messageId);
                    return true;
                } catch (Exception e) {
                    log.error("死信消息处理失败", e);
                    return false;
                }
            }
        });
    }

    /**
     * 处理死信消息
     */
    private void processDLQMessage(String message, String messageId) {
        // 实现具体的死信处理逻辑
        log.info("处理死信消息: message={}, messageId={}", message, messageId);
        
        // 可以将死信消息存储到数据库
        // 可以发送告警通知
        // 可以进行人工干预处理等
    }

    /**
     * 取消订阅示例
     */
    public void unsubscribeExample() {
        messageService.unsubscribe("TEST_TOPIC", "ASYNC_CONSUMER_GROUP");
        log.info("取消订阅完成");
    }
} 