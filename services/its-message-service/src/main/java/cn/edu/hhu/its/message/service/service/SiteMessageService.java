package cn.edu.hhu.its.message.service.service;

import cn.edu.hhu.its.message.service.model.dto.request.MessageListRequestDTO;
import cn.edu.hhu.its.message.service.model.dto.request.SendMessageRequestDTO;
import cn.edu.hhu.its.message.service.model.dto.response.MessageDetailResponseDTO;
import cn.edu.hhu.spring.boot.starter.common.page.PageResult;
import cn.edu.hhu.spring.boot.starter.common.result.Result;

import java.util.List;

/**
 * 站内消息服务接口
 * 
 * @author ITS项目组
 */
public interface SiteMessageService {

    /**
     * 发送站内消息
     *
     * @param requestDTO 发送消息请求
     * @param senderId 发送者ID
     * @return 发送结果
     */
    Result<Void> sendMessage(SendMessageRequestDTO requestDTO, Long senderId);

    /**
     * 批量发送站内消息
     *
     * @param requestDTO 发送消息请求
     * @param senderId 发送者ID
     * @param receiverIds 接收者ID列表
     * @return 发送结果
     */
    Result<Void> batchSendMessage(SendMessageRequestDTO requestDTO, Long senderId, List<Long> receiverIds);

    /**
     * 根据消息模板发送消息
     *
     * @param templateCode 模板代码
     * @param receiverId 接收者ID
     * @param templateParams 模板参数
     * @param senderId 发送者ID（可为空，系统消息）
     * @return 发送结果
     */
    Result<Void> sendMessageByTemplate(String templateCode, Long receiverId, 
                                      java.util.Map<String, Object> templateParams, Long senderId);

    /**
     * 获取用户消息列表
     *
     * @param requestDTO 消息列表请求
     * @param userId 用户ID
     * @return 消息列表
     */
    Result<PageResult<MessageDetailResponseDTO>> getMessageList(MessageListRequestDTO requestDTO, Long userId);

    /**
     * 获取消息详情
     *
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 消息详情
     */
    Result<MessageDetailResponseDTO> getMessageDetail(Long messageId, Long userId);

    /**
     * 标记消息为已读
     *
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<Void> markAsRead(Long messageId, Long userId);

    /**
     * 批量标记消息为已读
     *
     * @param messageIds 消息ID列表
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<Void> batchMarkAsRead(List<Long> messageIds, Long userId);

    /**
     * 标记所有消息为已读
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<Void> markAllAsRead(Long userId);

    /**
     * 删除消息
     *
     * @param messageId 消息ID
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<Void> deleteMessage(Long messageId, Long userId);

    /**
     * 获取未读消息数量
     *
     * @param userId 用户ID
     * @return 未读消息数量
     */
    Result<Long> getUnreadCount(Long userId);

    /**
     * 清理过期消息
     *
     * @return 清理的消息数量
     */
    Result<Integer> cleanExpiredMessages();
} 