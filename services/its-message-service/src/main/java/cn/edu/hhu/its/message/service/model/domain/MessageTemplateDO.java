package cn.edu.hhu.its.message.service.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息模板表，支持占位符的消息模板
 * @TableName message_template
 */
@TableName(value = "message_template")
@Data
public class MessageTemplateDO implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 模板代码，唯一标识
     */
    private String templateCode;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 关联消息类型ID
     */
    private Long messageTypeId;

    /**
     * 标题模板，支持{参数名}占位符
     */
    private String titleTemplate;

    /**
     * 内容模板，支持{参数名}占位符
     */
    private String contentTemplate;

    /**
     * 是否启用
     */
    private Boolean isActive;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}