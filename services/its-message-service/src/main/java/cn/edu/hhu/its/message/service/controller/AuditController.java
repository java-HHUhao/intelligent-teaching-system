package cn.edu.hhu.its.message.service.controller;

import cn.edu.hhu.its.message.service.model.dto.request.ProcessAuditRequestDTO;
import cn.edu.hhu.its.message.service.model.dto.request.SubmitAuditRequestDTO;
import cn.edu.hhu.its.message.service.model.dto.response.AuditRecordResponseDTO;
import cn.edu.hhu.its.message.service.service.AuditService;
import cn.edu.hhu.spring.boot.starter.common.page.PageResult;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 审核Controller
 * 
 * @author ITS项目组
 */
@RestController
@RequestMapping("/message/audit")
@RequiredArgsConstructor
@Tag(name = "审核管理", description = "审核提交、处理、查询相关接口")
public class AuditController {

    private final AuditService auditService;

    @PostMapping("/submit")
    @Operation(summary = "提交审核", description = "提交审核申请")
    public Result<Long> submitAudit(@RequestBody @Valid SubmitAuditRequestDTO requestDTO) {
        return auditService.submitAudit(requestDTO);
    }

    @PostMapping("/process")
    @Operation(summary = "处理审核", description = "审核员处理审核申请")
    public Result<Void> processAudit(@RequestBody @Valid ProcessAuditRequestDTO requestDTO) {
        return auditService.processAudit(requestDTO);
    }

    @GetMapping("/{auditId}")
    @Operation(summary = "获取审核记录详情", description = "获取指定审核记录的详细信息")
    public Result<AuditRecordResponseDTO> getAuditRecord(
            @Parameter(description = "审核记录ID", example = "12345") @PathVariable Long auditId) {
        return auditService.getAuditRecord(auditId);
    }

    @GetMapping("/pending")
    @Operation(summary = "获取待审核列表", description = "分页获取待审核的记录列表")
    public Result<PageResult<AuditRecordResponseDTO>> getPendingAudits(
            @Parameter(description = "审核类型（可选）", example = "RESOURCE") @RequestParam(required = false) String auditType,
            @Parameter(description = "审核员ID（可选）", example = "12345") @RequestParam(required = false) Long auditorId,
            @Parameter(description = "当前页码", example = "1") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小", example = "20") @RequestParam(defaultValue = "20") Integer size) {
        return auditService.getPendingAudits(auditType, auditorId, current, size);
    }

    @GetMapping("/user/{submitterId}")
    @Operation(summary = "获取用户提交的审核记录", description = "分页获取指定用户提交的审核记录")
    public Result<PageResult<AuditRecordResponseDTO>> getUserAudits(
            @Parameter(description = "提交者ID", example = "12345") @PathVariable Long submitterId,
            @Parameter(description = "审核类型（可选）", example = "RESOURCE") @RequestParam(required = false) String auditType,
            @Parameter(description = "审核状态（可选）", example = "1") @RequestParam(required = false) Integer status,
            @Parameter(description = "当前页码", example = "1") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小", example = "20") @RequestParam(defaultValue = "20") Integer size) {
        return auditService.getUserAudits(submitterId, auditType, status, current, size);
    }

    @GetMapping("/auditor/{auditorId}")
    @Operation(summary = "获取审核员处理的审核记录", description = "分页获取指定审核员处理的审核记录")
    public Result<PageResult<AuditRecordResponseDTO>> getAuditorRecords(
            @Parameter(description = "审核员ID", example = "12345") @PathVariable Long auditorId,
            @Parameter(description = "审核类型（可选）", example = "RESOURCE") @RequestParam(required = false) String auditType,
            @Parameter(description = "审核状态（可选）", example = "1") @RequestParam(required = false) Integer status,
            @Parameter(description = "当前页码", example = "1") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小", example = "20") @RequestParam(defaultValue = "20") Integer size) {
        return auditService.getAuditorRecords(auditorId, auditType, status, current, size);
    }

    @PostMapping("/batch-process")
    @Operation(summary = "批量处理审核", description = "批量处理多个审核申请")
    public Result<Void> batchProcessAudit(
            @Parameter(description = "审核记录ID列表") @RequestParam List<Long> auditIds,
            @Parameter(description = "审核员ID", example = "12345") @RequestParam Long auditorId,
            @Parameter(description = "审核状态", example = "1") @RequestParam Integer status,
            @Parameter(description = "审核意见") @RequestParam(required = false) String auditReason) {
        return auditService.batchProcessAudit(auditIds, auditorId, status, auditReason);
    }

    @PostMapping("/{auditId}/withdraw")
    @Operation(summary = "撤回审核申请", description = "撤回待审核的申请")
    public Result<Void> withdrawAudit(
            @Parameter(description = "审核记录ID", example = "12345") @PathVariable Long auditId,
            @Parameter(description = "提交者ID", example = "67890") @RequestParam Long submitterId) {
        return auditService.withdrawAudit(auditId, submitterId);
    }

    @GetMapping("/statistics")
    @Operation(summary = "获取审核统计信息", description = "获取审核相关的统计数据")
    public Result<Map<String, Object>> getAuditStatistics(
            @Parameter(description = "审核类型（可选）", example = "RESOURCE") @RequestParam(required = false) String auditType,
            @Parameter(description = "审核员ID（可选）", example = "12345") @RequestParam(required = false) Long auditorId) {
        return auditService.getAuditStatistics(auditType, auditorId);
    }

    @PostMapping("/auto-process")
    @Operation(summary = "自动审核处理", description = "执行自动审核逻辑")
    public Result<Integer> autoProcessAudit(
            @Parameter(description = "审核类型", example = "USER_AVATAR") @RequestParam String auditType) {
        return auditService.autoProcessAudit(auditType);
    }

    @PostMapping("/process-timeout")
    @Operation(summary = "处理超时审核", description = "处理超时的审核申请")
    public Result<Integer> processTimeoutAudits() {
        return auditService.processTimeoutAudits();
    }

    @GetMapping("/check-permission")
    @Operation(summary = "检查审核权限", description = "检查用户是否有特定类型的审核权限")
    public Result<Boolean> checkAuditPermission(
            @Parameter(description = "用户ID", example = "12345") @RequestParam Long userId,
            @Parameter(description = "审核类型", example = "RESOURCE") @RequestParam String auditType) {
        return auditService.checkAuditPermission(userId, auditType);
    }
} 