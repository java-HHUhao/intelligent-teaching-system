package cn.edu.hhu.aiagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class AIAgentApplication {
    public static void main(String[] args) {
        SpringApplication.run(AIAgentApplication.class, args);
    }
}
