/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.edu.hhu.spring.boot.starter.idempotent.config;
import cn.edu.hhu.spring.boot.starter.cache.core.service.distributed.DistributedCache;
import cn.edu.hhu.spring.boot.starter.idempotent.aop.IdempotentAspect;
import cn.edu.hhu.spring.boot.starter.idempotent.core.param.IdempotentParamExecuteHandler;
import cn.edu.hhu.spring.boot.starter.idempotent.core.param.IdempotentParamHandler;
import cn.edu.hhu.spring.boot.starter.idempotent.core.spel.IdempotentSpELByMQExecuteHandler;
import cn.edu.hhu.spring.boot.starter.idempotent.core.spel.IdempotentSpELByRestAPIExecuteHandler;
import cn.edu.hhu.spring.boot.starter.idempotent.core.spel.IdempotentSpELHandler;
import cn.edu.hhu.spring.boot.starter.idempotent.core.token.IdempotentTokenController;
import cn.edu.hhu.spring.boot.starter.idempotent.core.token.IdempotentTokenExecuteHandler;
import cn.edu.hhu.spring.boot.starter.idempotent.core.token.IdempotentTokenHandler;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 幂等自动装配
 */
@EnableConfigurationProperties(IdempotentProperties.class)
public class IdempotentAutoConfiguration {

    /**
     * 幂等切面
     */
    @Bean
    public IdempotentAspect idempotentAspect() {
        return new IdempotentAspect();
    }

    /**
     * 参数方式幂等实现，基于 RestAPI 场景
     */
    @Bean
    @ConditionalOnMissingBean
    public IdempotentParamHandler idempotentParamExecuteHandler(RedissonClient redissonClient) {
        return new IdempotentParamExecuteHandler(redissonClient);
    }

    /**
     * Token 方式幂等实现，基于 RestAPI 场景
     */
    @Bean
    @ConditionalOnMissingBean
    public IdempotentTokenHandler idempotentTokenExecuteHandler(DistributedCache distributedCache,
                                                                IdempotentProperties idempotentProperties) {
        return new IdempotentTokenExecuteHandler(distributedCache, idempotentProperties);
    }

    /**
     * 申请幂等 Token 控制器，基于 RestAPI 场景
     */
    @Bean
    public IdempotentTokenController idempotentTokenController(IdempotentTokenHandler idempotentTokenHandler) {
        return new IdempotentTokenController(idempotentTokenHandler);
    }

    /**
     * SpEL 方式幂等实现，基于 RestAPI 场景
     */
    @Bean
    @ConditionalOnMissingBean
    public IdempotentSpELHandler idempotentSpELByRestAPIExecuteHandler(RedissonClient redissonClient) {
        return new IdempotentSpELByRestAPIExecuteHandler(redissonClient);
    }

    /**
     * SpEL 方式幂等实现，基于 MQ 场景
     */
    @Bean
    @ConditionalOnMissingBean
    public IdempotentSpELByMQExecuteHandler idempotentSpELByMQExecuteHandler(DistributedCache distributedCache) {
        return new IdempotentSpELByMQExecuteHandler(distributedCache);
    }
}
