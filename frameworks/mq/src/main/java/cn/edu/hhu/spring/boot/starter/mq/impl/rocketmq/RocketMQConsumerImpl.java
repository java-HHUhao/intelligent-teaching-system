package cn.edu.hhu.spring.boot.starter.mq.impl.rocketmq;

import cn.edu.hhu.spring.boot.starter.mq.core.MessageConsumer;
import cn.edu.hhu.spring.boot.starter.mq.core.MessageListener;
import cn.edu.hhu.spring.boot.starter.mq.enums.ConsumerModeEnum;
import cn.edu.hhu.spring.boot.starter.mq.model.DeadLetterMessage;
import cn.edu.hhu.spring.boot.starter.mq.model.MessageWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RocketMQ消费者实现
 * 
 * @author intelligent-teaching-system
 */
@Slf4j
public class RocketMQConsumerImpl implements MessageConsumer, InitializingBean, DisposableBean {

    private final Map<String, DefaultMQPushConsumer> consumers = new ConcurrentHashMap<>();
    private final String nameServer;

    public RocketMQConsumerImpl(String nameServer) {
        this.nameServer = nameServer;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("RocketMQ Consumer initialized");
    }

    @Override
    public void destroy() throws Exception {
        consumers.values().forEach(consumer -> {
            try {
                consumer.shutdown();
                log.info("RocketMQ Consumer shutdown: {}", consumer.getConsumerGroup());
            } catch (Exception e) {
                log.error("Failed to shutdown consumer: {}", consumer.getConsumerGroup(), e);
            }
        });
        consumers.clear();
    }

    @Override
    public void subscribe(String topic, String tag, String consumerGroup, MessageListener listener, ConsumerModeEnum mode) {
        try {
            String consumerKey = consumerGroup + "@" + topic;
            
            if (consumers.containsKey(consumerKey)) {
                log.warn("Consumer already exists for topic={}, consumerGroup={}", topic, consumerGroup);
                return;
            }

            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
            consumer.setNamesrvAddr(nameServer);
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
            
            // 设置消费模式
            setupConsumerMode(consumer, mode);
            
            // 订阅主题
            consumer.subscribe(topic, tag);
            
            // 注册消息监听器
            registerMessageListener(consumer, listener, mode, topic, tag, consumerGroup);
            
            // 启动消费者
            consumer.start();
            consumers.put(consumerKey, consumer);
            
            log.info("Successfully subscribed to topic={}, tag={}, consumerGroup={}, mode={}", 
                    topic, tag, consumerGroup, mode);
            
        } catch (Exception e) {
            log.error("Failed to subscribe to topic={}, tag={}, consumerGroup={}", 
                    topic, tag, consumerGroup, e);
            throw new RuntimeException("Failed to subscribe", e);
        }
    }

    @Override
    public void subscribe(String topic, String tag, String consumerGroup, MessageListener listener) {
        subscribe(topic, tag, consumerGroup, listener, ConsumerModeEnum.ASYNC);
    }

    @Override
    public void unsubscribe(String topic, String consumerGroup) {
        String consumerKey = consumerGroup + "@" + topic;
        DefaultMQPushConsumer consumer = consumers.remove(consumerKey);
        
        if (consumer != null) {
            try {
                consumer.shutdown();
                log.info("Successfully unsubscribed from topic={}, consumerGroup={}", topic, consumerGroup);
            } catch (Exception e) {
                log.error("Failed to unsubscribe from topic={}, consumerGroup={}", topic, consumerGroup, e);
            }
        }
    }

    @Override
    public void start() {
        // 消费者在订阅时已启动
        log.info("RocketMQ Consumer service started");
    }

    @Override
    public void shutdown() {
        try {
            destroy();
        } catch (Exception e) {
            log.error("Failed to shutdown RocketMQ Consumer", e);
        }
    }

    /**
     * 设置消费模式
     */
    private void setupConsumerMode(DefaultMQPushConsumer consumer, ConsumerModeEnum mode) {
        switch (mode) {
            case SYNC:
                consumer.setConsumeMessageBatchMaxSize(1);
                consumer.setConsumeThreadMin(1);
                consumer.setConsumeThreadMax(1);
                break;
            case ASYNC:
                consumer.setConsumeMessageBatchMaxSize(1);
                consumer.setConsumeThreadMin(20);
                consumer.setConsumeThreadMax(64);
                break;
            case BATCH:
                consumer.setConsumeMessageBatchMaxSize(32);
                consumer.setConsumeThreadMin(20);
                consumer.setConsumeThreadMax(64);
                break;
            case ORDERLY:
                consumer.setConsumeMessageBatchMaxSize(1);
                consumer.setConsumeThreadMin(1);
                consumer.setConsumeThreadMax(1);
                break;
            case CONCURRENTLY:
                consumer.setConsumeMessageBatchMaxSize(1);
                consumer.setConsumeThreadMin(20);
                consumer.setConsumeThreadMax(64);
                break;
            default:
                // 默认异步消费
                consumer.setConsumeMessageBatchMaxSize(1);
                consumer.setConsumeThreadMin(20);
                consumer.setConsumeThreadMax(64);
        }
    }

