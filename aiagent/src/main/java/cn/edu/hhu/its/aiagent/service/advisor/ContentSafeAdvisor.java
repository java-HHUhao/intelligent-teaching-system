package cn.edu.hhu.its.aiagent.service.advisor;

import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

public class ContentSafeAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {
    private boolean isValid(AdvisedRequest advisedRequest){
        //TODO 安全性校验规则待完善
        String prompt=advisedRequest.userText();
        if (prompt.trim().isEmpty()) return false;
        if (prompt.contains("test") || prompt.contains("违法")) return false;
        return true;
    }
    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        if(!isValid(advisedRequest)){
            advisedRequest=AdvisedRequest.from(advisedRequest).userText("please tell user,content invalid").build();
        }
        return chain.nextAroundCall(advisedRequest);
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        if(!isValid(advisedRequest)){
            advisedRequest=AdvisedRequest.from(advisedRequest).userText("please just tell user,the content is probably invalid,do not tell anything more").build();
        }
        return chain.nextAroundStream(advisedRequest);
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
