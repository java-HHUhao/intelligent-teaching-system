package cn.edu.hhu.its.message.service.model.domain;

import cn.edu.hhu.its.message.service.handler.StringToJsonbTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.util.Date;

/**
 * 审核记录表，支持资源、用户等各种审核
 * @TableName audit_record
 */
@TableName(value = "audit_record")
@Data
public class AuditRecordDO implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 审核类型：RESOURCE、USER_AVATAR等
     */
    private String auditType;

    /**
     * 审核目标ID
     */
    private Long targetId;

    /**
     * 目标类型：RESOURCE、USER等
     */
    private String targetType;

    /**
     * 提交者ID
     */
    private Long submitterId;

    /**
     * 审核员ID
     */
    private Long auditorId;

    /**
     * 审核状态：0待审核，1通过，2拒绝
     */
    private Integer status;

    /**
     * 审核意见或拒绝原因
     */
    private String auditReason;

    /**
     * 审核相关数据，JSON格式
     */
    @TableField(value = "audit_data", jdbcType = JdbcType.OTHER, typeHandler = StringToJsonbTypeHandler.class)
    private String auditData;

    /**
     * 提交时间
     */
    private Date submittedAt;

    /**
     * 审核时间
     */
    private Date auditedAt;

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