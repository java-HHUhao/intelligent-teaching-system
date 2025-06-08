package cn.edu.hhu.aiagent.controller;

import cn.edu.hhu.spring.boot.starter.log.annotation.ILog;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {
    ChatClient chatClient;

    public TestController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/chat")
    @ILog
    public String chat(@RequestParam("message") String message) {
        ChatResponse chatResponse = chatClient.prompt(new Prompt(new UserMessage(message)))
                .call()
                .chatResponse();
        return chatResponse.getResult().getOutput().toString();
    }
}
