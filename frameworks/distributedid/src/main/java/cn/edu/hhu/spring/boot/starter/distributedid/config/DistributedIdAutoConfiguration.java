package cn.edu.hhu.spring.boot.starter.distributedid.config;

import cn.edu.hhu.spring.boot.starter.context.contextholder.ApplicationContextHolder;
import cn.edu.hhu.spring.boot.starter.distributedid.core.serviceid.workid.RandomWorkIdChoose;
import cn.edu.hhu.spring.boot.starter.distributedid.core.serviceid.workid.RedisWorkIdChoose;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(ApplicationContextHolder.class)
public class DistributedIdAutoConfiguration {
    /**
     * 本地 Redis 构建雪花 WorkId 选择器
     */
    @Bean
    @ConditionalOnProperty("spring.data.redis.host")
    public RedisWorkIdChoose redisWorkIdChoose() {
        return new RedisWorkIdChoose();
    }

    /**
     * 随机数构建雪花 WorkId 选择器。如果项目未使用 Redis，使用该选择器
     */
    @Bean
    @ConditionalOnMissingBean(RedisWorkIdChoose.class)
    public RandomWorkIdChoose randomWorkIdChoose() {
        return new RandomWorkIdChoose();
    }
}
