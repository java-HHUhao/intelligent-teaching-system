package cn.edu.hhu.its.message.service.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 站内消息表，支持用户间消息和系统通知
 * @TableName site_message
 */
@TableName(value ="site_message")
@Data
public class SiteMessageDO implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 消息类型ID
     */
    private Long messageTypeId;

    /**
     * 发送者ID，系统消息可为空
     */
    private Long senderId;

    /**
     * 接收者ID
     */
    private Long receiverId;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 是否已读
     */
    private Boolean isRead;

    /**
     * 优先级：1普通，2重要，3紧急
     */
    private Integer priority;

    /**
     * 过期时间，NULL表示永不过期
     */
    private Date expiresAt;

    /**
     * 读取时间
     */
    private Date readAt;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 逻辑删除标志
     */
    private Boolean isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}