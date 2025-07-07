package cn.edu.hhu.spring.boot.starter.mq.factory;

import cn.edu.hhu.spring.boot.starter.mq.core.MessageService;
import cn.edu.hhu.spring.boot.starter.mq.impl.rocketmq.RocketMQServiceImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息服务工厂
 * 
 * @author intelligent-teaching-system
 */
@Slf4j
public class MessageServiceFactory {

    /**
     * MQ类型枚举
     */
    @Getter
    public enum MQType {
        ROCKETMQ("rocketmq"),
        REDIS("redis"),
        RABBITMQ("rabbitmq"),
        KAFKA("kafka");

        private final String type;

        MQType(String type) {
            this.type = type;
        }

        public static MQType fromType(String type) {
            for (MQType mqType : values()) {
                if (mqType.type.equalsIgnoreCase(type)) {
                    return mqType;
                }
            }
            return ROCKETMQ; // 默认使用RocketMQ
        }
    }

    /**
     * 创建消息服务
     *
     * @param mqType MQ类型
     * @param nameServer 名称服务器地址
     * @param producerGroup 生产者组
     * @return 消息服务实例
     */
    public static MessageService createMessageService(MQType mqType, String nameServer, String producerGroup) {
        // TODO: 实现Redis消息服务
        // TODO: 实现RabbitMQ消息服务
        // TODO: 实现Kafka消息服务
        return switch (mqType) {
            case ROCKETMQ -> createRocketMQService(nameServer, producerGroup);
            case REDIS -> throw new UnsupportedOperationException("Redis MQ not implemented yet");
            case RABBITMQ -> throw new UnsupportedOperationException("RabbitMQ not implemented yet");
            case KAFKA -> throw new UnsupportedOperationException("Kafka not implemented yet");
            default -> throw new IllegalArgumentException("Unsupported MQ type: " + mqType);
        };
    }

    /**
     * 创建消息服务（使用字符串类型）
     *
     * @param mqType MQ类型字符串
     * @param nameServer 名称服务器地址
     * @param producerGroup 生产者组
     * @return 消息服务实例
     */
    public static MessageService createMessageService(String mqType, String nameServer, String producerGroup) {
        return createMessageService(MQType.fromType(mqType), nameServer, producerGroup);
    }

    /**
     * 创建RocketMQ服务
     *
     * @param nameServer 名称服务器地址
     * @param producerGroup 生产者组
     * @return RocketMQ服务实例
     */
    private static MessageService createRocketMQService(String nameServer, String producerGroup) {
        log.info("Creating RocketMQ service with nameServer={}, producerGroup={}", nameServer, producerGroup);
        return new RocketMQServiceImpl(nameServer, producerGroup);
    }

    /**
     * 创建默认消息服务（RocketMQ）
     *
     * @param nameServer 名称服务器地址
     * @param producerGroup 生产者组
     * @return 默认消息服务实例
     */
    public static MessageService createDefaultMessageService(String nameServer, String producerGroup) {
        return createMessageService(MQType.ROCKETMQ, nameServer, producerGroup);
    }
}
