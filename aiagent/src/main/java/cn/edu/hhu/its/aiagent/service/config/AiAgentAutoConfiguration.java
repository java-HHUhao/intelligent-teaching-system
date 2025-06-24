package cn.edu.hhu.its.aiagent.service.config;

import cn.edu.hhu.its.aiagent.service.manager.ChatModelManager;
import cn.edu.hhu.its.aiagent.service.aimodels.ModelProperties;
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
