-- 用户模块测试数据
-- 基于控制器接口和网关权限解析规则

-- 1. 插入角色数据
INSERT INTO its_user_module.role (id, name, description, created_at, updated_at) VALUES
(1, 'SUPER_ADMIN', '超级管理员，拥有系统所有权限', NOW(), NOW()),
(2, 'ADMIN', '管理员，负责系统管理工作', NOW(), NOW()),
(3, 'TEACHER', '教师，负责教学相关工作', NOW(), NOW()),
(4, 'STUDENT', '学生，参与学习活动', NOW(), NOW());

-- 2. 插入权限数据（基于控制器接口路径和网关解析规则）
INSERT INTO its_user_module.permission (id, code, name, type, parent_id, description, created_at, updated_at) VALUES
-- 用户管理相关权限
(1, 'user:admin:view', '查看用户管理', 'API', NULL, '获取用户列表、搜索用户', NOW(), NOW()),
(2, 'user:admin:add', '添加用户管理', 'API', NULL, '创建角色、分配权限', NOW(), NOW()),
(3, 'user:admin:edit', '编辑用户管理', 'API', NULL, '更新权限', NOW(), NOW()),
(4, 'user:admin:delete', '删除用户管理', 'API', NULL, '删除角色、删除权限', NOW(), NOW()),

-- 用户登录相关权限
(5, 'user:login:add', '用户登录注册', 'API', NULL, '用户登录、注册、登出', NOW(), NOW()),

-- 用户认证相关权限
(6, 'user:auth:view', '查看用户权限', 'API', NULL, '获取用户权限信息', NOW(), NOW()),

-- 用户信息相关权限
(7, 'user:info:view', '查看用户信息', 'API', NULL, '获取用户个人资料', NOW(), NOW()),
(8, 'user:info:edit', '编辑用户信息', 'API', NULL, '更新用户资料、账户信息、用户名', NOW(), NOW()),

-- 资源管理权限（扩展）
(9, 'resource:*:view', '查看资源模块', 'API', NULL, '查看所有资源相关信息', NOW(), NOW()),
(10, 'resource:*:add', '添加资源模块', 'API', NULL, '创建资源相关内容', NOW(), NOW()),
(11, 'resource:*:edit', '编辑资源模块', 'API', NULL, '修改资源相关内容', NOW(), NOW()),
(12, 'resource:*:delete', '删除资源模块', 'API', NULL, '删除资源相关内容', NOW(), NOW()),

-- 生成器权限（扩展）
(13, 'generator:*:view', '查看生成器模块', 'API', NULL, '查看生成器相关信息', NOW(), NOW()),
(14, 'generator:*:add', '添加生成器模块', 'API', NULL, '创建生成器任务', NOW(), NOW()),
(15, 'generator:*:edit', '编辑生成器模块', 'API', NULL, '修改生成器任务', NOW(), NOW()),

-- 消息权限（扩展）
(16, 'message:*:view', '查看消息模块', 'API', NULL, '查看消息相关信息', NOW(), NOW()),
(17, 'message:*:add', '添加消息模块', 'API', NULL, '发送消息', NOW(), NOW());

-- 3. 插入用户数据
INSERT INTO its_user_module.sys_user (id, username, password, email, phone, status, created_at, updated_at, is_deleted) VALUES
(1, 'superadmin', 'root', 'superadmin@hhu.edu.cn', '13800000001', 1, NOW(), NOW(), FALSE),
(2, 'admin', 'root', 'admin@hhu.edu.cn', '13800000002', 1, NOW(), NOW(), FALSE),
(3, 'teacher01', 'teacher', 'teacher01@hhu.edu.cn', '13800000003', 1, NOW(), NOW(), FALSE),
(4, 'student01', 'student', 'student01@hhu.edu.cn', '13800000004', 1, NOW(), NOW(), FALSE);

