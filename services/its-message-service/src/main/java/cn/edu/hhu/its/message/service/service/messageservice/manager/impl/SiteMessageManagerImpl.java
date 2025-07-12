package cn.edu.hhu.its.message.service.service.messageservice.manager.impl;

import cn.edu.hhu.its.message.service.common.enums.MessageErrorCode;
import cn.edu.hhu.its.message.service.model.domain.SiteMessageDO;
import cn.edu.hhu.its.message.service.model.mapper.SiteMessageMapper;
import cn.edu.hhu.its.message.service.service.messageservice.manager.SiteMessageManager;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import cn.edu.hhu.spring.boot.starter.common.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * 站内消息基础数据管理器实现类
 * 
 * @author ITS项目组
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SiteMessageManagerImpl implements SiteMessageManager {

     private final SiteMessageMapper siteMessageMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> saveMessage(SiteMessageDO message) {
        try {
            // 参数验证
            if (message == null) {
                return ResultUtil.fail(MessageErrorCode.PARAM_VALIDATION_ERROR);
            }
            
            // 设置创建时间
            Date now = new Date();
            message.setCreatedAt(now);
            message.setUpdatedAt(now);
            message.setIsRead(false);
            message.setIsDeleted(false);
            
            // 保存到数据库
            siteMessageMapper.insert(message);
            
            log.info("保存站内消息成功，消息ID: {}, 接收者: {}", message.getId(), message.getReceiverId());
            
            return ResultUtil.success(message.getId());
        } catch (Exception e) {
            log.error("保存站内消息失败", e);
            return ResultUtil.fail(MessageErrorCode.MESSAGE_SEND_ERROR);
        }
    }
    
    @Override
    public Result<SiteMessageDO> getMessageById(Long messageId) {
        try {
            if (messageId == null) {
                return ResultUtil.fail(MessageErrorCode.PARAM_VALIDATION_ERROR);
            }
            
            // 从数据库查询
            // SiteMessageDO message = siteMessageMapper.selectById(messageId);
            // 临时返回null
            SiteMessageDO message = null;
            
            if (message == null) {
                return ResultUtil.fail(MessageErrorCode.MESSAGE_NOT_FOUND);
            }
            
            return ResultUtil.success(message);
        } catch (Exception e) {
            log.error("查询站内消息失败，消息ID: {}", messageId, e);
            return ResultUtil.fail(MessageErrorCode.SYSTEM_BUSY);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> markAsRead(Long messageId, Long userId) {
        try {
            if (messageId == null || userId == null) {
                return ResultUtil.fail(MessageErrorCode.PARAM_VALIDATION_ERROR);
            }
            
            // 查询消息
            Result<SiteMessageDO> messageResult = getMessageById(messageId);
            if (!messageResult.getCode().equals("0")) {
                return ResultUtil.fail(messageResult.getMessage(), messageResult.getCode());
            }
            
            SiteMessageDO message = messageResult.getData();
            if (!Objects.equals(message.getReceiverId(), userId)) {
                return ResultUtil.fail(MessageErrorCode.PERMISSION_DENIED);
            }
            
            if (message.getIsRead()) {
                return ResultUtil.fail(MessageErrorCode.MESSAGE_ALREADY_READ);
            }
            
            // 更新为已读
            message.setIsRead(true);
            message.setReadAt(new Date());
            message.setUpdatedAt(new Date());
            
            // 保存到数据库
            // siteMessageMapper.updateById(message);
            
            log.info("标记消息已读成功，消息ID: {}, 用户ID: {}", messageId, userId);
            
            return ResultUtil.success();
        } catch (Exception e) {
            log.error("标记消息已读失败，消息ID: {}, 用户ID: {}", messageId, userId, e);
            return ResultUtil.fail(MessageErrorCode.SYSTEM_BUSY);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> deleteMessage(Long messageId, Long userId) {
        try {
            if (messageId == null || userId == null) {
                return ResultUtil.fail(MessageErrorCode.PARAM_VALIDATION_ERROR);
            }
            
            // 查询消息
            Result<SiteMessageDO> messageResult = getMessageById(messageId);
            if (!messageResult.getCode().equals("0")) {
                return ResultUtil.fail(messageResult.getMessage(), messageResult.getCode());
            }
            
            SiteMessageDO message = messageResult.getData();
            if (!Objects.equals(message.getReceiverId(), userId)) {
                return ResultUtil.fail(MessageErrorCode.PERMISSION_DENIED);
            }
            
            // 逻辑删除
            message.setIsDeleted(true);
            message.setUpdatedAt(new Date());
            
            // 保存到数据库
            // siteMessageMapper.updateById(message);
            
            log.info("删除消息成功，消息ID: {}, 用户ID: {}", messageId, userId);
            
            return ResultUtil.success();
        } catch (Exception e) {
            log.error("删除消息失败，消息ID: {}, 用户ID: {}", messageId, userId, e);
            return ResultUtil.fail(MessageErrorCode.SYSTEM_BUSY);
        }
    }
    
    @Override
    public Result<Long> getUnreadCount(Long userId) {
        try {
            if (userId == null) {
                return ResultUtil.fail(MessageErrorCode.PARAM_VALIDATION_ERROR);
            }
            
            // 查询未读消息数量
            // LambdaQueryWrapper<SiteMessageDO> queryWrapper = new LambdaQueryWrapper<SiteMessageDO>()
            //         .eq(SiteMessageDO::getReceiverId, userId)
            //         .eq(SiteMessageDO::getIsRead, false)
            //         .eq(SiteMessageDO::getIsDeleted, false);
            // Long count = siteMessageMapper.selectCount(queryWrapper);
            
            // 临时返回0
            Long count = 0L;
            
            return ResultUtil.success(count);
        } catch (Exception e) {
            log.error("获取未读消息数量失败，用户ID: {}", userId, e);
            return ResultUtil.fail(MessageErrorCode.SYSTEM_BUSY);
        }
    }
} 