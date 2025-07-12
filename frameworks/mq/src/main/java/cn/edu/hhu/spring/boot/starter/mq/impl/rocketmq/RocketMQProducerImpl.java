package cn.edu.hhu.spring.boot.starter.mq.impl.rocketmq;

import cn.edu.hhu.spring.boot.starter.mq.core.MessageProducer;
import cn.edu.hhu.spring.boot.starter.mq.enums.MessageStatusEnum;
import cn.edu.hhu.spring.boot.starter.mq.model.DelayMessage;
import cn.edu.hhu.spring.boot.starter.mq.model.MessageWrapper;
import cn.edu.hhu.spring.boot.starter.mq.model.SendResult;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * RocketMQ生产者实现
 * 
 * @author intelligent-teaching-system
 */
@Slf4j
public class RocketMQProducerImpl implements MessageProducer, InitializingBean, DisposableBean {

    private DefaultMQProducer producer;
    private TransactionMQProducer transactionProducer;
    private final String nameServer;
    private final String producerGroup;

    public RocketMQProducerImpl(String nameServer, String producerGroup) {
        this.nameServer = nameServer;
        this.producerGroup = producerGroup;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化普通生产者
        producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(nameServer);
        producer.setRetryTimesWhenSendFailed(3);
        producer.setRetryTimesWhenSendAsyncFailed(3);
        producer.setSendMsgTimeout(3000);
        
        try {
            producer.start();
            log.info("RocketMQ Producer started successfully");
        } catch (MQClientException e) {
            log.error("Failed to start RocketMQ Producer", e);
            throw new RuntimeException("Failed to start RocketMQ Producer", e);
        }

        // 初始化事务生产者
        transactionProducer = new TransactionMQProducer(producerGroup + "_TRANSACTION");
        transactionProducer.setNamesrvAddr(nameServer);
        
        try {
            transactionProducer.start();
            log.info("RocketMQ Transaction Producer started successfully");
        } catch (MQClientException e) {
            log.error("Failed to start RocketMQ Transaction Producer", e);
        }
    }

    @Override
    public void destroy() throws Exception {
        if (producer != null) {
            producer.shutdown();
            log.info("RocketMQ Producer shutdown successfully");
        }
        if (transactionProducer != null) {
            transactionProducer.shutdown();
            log.info("RocketMQ Transaction Producer shutdown successfully");
        }
    }

    @Override
    public SendResult sendSync(String topic, String tag, Object message) {
        MessageWrapper wrapper = new MessageWrapper(topic, tag, message);
        return sendSync(wrapper);
    }

    @Override
    public SendResult sendSync(MessageWrapper messageWrapper) {
        long startTime = System.currentTimeMillis();
        try {
            Message message = buildMessage(messageWrapper);
            org.apache.rocketmq.client.producer.SendResult rocketResult = producer.send(message);
            
            SendResult result = new SendResult()
                    .setMessageId(rocketResult.getMsgId())
                    .setStatus(MessageStatusEnum.SEND_OK)
                    .setTopic(messageWrapper.getTopic())
                    .setTag(messageWrapper.getTag())
                    .setMessageKey(messageWrapper.getMessageKey())
                    .setQueueId(rocketResult.getMessageQueue().getQueueId())
                    .setQueueOffset(rocketResult.getQueueOffset())
                    .setCostTime(System.currentTimeMillis() - startTime);
            
            log.debug("Message sent successfully: {}", result);
            return result;
            
        } catch (Exception e) {
            log.error("Failed to send message: {}", messageWrapper, e);
            return SendResult.failure(UUID.randomUUID().toString(), e.getMessage())
                    .setCostTime(System.currentTimeMillis() - startTime);
        }
    }

    @Override
    public CompletableFuture<SendResult> sendAsync(String topic, String tag, Object message) {
        MessageWrapper wrapper = new MessageWrapper(topic, tag, message);
        return sendAsync(wrapper);
    }

