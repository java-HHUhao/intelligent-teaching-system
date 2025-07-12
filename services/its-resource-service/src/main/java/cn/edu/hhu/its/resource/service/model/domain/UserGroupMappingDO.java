package cn.edu.hhu.its.resource.service.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户与用户组的多对多关联表
 * @TableName user_group_mapping
 */
@TableName(value ="user_group_mapping", schema = "its_user_module")
@Data
public class UserGroupMappingDO implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户组ID
     */
    private Long groupId;

    /**
     * 加入时间
     */
    private Date joinedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
} 