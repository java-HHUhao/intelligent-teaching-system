-- 用户表
CREATE TABLE its_user_module.sys_user
(
    id         BIGINT PRIMARY KEY,        -- 主键
    username   VARCHAR(64)  NOT NULL UNIQUE, -- 用户名，唯一
    password   VARCHAR(128) NOT NULL,        -- 密码，加密存储
    email      VARCHAR(128) UNIQUE,          -- 邮箱
    phone      VARCHAR(20) UNIQUE,           -- 手机号
    status     SMALLINT  DEFAULT 1,          -- 状态：1-启用，0-禁用
    created_at TIMESTAMP DEFAULT NOW(),      -- 创建时间
    updated_at TIMESTAMP DEFAULT NOW(),      -- 更新时间
    is_deleted BOOLEAN   DEFAULT FALSE       -- 是否逻辑删除
);

COMMENT ON TABLE its_user_module.sys_user IS '系统用户表';
COMMENT ON COLUMN its_user_module.sys_user.id IS '主键';
COMMENT ON COLUMN its_user_module.sys_user.username IS '用户名，唯一';
COMMENT ON COLUMN its_user_module.sys_user.password IS '密码，加密存储';
COMMENT ON COLUMN its_user_module.sys_user.email IS '电子邮箱';
COMMENT ON COLUMN its_user_module.sys_user.phone IS '手机号';
COMMENT ON COLUMN its_user_module.sys_user.status IS '状态：1-启用，0-禁用';
COMMENT ON COLUMN its_user_module.sys_user.created_at IS '创建时间';
COMMENT ON COLUMN its_user_module.sys_user.updated_at IS '更新时间';
COMMENT ON COLUMN its_user_module.sys_user.is_deleted IS '逻辑删除标志';

-- 用户详情表
CREATE TABLE its_user_module."user_detail"
(
    id         BIGINT PRIMARY KEY,   -- 主键
    user_id    BIGINT NOT NULL UNIQUE,  -- 用户ID，唯一
    avatar_url TEXT,                    -- 头像地址
    gender     VARCHAR(10),             -- 性别
    birthday   DATE,                    -- 生日
    address    VARCHAR(64)              -- 所在地区
    bio        TEXT,                    -- 简介
    avatar_audit_status smallint default 0,-- 0=未提交，1=待审核，2=审核通过，3=驳回
    created_at TIMESTAMP DEFAULT NOW(), -- 创建时间
    updated_at TIMESTAMP DEFAULT NOW()  -- 更新时间
);

COMMENT ON TABLE its_user_module."user_detail" IS '用户详情信息表';
COMMENT ON COLUMN its_user_module."user_detail".id IS '主键';
COMMENT ON COLUMN its_user_module."user_detail".user_id IS '关联用户ID';
COMMENT ON COLUMN its_user_module."user_detail".avatar_url IS '头像地址';
COMMENT ON COLUMN its_user_module."user_detail".gender IS '性别';
COMMENT ON COLUMN its_user_module."user_detail".birthday IS '生日';
COMMENT ON COLUMN its_user_module."user_detail".address IS '所在地区';
COMMENT ON COLUMN its_user_module."user_detail".bio IS '个人简介';
COMMENT ON COLUMN its_user_module."user_detail".avatar_audit_status IS '头像审核状态';
COMMENT ON COLUMN its_user_module."user_detail".created_at IS '创建时间';
COMMENT ON COLUMN its_user_module."user_detail".updated_at IS '更新时间';

-- 角色表
CREATE TABLE its_user_module.role (
                                      id BIGINT PRIMARY KEY, -- 主键
                                      name VARCHAR(64) NOT NULL UNIQUE, -- 角色名，如 ADMIN
                                      description TEXT, -- 描述
                                      created_at TIMESTAMP DEFAULT NOW(), -- 创建时间
                                      updated_at TIMESTAMP DEFAULT NOW() -- 更新时间
);

COMMENT ON TABLE its_user_module.role IS '系统角色表';
COMMENT ON COLUMN its_user_module.role.id IS '主键';
COMMENT ON COLUMN its_user_module.role.name IS '角色名，唯一，如 ADMIN';
COMMENT ON COLUMN its_user_module.role.description IS '角色描述';
COMMENT ON COLUMN its_user_module.role.created_at IS '创建时间';
COMMENT ON COLUMN its_user_module.role.updated_at IS '更新时间';

