package cn.edu.hhu.its.aiagent.service.controller;

import cn.edu.hhu.its.aiagent.service.advisor.ContentSafeAdvisor;
import cn.edu.hhu.its.aiagent.service.advisor.RereadingAdvisor;
import cn.edu.hhu.its.aiagent.service.outputmode.FluxOutputStrategy;
import cn.edu.hhu.its.aiagent.service.outputmode.PromptPropertiesDTO;
import cn.edu.hhu.spring.boot.starter.log.annotation.ILog;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    Advisor ragCloudAdvisor;
    @Autowired
    VectorStore vectorStore;
    @Autowired
    ChatClient.Builder builder;
    @Resource
    private ToolCallback[] allTools;
    ChatClient chatClient;
    @PostConstruct
    public void init() {
        ChatMemory chatMemory=new InMemoryChatMemory();
        this.chatClient = builder.defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory),
                new RereadingAdvisor(),
                ragCloudAdvisor,
                new ContentSafeAdvisor())
                .defaultTools(allTools)
                .build();
    }

    @GetMapping("/chat")
    @ILog
    public Flux<String> chat(@RequestParam("message") String message) {
        PromptPropertiesDTO promptProperties=new PromptPropertiesDTO(message,"555",10);
        return new FluxOutputStrategy(chatClient).executeResp(promptProperties);
    }

    @GetMapping("/input")
    @ILog
    public String input(){
        List<Document> documents = List.of(
                new Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", Map.of("meta1", "meta1")),
                new Document("The World is Big and Salvation Lurks Around the Corner"),
                new Document("You walk forward facing the past and you turn back toward the future.", Map.of("meta2", "meta2")));
        vectorStore.add(documents);
        List<Document> results = this.vectorStore.similaritySearch(SearchRequest.builder().query("Spring").topK(5).build());
        System.out.println(results);
        return "空";
    }

    @GetMapping("/generate")
    public Flux<String> doChatWithTools(@RequestParam("message") String message){
        PromptPropertiesDTO promptProperties=new PromptPropertiesDTO(message,"666",10);
        return new FluxOutputStrategy(chatClient).executeResp(promptProperties);
    }
}
