package cn.edu.hhu.spring.boot.starter.designpattern.strategy;

public interface AbstractStrategyExecutor<REQUEST, RESPONSE> {
    /**
     * 策略标识
     *
     */
    String mark();

    /**
     *执行策略范匹配标识
     */
    default String patternMatchMark(){return null;};

    /**
     * 无返回值
     *
     */
    default void execute(REQUEST request){};

    /**
     * 带返回值
     *
     */
    default RESPONSE executeResp(REQUEST request){return null;};
}