    /**
     * 注册消息监听器
     */
    private void registerMessageListener(DefaultMQPushConsumer consumer, MessageListener listener,
                                         ConsumerModeEnum mode, String topic, String tag, String consumerGroup) {
        
        if (mode == ConsumerModeEnum.ORDERLY) {
            // 顺序消费
            consumer.registerMessageListener(new MessageListenerOrderly() {
                @Override
                public ConsumeOrderlyStatus consumeMessage(List<MessageExt> messages, 
                                                          ConsumeOrderlyContext context) {
                    return processMessages(messages, listener, topic, tag, consumerGroup) 
                            ? ConsumeOrderlyStatus.SUCCESS 
                            : ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
            });
        } else {
            // 并发消费
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages, 
                                                              ConsumeConcurrentlyContext context) {
                    return processMessages(messages, listener, topic, tag, consumerGroup) 
                            ? ConsumeConcurrentlyStatus.CONSUME_SUCCESS 
                            : ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            });
        }
    }

    /**
     * 处理消息
     */
    private boolean processMessages(List<MessageExt> messages, MessageListener listener, 
                                  String topic, String tag, String consumerGroup) {
        boolean allSuccess = true;
        
        for (MessageExt messageExt : messages) {
            try {
                String messageBody = new String(messageExt.getBody(), StandardCharsets.UTF_8);
                String messageId = messageExt.getMsgId();
                
                // 调用业务处理逻辑
                boolean success = listener.onMessage(topic, tag, messageBody, messageId);
                
                if (!success) {
                    allSuccess = false;
                    log.warn("Message processing failed: topic={}, tag={}, messageId={}", 
                            topic, tag, messageId);
                    
                    // 检查是否需要发送到死信队列
                    if (shouldSendToDeadLetter(messageExt)) {
                        sendToDeadLetterQueue(messageExt, consumerGroup, "业务处理失败");
                    }
                }
                
            } catch (Exception e) {
                allSuccess = false;
                log.error("Exception occurred while processing message: topic={}, tag={}, messageId={}", 
                        topic, tag, messageExt.getMsgId(), e);
                
                // 检查是否需要发送到死信队列
                if (shouldSendToDeadLetter(messageExt)) {
                    sendToDeadLetterQueue(messageExt, consumerGroup, e.getMessage());
                }
            }
        }
        
        return allSuccess;
    }

    /**
     * 判断是否应该发送到死信队列
     */
    private boolean shouldSendToDeadLetter(MessageExt messageExt) {
        // RocketMQ会自动重试，这里检查重试次数
        return messageExt.getReconsumeTimes() >= 16; // RocketMQ默认最大重试16次
    }

    /**
     * 发送到死信队列
     */
    private void sendToDeadLetterQueue(MessageExt messageExt, String consumerGroup, String failureReason) {
        try {
            // 构建原始消息包装器
            MessageWrapper originalMessage = new MessageWrapper()
                    .setTopic(messageExt.getTopic())
                    .setTag(messageExt.getTags())
                    .setMessage(new String(messageExt.getBody(), StandardCharsets.UTF_8))
                    .setMessageId(messageExt.getMsgId())
                    .setMessageKey(messageExt.getKeys())
                    .setConsumerGroup(consumerGroup)
                    .setRetryTimes(messageExt.getReconsumeTimes());

            // 创建死信消息
            DeadLetterMessage deadLetterMessage = new DeadLetterMessage(originalMessage, failureReason);
            
            log.error("Sending message to dead letter queue: originalTopic={}, messageId={}, reason={}", 
                    messageExt.getTopic(), messageExt.getMsgId(), failureReason);
            
            // 这里可以通过生产者发送到死信主题
            // 由于这是消费者实现，暂时只记录日志
            
        } catch (Exception e) {
            log.error("Failed to send message to dead letter queue: messageId={}", 
                    messageExt.getMsgId(), e);
        }
    }
} 