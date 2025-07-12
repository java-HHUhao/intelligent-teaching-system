package cn.edu.hhu.its.message.service.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 消息类型定义表
 * @TableName message_type
 */
@TableName(value ="message_type")
@Data
public class MessageTypeDO implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 消息类型代码，如GROUP_JOIN、RESOURCE_AUDIT
     */
    private String typeCode;

    /**
     * 消息类型名称
     */
    private String typeName;

    /**
     * 类型描述
     */
    private String description;

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