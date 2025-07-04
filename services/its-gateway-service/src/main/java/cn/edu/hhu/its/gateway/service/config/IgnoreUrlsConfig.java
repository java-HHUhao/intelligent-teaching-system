package cn.edu.hhu.its.gateway.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "spring.cloud.gateway.ignore")
@Data
@RefreshScope
public class IgnoreUrlsConfig {
    private List<String> whitePaths=new ArrayList<>();
}
