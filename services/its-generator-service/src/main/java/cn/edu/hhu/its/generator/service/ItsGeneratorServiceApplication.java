package cn.edu.hhu.its.generator.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ItsGeneratorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItsGeneratorServiceApplication.class, args);
    }

}
