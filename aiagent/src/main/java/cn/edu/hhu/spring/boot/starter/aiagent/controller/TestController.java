package cn.edu.hhu.spring.boot.starter.aiagent.controller;

import cn.edu.hhu.spring.boot.starter.aiagent.outputmode.FluxOutputStrategy;
import cn.edu.hhu.spring.boot.starter.log.annotation.ILog;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    ChatClient.Builder builder;

    @GetMapping("/chat")
    @ILog
    public Flux<String> chat(@RequestParam("message") String message) {
        return new FluxOutputStrategy(builder).executeResp(message);
    }
}
