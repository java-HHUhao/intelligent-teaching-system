package cn.edu.hhu.spring.boot.starter.aiagent.config;

import cn.edu.hhu.spring.boot.starter.aiagent.manager.ChatModelManager;
import cn.edu.hhu.spring.boot.starter.aiagent.aimodels.ModelProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiAgentAutoConfiguration {
    @Bean
    public ModelProperties modelProperties() {
        return new ModelProperties();
    }

    @Bean
    public ChatModelManager chatClientManager(ModelProperties modelProperties) {
        return new ChatModelManager();
    }
}
