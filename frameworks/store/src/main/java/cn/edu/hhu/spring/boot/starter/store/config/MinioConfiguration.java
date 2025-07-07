package cn.edu.hhu.spring.boot.starter.store.config;

import cn.edu.hhu.spring.boot.starter.store.utils.MinioUtils;
import io.minio.MinioClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfiguration {

    @Bean
    public MinioClient minioClient(MinioProperties properties) {
        // 初始化MinioUtils
        MinioUtils.init(properties.getEndpoint(), 
                       properties.getAccessKey(), 
                       properties.getSecretKey());
        
        return MinioClient.builder()
                .endpoint(properties.getEndpoint())
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .build();
    }
} 