-- 权限表
CREATE TABLE its_user_module.permission (
                                            id BIGINT PRIMARY KEY, -- 主键
                                            code VARCHAR(64) NOT NULL UNIQUE, -- 权限标识，如 resource:create
                                            name VARCHAR(128) NOT NULL, -- 权限中文名
                                            type VARCHAR(32), -- 权限类型，如 API、菜单、按钮
                                            parent_id BIGINT, -- 父权限ID，支持权限树结构
                                            description TEXT, -- 描述
                                            created_at TIMESTAMP DEFAULT NOW(), -- 创建时间
                                            updated_at TIMESTAMP DEFAULT NOW() -- 更新时间
);

COMMENT ON TABLE its_user_module.permission IS '系统权限表';
COMMENT ON COLUMN its_user_module.permission.id IS '主键';
COMMENT ON COLUMN its_user_module.permission.code IS '权限标识，唯一';
COMMENT ON COLUMN its_user_module.permission.name IS '权限名称，中文';
COMMENT ON COLUMN its_user_module.permission.type IS '权限类型，如 API、菜单、按钮';
COMMENT ON COLUMN its_user_module.permission.parent_id IS '父权限ID，构成权限树';
COMMENT ON COLUMN its_user_module.permission.description IS '权限描述';
COMMENT ON COLUMN its_user_module.permission.created_at IS '创建时间';
COMMENT ON COLUMN its_user_module.permission.updated_at IS '更新时间';

-- 用户角色关系表
CREATE TABLE its_user_module.user_role (
                                           id BIGINT PRIMARY KEY, -- 主键
                                           user_id BIGINT NOT NULL, -- 用户ID
                                           role_id BIGINT NOT NULL, -- 角色ID
                                           created_at TIMESTAMP DEFAULT NOW(), -- 创建时间
                                           UNIQUE (user_id, role_id)
);

COMMENT ON TABLE its_user_module.user_role IS '用户角色关系表';
COMMENT ON COLUMN its_user_module.user_role.id IS '主键';
COMMENT ON COLUMN its_user_module.user_role.user_id IS '用户ID';
COMMENT ON COLUMN its_user_module.user_role.role_id IS '角色ID';
COMMENT ON COLUMN its_user_module.user_role.created_at IS '创建时间';

-- 角色权限关系表
CREATE TABLE its_user_module.role_permission (
                                                 id BIGINT PRIMARY KEY, -- 主键
                                                 role_id BIGINT NOT NULL, -- 角色ID
                                                 permission_id BIGINT NOT NULL, -- 权限ID
                                                 created_at TIMESTAMP DEFAULT NOW(), -- 创建时间
                                                 UNIQUE (role_id, permission_id)
);

COMMENT ON TABLE its_user_module.role_permission IS '角色权限关系表';
COMMENT ON COLUMN its_user_module.role_permission.id IS '主键';
COMMENT ON COLUMN its_user_module.role_permission.role_id IS '角色ID';
COMMENT ON COLUMN its_user_module.role_permission.permission_id IS '权限ID';
COMMENT ON COLUMN its_user_module.role_permission.created_at IS '创建时间';

-- 登录日志表
CREATE TABLE its_user_module.login_log (
                                           id BIGINT PRIMARY KEY, -- 主键
                                           user_id BIGINT, -- 用户ID
                                           login_time TIMESTAMP DEFAULT NOW(), -- 登录时间
                                           ip_address VARCHAR(45), -- IP地址
                                           user_agent TEXT, -- 浏览器/设备信息
                                           login_status SMALLINT DEFAULT 1-- 状态：1-成功，0-失败
);

COMMENT ON TABLE its_user_module.login_log IS '用户登录日志表';
COMMENT ON COLUMN its_user_module.login_log.id IS '主键';
COMMENT ON COLUMN its_user_module.login_log.user_id IS '关联用户ID';
COMMENT ON COLUMN its_user_module.login_log.login_time IS '登录时间';
COMMENT ON COLUMN its_user_module.login_log.ip_address IS '登录IP地址';
COMMENT ON COLUMN its_user_module.login_log.user_agent IS '用户代理信息';
COMMENT ON COLUMN its_user_module.login_log.login_status IS '登录状态：1成功，0失败';
