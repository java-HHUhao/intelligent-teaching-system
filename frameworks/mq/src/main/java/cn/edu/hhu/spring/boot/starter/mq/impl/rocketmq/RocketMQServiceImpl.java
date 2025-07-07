package cn.edu.hhu.spring.boot.starter.mq.impl.rocketmq;

import cn.edu.hhu.spring.boot.starter.mq.core.MessageConsumer;
import cn.edu.hhu.spring.boot.starter.mq.core.MessageListener;
import cn.edu.hhu.spring.boot.starter.mq.core.MessageProducer;
import cn.edu.hhu.spring.boot.starter.mq.core.MessageService;
import cn.edu.hhu.spring.boot.starter.mq.enums.ConsumerModeEnum;
import cn.edu.hhu.spring.boot.starter.mq.model.DelayMessage;
import cn.edu.hhu.spring.boot.starter.mq.model.MessageWrapper;
import cn.edu.hhu.spring.boot.starter.mq.model.SendResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.CompletableFuture;

/**
 * RocketMQ服务实现
 * 
 * @author intelligent-teaching-system
 */
@Slf4j
public class RocketMQServiceImpl implements MessageService, InitializingBean, DisposableBean {

    private final MessageProducer producer;
    private final MessageConsumer consumer;

    public RocketMQServiceImpl(String nameServer, String producerGroup) {
        this.producer = new RocketMQProducerImpl(nameServer, producerGroup);
        this.consumer = new RocketMQConsumerImpl(nameServer);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initialize();
    }

    @Override
    public void destroy() {
        try {
            if (producer instanceof DisposableBean) {
                ((DisposableBean) producer).destroy();
            }
            if (consumer instanceof DisposableBean) {
                ((DisposableBean) consumer).destroy();
            }
            log.info("RocketMQ Service destroyed successfully");
        } catch (Exception e) {
            log.error("Failed to destroy RocketMQ Service", e);
        }
    }

    @Override
    public void initialize() {
        try {
            if (producer instanceof InitializingBean) {
                ((InitializingBean) producer).afterPropertiesSet();
            }
            if (consumer instanceof InitializingBean) {
                ((InitializingBean) consumer).afterPropertiesSet();
            }
            log.info("RocketMQ Service initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize RocketMQ Service", e);
            throw new RuntimeException("Failed to initialize RocketMQ Service", e);
        }
    }

    @Override
    public MessageProducer getProducer() {
        return producer;
    }

    @Override
    public MessageConsumer getConsumer() {
        return consumer;
    }

    // MessageProducer接口实现
    @Override
    public SendResult sendSync(String topic, String tag, Object message) {
        return producer.sendSync(topic, tag, message);
    }

    @Override
    public SendResult sendSync(MessageWrapper messageWrapper) {
        return producer.sendSync(messageWrapper);
    }

    @Override
    public CompletableFuture<SendResult> sendAsync(String topic, String tag, Object message) {
        return producer.sendAsync(topic, tag, message);
    }

    @Override
    public CompletableFuture<SendResult> sendAsync(MessageWrapper messageWrapper) {
        return producer.sendAsync(messageWrapper);
    }

    @Override
    public void sendOneWay(String topic, String tag, Object message) {
        producer.sendOneWay(topic, tag, message);
    }

    @Override
    public void sendOneWay(MessageWrapper messageWrapper) {
        producer.sendOneWay(messageWrapper);
    }

    @Override
    public SendResult sendDelayMessage(DelayMessage delayMessage) {
        return producer.sendDelayMessage(delayMessage);
    }

    @Override
    public SendResult sendTransactionMessage(String topic, String tag, Object message, String transactionId) {
        return producer.sendTransactionMessage(topic, tag, message, transactionId);
    }

    // MessageConsumer接口实现
    @Override
    public void subscribe(String topic, String tag, String consumerGroup, MessageListener listener, ConsumerModeEnum mode) {
        consumer.subscribe(topic, tag, consumerGroup, listener, mode);
    }

    @Override
    public void subscribe(String topic, String tag, String consumerGroup, MessageListener listener) {
        consumer.subscribe(topic, tag, consumerGroup, listener);
    }

    @Override
    public void unsubscribe(String topic, String consumerGroup) {
        consumer.unsubscribe(topic, consumerGroup);
    }

    @Override
    public void start() {
        consumer.start();
    }

    @Override
    public void shutdown() {
        consumer.shutdown();
    }
} 