-- 4. 插入用户详情数据
INSERT INTO its_user_module.user_detail (id, user_id, avatar_url, gender, birthday, address, bio, avatar_audit_status, created_at, updated_at) VALUES
(1, 1, 'https://example.com/avatar/superadmin.png', '男', '1980-01-01', '江苏省南京市', '系统超级管理员', 2, NOW(), NOW()),
(2, 2, 'https://example.com/avatar/admin.png', '女', '1985-05-15', '江苏省南京市', '系统管理员', 2, NOW(), NOW()),
(3, 3, 'https://example.com/avatar/teacher.png', '男', '1975-09-20', '江苏省南京市', '计算机科学与技术专业教师', 2, NOW(), NOW()),
(4, 4, 'https://example.com/avatar/student.png', '女', '2000-12-05', '江苏省南京市', '计算机科学与技术专业学生', 2, NOW(), NOW());

-- 5. 插入用户角色关系数据
INSERT INTO its_user_module.user_role (id, user_id, role_id, created_at) VALUES
(1, 1, 1, NOW()), -- superadmin -> SUPER_ADMIN
(2, 2, 2, NOW()), -- admin -> ADMIN
(3, 3, 3, NOW()), -- teacher01 -> TEACHER
(4, 4, 4, NOW()); -- student01 -> STUDENT

-- 6. 插入角色权限关系数据
-- 超级管理员：拥有所有权限
INSERT INTO its_user_module.role_permission (id, role_id, permission_id, created_at) VALUES
(1, 1, 1, NOW()), (2, 1, 2, NOW()), (3, 1, 3, NOW()), (4, 1, 4, NOW()),
(5, 1, 5, NOW()), (6, 1, 6, NOW()), (7, 1, 7, NOW()), (8, 1, 8, NOW()),
(9, 1, 9, NOW()), (10, 1, 10, NOW()), (11, 1, 11, NOW()), (12, 1, 12, NOW()),
(13, 1, 13, NOW()), (14, 1, 14, NOW()), (15, 1, 15, NOW()), (16, 1, 16, NOW()), (17, 1, 17, NOW());

-- 管理员：用户管理权限 + 部分系统权限
INSERT INTO its_user_module.role_permission (id, role_id, permission_id, created_at) VALUES
(18, 2, 1, NOW()), (19, 2, 2, NOW()), (20, 2, 3, NOW()), (21, 2, 4, NOW()),
(22, 2, 5, NOW()), (23, 2, 6, NOW()), (24, 2, 7, NOW()), (25, 2, 8, NOW()),
(26, 2, 9, NOW()), (27, 2, 10, NOW()), (28, 2, 11, NOW()), (29, 2, 16, NOW()), (30, 2, 17, NOW());

-- 教师：教学相关权限
INSERT INTO its_user_module.role_permission (id, role_id, permission_id, created_at) VALUES
(31, 3, 5, NOW()), (32, 3, 6, NOW()), (33, 3, 7, NOW()), (34, 3, 8, NOW()),
(35, 3, 9, NOW()), (36, 3, 10, NOW()), (37, 3, 11, NOW()), (38, 3, 13, NOW()),
(39, 3, 14, NOW()), (40, 3, 15, NOW()), (41, 3, 16, NOW()), (42, 3, 17, NOW());

-- 学生：基础权限
INSERT INTO its_user_module.role_permission (id, role_id, permission_id, created_at) VALUES
(43, 4, 5, NOW()), (44, 4, 6, NOW()), (45, 4, 7, NOW()), (46, 4, 8, NOW()),
(47, 4, 9, NOW()), (48, 4, 13, NOW()), (49, 4, 16, NOW());


-- 查询验证数据
-- 验证用户及其角色
SELECT u.id, u.username, r.name as role_name, r.description
FROM its_user_module.sys_user u
JOIN its_user_module.user_role ur ON u.id = ur.user_id
JOIN its_user_module.role r ON ur.role_id = r.id
WHERE u.is_deleted = FALSE
ORDER BY u.id;

-- 验证角色权限
SELECT r.name as role_name, p.code as permission_code, p.name as permission_name
FROM its_user_module.role r
JOIN its_user_module.role_permission rp ON r.id = rp.role_id
JOIN its_user_module.permission p ON rp.permission_id = p.id
ORDER BY r.name, p.code; 