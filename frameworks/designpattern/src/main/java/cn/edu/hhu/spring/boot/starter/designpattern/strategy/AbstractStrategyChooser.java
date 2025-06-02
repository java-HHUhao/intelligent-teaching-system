package cn.edu.hhu.spring.boot.starter.designpattern.strategy;

import cn.edu.hhu.spring.boot.starter.common.exception.ServerException;
import cn.edu.hhu.spring.boot.starter.context.contextholder.ApplicationContextHolder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class AbstractStrategyChooser implements ApplicationListener<ApplicationEvent> {
    /**
     * 策略集合
     */
    private final Map<String,AbstractStrategyExecutor> abstractStrategyExecutorMap=new HashMap<>();

    public AbstractStrategyExecutor choose(String mark,Boolean predicateFlag){
        if(predicateFlag!=null&&!predicateFlag){
            return abstractStrategyExecutorMap.values().stream()
                    .filter(each-> StringUtils.hasLength(each.patternMatchMark()))
                    .filter(each-> Pattern.compile(each.patternMatchMark()).matcher(mark).matches())
                    .findFirst()
                    .orElseThrow(()->new ServerException("策略未定义"));
        }
        return Optional.ofNullable(abstractStrategyExecutorMap.get(mark))
                .orElseThrow(()->new ServerException(String.format("[%s] 策略未定义",mark)));
    }

    /**
     * 根据策略mark，寻找具体策略并执行
     *
     */
    public <REQUEST>void choose(String mark,REQUEST request){
        AbstractStrategyExecutor strategyExecutor=choose(mark,null);
        strategyExecutor.execute(request);
    }

    public <REQUEST>void  choose(String mark,REQUEST request,Boolean predicateFlag){
        AbstractStrategyExecutor strategyExecutor=choose(mark,predicateFlag);
        strategyExecutor.execute(request);
    }
    /**
     * 根据策略mark 寻找具体策略执行并返回结果
     */
    public <REQUEST,RESPONSE> RESPONSE chooseResp(String mark,REQUEST request){
        AbstractStrategyExecutor strategyExecutor=choose(mark,null);
        return (RESPONSE) strategyExecutor.executeResp(request);
    }
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        Map<String,AbstractStrategyExecutor> actual= ApplicationContextHolder.getBeansOfType(AbstractStrategyExecutor.class);
        actual.forEach((beanName,executor)->{
            AbstractStrategyExecutor strategyExecutor=abstractStrategyExecutorMap.get(executor.mark());
            if(strategyExecutor!=null){
                throw new ServerException(String.format("[%s] 策略重复定义",executor.mark()));
            }
            abstractStrategyExecutorMap.put(executor.mark(),executor);
        });
    }
}
