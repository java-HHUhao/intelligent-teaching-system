package cn.edu.hhu.spring.boot.starter.idempotent.handler.factory;

import cn.edu.hhu.spring.boot.starter.context.contextholder.ApplicationContextHolder;
import cn.edu.hhu.spring.boot.starter.idempotent.common.enums.IdempotentMethodEnum;
import cn.edu.hhu.spring.boot.starter.idempotent.common.enums.IdempotentSceneEnum;
import cn.edu.hhu.spring.boot.starter.idempotent.core.param.IdempotentParamHandler;
import cn.edu.hhu.spring.boot.starter.idempotent.core.spel.IdempotentSpELByMQExecuteHandler;
import cn.edu.hhu.spring.boot.starter.idempotent.core.spel.IdempotentSpELByRestAPIExecuteHandler;
import cn.edu.hhu.spring.boot.starter.idempotent.core.token.IdempotentTokenHandler;
import cn.edu.hhu.spring.boot.starter.idempotent.handler.IdempotentExecuteHandler;

public final class IdempotentExecuteHandlerFactory {
    /**
     * 获取幂等执行处理器
     *
     * @param scene 指定幂等验证场景类型
     * @param type  指定幂等处理类型
     * @return 幂等执行处理器
     */
    public static IdempotentExecuteHandler getInstance(IdempotentSceneEnum scene, IdempotentMethodEnum type) {
        IdempotentExecuteHandler result = null;
        switch (scene) {
            case RESTAPI -> {
                switch (type) {
                    case PARAM -> result = ApplicationContextHolder.getBean(IdempotentParamHandler.class);
                    case TOKEN -> result = ApplicationContextHolder.getBean(IdempotentTokenHandler.class);
                    case SPEL -> result = ApplicationContextHolder.getBean(IdempotentSpELByRestAPIExecuteHandler.class);
                    default -> {
                    }
                }
            }
            case MQ -> result = ApplicationContextHolder.getBean(IdempotentSpELByMQExecuteHandler.class);
            default -> {
            }
        }
        return result;
    }
}
