package cn.edu.hhu.spring.boot.starter.aiagent.aimodels;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties("ai")
public class ModelProperties {
    private List<ModelConfig> models;
    @Data
    public static class ModelConfig {
        private String type;
        private String model;
    }
}
