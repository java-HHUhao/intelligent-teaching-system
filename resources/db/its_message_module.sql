-- 消息类型表
CREATE TABLE its_message_module.message_type (
    id BIGINT PRIMARY KEY,  -- 主键
    type_code VARCHAR(16) NOT NULL UNIQUE,  -- 消息类型代码
    type_name VARCHAR(32) NOT NULL,  -- 消息类型名称
    description TEXT,  -- 类型描述
    is_active BOOLEAN DEFAULT TRUE,  -- 是否启用
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

COMMENT ON TABLE its_message_module.message_type IS '消息类型定义表';
COMMENT ON COLUMN its_message_module.message_type.id IS '主键ID';
COMMENT ON COLUMN its_message_module.message_type.type_code IS '消息类型代码';
COMMENT ON COLUMN its_message_module.message_type.type_name IS '消息类型名称';
COMMENT ON COLUMN its_message_module.message_type.description IS '类型描述';
COMMENT ON COLUMN its_message_module.message_type.is_active IS '是否启用';
COMMENT ON COLUMN its_message_module.message_type.created_at IS '创建时间';
COMMENT ON COLUMN its_message_module.message_type.updated_at IS '更新时间';

-- 消息模板表
CREATE TABLE its_message_module.message_template (
    id BIGINT PRIMARY KEY,  -- 主键
    template_code VARCHAR(64) NOT NULL UNIQUE,  -- 模板代码
    template_name VARCHAR(32) NOT NULL,  -- 模板名称
    message_type_id BIGINT NOT NULL,  -- 关联消息类型ID
    title_template TEXT NOT NULL,  -- 标题模板，支持占位符 {参数名}
    content_template TEXT NOT NULL,  -- 内容模板，支持占位符 {参数名}
    is_active BOOLEAN DEFAULT TRUE,  -- 是否启用
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

COMMENT ON TABLE its_message_module.message_template IS '消息模板表，支持占位符的消息模板';
COMMENT ON COLUMN its_message_module.message_template.id IS '主键ID';
COMMENT ON COLUMN its_message_module.message_template.template_code IS '模板代码，唯一标识';
COMMENT ON COLUMN its_message_module.message_template.template_name IS '模板名称';
COMMENT ON COLUMN its_message_module.message_template.message_type_id IS '关联消息类型ID';
COMMENT ON COLUMN its_message_module.message_template.title_template IS '标题模板，支持{参数名}占位符';
COMMENT ON COLUMN its_message_module.message_template.content_template IS '内容模板，支持{参数名}占位符';
COMMENT ON COLUMN its_message_module.message_template.is_active IS '是否启用';
COMMENT ON COLUMN its_message_module.message_template.created_at IS '创建时间';
COMMENT ON COLUMN its_message_module.message_template.updated_at IS '更新时间';

-- 站内消息表
CREATE TABLE its_message_module.site_message (
    id BIGINT PRIMARY KEY,  -- 主键
    message_type_id BIGINT NOT NULL,  -- 消息类型ID
    sender_id BIGINT,  -- 发送者ID，系统消息可为空
    receiver_id BIGINT NOT NULL,  -- 接收者ID
    title VARCHAR(64) NOT NULL,  -- 消息标题
    content TEXT NOT NULL,  -- 消息内容
    is_read BOOLEAN DEFAULT FALSE,  -- 是否已读
    priority SMALLINT DEFAULT 1,  -- 优先级：1-普通，2-重要，3-紧急
    expires_at TIMESTAMP,  -- 过期时间，可为空表示永不过期
    read_at TIMESTAMP,  -- 读取时间
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    is_deleted BOOLEAN DEFAULT FALSE  -- 逻辑删除标志
);

COMMENT ON TABLE its_message_module.site_message IS '站内消息表，支持用户间消息和系统通知';
COMMENT ON COLUMN its_message_module.site_message.id IS '主键ID';
COMMENT ON COLUMN its_message_module.site_message.message_type_id IS '消息类型ID';
COMMENT ON COLUMN its_message_module.site_message.sender_id IS '发送者ID，系统消息可为空';
COMMENT ON COLUMN its_message_module.site_message.receiver_id IS '接收者ID';
COMMENT ON COLUMN its_message_module.site_message.title IS '消息标题';
COMMENT ON COLUMN its_message_module.site_message.content IS '消息内容';
COMMENT ON COLUMN its_message_module.site_message.is_read IS '是否已读';
COMMENT ON COLUMN its_message_module.site_message.priority IS '优先级：1普通，2重要，3紧急';
COMMENT ON COLUMN its_message_module.site_message.expires_at IS '过期时间，NULL表示永不过期';
COMMENT ON COLUMN its_message_module.site_message.read_at IS '读取时间';
COMMENT ON COLUMN its_message_module.site_message.created_at IS '创建时间';
COMMENT ON COLUMN its_message_module.site_message.updated_at IS '更新时间';
COMMENT ON COLUMN its_message_module.site_message.is_deleted IS '逻辑删除标志';

-- 验证码表
CREATE TABLE its_message_module.verification_code (
    id BIGINT PRIMARY KEY,  -- 主键
    code_type VARCHAR(16) NOT NULL,  -- 验证码类型：EMAIL、SMS、IMAGE等
    target VARCHAR(32) NOT NULL,  -- 目标（邮箱、手机号、session等）
    code VARCHAR(16) NOT NULL,  -- 验证码
    user_id BIGINT,  -- 关联用户ID，可为空
    ip_address VARCHAR(45),  -- 请求IP地址
    attempts INTEGER DEFAULT 0,  -- 验证尝试次数
    is_used BOOLEAN DEFAULT FALSE,  -- 是否已使用
    expires_at TIMESTAMP NOT NULL,  -- 过期时间
    used_at TIMESTAMP,  -- 使用时间
    created_at TIMESTAMP DEFAULT NOW()
);

COMMENT ON TABLE its_message_module.verification_code IS '验证码表，支持邮箱、短信、图片验证码等';
COMMENT ON COLUMN its_message_module.verification_code.id IS '主键ID';
COMMENT ON COLUMN its_message_module.verification_code.code_type IS '验证码类型：EMAIL、SMS、IMAGE等';
COMMENT ON COLUMN its_message_module.verification_code.target IS '目标标识（邮箱、手机号、sessionId等）';
COMMENT ON COLUMN its_message_module.verification_code.code IS '验证码';
COMMENT ON COLUMN its_message_module.verification_code.user_id IS '关联用户ID，可为空';
COMMENT ON COLUMN its_message_module.verification_code.ip_address IS '请求IP地址';
COMMENT ON COLUMN its_message_module.verification_code.attempts IS '验证尝试次数';
COMMENT ON COLUMN its_message_module.verification_code.is_used IS '是否已使用';
COMMENT ON COLUMN its_message_module.verification_code.expires_at IS '过期时间';
COMMENT ON COLUMN its_message_module.verification_code.used_at IS '使用时间';
COMMENT ON COLUMN its_message_module.verification_code.created_at IS '创建时间';

-- 审核记录表
CREATE TABLE its_message_module.audit_record (
    id BIGINT PRIMARY KEY,  -- 主键
    audit_type VARCHAR(16) NOT NULL,  -- 审核类型：RESOURCE、USER_AVATAR等
    target_id BIGINT NOT NULL,  -- 审核目标ID（资源ID、用户ID等）
    target_type VARCHAR(16) NOT NULL,  -- 目标类型：RESOURCE、USER等
    submitter_id BIGINT NOT NULL,  -- 提交者ID
    auditor_id BIGINT,  -- 审核员ID
    status SMALLINT DEFAULT 0,  -- 审核状态：0-待审核，1-审核通过，2-审核拒绝
    audit_reason TEXT,  -- 审核意见或拒绝原因
    audit_data JSONB,  -- 审核相关数据（JSON格式）
    submitted_at TIMESTAMP DEFAULT NOW(),  -- 提交时间
    audited_at TIMESTAMP,  -- 审核时间
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

COMMENT ON TABLE its_message_module.audit_record IS '审核记录表，支持资源、用户等各种审核';
COMMENT ON COLUMN its_message_module.audit_record.id IS '主键ID';
COMMENT ON COLUMN its_message_module.audit_record.audit_type IS '审核类型：RESOURCE、USER_AVATAR等';
COMMENT ON COLUMN its_message_module.audit_record.target_id IS '审核目标ID';
COMMENT ON COLUMN its_message_module.audit_record.target_type IS '目标类型：RESOURCE、USER等';
COMMENT ON COLUMN its_message_module.audit_record.submitter_id IS '提交者ID';
COMMENT ON COLUMN its_message_module.audit_record.auditor_id IS '审核员ID';
COMMENT ON COLUMN its_message_module.audit_record.status IS '审核状态：0待审核，1通过，2拒绝';
COMMENT ON COLUMN its_message_module.audit_record.audit_reason IS '审核意见或拒绝原因';
COMMENT ON COLUMN its_message_module.audit_record.audit_data IS '审核相关数据，JSON格式';
COMMENT ON COLUMN its_message_module.audit_record.submitted_at IS '提交时间';
COMMENT ON COLUMN its_message_module.audit_record.audited_at IS '审核时间';
COMMENT ON COLUMN its_message_module.audit_record.created_at IS '创建时间';
COMMENT ON COLUMN its_message_module.audit_record.updated_at IS '更新时间';

-- 审核流程配置表
CREATE TABLE its_message_module.audit_config (
    id BIGINT PRIMARY KEY,  -- 主键
    audit_type VARCHAR(16) NOT NULL UNIQUE,  -- 审核类型
    config_name VARCHAR(64) NOT NULL,  -- 配置名称
    auto_audit BOOLEAN DEFAULT FALSE,  -- 是否自动审核
    audit_timeout INTEGER DEFAULT 72,  -- 审核超时时间（小时）
    required_auditor_count INTEGER DEFAULT 1,  -- 需要的审核员数量
    audit_rules JSONB,  -- 审核规则配置（JSON格式）
    is_active BOOLEAN DEFAULT TRUE,  -- 是否启用
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

COMMENT ON TABLE its_message_module.audit_config IS '审核流程配置表，定义不同类型的审核规则';
COMMENT ON COLUMN its_message_module.audit_config.id IS '主键ID';
COMMENT ON COLUMN its_message_module.audit_config.audit_type IS '审核类型，唯一';
COMMENT ON COLUMN its_message_module.audit_config.config_name IS '配置名称';
COMMENT ON COLUMN its_message_module.audit_config.auto_audit IS '是否自动审核';
COMMENT ON COLUMN its_message_module.audit_config.audit_timeout IS '审核超时时间（小时）';
COMMENT ON COLUMN its_message_module.audit_config.required_auditor_count IS '需要的审核员数量';
COMMENT ON COLUMN its_message_module.audit_config.audit_rules IS '审核规则配置，JSON格式';
COMMENT ON COLUMN its_message_module.audit_config.is_active IS '是否启用';
COMMENT ON COLUMN its_message_module.audit_config.created_at IS '创建时间';
COMMENT ON COLUMN its_message_module.audit_config.updated_at IS '更新时间';

-- 创建索引
CREATE INDEX idx_site_message_receiver_read ON its_message_module.site_message(receiver_id, is_read);
CREATE INDEX idx_site_message_type_created ON its_message_module.site_message(message_type_id, created_at);
CREATE INDEX idx_verification_code_target_type ON its_message_module.verification_code(target, code_type);
CREATE INDEX idx_verification_code_expires ON its_message_module.verification_code(expires_at);
CREATE INDEX idx_audit_record_status_type ON its_message_module.audit_record(status, audit_type);
CREATE INDEX idx_audit_record_submitter ON its_message_module.audit_record(submitter_id, created_at);

-- 插入基础消息类型数据
INSERT INTO its_message_module.message_type (id, type_code, type_name, description) VALUES 
(1, 'SYSTEM_NOTICE', '系统通知', '系统级别的通知消息'),
(2, 'GROUP_JOIN', '加入组通知', '用户加入组的通知'),
(3, 'GROUP_LEAVE', '退出组通知', '用户退出组的通知'),
(4, 'GROUP_INVITE', '组邀请通知', '邀请用户加入组'),
(5, 'RESOURCE_AUDIT', '资源审核通知', '资源审核结果通知'),
(6, 'USER_AUDIT', '用户审核通知', '用户相关审核通知');

-- 插入基础消息模板数据
INSERT INTO its_message_module.message_template (id, template_code, template_name, message_type_id, title_template, content_template) VALUES 
(1, 'GROUP_JOIN_NOTICE', '加入组通知模板', 2, '欢迎加入组：{groupName}', '您已成功加入组"{groupName}"，现在可以访问组内共享的资源了。'),
(2, 'GROUP_LEAVE_NOTICE', '退出组通知模板', 3, '您已退出组：{groupName}', '您已成功退出组"{groupName}"，将无法继续访问该组的共享资源。'),
(3, 'GROUP_INVITE_NOTICE', '组邀请通知模板', 4, '邀请您加入组：{groupName}', '用户"{inviterName}"邀请您加入组"{groupName}"，请确认是否接受邀请。'),
(4, 'RESOURCE_AUDIT_PASS', '资源审核通过模板', 5, '资源审核通过：{resourceTitle}', '您提交的资源"{resourceTitle}"已通过审核，现在可以正常使用了。'),
(5, 'RESOURCE_AUDIT_REJECT', '资源审核拒绝模板', 5, '资源审核未通过：{resourceTitle}', '您提交的资源"{resourceTitle}"未通过审核，原因：{reason}。请修改后重新提交。');

-- 插入基础审核配置数据
INSERT INTO its_message_module.audit_config (id, audit_type, config_name, auto_audit, audit_timeout, required_auditor_count, audit_rules) VALUES 
(1, 'RESOURCE', '资源审核配置', FALSE, 72, 1, '{"allowedTypes": ["doc", "ppt", "pdf", "image"], "maxFileSize": "100MB"}'),
(2, 'USER_AVATAR', '用户头像审核配置', TRUE, 24, 1, '{"autoRejectKeywords": ["政治", "暴力", "色情"], "enableImageRecognition": true}');

