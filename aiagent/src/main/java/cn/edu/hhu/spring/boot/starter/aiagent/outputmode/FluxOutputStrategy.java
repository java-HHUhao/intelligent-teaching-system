package cn.edu.hhu.spring.boot.starter.aiagent.outputmode;

import cn.edu.hhu.spring.boot.starter.context.contextholder.ApplicationContextHolder;
import cn.edu.hhu.spring.boot.starter.designpattern.strategy.AbstractStrategyExecutor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class FluxOutputStrategy implements AbstractStrategyExecutor<String, Flux<String>> {
    private final ChatClient chatClient;

    public FluxOutputStrategy(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public String mark() {
        return "aiagent-outputStrategy-flux";
    }

    @Override
    public Flux<String> executeResp(String prompt) {
        return chatClient.prompt().user(prompt).stream().content();
    }
}
