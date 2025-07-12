package cn.edu.hhu.its.message.service.service.messageservice.manager;

import cn.edu.hhu.its.message.service.model.domain.SiteMessageDO;
import cn.edu.hhu.spring.boot.starter.common.result.Result;

/**
 * 站内消息基础数据管理器
 * 负责站内消息的基础CRUD操作，不包含业务逻辑
 * 
 * @author ITS项目组
 */
public interface SiteMessageManager {
    
    /**
     * 保存站内消息
     * 
     * @param message 消息对象
     * @return 操作结果
     */
    Result<Long> saveMessage(SiteMessageDO message);
    
    /**
     * 根据ID查询消息
     * 
     * @param messageId 消息ID
     * @return 消息对象
     */
    Result<SiteMessageDO> getMessageById(Long messageId);
    
    /**
     * 更新消息状态为已读
     * 
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<Void> markAsRead(Long messageId, Long userId);
    
    /**
     * 逻辑删除消息
     * 
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<Void> deleteMessage(Long messageId, Long userId);
    
    /**
     * 获取用户未读消息数量
     * 
     * @param userId 用户ID
     * @return 未读消息数量
     */
    Result<Long> getUnreadCount(Long userId);
} 