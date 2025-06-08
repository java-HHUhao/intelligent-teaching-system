package cn.edu.hhu.spring.boot.starter.designpattern.config;

import cn.edu.hhu.spring.boot.starter.designpattern.chain.AbstractChainContext;
import cn.edu.hhu.spring.boot.starter.designpattern.strategy.AbstractStrategyChooser;
import org.springframework.context.annotation.Bean;

public class DesignPatternAutoConfiguration {
    @Bean
    AbstractChainContext abstractChainContext(){return new AbstractChainContext();}
    @Bean
    AbstractStrategyChooser abstractStrategyChooser(){return new AbstractStrategyChooser();}
}
