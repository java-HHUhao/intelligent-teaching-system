package cn.edu.hhu.spring.boot.starter.store.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "its.minio")
public class MinioProperties {
    private String endpoint = "http://127.0.0.1:9000";
    private String accessKey="root";
    private String secretKey="rootroot";
    private String bucketName = "its-bucket";
    private boolean secure = false;

}