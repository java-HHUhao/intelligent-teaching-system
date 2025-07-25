package cn.edu.hhu.its.aiagent.service.aimodels;

import lombok.Data;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.document.DocumentTransformer;
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
