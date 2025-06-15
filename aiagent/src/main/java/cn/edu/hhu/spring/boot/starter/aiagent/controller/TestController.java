package cn.edu.hhu.spring.boot.starter.aiagent.controller;

import cn.edu.hhu.spring.boot.starter.aiagent.advisor.ContentSafeAdvisor;
import cn.edu.hhu.spring.boot.starter.aiagent.advisor.RereadingAdvisor;
import cn.edu.hhu.spring.boot.starter.aiagent.outputmode.FluxOutputStrategy;
import cn.edu.hhu.spring.boot.starter.aiagent.outputmode.PromptPropertiesDTO;
import cn.edu.hhu.spring.boot.starter.log.annotation.ILog;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    ChatClient.Builder builder;
    ChatClient chatClient;
    @PostConstruct
    public void init() {
        ChatMemory chatMemory=new InMemoryChatMemory();
        this.chatClient = builder.defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory),
                new RereadingAdvisor(),
                new ContentSafeAdvisor()).build(); // 保证在 Spring 注入之后再执行
    }

    @GetMapping("/chat")
    @ILog
    public Flux<String> chat(@RequestParam("message") String message) {
        PromptPropertiesDTO promptProperties=new PromptPropertiesDTO(message,"555",10);
        return new FluxOutputStrategy(chatClient).executeResp(promptProperties);
    }
}
