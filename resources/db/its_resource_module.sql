-- 资源表
CREATE TABLE its_resource_module.resource (
                                            id BIGSERIAL PRIMARY KEY,  -- 主键
                                            user_id BIGINT NOT NULL,  -- 上传用户ID
                                            title VARCHAR(255) NOT NULL,  -- 资源标题
                                            type VARCHAR(32) NOT NULL,  -- 资源类型：doc、ppt、video、image等
                                            visibility SMALLINT NOT NULL DEFAULT 0,  -- 0-私有，1-组共享，2-公开
                                            created_at TIMESTAMP DEFAULT NOW(),  -- 创建时间
                                            updated_at TIMESTAMP DEFAULT NOW()   -- 更新时间
);

COMMENT ON TABLE its_resource_module.resource IS '资源主表，包含标题、上传人、访问权限等元信息';
COMMENT ON COLUMN its_resource_module.resource.id IS '主键ID';
COMMENT ON COLUMN its_resource_module.resource.user_id IS '上传用户ID';
COMMENT ON COLUMN its_resource_module.resource.title IS '资源标题';
COMMENT ON COLUMN its_resource_module.resource.type IS '资源类型，如doc、ppt、video';
COMMENT ON COLUMN its_resource_module.resource.visibility IS '资源可见性：0私有，1组共享，2公开';
COMMENT ON COLUMN its_resource_module.resource.created_at IS '创建时间';
COMMENT ON COLUMN its_resource_module.resource.updated_at IS '更新时间';

-- 资源详情表
CREATE TABLE its_resource_module.resource_detail (
                                                   id BIGSERIAL PRIMARY KEY,  -- 主键
                                                   resource_id BIGINT NOT NULL,  -- 关联的资源ID
                                                   file_path TEXT NOT NULL, -- 文件物理路径
                                                   description TEXT,  -- 资源描述
                                                   metadata JSONB,  -- 扩展元信息，学科/学段/关键词等
                                                   created_at TIMESTAMP DEFAULT NOW()
);

COMMENT ON TABLE its_resource_module.resource_detail IS '资源详情表，记录资源描述与扩展信息';
COMMENT ON COLUMN its_resource_module.resource_detail.id IS '主键ID';
COMMENT ON COLUMN its_resource_module.resource_detail.resource_id IS '关联资源ID';
COMMENT ON COLUMN its_resource_module.resource_detail.file_path IS '文件的物理路径';
COMMENT ON COLUMN its_resource_module.resource_detail.description IS '资源描述';
COMMENT ON COLUMN its_resource_module.resource_detail.metadata IS 'JSON格式扩展元数据';
COMMENT ON COLUMN its_resource_module.resource_detail.created_at IS '创建时间';

-- 文件夹表
CREATE TABLE its_resource_module.folder (
                                          id BIGSERIAL PRIMARY KEY,  -- 主键
                                          user_id BIGINT NOT NULL,  -- 所属用户
                                          parent_id BIGINT,  -- 父文件夹ID，根目录为NULL
                                          folder_name VARCHAR(128) NOT NULL,  -- 文件夹名称
                                          created_at TIMESTAMP DEFAULT NOW(),
                                          updated_at TIMESTAMP DEFAULT NOW(),
                                          UNIQUE(user_id, parent_id, folder_name)  -- 同级目录下唯一
);

COMMENT ON TABLE its_resource_module.folder IS '文件夹表，支持用户自定义的多级目录结构';
COMMENT ON COLUMN its_resource_module.folder.id IS '主键ID';
COMMENT ON COLUMN its_resource_module.folder.user_id IS '所属用户ID';
COMMENT ON COLUMN its_resource_module.folder.parent_id IS '父文件夹ID，NULL为根目录';
COMMENT ON COLUMN its_resource_module.folder.folder_name IS '文件夹名称';
COMMENT ON COLUMN its_resource_module.folder.created_at IS '创建时间';
COMMENT ON COLUMN its_resource_module.folder.updated_at IS '更新时间';
-- 文件夹资源映射表
CREATE TABLE its_resource_module.folder_resource_mapping (
                                                           id BIGSERIAL PRIMARY KEY,
                                                           folder_id BIGINT NOT NULL,  -- 文件夹ID
                                                           resource_id BIGINT NOT NULL,  -- 资源ID
                                                           resource_alias VARCHAR(255) NOT NULL,  -- 在该文件夹中的资源显示名
                                                           created_at TIMESTAMP DEFAULT NOW(),
                                                           UNIQUE(folder_id, resource_alias)  -- 同文件夹下资源名称唯一
);

COMMENT ON TABLE its_resource_module.folder_resource_mapping IS '文件夹与资源之间的映射关系表';
COMMENT ON COLUMN its_resource_module.folder_resource_mapping.id IS '主键ID';
COMMENT ON COLUMN its_resource_module.folder_resource_mapping.folder_id IS '文件夹ID';
COMMENT ON COLUMN its_resource_module.folder_resource_mapping.resource_id IS '资源ID';
COMMENT ON COLUMN its_resource_module.folder_resource_mapping.resource_alias IS '资源在文件夹中的显示名';
COMMENT ON COLUMN its_resource_module.folder_resource_mapping.created_at IS '创建时间';

-- 资源组表
CREATE TABLE its_resource_module.group_resource (
                                                  id BIGSERIAL PRIMARY KEY,
                                                  group_id BIGINT NOT NULL,  -- 用户组ID
                                                  resource_id BIGINT NOT NULL,  -- 资源ID
                                                  created_at TIMESTAMP DEFAULT NOW(),
                                                  UNIQUE(group_id, resource_id)
);

COMMENT ON TABLE its_resource_module.group_resource IS '用户组与资源的关联表，支持组共享资源';
COMMENT ON COLUMN its_resource_module.group_resource.id IS '主键ID';
COMMENT ON COLUMN its_resource_module.group_resource.group_id IS '用户组ID';
COMMENT ON COLUMN its_resource_module.group_resource.resource_id IS '资源ID';
COMMENT ON COLUMN its_resource_module.group_resource.created_at IS '创建时间';

