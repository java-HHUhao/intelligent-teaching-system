package cn.edu.hhu.its.generator.service.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 资源生成任务记录表
 * @TableName generate_task
 */
@TableName(value ="generate_task")
@Data
public class GenerateTask implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 发起任务的用户ID
     */
    private Long userId;

    /**
     * 输入类型，如text/doc/image
     */
    private String inputType;

    /**
     * 输入内容（原始文本或路径）
     */
    private String inputData;

    /**
     * 生成类型，如video/ppt等
     */
    private String taskType;

    /**
     * 任务状态：PENDING/PROCESSING/SUCCESS/FAILED
     */
    private String status;

    /**
     * 生成成功后对应的资源ID
     */
    private Long resourceId;

    /**
     * 任务失败原因（如模型错误）
     */
    private String errorMessage;

    /**
     * 
     */
    private Date createdAt;

    /**
     * 
     */
    private Date updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        GenerateTask other = (GenerateTask) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getInputType() == null ? other.getInputType() == null : this.getInputType().equals(other.getInputType()))
            && (this.getInputData() == null ? other.getInputData() == null : this.getInputData().equals(other.getInputData()))
            && (this.getTaskType() == null ? other.getTaskType() == null : this.getTaskType().equals(other.getTaskType()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getResourceId() == null ? other.getResourceId() == null : this.getResourceId().equals(other.getResourceId()))
            && (this.getErrorMessage() == null ? other.getErrorMessage() == null : this.getErrorMessage().equals(other.getErrorMessage()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getInputType() == null) ? 0 : getInputType().hashCode());
        result = prime * result + ((getInputData() == null) ? 0 : getInputData().hashCode());
        result = prime * result + ((getTaskType() == null) ? 0 : getTaskType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getResourceId() == null) ? 0 : getResourceId().hashCode());
        result = prime * result + ((getErrorMessage() == null) ? 0 : getErrorMessage().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", inputType=").append(inputType);
        sb.append(", inputData=").append(inputData);
        sb.append(", taskType=").append(taskType);
        sb.append(", status=").append(status);
        sb.append(", resourceId=").append(resourceId);
        sb.append(", errorMessage=").append(errorMessage);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}