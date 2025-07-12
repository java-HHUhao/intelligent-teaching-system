package cn.edu.hhu.its.message.service.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 审核流程配置表，定义不同类型的审核规则
 * @TableName audit_config
 */
@TableName(value = "audit_config")
@Data
public class AuditConfigDO implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 审核类型，唯一
     */
    private String auditType;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 是否自动审核
     */
    private Boolean autoAudit;

    /**
     * 审核超时时间（小时）
     */
    private Integer auditTimeout;

    /**
     * 需要的审核员数量
     */
    private Integer requiredAuditorCount;

    /**
     * 审核规则配置，JSON格式
     */
    private String auditRules;

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