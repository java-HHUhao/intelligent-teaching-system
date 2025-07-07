package cn.edu.hhu.spring.boot.starter.mq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MQ配置属性
 * 
 * @author intelligent-teaching-system
 */
@Data
@ConfigurationProperties(prefix = "its.mq")
public class MQProperties {

    /**
     * 是否启用MQ
     */
    private boolean enabled = true;

    /**
     * MQ类型（rocketmq, redis, rabbitmq, kafka）
     */
    private String type = "rocketmq";

    /**
     * 名称服务器地址
     */
    private String nameServer = "localhost:9876";

    /**
     * 生产者组
     */
    private String producerGroup = "DEFAULT_PRODUCER_GROUP";

    /**
     * RocketMQ特定配置
     */
    private RocketMQ rocketmq = new RocketMQ();

    /**
     * Redis特定配置
     */
    private Redis redis = new Redis();

    /**
     * 消费者配置
     */
    private Consumer consumer = new Consumer();

    /**
     * 生产者配置
     */
    private Producer producer = new Producer();

    @Data
    public static class RocketMQ {
        /**
         * 发送超时时间（毫秒）
         */
        private int sendTimeout = 3000;

        /**
         * 发送失败重试次数
         */
        private int retryTimes = 3;

        /**
         * 异步发送失败重试次数
         */
        private int asyncRetryTimes = 3;

        /**
         * 最大消息大小（字节）
         */
        private int maxMessageSize = 4194304; // 4MB
    }

    @Data
    public static class Redis {
        /**
         * Redis连接地址
         */
        private String host = "localhost";

        /**
         * Redis端口
         */
        private int port = 6379;

        /**
         * Redis密码
         */
        private String password;

        /**
         * Redis数据库索引
         */
        private int database = 0;

        /**
         * 连接超时时间（毫秒）
         */
        private int timeout = 3000;
    }

    @Data
    public static class Consumer {
        /**
         * 默认消费者组
         */
        private String defaultGroup = "DEFAULT_CONSUMER_GROUP";

        /**
         * 消费线程数最小值
         */
        private int threadMin = 20;

        /**
         * 消费线程数最大值
         */
        private int threadMax = 64;

        /**
         * 批量消费最大消息数
         */
        private int batchMaxSize = 1;

        /**
         * 消费超时时间（分钟）
         */
        private int timeout = 15;

        /**
         * 最大重试次数
         */
        private int maxRetryTimes = 16;
    }

    @Data
    public static class Producer {
        /**
         * 发送队列线程数
         */
        private int sendThreads = 4;

        /**
         * 发送队列大小
         */
        private int sendQueueSize = 10000;

        /**
         * 压缩消息阈值（字节）
         */
        private int compressThreshold = 4096;

        /**
         * 是否启用压缩
         */
        private boolean compressEnabled = false;
    }
} 