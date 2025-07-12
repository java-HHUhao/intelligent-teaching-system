package cn.edu.hhu.its.message.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 消息中心服务启动类
 * 
 * @author ITS项目组
 */
@SpringBootApplication
@MapperScan("cn.edu.hhu.its.message.service.model.mapper")
@EnableAsync
@EnableScheduling
public class ItsMessageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItsMessageServiceApplication.class, args);
    }

}
