package cn.edu.hhu.its.message.service.service.messageservice.template.impl;

import cn.edu.hhu.its.message.service.model.domain.MessageTemplateDO;
import cn.edu.hhu.its.message.service.model.mapper.MessageTemplateMapper;
import cn.edu.hhu.its.message.service.service.messageservice.template.MessageTemplateProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 默认消息模板处理器实现
 * 支持{参数名}格式的占位符替换
 * 
 * @author ITS项目组
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMessageTemplateProcessor implements MessageTemplateProcessor {

    private final MessageTemplateMapper messageTemplateMapper;

    /**
     * 占位符模式：{参数名}
     */
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{([^}]+)\\}");

    @Override
    public TemplateRenderResult renderTemplate(String templateCode, Map<String, Object> templateParams) {
        try {
            // 根据模板代码查询模板
            MessageTemplateDO template = messageTemplateMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<MessageTemplateDO>()
                    .eq(MessageTemplateDO::getTemplateCode, templateCode)
            );
            if (template == null) {
                return new TemplateRenderResult(false, "模板不存在: " + templateCode);
            }

            if (!template.getIsActive()) {
                return new TemplateRenderResult(false, "模板已禁用: " + templateCode);
            }

            return renderTemplateContent(template.getTitleTemplate(), template.getContentTemplate(), templateParams);

        } catch (Exception e) {
            log.error("渲染模板异常: templateCode={}", templateCode, e);
            return new TemplateRenderResult(false, "模板渲染异常: " + e.getMessage());
        }
    }

    @Override
    public TemplateRenderResult renderTemplateById(Long templateId, Map<String, Object> templateParams) {
        try {
            // 根据模板ID查询模板
            MessageTemplateDO template = messageTemplateMapper.selectById(templateId);
            if (template == null) {
                return new TemplateRenderResult(false, "模板不存在: " + templateId);
            }

            if (!template.getIsActive()) {
                return new TemplateRenderResult(false, "模板已禁用: " + templateId);
            }

            return renderTemplateContent(template.getTitleTemplate(), template.getContentTemplate(), templateParams);

        } catch (Exception e) {
            log.error("渲染模板异常: templateId={}", templateId, e);
            return new TemplateRenderResult(false, "模板渲染异常: " + e.getMessage());
        }
    }

    @Override
    public boolean validateTemplateParams(String templateCode, Map<String, Object> templateParams) {
        try {
            MessageTemplateDO template = messageTemplateMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<MessageTemplateDO>()
                    .eq(MessageTemplateDO::getTemplateCode, templateCode)
            );
            if (template == null) {
                return false;
            }

            // 检查标题模板的必需参数
            if (!validatePlaceholders(template.getTitleTemplate(), templateParams)) {
                return false;
            }

            // 检查内容模板的必需参数
            return validatePlaceholders(template.getContentTemplate(), templateParams);

        } catch (Exception e) {
            log.error("验证模板参数异常: templateCode={}", templateCode, e);
            return false;
        }
    }

    /**
     * 渲染模板内容
     */
    private TemplateRenderResult renderTemplateContent(String titleTemplate, String contentTemplate, 
                                                      Map<String, Object> templateParams) {
        try {
            // 渲染标题
            String renderedTitle = replacePlaceholders(titleTemplate, templateParams);
            
            // 渲染内容
            String renderedContent = replacePlaceholders(contentTemplate, templateParams);

            return new TemplateRenderResult(true, renderedTitle, renderedContent);

        } catch (Exception e) {
            log.error("渲染模板内容异常", e);
            return new TemplateRenderResult(false, "模板内容渲染失败: " + e.getMessage());
        }
    }

    /**
     * 替换占位符
     * 
     * @param template 模板字符串
     * @param params 参数Map
     * @return 替换后的字符串
     */
    private String replacePlaceholders(String template, Map<String, Object> params) {
        if (!StringUtils.hasText(template)) {
            return "";
        }

        if (params == null || params.isEmpty()) {
            // 如果没有参数，检查模板是否包含占位符
            Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);
            if (matcher.find()) {
                log.warn("模板包含占位符但未提供参数: {}", template);
            }
            return template;
        }

        StringBuffer result = new StringBuffer();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);

        while (matcher.find()) {
            String paramName = matcher.group(1);
            Object paramValue = params.get(paramName);
            
            String replacement;
            if (paramValue != null) {
                replacement = String.valueOf(paramValue);
            } else {
                // 参数不存在，保留原占位符或使用默认值
                replacement = "[" + paramName + "]";
                log.warn("模板参数不存在: {}", paramName);
            }
            
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * 验证占位符参数是否完整
     */
    private boolean validatePlaceholders(String template, Map<String, Object> params) {
        if (!StringUtils.hasText(template)) {
            return true;
        }

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);
        while (matcher.find()) {
            String paramName = matcher.group(1);
            if (params == null || !params.containsKey(paramName)) {
                log.debug("缺少必需的模板参数: {}", paramName);
                return false;
            }
        }
        return true;
    }
} 