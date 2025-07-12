package cn.edu.hhu.its.message.service.service.messageservice;

import cn.edu.hhu.its.message.service.common.enums.ChannelTypeEnum;
import cn.edu.hhu.its.message.service.common.enums.MessageErrorCode;
import cn.edu.hhu.its.message.service.model.dto.request.MultiChannelMessageRequestDTO;
import cn.edu.hhu.its.message.service.model.dto.response.MessageSendResultDTO;
import cn.edu.hhu.its.message.service.service.messageservice.template.MessageTemplateProcessor;
import cn.edu.hhu.spring.boot.starter.common.exception.ServerException;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import cn.edu.hhu.spring.boot.starter.common.thread.core.ThreadPoolManager;
import cn.edu.hhu.spring.boot.starter.common.utils.ResultUtil;
import cn.edu.hhu.spring.boot.starter.designpattern.strategy.AbstractStrategyChooser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 消息服务实现类
 * 
 * @author ITS项目组
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final AbstractStrategyChooser strategyChooser;
    private final MessageTemplateProcessor templateProcessor;
    private final ThreadPoolManager threadPoolManager;

    private static final String MESSAGE_THREAD_POOL_NAME = "message-send-pool";
    private static final int SEND_TIMEOUT_SECONDS = 30;

    @Override
    public Result<MessageSendResultDTO> sendMultiChannelMessage(MultiChannelMessageRequestDTO request) {
        try {
            // 参数验证
            Result<Void> validateResult = validateRequest(request);
            if (validateResult != null) {
                return ResultUtil.fail(validateResult.getMessage(), validateResult.getCode());
            }

            MessageSendResultDTO result = new MessageSendResultDTO();
            result.setSendTime(new Date());
            result.setChannelResults(new ArrayList<>());

            boolean hasSuccess = false;
            List<String> errorMessages = new ArrayList<>();

            // 获取线程池进行并发发送
            ThreadPoolExecutor executor = threadPoolManager.get(MESSAGE_THREAD_POOL_NAME);
            
            List<CompletableFuture<MessageSendResultDTO.ChannelResult>> futures = new ArrayList<>();
            
            for (ChannelTypeEnum channel : request.getChannels()) {
                CompletableFuture<MessageSendResultDTO.ChannelResult> future;
                
                if (executor != null) {
                    // 使用自定义线程池异步发送
                    future = CompletableFuture.supplyAsync(() -> sendToChannel(channel.name(), request), executor);
                } else {
                    // 降级使用默认线程池
                    log.warn("消息发送线程池未找到，使用默认线程池: {}", MESSAGE_THREAD_POOL_NAME);
                    future = CompletableFuture.supplyAsync(() -> sendToChannel(channel.name(), request));
                }
                
                futures.add(future);
            }

            // 等待所有渠道发送完成，设置超时时间
            for (CompletableFuture<MessageSendResultDTO.ChannelResult> future : futures) {
                try {
                    MessageSendResultDTO.ChannelResult channelResult = future.get(SEND_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    result.getChannelResults().add(channelResult);
                    
                    if (channelResult.getSuccess()) {
                        hasSuccess = true;
                    } else {
                        errorMessages.add(channelResult.getChannel() + ": " + channelResult.getMessage());
                    }
                } catch (Exception e) {
                    log.error("获取渠道发送结果异常", e);
                    String errorMsg = "渠道发送异常: " + e.getMessage();
                    errorMessages.add(errorMsg);
                    
                    // 创建失败结果
                    MessageSendResultDTO.ChannelResult errorResult = createErrorResult(null, errorMsg);
                    result.getChannelResults().add(errorResult);
                }
            }

            // 设置总体结果
            result.setSuccess(hasSuccess);
            if (hasSuccess) {
                result.setMessage("消息发送完成");
                if (!errorMessages.isEmpty()) {
                    result.setMessage("消息发送完成，部分渠道失败: " + String.join(", ", errorMessages));
                }
                return ResultUtil.success(result);
            } else {
                result.setMessage("所有渠道发送失败: " + String.join(", ", errorMessages));
                Result<MessageSendResultDTO> failResult = ResultUtil.fail(MessageErrorCode.MESSAGE_SEND_ERROR.getMessage(), 
                                     MessageErrorCode.MESSAGE_SEND_ERROR.getCode());
                failResult.setData(result);
                return failResult;
            }

        } catch (Exception e) {
            log.error("多渠道消息发送异常", e);
            return ResultUtil.fail(MessageErrorCode.MESSAGE_SEND_ERROR.getMessage() + ": " + e.getMessage(), 
                                 MessageErrorCode.MESSAGE_SEND_ERROR.getCode());
        }
    }

    @Override
    public Result<MessageSendResultDTO.ChannelResult> sendSingleChannelMessage(String channelType, MultiChannelMessageRequestDTO request) {
        try {
            // 参数验证
            if (!StringUtils.hasText(channelType)) {
                return new Result<MessageSendResultDTO.ChannelResult>()
                    .setCode(MessageErrorCode.PARAM_VALIDATION_ERROR.getCode())
                    .setMessage(MessageErrorCode.PARAM_VALIDATION_ERROR.getMessage());
            }
            
            Result<Void> validateResult = validateRequest(request);
            if (validateResult != null) {
                return ResultUtil.fail(validateResult.getMessage(), validateResult.getCode());
            }

            MessageSendResultDTO.ChannelResult result = sendToChannel(channelType, request);
            
            if (result.getSuccess()) {
                return ResultUtil.success(result);
            } else {
                return new Result<MessageSendResultDTO.ChannelResult>()
                    .setCode(result.getErrorCode())
                    .setMessage(result.getMessage())
                    .setData(result);
            }
            
        } catch (Exception e) {
            log.error("单渠道消息发送异常: channelType={}", channelType, e);
            MessageSendResultDTO.ChannelResult errorResult = createErrorResult(channelType, e.getMessage());
            return new Result<MessageSendResultDTO.ChannelResult>()
                .setCode(MessageErrorCode.MESSAGE_SEND_ERROR.getCode())
                .setMessage(MessageErrorCode.MESSAGE_SEND_ERROR.getMessage())
                .setData(errorResult);
        }
    }

    @Override
    public Result<MessageSendResultDTO> sendTemplateMessage(MultiChannelMessageRequestDTO request) {
        try {
            // 参数验证
            Result<Void> validateResult = validateRequest(request);
            if (validateResult != null) {
                return ResultUtil.fail(validateResult.getMessage(), validateResult.getCode());
            }

            // 使用模板渲染消息内容
            if (StringUtils.hasText(request.getTemplateCode())) {
                MessageTemplateProcessor.TemplateRenderResult renderResult = 
                    templateProcessor.renderTemplate(request.getTemplateCode(), request.getTemplateParams());
                
                if (!renderResult.isSuccess()) {
                    log.error("模板渲染失败: templateCode={}, error={}", 
                            request.getTemplateCode(), renderResult.getErrorMessage());
                    return ResultUtil.fail(MessageErrorCode.MESSAGE_TEMPLATE_PARSE_ERROR.getMessage() + ": " + renderResult.getErrorMessage(),
                                         MessageErrorCode.MESSAGE_TEMPLATE_PARSE_ERROR.getCode());
                }

                // 更新请求中的标题和内容
                request.setTitle(renderResult.getTitle());
                request.setContent(renderResult.getContent());
            }

            // 调用多渠道发送
            return sendMultiChannelMessage(request);

        } catch (Exception e) {
            log.error("模板消息发送异常", e);
            return ResultUtil.fail(MessageErrorCode.MESSAGE_TEMPLATE_PARSE_ERROR.getMessage() + ": " + e.getMessage(),
                                 MessageErrorCode.MESSAGE_TEMPLATE_PARSE_ERROR.getCode());
        }
    }

    /**
     * 发送到指定渠道
     */
    private MessageSendResultDTO.ChannelResult sendToChannel(String channelType, MultiChannelMessageRequestDTO request) {
        try {
            // 使用策略模式选择对应的渠道发送器
            return strategyChooser.chooseResp(channelType, request);
            
        } catch (ServerException e) {
            // 策略未找到异常
            log.error("策略未找到: channelType={}", channelType, e);
            return createErrorResult(channelType, "发送渠道未支持: " + channelType);
            
        } catch (Exception e) {
            log.error("渠道发送异常: channelType={}", channelType, e);
            return createErrorResult(channelType, "渠道发送异常: " + e.getMessage());
        }
    }

    /**
     * 创建错误结果
     */
    private MessageSendResultDTO.ChannelResult createErrorResult(String channelType, String errorMessage) {
        MessageSendResultDTO.ChannelResult errorResult = new MessageSendResultDTO.ChannelResult();
        
        if (StringUtils.hasText(channelType)) {
            try {
                errorResult.setChannel(ChannelTypeEnum.valueOf(channelType));
            } catch (IllegalArgumentException e) {
                log.warn("无效的渠道类型: {}", channelType);
                errorResult.setChannel(null);
            }
        }
        
        errorResult.setSuccess(false);
        errorResult.setMessage(errorMessage);
        errorResult.setErrorCode(MessageErrorCode.MESSAGE_SEND_ERROR.getCode());
        errorResult.setSendTime(new Date());
        
        return errorResult;
    }

    /**
     * 验证请求参数
     */
    private Result<Void> validateRequest(MultiChannelMessageRequestDTO request) {
        if (request == null) {
            return ResultUtil.fail(MessageErrorCode.PARAM_VALIDATION_ERROR.getMessage() + ": 请求参数不能为空", 
                                 MessageErrorCode.PARAM_VALIDATION_ERROR.getCode());
        }

        if (request.getChannels() == null || request.getChannels().isEmpty()) {
            return ResultUtil.fail(MessageErrorCode.PARAM_VALIDATION_ERROR.getMessage() + ": 发送渠道不能为空", 
                                 MessageErrorCode.PARAM_VALIDATION_ERROR.getCode());
        }

        if (request.getReceiver() == null) {
            return ResultUtil.fail(MessageErrorCode.PARAM_VALIDATION_ERROR.getMessage() + ": 接收者信息不能为空", 
                                 MessageErrorCode.PARAM_VALIDATION_ERROR.getCode());
        }

        if (!StringUtils.hasText(request.getMessageType())) {
            return ResultUtil.fail(MessageErrorCode.PARAM_VALIDATION_ERROR.getMessage() + ": 消息类型不能为空", 
                                 MessageErrorCode.PARAM_VALIDATION_ERROR.getCode());
        }

        // 如果没有提供模板代码，必须提供标题或内容
        if (!StringUtils.hasText(request.getTemplateCode())) {
            if (!StringUtils.hasText(request.getTitle()) && !StringUtils.hasText(request.getContent())) {
                return ResultUtil.fail(MessageErrorCode.PARAM_VALIDATION_ERROR.getMessage() + ": 未使用模板时，标题或内容不能同时为空", 
                                     MessageErrorCode.PARAM_VALIDATION_ERROR.getCode());
            }
        }

        return null; // 验证通过返回null
    }
}
