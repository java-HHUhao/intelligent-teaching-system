package cn.edu.hhu.spring.boot.starter.distributedid.core;

/**
 * ID生成器的顶级接口
 */
public interface IdGenerator {
    /**
     * 下一个 ID
     */
    default long nextId() {
        return 0L;
    }

    /**
     * 下一个 ID 字符串
     */
    default String nextIdStr() {
        return "";
    }
}
