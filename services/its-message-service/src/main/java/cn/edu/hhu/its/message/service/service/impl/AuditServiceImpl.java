package cn.edu.hhu.its.message.service.service.impl;

import cn.edu.hhu.its.message.service.common.enums.ChannelTypeEnum;
import cn.edu.hhu.its.message.service.common.enums.MessageErrorCode;
import cn.edu.hhu.its.message.service.model.domain.AuditConfigDO;
import cn.edu.hhu.its.message.service.model.domain.AuditRecordDO;
import cn.edu.hhu.its.message.service.model.dto.request.MultiChannelMessageRequestDTO;
import cn.edu.hhu.its.message.service.model.dto.request.ProcessAuditRequestDTO;
import cn.edu.hhu.its.message.service.model.dto.request.SubmitAuditRequestDTO;
import cn.edu.hhu.its.message.service.model.dto.response.AuditRecordResponseDTO;
import cn.edu.hhu.its.message.service.model.mapper.AuditConfigMapper;
import cn.edu.hhu.its.message.service.model.mapper.AuditRecordMapper;
import cn.edu.hhu.its.message.service.service.AuditService;
import cn.edu.hhu.its.message.service.service.messageservice.MessageService;
import cn.edu.hhu.spring.boot.starter.common.page.PageResult;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import cn.edu.hhu.spring.boot.starter.common.utils.ResultUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 审核服务实现类
 *
 * @author ITS项目组
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRecordMapper auditRecordMapper;
    private final AuditConfigMapper auditConfigMapper;
    private final MessageService messageService;

    // 默认系统发送者ID
    private static final Long SYSTEM_SENDER_ID = 0L;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> submitAudit(SubmitAuditRequestDTO requestDTO) {
        try {
            // 查询审核配置
            AuditConfigDO config = getAuditConfig(requestDTO.getAuditType());
            if (config == null || !config.getIsActive()) {
                return ResultUtil.fail(MessageErrorCode.AUDIT_CONFIG_NOT_FOUND);
            }

            // 检查是否存在待审核的记录
            LambdaQueryWrapper<AuditRecordDO> existWrapper = new LambdaQueryWrapper<>();
            existWrapper.eq(AuditRecordDO::getAuditType, requestDTO.getAuditType())
                    .eq(AuditRecordDO::getTargetId, requestDTO.getTargetId())
                    .eq(AuditRecordDO::getTargetType, requestDTO.getTargetType())
                    .eq(AuditRecordDO::getStatus, 0); // 待审核状态

            Long existingCount = auditRecordMapper.selectCount(existWrapper);
            if (existingCount > 0) {
                return ResultUtil.fail("已存在待审核的记录，请勿重复提交", "AUDIT_ALREADY_PENDING");
            }

            // 构建审核记录
            AuditRecordDO auditRecord = buildAuditRecord(requestDTO, config);

            // 保存审核记录
            auditRecordMapper.insert(auditRecord);

            // 如果需要自动审核，立即处理
            if (config.getAutoAudit()) {
                autoProcessSingleAudit(auditRecord, config);
            } else {
                // 发送通知给审核员
                sendAuditNotificationToAuditor(auditRecord, config);
            }

            log.info("提交审核成功，审核记录ID: {}, 类型: {}, 提交者: {}", 
                    auditRecord.getId(), requestDTO.getAuditType(), requestDTO.getSubmitterId());

            return ResultUtil.success(auditRecord.getId());

        } catch (Exception e) {
            log.error("提交审核失败", e);
            return ResultUtil.fail(MessageErrorCode.AUDIT_SUBMIT_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> processAudit(ProcessAuditRequestDTO requestDTO) {
        try {
            // 查询审核记录
            AuditRecordDO auditRecord = auditRecordMapper.selectById(requestDTO.getAuditId());
            if (auditRecord == null) {
                return ResultUtil.fail(MessageErrorCode.AUDIT_RECORD_NOT_FOUND);
            }

            if (auditRecord.getStatus() != 0) {
                return ResultUtil.fail(MessageErrorCode.AUDIT_ALREADY_PROCESSED);
            }

            // 验证审核权限
            Result<Boolean> permissionResult = checkAuditPermission(requestDTO.getAuditorId(), auditRecord.getAuditType());
            if (!permissionResult.getData()) {
                return ResultUtil.fail(MessageErrorCode.AUDIT_PERMISSION_DENIED);
            }

            // 更新审核记录
            auditRecord.setAuditorId(requestDTO.getAuditorId());
            auditRecord.setStatus(requestDTO.getStatus());
            auditRecord.setAuditReason(requestDTO.getAuditReason());
            auditRecord.setAuditedAt(new Date());
            auditRecord.setUpdatedAt(new Date());

            auditRecordMapper.updateById(auditRecord);

            // 发送审核结果通知给提交者
            sendAuditResultNotification(auditRecord);

            log.info("处理审核成功，审核记录ID: {}, 审核员: {}, 结果: {}", 
                    requestDTO.getAuditId(), requestDTO.getAuditorId(), 
                    requestDTO.getStatus() == 1 ? "通过" : "拒绝");

            return ResultUtil.success();

        } catch (Exception e) {
            log.error("处理审核失败", e);
            return ResultUtil.fail(MessageErrorCode.SYSTEM_BUSY);
        }
    }

    @Override
    public Result<AuditRecordResponseDTO> getAuditRecord(Long auditId) {
        try {
            AuditRecordDO auditRecord = auditRecordMapper.selectById(auditId);
            if (auditRecord == null) {
                return ResultUtil.fail(MessageErrorCode.AUDIT_RECORD_NOT_FOUND);
            }

            AuditRecordResponseDTO response = convertToResponse(auditRecord);
            return ResultUtil.success(response);

        } catch (Exception e) {
            log.error("获取审核记录失败，ID: {}", auditId, e);
            return ResultUtil.fail(MessageErrorCode.SYSTEM_BUSY);
        }
    }

    @Override
    public Result<PageResult<AuditRecordResponseDTO>> getPendingAudits(String auditType, Long auditorId, 
                                                                       Integer current, Integer size) {
        try {
            LambdaQueryWrapper<AuditRecordDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AuditRecordDO::getStatus, 0); // 待审核状态

            if (StringUtils.hasText(auditType)) {
                queryWrapper.eq(AuditRecordDO::getAuditType, auditType);
            }

            if (auditorId != null) {
                // 如果指定了审核员ID，可以根据权限过滤
                // 这里简化处理，直接查询所有待审核记录
            }

            queryWrapper.orderByAsc(AuditRecordDO::getSubmittedAt);

            Page<AuditRecordDO> page = new Page<>(current, size);
            IPage<AuditRecordDO> result = auditRecordMapper.selectPage(page, queryWrapper);

            List<AuditRecordResponseDTO> responseList = result.getRecords().stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());

            PageResult<AuditRecordResponseDTO> pageResult = PageResult.<AuditRecordResponseDTO>builder()
                    .list(responseList)
                    .total(result.getTotal())
                    .pageNum((int) result.getCurrent())
                    .pageSize((int) result.getSize())
                    .build();

            return ResultUtil.success(pageResult);

        } catch (Exception e) {
            log.error("获取待审核列表失败", e);
            return ResultUtil.fail(MessageErrorCode.SYSTEM_BUSY);
        }
    }

    @Override
    public Result<PageResult<AuditRecordResponseDTO>> getUserAudits(Long submitterId, String auditType, 
                                                                   Integer status, Integer current, Integer size) {
        try {
            LambdaQueryWrapper<AuditRecordDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AuditRecordDO::getSubmitterId, submitterId);

            if (StringUtils.hasText(auditType)) {
                queryWrapper.eq(AuditRecordDO::getAuditType, auditType);
            }

            if (status != null) {
                queryWrapper.eq(AuditRecordDO::getStatus, status);
            }

            queryWrapper.orderByDesc(AuditRecordDO::getSubmittedAt);

            Page<AuditRecordDO> page = new Page<>(current, size);
            IPage<AuditRecordDO> result = auditRecordMapper.selectPage(page, queryWrapper);

            List<AuditRecordResponseDTO> responseList = result.getRecords().stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());

            PageResult<AuditRecordResponseDTO> pageResult = PageResult.<AuditRecordResponseDTO>builder()
                    .list(responseList)
                    .total(result.getTotal())
                    .pageNum((int) result.getCurrent())
                    .pageSize((int) result.getSize())
                    .build();

            return ResultUtil.success(pageResult);

        } catch (Exception e) {
            log.error("获取用户审核记录失败，用户ID: {}", submitterId, e);
            return ResultUtil.fail(MessageErrorCode.SYSTEM_BUSY);
        }
    }

    @Override
    public Result<PageResult<AuditRecordResponseDTO>> getAuditorRecords(Long auditorId, String auditType, 
                                                                       Integer status, Integer current, Integer size) {
        try {
            LambdaQueryWrapper<AuditRecordDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AuditRecordDO::getAuditorId, auditorId);

            if (StringUtils.hasText(auditType)) {
                queryWrapper.eq(AuditRecordDO::getAuditType, auditType);
            }

            if (status != null) {
                queryWrapper.eq(AuditRecordDO::getStatus, status);
            }

            queryWrapper.orderByDesc(AuditRecordDO::getAuditedAt);

            Page<AuditRecordDO> page = new Page<>(current, size);
            IPage<AuditRecordDO> result = auditRecordMapper.selectPage(page, queryWrapper);

            List<AuditRecordResponseDTO> responseList = result.getRecords().stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());

            PageResult<AuditRecordResponseDTO> pageResult = PageResult.<AuditRecordResponseDTO>builder()
                    .list(responseList)
                    .total(result.getTotal())
                    .pageNum((int) result.getCurrent())
                    .pageSize((int) result.getSize())
                    .build();

            return ResultUtil.success(pageResult);

        } catch (Exception e) {
            log.error("获取审核员记录失败，审核员ID: {}", auditorId, e);
            return ResultUtil.fail(MessageErrorCode.SYSTEM_BUSY);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> batchProcessAudit(List<Long> auditIds, Long auditorId, Integer status, String auditReason) {
        try {
            if (CollectionUtils.isEmpty(auditIds)) {
                return ResultUtil.fail(MessageErrorCode.PARAM_VALIDATION_ERROR);
            }

            if (auditIds.size() > 50) {
                return ResultUtil.fail("批量处理数量不能超过50条", "BATCH_SIZE_EXCEEDED");
            }

            List<AuditRecordDO> auditRecords = auditRecordMapper.selectBatchIds(auditIds);
            
            for (AuditRecordDO record : auditRecords) {
                if (record.getStatus() != 0) {
                    log.warn("审核记录{}已处理，跳过", record.getId());
                    continue;
                }

                // 验证审核权限
                Result<Boolean> permissionResult = checkAuditPermission(auditorId, record.getAuditType());
                if (!permissionResult.getData()) {
                    log.warn("审核员{}对类型{}无权限，跳过记录{}", auditorId, record.getAuditType(), record.getId());
                    continue;
                }

                // 更新审核记录
                record.setAuditorId(auditorId);
                record.setStatus(status);
                record.setAuditReason(auditReason);
                record.setAuditedAt(new Date());
                record.setUpdatedAt(new Date());

                auditRecordMapper.updateById(record);

                // 发送通知
                sendAuditResultNotification(record);
            }

            log.info("批量处理审核完成，处理数量: {}, 审核员: {}", auditIds.size(), auditorId);

            return ResultUtil.success();

        } catch (Exception e) {
            log.error("批量处理审核失败", e);
            return ResultUtil.fail(MessageErrorCode.SYSTEM_BUSY);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> withdrawAudit(Long auditId, Long submitterId) {
        try {
            AuditRecordDO auditRecord = auditRecordMapper.selectById(auditId);
            if (auditRecord == null) {
                return ResultUtil.fail(MessageErrorCode.AUDIT_RECORD_NOT_FOUND);
            }

            if (!Objects.equals(auditRecord.getSubmitterId(), submitterId)) {
                return ResultUtil.fail(MessageErrorCode.PERMISSION_DENIED);
            }

            if (auditRecord.getStatus() != 0) {
                return ResultUtil.fail("只能撤回待审核状态的申请", "CANNOT_WITHDRAW_PROCESSED");
            }

            // 删除审核记录（逻辑删除或物理删除）
            auditRecordMapper.deleteById(auditId);

            log.info("撤回审核申请成功，审核记录ID: {}, 提交者: {}", auditId, submitterId);

            return ResultUtil.success();

        } catch (Exception e) {
            log.error("撤回审核申请失败", e);
            return ResultUtil.fail(MessageErrorCode.SYSTEM_BUSY);
        }
    }

    @Override
    public Result<Map<String, Object>> getAuditStatistics(String auditType, Long auditorId) {
        try {
            Map<String, Object> statistics = new HashMap<>();

            LambdaQueryWrapper<AuditRecordDO> baseWrapper = new LambdaQueryWrapper<>();
            
            if (StringUtils.hasText(auditType)) {
                baseWrapper.eq(AuditRecordDO::getAuditType, auditType);
            }
            
            if (auditorId != null) {
                baseWrapper.eq(AuditRecordDO::getAuditorId, auditorId);
            }

            // 待审核数量
            LambdaQueryWrapper<AuditRecordDO> pendingWrapper = baseWrapper.clone();
            pendingWrapper.eq(AuditRecordDO::getStatus, 0);
            Long pendingCount = auditRecordMapper.selectCount(pendingWrapper);

            // 已通过数量
            LambdaQueryWrapper<AuditRecordDO> approvedWrapper = baseWrapper.clone();
            approvedWrapper.eq(AuditRecordDO::getStatus, 1);
            Long approvedCount = auditRecordMapper.selectCount(approvedWrapper);

            // 已拒绝数量
            LambdaQueryWrapper<AuditRecordDO> rejectedWrapper = baseWrapper.clone();
            rejectedWrapper.eq(AuditRecordDO::getStatus, 2);
            Long rejectedCount = auditRecordMapper.selectCount(rejectedWrapper);

            statistics.put("pendingCount", pendingCount);
            statistics.put("approvedCount", approvedCount);
            statistics.put("rejectedCount", rejectedCount);
            statistics.put("totalCount", pendingCount + approvedCount + rejectedCount);

            return ResultUtil.success(statistics);

        } catch (Exception e) {
            log.error("获取审核统计信息失败", e);
            return ResultUtil.fail(MessageErrorCode.SYSTEM_BUSY);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> autoProcessAudit(String auditType) {
        try {
            AuditConfigDO config = getAuditConfig(auditType);
            if (config == null || !config.getAutoAudit()) {
                return ResultUtil.fail("该审核类型不支持自动审核", "AUTO_AUDIT_NOT_SUPPORTED");
            }

            LambdaQueryWrapper<AuditRecordDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AuditRecordDO::getAuditType, auditType)
                    .eq(AuditRecordDO::getStatus, 0);

            List<AuditRecordDO> pendingRecords = auditRecordMapper.selectList(queryWrapper);
            
            int processedCount = 0;
            for (AuditRecordDO record : pendingRecords) {
                if (autoProcessSingleAudit(record, config)) {
                    processedCount++;
                }
            }

            log.info("自动审核处理完成，类型: {}, 处理数量: {}", auditType, processedCount);

            return ResultUtil.success(processedCount);

        } catch (Exception e) {
            log.error("自动审核处理失败", e);
            return ResultUtil.fail(MessageErrorCode.SYSTEM_BUSY);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> processTimeoutAudits() {
        try {
            // 查询所有审核配置
            List<AuditConfigDO> configs = auditConfigMapper.selectList(
                    new LambdaQueryWrapper<AuditConfigDO>()
                            .eq(AuditConfigDO::getIsActive, true)
            );

            int processedCount = 0;
            
            for (AuditConfigDO config : configs) {
                if (config.getAuditTimeout() != null && config.getAuditTimeout() > 0) {
                    // 计算超时时间
                    Date timeoutDate = new Date(System.currentTimeMillis() - 
                            config.getAuditTimeout() * 60 * 60 * 1000L);

                    LambdaQueryWrapper<AuditRecordDO> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(AuditRecordDO::getAuditType, config.getAuditType())
                            .eq(AuditRecordDO::getStatus, 0)
                            .lt(AuditRecordDO::getSubmittedAt, timeoutDate);

                    List<AuditRecordDO> timeoutRecords = auditRecordMapper.selectList(queryWrapper);
                    
                    for (AuditRecordDO record : timeoutRecords) {
                        // 超时自动处理（默认拒绝）
                        record.setStatus(2);
                        record.setAuditReason("审核超时，系统自动拒绝");
                        record.setAuditorId(SYSTEM_SENDER_ID);
                        record.setAuditedAt(new Date());
                        record.setUpdatedAt(new Date());

                        auditRecordMapper.updateById(record);

                        // 发送超时通知
                        sendAuditTimeoutNotification(record);
                        processedCount++;
                    }
                }
            }

            log.info("处理超时审核完成，处理数量: {}", processedCount);

            return ResultUtil.success(processedCount);

        } catch (Exception e) {
            log.error("处理超时审核失败", e);
            return ResultUtil.fail(MessageErrorCode.SYSTEM_BUSY);
        }
    }

    @Override
    public Result<Boolean> checkAuditPermission(Long userId, String auditType) {
        try {
            // 这里实现权限检查逻辑
            // 可以根据用户角色、审核类型等判断权限
            // 简化实现：管理员和审核员有权限
            boolean hasPermission = isAuditor(userId, auditType);
            
            return ResultUtil.success(hasPermission);

        } catch (Exception e) {
            log.error("检查审核权限失败，用户ID: {}, 审核类型: {}", userId, auditType, e);
            return ResultUtil.fail(MessageErrorCode.SYSTEM_BUSY);
        }
    }

    // 私有方法
    private AuditConfigDO getAuditConfig(String auditType) {
        return auditConfigMapper.selectOne(
                new LambdaQueryWrapper<AuditConfigDO>()
                        .eq(AuditConfigDO::getAuditType, auditType)
                        .eq(AuditConfigDO::getIsActive, true)
        );
    }

    private AuditRecordDO buildAuditRecord(SubmitAuditRequestDTO requestDTO, AuditConfigDO config) {
        AuditRecordDO auditRecord = new AuditRecordDO();
        auditRecord.setAuditType(requestDTO.getAuditType());
        auditRecord.setTargetId(requestDTO.getTargetId());
        auditRecord.setTargetType(requestDTO.getTargetType());
        auditRecord.setSubmitterId(requestDTO.getSubmitterId());
        auditRecord.setStatus(0); // 待审核
        auditRecord.setAuditData(requestDTO.getAuditData() != null ? 
                requestDTO.getAuditData().toString() : null);
        auditRecord.setSubmittedAt(new Date());
        auditRecord.setCreatedAt(new Date());
        auditRecord.setUpdatedAt(new Date());
        return auditRecord;
    }

    private void sendAuditNotificationToAuditor(AuditRecordDO auditRecord, AuditConfigDO config) {
        try {
            // 获取审核员列表（这里简化处理）
            List<Long> auditorIds = getAuditorsForType(auditRecord.getAuditType());
            
            for (Long auditorId : auditorIds) {
                MultiChannelMessageRequestDTO messageRequest = new MultiChannelMessageRequestDTO();
                messageRequest.setSenderId(SYSTEM_SENDER_ID);
                messageRequest.setChannels(Arrays.asList(ChannelTypeEnum.IN_APP));
                
                MultiChannelMessageRequestDTO.ReceiverInfo receiver = new MultiChannelMessageRequestDTO.ReceiverInfo();
                receiver.setUserId(auditorId);
                messageRequest.setReceiver(receiver);
                
                messageRequest.setTitle("新的审核申请");
                messageRequest.setContent(String.format("您有一个新的%s审核申请待处理，申请编号：%d", 
                        getAuditTypeName(auditRecord.getAuditType()), auditRecord.getId()));
                messageRequest.setMessageType("AUDIT_NOTIFICATION");
                
                // 异步发送消息
                messageService.sendMultiChannelMessage(messageRequest);
            }
            
        } catch (Exception e) {
            log.error("发送审核通知失败", e);
        }
    }

    private void sendAuditResultNotification(AuditRecordDO auditRecord) {
        try {
            MultiChannelMessageRequestDTO messageRequest = new MultiChannelMessageRequestDTO();
            messageRequest.setSenderId(SYSTEM_SENDER_ID);
            messageRequest.setChannels(Arrays.asList(ChannelTypeEnum.IN_APP));
            
            MultiChannelMessageRequestDTO.ReceiverInfo receiver = new MultiChannelMessageRequestDTO.ReceiverInfo();
            receiver.setUserId(auditRecord.getSubmitterId());
            messageRequest.setReceiver(receiver);
            
            String resultText = auditRecord.getStatus() == 1 ? "通过" : "拒绝";
            messageRequest.setTitle("审核结果通知");
            messageRequest.setContent(String.format("您的%s审核申请已%s。%s", 
                    getAuditTypeName(auditRecord.getAuditType()), 
                    resultText,
                    StringUtils.hasText(auditRecord.getAuditReason()) ? "原因：" + auditRecord.getAuditReason() : ""));
            messageRequest.setMessageType("AUDIT_RESULT");
            
            // 发送消息
            messageService.sendMultiChannelMessage(messageRequest);
            
        } catch (Exception e) {
            log.error("发送审核结果通知失败", e);
        }
    }

    private void sendAuditTimeoutNotification(AuditRecordDO auditRecord) {
        try {
            MultiChannelMessageRequestDTO messageRequest = new MultiChannelMessageRequestDTO();
            messageRequest.setSenderId(SYSTEM_SENDER_ID);
            messageRequest.setChannels(Arrays.asList(ChannelTypeEnum.IN_APP));
            
            MultiChannelMessageRequestDTO.ReceiverInfo receiver = new MultiChannelMessageRequestDTO.ReceiverInfo();
            receiver.setUserId(auditRecord.getSubmitterId());
            messageRequest.setReceiver(receiver);
            
            messageRequest.setTitle("审核超时通知");
            messageRequest.setContent(String.format("您的%s审核申请因超时未处理，已被系统自动拒绝。如有疑问，请重新提交申请。", 
                    getAuditTypeName(auditRecord.getAuditType())));
            messageRequest.setMessageType("AUDIT_TIMEOUT");
            
            // 发送消息
            messageService.sendMultiChannelMessage(messageRequest);
            
        } catch (Exception e) {
            log.error("发送审核超时通知失败", e);
        }
    }

    private boolean autoProcessSingleAudit(AuditRecordDO record, AuditConfigDO config) {
        try {
            // 自动审核逻辑（简化实现）
            // 可以根据审核规则、目标类型等进行自动判断
            boolean autoApprove = shouldAutoApprove(record, config);
            
            record.setStatus(autoApprove ? 1 : 2);
            record.setAuditorId(SYSTEM_SENDER_ID);
            record.setAuditReason(autoApprove ? "系统自动审核通过" : "系统自动审核拒绝");
            record.setAuditedAt(new Date());
            record.setUpdatedAt(new Date());
            
            auditRecordMapper.updateById(record);
            
            // 发送结果通知
            sendAuditResultNotification(record);
            
            return true;
            
        } catch (Exception e) {
            log.error("自动审核处理失败，记录ID: {}", record.getId(), e);
            return false;
        }
    }

    private boolean shouldAutoApprove(AuditRecordDO record, AuditConfigDO config) {
        // 简化的自动审核逻辑
        // 实际应用中可以根据审核规则配置、机器学习模型等进行判断
        return "USER_AVATAR".equals(record.getAuditType()); // 用户头像自动通过
    }

    private List<Long> getAuditorsForType(String auditType) {
        // 简化实现：返回模拟的审核员ID列表
        // 实际应用中应该根据审核类型查询具有权限的审核员
        return Arrays.asList(1L, 2L); // 模拟审核员ID
    }

    private boolean isAuditor(Long userId, String auditType) {
        // 简化实现：检查用户是否为审核员
        // 实际应用中应该检查用户角色和权限
        List<Long> auditors = getAuditorsForType(auditType);
        return auditors.contains(userId);
    }

    private String getAuditTypeName(String auditType) {
        switch (auditType) {
            case "RESOURCE":
                return "资源";
            case "USER_AVATAR":
                return "用户头像";
            default:
                return auditType;
        }
    }

    private AuditRecordResponseDTO convertToResponse(AuditRecordDO record) {
        AuditRecordResponseDTO response = new AuditRecordResponseDTO();
        BeanUtils.copyProperties(record, response);
        return response;
    }
}
