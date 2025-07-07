package cn.edu.hhu.spring.boot.starter.mq.config;

import cn.edu.hhu.spring.boot.starter.mq.core.MessageService;
import cn.edu.hhu.spring.boot.starter.mq.factory.MessageServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MQ自动配置类
 * 
 * @author intelligent-teaching-system
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(MQProperties.class)
@ConditionalOnProperty(prefix = "its.mq", name = "enabled", havingValue = "true", matchIfMissing = true)
public class MQAutoConfiguration {

    private final MQProperties mqProperties;

    public MQAutoConfiguration(MQProperties mqProperties) {
        this.mqProperties = mqProperties;
    }

    /**
     * 创建消息服务Bean
     */
    @Bean
    @ConditionalOnMissingBean
    public MessageService messageService() {
        log.info("Initializing MQ service with type: {}, nameServer: {}, producerGroup: {}", 
                mqProperties.getType(), mqProperties.getNameServer(), mqProperties.getProducerGroup());
        
        MessageService messageService = MessageServiceFactory.createMessageService(
                mqProperties.getType(),
                mqProperties.getNameServer(),
                mqProperties.getProducerGroup()
        );
        
        // 初始化服务
        messageService.initialize();
        
        log.info("MQ service initialized successfully");
        return messageService;
    }

    /**
     * 注册MQ配置信息到Spring容器
     */
    @Bean
    @ConditionalOnMissingBean
    public MQProperties mqProperties() {
        return mqProperties;
    }
} 