package cn.edu.hhu.its.resource.service.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户组表，定义教学组/班级等逻辑分组
 * @TableName user_group
 */
@TableName(value ="user_group", schema = "its_user_module")
@Data
public class UserGroupDO implements Serializable {
    /**
     * 用户组ID
     */
    @TableId
    private Long id;

    /**
     * 组名称
     */
    private String groupName;

    /**
     * 组描述
     */
    private String description;

    /**
     * 创建者ID
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createdAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
} 