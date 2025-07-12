package cn.edu.hhu.its.message.service.controller;

import cn.edu.hhu.its.message.service.model.dto.request.MessageListRequestDTO;
import cn.edu.hhu.its.message.service.model.dto.request.SendMessageRequestDTO;
import cn.edu.hhu.its.message.service.model.dto.response.MessageDetailResponseDTO;
import cn.edu.hhu.its.message.service.service.SiteMessageService;
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
 * 站内消息Controller
 * 
 * @author ITS项目组
 */
@RestController
@RequestMapping("/message/site")
@RequiredArgsConstructor
@Tag(name = "站内消息管理", description = "站内消息发送、接收、管理相关接口")
public class SiteMessageController {

    private final SiteMessageService siteMessageService;

    @PostMapping("/send")
    @Operation(summary = "发送站内消息", description = "发送单条站内消息")
    public Result<Void> sendMessage(
            @RequestBody @Valid SendMessageRequestDTO requestDTO,
            @Parameter(description = "发送者ID", example = "12345") @RequestHeader("X-userId") Long senderId) {
        return siteMessageService.sendMessage(requestDTO, senderId);
    }

    @PostMapping("/batch-send")
    @Operation(summary = "批量发送站内消息", description = "向多个用户发送相同的站内消息")
    public Result<Void> batchSendMessage(
            @RequestBody @Valid SendMessageRequestDTO requestDTO,
            @Parameter(description = "接收者ID列表") @RequestParam List<Long> receiverIds,
            @Parameter(description = "发送者ID", example = "12345") @RequestHeader("X-userId") Long senderId) {
        return siteMessageService.batchSendMessage(requestDTO, senderId, receiverIds);
    }

    @PostMapping("/send-by-template")
    @Operation(summary = "根据模板发送消息", description = "使用消息模板发送消息")
    public Result<Void> sendMessageByTemplate(
            @Parameter(description = "模板代码", example = "GROUP_JOIN_NOTICE") @RequestParam String templateCode,
            @Parameter(description = "接收者ID", example = "12345") @RequestParam Long receiverId,
            @Parameter(description = "模板参数") @RequestBody Map<String, Object> templateParams,
            @Parameter(description = "发送者ID（可选）", example = "67890") @RequestHeader(value = "X-userId", required = false) Long senderId) {
        return siteMessageService.sendMessageByTemplate(templateCode, receiverId, templateParams, senderId);
    }

    @GetMapping("/list")
    @Operation(summary = "获取消息列表", description = "分页获取用户的消息列表")
    public Result<PageResult<MessageDetailResponseDTO>> getMessageList(
            @Parameter(description = "消息列表查询条件") @ModelAttribute MessageListRequestDTO requestDTO,
            @Parameter(description = "用户ID", example = "12345") @RequestHeader("X-userId") Long userId) {
        return siteMessageService.getMessageList(requestDTO, userId);
    }

    @GetMapping("/{messageId}")
    @Operation(summary = "获取消息详情", description = "获取指定消息的详细信息")
    public Result<MessageDetailResponseDTO> getMessageDetail(
            @Parameter(description = "消息ID", example = "12345") @PathVariable Long messageId,
            @Parameter(description = "用户ID", example = "67890") @RequestHeader("X-userId") Long userId) {
        return siteMessageService.getMessageDetail(messageId, userId);
    }

    @PutMapping("/{messageId}/read")
    @Operation(summary = "标记消息已读", description = "将指定消息标记为已读状态")
    public Result<Void> markAsRead(
            @Parameter(description = "消息ID", example = "12345") @PathVariable Long messageId,
            @Parameter(description = "用户ID", example = "67890") @RequestHeader("X-userId") Long userId) {
        return siteMessageService.markAsRead(messageId, userId);
    }

    @PutMapping("/batch-read")
    @Operation(summary = "批量标记消息已读", description = "批量将多条消息标记为已读状态")
    public Result<Void> batchMarkAsRead(
            @Parameter(description = "消息ID列表") @RequestBody List<Long> messageIds,
            @Parameter(description = "用户ID", example = "12345") @RequestHeader("X-userId") Long userId) {
        return siteMessageService.batchMarkAsRead(messageIds, userId);
    }

    @PutMapping("/all-read")
    @Operation(summary = "标记所有消息已读", description = "将用户的所有未读消息标记为已读")
    public Result<Void> markAllAsRead(
            @Parameter(description = "用户ID", example = "12345") @RequestHeader("X-userId") Long userId) {
        return siteMessageService.markAllAsRead(userId);
    }

    @DeleteMapping("/{messageId}")
    @Operation(summary = "删除消息", description = "删除指定的消息（逻辑删除）")
    public Result<Void> deleteMessage(
            @Parameter(description = "消息ID", example = "12345") @PathVariable Long messageId,
            @Parameter(description = "用户ID", example = "67890") @RequestHeader("X-userId") Long userId) {
        return siteMessageService.deleteMessage(messageId, userId);
    }

    @GetMapping("/unread-count")
    @Operation(summary = "获取未读消息数量", description = "获取用户的未读消息总数")
    public Result<Long> getUnreadCount(
            @Parameter(description = "用户ID", example = "12345") @RequestHeader("X-userId") Long userId) {
        return siteMessageService.getUnreadCount(userId);
    }
} 