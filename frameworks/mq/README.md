# ITS MQ Spring Boot Starter

智能教学系统消息队列启动器，提供统一的消息队列抽象层，支持多种MQ实现。

## 特性

- 🚀 **统一抽象接口**：支持同步/异步发送消息，同步/异步消费消息
- ⏰ **延迟消息支持**：支持延迟消息和定时消息
- 💀 **死信队列处理**：自动处理消费失败的消息
- 🔄 **事务消息**：支持事务消息发送
- 📋 **多种消费模式**：支持同步、异步、批量、顺序、并发消费
- 🔧 **易于配置**：Spring Boot自动配置，开箱即用
- 🏭 **工厂模式**：支持多种MQ实现（当前支持RocketMQ）

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>cn.edu.hhu</groupId>
    <artifactId>its-mq-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### 2. 配置

在 `application.yml` 中添加配置：

```yaml
its:
  mq:
    enabled: true
    type: rocketmq
    name-server: localhost:9876
    producer-group: ITS_PRODUCER_GROUP
```

### 3. 使用

#### 注入服务

```java
@Autowired
private MessageService messageService;
```

#### 发送消息

```java
// 同步发送
SendResult result = messageService.sendSync("TEST_TOPIC", "TEST_TAG", "Hello World!");

// 异步发送
CompletableFuture<SendResult> future = messageService.sendAsync("TEST_TOPIC", "TEST_TAG", "Async Hello!");

// 单向发送
messageService.sendOneWay("TEST_TOPIC", "ONE_WAY_TAG", "One way message");
```

#### 消费消息

```java
// 异步消费
messageService.subscribe("TEST_TOPIC", "*", "CONSUMER_GROUP", 
    (topic, tag, message, messageId) -> {
        log.info("收到消息: {}", message);
        return true; // 返回true表示消费成功
    });

// 同步消费
messageService.subscribe("TEST_TOPIC", "*", "SYNC_GROUP", 
    (topic, tag, message, messageId) -> {
        // 同步处理逻辑
        return true;
    }, ConsumerMode.SYNC);
```

## 高级功能

### 延迟消息

```java
// 延迟5秒
DelayMessage delayMessage = new DelayMessage("DELAY_TOPIC", "DELAY_TAG", "延迟消息", 5000L);
SendResult result = messageService.sendDelayMessage(delayMessage);

// 指定执行时间
DelayMessage scheduledMessage = new DelayMessage("DELAY_TOPIC", "DELAY_TAG", 
    "定时消息", LocalDateTime.now().plusHours(1));
SendResult result2 = messageService.sendDelayMessage(scheduledMessage);
```

### 事务消息

```java
String transactionId = "tx_" + System.currentTimeMillis();
SendResult result = messageService.sendTransactionMessage("TX_TOPIC", "TX_TAG", 
    "事务消息", transactionId);
```

### 死信队列处理

```java
// 消费死信队列（主题名_DLQ）
messageService.subscribe("TEST_TOPIC_DLQ", "*", "DLQ_GROUP", 
    (topic, tag, message, messageId) -> {
        log.error("处理死信消息: {}", message);
        // 死信处理逻辑：记录日志、发送告警、人工干预等
        return true;
    });
```

### 消息包装器

```java
MessageWrapper wrapper = new MessageWrapper("TEST_TOPIC", "TEST_TAG", "消息内容")
    .setMessageKey("unique_key")
    .setTimeout(5000L)
    .setMaxRetryTimes(5)
    .setTransactional(true);

SendResult result = messageService.sendSync(wrapper);
```

## 配置详解

### 完整配置示例

```yaml
its:
  mq:
    # 基础配置
    enabled: true                    # 是否启用MQ
    type: rocketmq                   # MQ类型
    name-server: localhost:9876      # 名称服务器
    producer-group: ITS_PRODUCER     # 生产者组
    
    # RocketMQ特定配置
    rocketmq:
      send-timeout: 3000             # 发送超时(ms)
      retry-times: 3                 # 重试次数
      async-retry-times: 3           # 异步重试次数
      max-message-size: 4194304      # 最大消息大小(字节)
    
    # 消费者配置
    consumer:
      default-group: ITS_CONSUMER    # 默认消费者组
      thread-min: 20                 # 最小线程数
      thread-max: 64                 # 最大线程数
      batch-max-size: 1              # 批量消费大小
      timeout: 15                    # 消费超时(分钟)
      max-retry-times: 16            # 最大重试次数
    
    # 生产者配置  
    producer:
      send-threads: 4                # 发送线程数
      send-queue-size: 10000         # 发送队列大小
      compress-threshold: 4096       # 压缩阈值
      compress-enabled: false        # 是否启用压缩
```

## 消费模式

| 模式 | 描述 | 适用场景 |
|------|------|----------|
| SYNC | 同步消费 | 需要同步处理，对处理顺序有要求 |
| ASYNC | 异步消费 | 高并发场景，快速消费 |
| BATCH | 批量消费 | 批量处理，提高吞吐量 |
| ORDERLY | 顺序消费 | 严格保证消息顺序 |
| CONCURRENTLY | 并发消费 | 普通并发处理 |

## 错误处理

### 重试机制

- 消息消费失败时，会自动重试
- 默认最大重试16次
- 超过重试次数后进入死信队列

### 死信队列

- 死信主题：`原始主题_DLQ`
- 自动创建死信消息包装器
- 包含失败原因和重试次数

## 扩展性

### 支持新的MQ实现

1. 实现 `MessageProducer` 和 `MessageConsumer` 接口
2. 实现 `MessageService` 接口
3. 在 `MessageServiceFactory` 中添加新的创建逻辑

### 自定义消息监听器

```java
public class CustomMessageListener implements MessageListener {
    @Override
    public boolean onMessage(String topic, String tag, String message, String messageId) {
        // 自定义处理逻辑
        return true;
    }
}
```

## 监控和日志

- 自动记录发送和消费日志
- 支持消息发送耗时统计
- 异常情况详细日志记录

## 注意事项

1. **资源管理**：应用关闭时会自动清理MQ连接
2. **线程安全**：所有接口都是线程安全的
3. **重复消费**：业务逻辑需要考虑幂等性
4. **消息大小**：默认最大4MB，可配置调整
5. **连接数**：合理配置线程数，避免资源浪费

## 示例项目

完整使用示例请参考 `MQUsageExample.java` 文件。

## 版本历史

- v0.0.1：初始版本，支持RocketMQ基础功能
  - 同步/异步发送消息
  - 同步/异步消费消息  
  - 延迟消息
  - 死信队列
  - 事务消息
  - 多种消费模式

## 贡献

欢迎提交Issue和Pull Request！ 