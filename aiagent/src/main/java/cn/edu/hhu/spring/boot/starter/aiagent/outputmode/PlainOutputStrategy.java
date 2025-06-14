package cn.edu.hhu.spring.boot.starter.aiagent.outputmode;

import cn.edu.hhu.spring.boot.starter.context.contextholder.ApplicationContextHolder;
import cn.edu.hhu.spring.boot.starter.designpattern.strategy.AbstractStrategyExecutor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class PlainOutputStrategy implements AbstractStrategyExecutor<String, Mono<String>> {
    private final ChatClient chatClient;

    public PlainOutputStrategy(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public String mark() {
        return "aiagent-outputStrategy-plain";
    }

    @Override
    public Mono<String> executeResp(String prompt) {
        return Mono.just(Optional.ofNullable(chatClient.prompt().user(prompt).call().content()).orElse(""));
    }
}