    @Override
    public CompletableFuture<SendResult> sendAsync(MessageWrapper messageWrapper) {
        CompletableFuture<SendResult> future = new CompletableFuture<>();
        long startTime = System.currentTimeMillis();
        
        try {
            Message message = buildMessage(messageWrapper);
            
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(org.apache.rocketmq.client.producer.SendResult rocketResult) {
                    SendResult result = new SendResult()
                            .setMessageId(rocketResult.getMsgId())
                            .setStatus(MessageStatusEnum.SEND_OK)
                            .setTopic(messageWrapper.getTopic())
                            .setTag(messageWrapper.getTag())
                            .setMessageKey(messageWrapper.getMessageKey())
                            .setQueueId(rocketResult.getMessageQueue().getQueueId())
                            .setQueueOffset(rocketResult.getQueueOffset())
                            .setCostTime(System.currentTimeMillis() - startTime);
                    
                    future.complete(result);
                    log.debug("Message sent asynchronously: {}", result);
                }

                @Override
                public void onException(Throwable e) {
                    SendResult result = SendResult.failure(UUID.randomUUID().toString(), e.getMessage())
                            .setCostTime(System.currentTimeMillis() - startTime);
                    future.complete(result);
                    log.error("Failed to send message asynchronously: {}", messageWrapper, e);
                }
            });
            
        } catch (Exception e) {
            SendResult result = SendResult.failure(UUID.randomUUID().toString(), e.getMessage())
                    .setCostTime(System.currentTimeMillis() - startTime);
            future.complete(result);
            log.error("Failed to send message asynchronously: {}", messageWrapper, e);
        }
        
        return future;
    }

    @Override
    public void sendOneWay(String topic, String tag, Object message) {
        MessageWrapper wrapper = new MessageWrapper(topic, tag, message);
        sendOneWay(wrapper);
    }

    @Override
    public void sendOneWay(MessageWrapper messageWrapper) {
        try {
            Message message = buildMessage(messageWrapper);
            producer.sendOneway(message);
            log.debug("Message sent one way: topic={}, tag={}", messageWrapper.getTopic(), messageWrapper.getTag());
        } catch (Exception e) {
            log.error("Failed to send one way message: {}", messageWrapper, e);
        }
    }

    @Override
    public SendResult sendDelayMessage(DelayMessage delayMessage) {
        long startTime = System.currentTimeMillis();
        try {
            Message message = buildMessage(delayMessage);
            
            // 设置延迟级别或延迟时间
            if (delayMessage.getDelayLevel() != null) {
                message.setDelayTimeLevel(delayMessage.getDelayLevel());
            } else if (delayMessage.getDelayTime() != null) {
                // RocketMQ不直接支持任意延迟时间，这里使用延迟级别近似
                message.setDelayTimeLevel(calculateDelayLevel(delayMessage.getDelayTime()));
            } else if (delayMessage.getExecuteTime() != null) {
                long delayTime = delayMessage.getExecuteTime().toInstant(ZoneOffset.of("+8")).toEpochMilli() 
                        - System.currentTimeMillis();
                message.setDelayTimeLevel(calculateDelayLevel(delayTime));
            }
            
            org.apache.rocketmq.client.producer.SendResult rocketResult = producer.send(message);
            
            SendResult result = new SendResult()
                    .setMessageId(rocketResult.getMsgId())
                    .setStatus(MessageStatusEnum.SEND_OK)
                    .setTopic(delayMessage.getTopic())
                    .setTag(delayMessage.getTag())
                    .setMessageKey(delayMessage.getMessageKey())
                    .setQueueId(rocketResult.getMessageQueue().getQueueId())
                    .setQueueOffset(rocketResult.getQueueOffset())
                    .setCostTime(System.currentTimeMillis() - startTime);
            
            log.debug("Delay message sent successfully: {}", result);
            return result;
            
        } catch (Exception e) {
            log.error("Failed to send delay message: {}", delayMessage, e);
            return SendResult.failure(UUID.randomUUID().toString(), e.getMessage())
                    .setCostTime(System.currentTimeMillis() - startTime);
        }
    }

    @Override
    public SendResult sendTransactionMessage(String topic, String tag, Object message, String transactionId) {
        long startTime = System.currentTimeMillis();
        try {
            MessageWrapper wrapper = new MessageWrapper(topic, tag, message)
                    .setTransactional(true)
                    .setTransactionId(transactionId);
            
            Message rocketMessage = buildMessage(wrapper);
            org.apache.rocketmq.client.producer.SendResult rocketResult = 
                    transactionProducer.sendMessageInTransaction(rocketMessage, transactionId);
            
            SendResult result = new SendResult()
                    .setMessageId(rocketResult.getMsgId())
                    .setStatus(MessageStatusEnum.SEND_OK)
                    .setTopic(topic)
                    .setTag(tag)
                    .setTransactionId(transactionId)
                    .setQueueId(rocketResult.getMessageQueue().getQueueId())
                    .setQueueOffset(rocketResult.getQueueOffset())
                    .setCostTime(System.currentTimeMillis() - startTime);
            
            log.debug("Transaction message sent successfully: {}", result);
            return result;
            
        } catch (Exception e) {
            log.error("Failed to send transaction message: topic={}, tag={}, transactionId={}", 
                    topic, tag, transactionId, e);
            return SendResult.failure(UUID.randomUUID().toString(), e.getMessage())
                    .setCostTime(System.currentTimeMillis() - startTime);
        }
    }

    /**
     * 构建RocketMQ消息
     */
    private Message buildMessage(MessageWrapper messageWrapper) {
        String messageBody = messageWrapper.getMessage() instanceof String 
                ? (String) messageWrapper.getMessage()
                : JSON.toJSONString(messageWrapper.getMessage());
        
        Message message = new Message(
                messageWrapper.getTopic(),
                messageWrapper.getTag(),
                messageWrapper.getMessageKey(),
                messageBody.getBytes(StandardCharsets.UTF_8)
        );

        // 设置自定义属性
        if (messageWrapper.getProperties() != null) {
            messageWrapper.getProperties().forEach(message::putUserProperty);
        }

        return message;
    }

    /**
     * 根据延迟时间计算延迟级别
     * RocketMQ延迟级别: 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
     */
    private int calculateDelayLevel(long delayTimeMs) {
        long delayTimeSeconds = delayTimeMs / 1000;
        
        if (delayTimeSeconds <= 1) return 1;
        if (delayTimeSeconds <= 5) return 2;
        if (delayTimeSeconds <= 10) return 3;
        if (delayTimeSeconds <= 30) return 4;
        if (delayTimeSeconds <= 60) return 5;
        if (delayTimeSeconds <= 120) return 6;
        if (delayTimeSeconds <= 180) return 7;
        if (delayTimeSeconds <= 240) return 8;
        if (delayTimeSeconds <= 300) return 9;
        if (delayTimeSeconds <= 360) return 10;
        if (delayTimeSeconds <= 420) return 11;
        if (delayTimeSeconds <= 480) return 12;
        if (delayTimeSeconds <= 540) return 13;
        if (delayTimeSeconds <= 600) return 14;
        if (delayTimeSeconds <= 1200) return 15;
        if (delayTimeSeconds <= 1800) return 16;
        if (delayTimeSeconds <= 3600) return 17;
        return 18; // 2小时
    }
} 