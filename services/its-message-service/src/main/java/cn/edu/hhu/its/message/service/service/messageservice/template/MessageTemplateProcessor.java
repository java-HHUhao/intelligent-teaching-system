package cn.edu.hhu.its.message.service.service.messageservice.template;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 消息模板处理器接口
 * 
 * @author ITS项目组
 */
public interface MessageTemplateProcessor {

    /**
     * 渲染消息模板
     * 
     * @param templateCode 模板代码
     * @param templateParams 模板参数
     * @return 渲染后的消息内容
     */
    TemplateRenderResult renderTemplate(String templateCode, Map<String, Object> templateParams);

    /**
     * 根据模板ID渲染消息模板
     * 
     * @param templateId 模板ID
     * @param templateParams 模板参数
     * @return 渲染后的消息内容
     */
    TemplateRenderResult renderTemplateById(Long templateId, Map<String, Object> templateParams);

    /**
     * 验证模板参数
     * 
     * @param templateCode 模板代码
     * @param templateParams 模板参数
     * @return 验证结果
     */
    boolean validateTemplateParams(String templateCode, Map<String, Object> templateParams);

    /**
     * 模板渲染结果
     */
    @Setter
    @Getter
    class TemplateRenderResult {
        // Getters and Setters
        private boolean success;
        private String title;
        private String content;
        private String errorMessage;

        public TemplateRenderResult() {}

        public TemplateRenderResult(boolean success, String title, String content) {
            this.success = success;
            this.title = title;
            this.content = content;
        }

        public TemplateRenderResult(boolean success, String errorMessage) {
            this.success = success;
            this.errorMessage = errorMessage;
        }

    }
} 