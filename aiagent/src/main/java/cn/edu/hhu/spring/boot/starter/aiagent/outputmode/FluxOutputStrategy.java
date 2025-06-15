package cn.edu.hhu.spring.boot.starter.aiagent.outputmode;

import cn.edu.hhu.spring.boot.starter.designpattern.strategy.AbstractStrategyExecutor;
import org.springframework.ai.chat.client.ChatClient;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;


public class FluxOutputStrategy implements AbstractStrategyExecutor<PromptPropertiesDTO,Flux<String>> {
    private final ChatClient chatClient;

    public FluxOutputStrategy(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public String mark() {
        return "aiagent-outputStrategy-flux";
    }

    @Override
    public Flux<String> executeResp(PromptPropertiesDTO promptPropertiesDTO) {
        return chatClient.prompt()
                .user(promptPropertiesDTO.getPrompt())
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY,promptPropertiesDTO.getChatId())
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, promptPropertiesDTO.getMemorySize()))
                .stream().content();
    }
}
