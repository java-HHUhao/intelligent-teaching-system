package cn.edu.hhu.its.message.service.service;

import cn.edu.hhu.its.message.service.model.dto.request.ProcessAuditRequestDTO;
import cn.edu.hhu.its.message.service.model.dto.request.SubmitAuditRequestDTO;
import cn.edu.hhu.its.message.service.model.dto.response.AuditRecordResponseDTO;
import cn.edu.hhu.spring.boot.starter.common.page.PageResult;
import cn.edu.hhu.spring.boot.starter.common.result.Result;

import java.util.List;

/**
 * 审核服务接口
 * 
 * @author ITS项目组
 */
public interface AuditService {

    /**
     * 提交审核
     *
     * @param requestDTO 提交审核请求
     * @return 审核记录ID
     */
    Result<Long> submitAudit(SubmitAuditRequestDTO requestDTO);

    /**
     * 处理审核
     *
     * @param requestDTO 审核处理请求
     * @return 处理结果
     */
    Result<Void> processAudit(ProcessAuditRequestDTO requestDTO);

    /**
     * 获取审核记录详情
     *
     * @param auditId 审核记录ID
     * @return 审核记录详情
     */
    Result<AuditRecordResponseDTO> getAuditRecord(Long auditId);

    /**
     * 获取待审核列表
     *
     * @param auditType 审核类型（可选）
     * @param auditorId 审核员ID（可选）
     * @param current 当前页码
     * @param size 每页大小
     * @return 待审核列表
     */
    Result<PageResult<AuditRecordResponseDTO>> getPendingAudits(String auditType, Long auditorId, 
                                                               Integer current, Integer size);

    /**
     * 获取用户提交的审核记录
     *
     * @param submitterId 提交者ID
     * @param auditType 审核类型（可选）
     * @param status 审核状态（可选）
     * @param current 当前页码
     * @param size 每页大小
     * @return 审核记录列表
     */
    Result<PageResult<AuditRecordResponseDTO>> getUserAudits(Long submitterId, String auditType, 
                                                            Integer status, Integer current, Integer size);

    /**
     * 获取审核员处理的审核记录
     *
     * @param auditorId 审核员ID
     * @param auditType 审核类型（可选）
     * @param status 审核状态（可选）
     * @param current 当前页码
     * @param size 每页大小
     * @return 审核记录列表
     */
    Result<PageResult<AuditRecordResponseDTO>> getAuditorRecords(Long auditorId, String auditType, 
                                                                Integer status, Integer current, Integer size);

    /**
     * 批量处理审核
     *
     * @param auditIds 审核记录ID列表
     * @param auditorId 审核员ID
     * @param status 审核状态
     * @param auditReason 审核意见
     * @return 处理结果
     */
    Result<Void> batchProcessAudit(List<Long> auditIds, Long auditorId, Integer status, String auditReason);

    /**
     * 撤回审核申请
     *
     * @param auditId 审核记录ID
     * @param submitterId 提交者ID
     * @return 撤回结果
     */
    Result<Void> withdrawAudit(Long auditId, Long submitterId);

    /**
     * 获取审核统计信息
     *
     * @param auditType 审核类型（可选）
     * @param auditorId 审核员ID（可选）
     * @return 统计信息
     */
    Result<java.util.Map<String, Object>> getAuditStatistics(String auditType, Long auditorId);

    /**
     * 自动审核处理
     *
     * @param auditType 审核类型
     * @return 自动审核处理的记录数
     */
    Result<Integer> autoProcessAudit(String auditType);

    /**
     * 处理超时审核
     *
     * @return 处理的超时审核数量
     */
    Result<Integer> processTimeoutAudits();

    /**
     * 检查用户是否有审核权限
     *
     * @param userId 用户ID
     * @param auditType 审核类型
     * @return 是否有权限
     */
    Result<Boolean> checkAuditPermission(Long userId, String auditType);
} 