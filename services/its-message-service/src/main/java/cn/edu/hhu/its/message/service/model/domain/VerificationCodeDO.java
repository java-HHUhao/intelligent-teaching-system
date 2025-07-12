package cn.edu.hhu.its.message.service.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 验证码表，支持邮箱、短信、图片验证码等
 * @TableName verification_code
 */
@TableName(value = "verification_code")
@Data
public class VerificationCodeDO implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 验证码类型：EMAIL、SMS、IMAGE等
     */
    private String codeType;

    /**
     * 目标标识（邮箱、手机号、sessionId等）
     */
    private String target;

    /**
     * 验证码
     */
    private String code;

    /**
     * 关联用户ID，可为空
     */
    private Long userId;

    /**
     * 请求IP地址
     */
    private String ipAddress;

    /**
     * 验证尝试次数
     */
    private Integer attempts;

    /**
     * 是否已使用
     */
    private Boolean isUsed;

    /**
     * 过期时间
     */
    private Date expiresAt;

    /**
     * 使用时间
     */
    private Date usedAt;

    /**
     * 创建时间
     */
    private Date createdAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}