package cn.edu.hhu.spring.boot.starter.distributedid.core.serviceid;

import cn.edu.hhu.spring.boot.starter.distributedid.core.IdGenerator;
import cn.edu.hhu.spring.boot.starter.distributedid.core.serviceid.snowflake.SnowflakeIdInfo;

/**
 * 业务ID生成器 第二级接口
 */
public interface ServiceIdGenerator extends IdGenerator {
    /**
     * 根据 {@param serviceId} 生成业务雪花算法 ID
     */
    default long nextId(long serviceId) {
        return 0L;
    }

    /**
     * 根据 {@param serviceId} 生成业务雪花算法 ID
     */
    default long nextId(String serviceId) {
        return 0L;
    }

    /**
     * 根据 {@param serviceId} 生成字符串类型雪花算法 ID
     */
    default String nextIdStr(long serviceId) {
        return null;
    }

    /**
     * 根据 {@param serviceId} 生成字符串类型雪花算法 ID
     */
    default String nextIdStr(String serviceId) {
        return null;
    }

    /**
     * 解析雪花算法
     */
    SnowflakeIdInfo parseSnowflakeId(long snowflakeId);
}
