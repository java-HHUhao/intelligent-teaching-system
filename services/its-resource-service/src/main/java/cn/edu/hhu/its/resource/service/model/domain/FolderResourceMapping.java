package cn.edu.hhu.its.resource.service.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 文件夹与资源之间的映射关系表
 * @TableName folder_resource_mapping
 */
@TableName(value ="folder_resource_mapping")
@Data
public class FolderResourceMapping implements Serializable {
    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 文件夹ID
     */
    private Long folderId;

    /**
     * 资源ID
     */
    private Long resourceId;

    /**
     * 资源在文件夹中的显示名
     */
    private String resourceAlias;

    /**
     * 创建时间
     */
    private Date createdAt;

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
        FolderResourceMapping other = (FolderResourceMapping) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getFolderId() == null ? other.getFolderId() == null : this.getFolderId().equals(other.getFolderId()))
            && (this.getResourceId() == null ? other.getResourceId() == null : this.getResourceId().equals(other.getResourceId()))
            && (this.getResourceAlias() == null ? other.getResourceAlias() == null : this.getResourceAlias().equals(other.getResourceAlias()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFolderId() == null) ? 0 : getFolderId().hashCode());
        result = prime * result + ((getResourceId() == null) ? 0 : getResourceId().hashCode());
        result = prime * result + ((getResourceAlias() == null) ? 0 : getResourceAlias().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", folderId=").append(folderId);
        sb.append(", resourceId=").append(resourceId);
        sb.append(", resourceAlias=").append(resourceAlias);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}