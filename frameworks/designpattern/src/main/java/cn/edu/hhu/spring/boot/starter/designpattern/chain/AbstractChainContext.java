package cn.edu.hhu.spring.boot.starter.designpattern.chain;

import cn.edu.hhu.spring.boot.starter.context.contextholder.ApplicationContextHolder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 抽象责任链上下文
 *
 */
public final class AbstractChainContext<T> implements CommandLineRunner {

    private final Map<String, List<AbstractChainHandler>> chainContainer =new HashMap<>();

    public void handle(String mark,T requestParam){
        List<AbstractChainHandler> chain= chainContainer.get(mark);
        if(chain==null){throw new RuntimeException(String.format("%s chain 未定义",mark));}
        for(AbstractChainHandler chainHandler:chain){
            chainHandler.handle(requestParam);
        }
    }

    /**
     * 启动时将所有责任链处理器按照mark注册到责任链容器中
     *
     */
    @Override
    public void run(String... args) throws Exception {
        //获取所有具体处理器
        Map<String,AbstractChainHandler> chainFilterMap= ApplicationContextHolder.getBeansOfType(AbstractChainHandler.class);
        //将具体处理器按照mark分组
        chainFilterMap.forEach((beanName,bean)->{
            List<AbstractChainHandler> chain=chainContainer.get(bean.mark());
            if (chain.isEmpty()){
                chain=new ArrayList<>();
            }
            chain.add(bean);
            //每个分组内再按照order排序
            List<AbstractChainHandler> actualChainHandlers=chain.stream()
                    .sorted(Comparator.comparing(Ordered::getOrder))
                    .collect(Collectors.toList());
            //最后装入容器内
            chainContainer.put(bean.mark(), actualChainHandlers);
        });
    }
}
