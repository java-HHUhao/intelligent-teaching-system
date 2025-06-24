package cn.edu.hhu.its.aiagent.service.outputmode;

import cn.edu.hhu.spring.boot.starter.designpattern.strategy.AbstractStrategyExecutor;
import org.springframework.ai.chat.client.ChatClient;
import reactor.core.publisher.Mono;

import java.util.Optional;


public class PlainOutputStrategy implements AbstractStrategyExecutor<PromptPropertiesDTO, Mono<String>> {
    private final ChatClient chatClient;

    public PlainOutputStrategy(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public String mark() {
        return "aiagent-outputStrategy-plain";
    }

    @Override
    public Mono<String> executeResp(PromptPropertiesDTO promptPropertiesDTO) {
        return Mono.just(Optional.ofNullable(chatClient.prompt().user(promptPropertiesDTO.getPrompt()).call().content()).orElse(""));
    }
}
