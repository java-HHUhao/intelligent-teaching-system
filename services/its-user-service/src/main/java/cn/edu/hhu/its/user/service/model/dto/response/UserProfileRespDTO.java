package cn.edu.hhu.its.user.service.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用户个人资料返回参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserProfileRespDTO {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像URL
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private String gender;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 所在地区
     */
    private String address;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 账号状态：1-启用，0-禁用
     */
    private Integer status;

    /**
     * 注册时间
     */
    private Date createdAt;
}
