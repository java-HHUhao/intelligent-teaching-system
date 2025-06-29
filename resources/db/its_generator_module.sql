-- 生成任务表
CREATE TABLE its_generator_module.generate_task (
                                                    id BIGINT PRIMARY KEY, -- 主键
                                                    user_id BIGINT NOT NULL, -- 发起用户
                                                    input_type VARCHAR(32) NOT NULL, -- 输入类型：text/image/doc/template等
                                                    input_data TEXT NOT NULL, -- 原始输入内容或路径
                                                    task_type VARCHAR(32) NOT NULL, -- 生成类型：video/ppt/doc等
                                                    status VARCHAR(16) NOT NULL DEFAULT 'PENDING', -- PENDING/SUCCESS/FAILED
                                                    resource_id BIGINT, -- 成功后关联生成的资源ID（逻辑外键）
                                                    error_message TEXT, -- 错误信息
                                                    created_at TIMESTAMP DEFAULT NOW(),
                                                    updated_at TIMESTAMP DEFAULT NOW()
);

COMMENT ON TABLE its_generator_module.generate_task IS '资源生成任务记录表';
COMMENT ON COLUMN its_generator_module.generate_task.user_id IS '发起任务的用户ID';
COMMENT ON COLUMN its_generator_module.generate_task.input_type IS '输入类型，如text/doc/image';
COMMENT ON COLUMN its_generator_module.generate_task.input_data IS '输入内容（原始文本或路径）';
COMMENT ON COLUMN its_generator_module.generate_task.task_type IS '生成类型，如video/ppt等';
COMMENT ON COLUMN its_generator_module.generate_task.status IS '任务状态：PENDING/PROCESSING/SUCCESS/FAILED';
COMMENT ON COLUMN its_generator_module.generate_task.resource_id IS '生成成功后对应的资源ID';
COMMENT ON COLUMN its_generator_module.generate_task.error_message IS '任务失败原因（如模型错误）';

-- 任务日志表
CREATE TABLE its_generator_module.generate_task_log (
                                                        id BIGINT PRIMARY KEY,
                                                        task_id BIGINT NOT NULL, -- 对应的任务ID
                                                        status VARCHAR(16) NOT NULL, -- 状态：PENDING/PROCESSING/FAILED/SUCCESS
                                                        message TEXT, -- 附加说明或日志
                                                        created_at TIMESTAMP DEFAULT NOW()
);

COMMENT ON TABLE its_generator_module.generate_task_log IS '资源生成任务的状态变更与日志记录';
COMMENT ON COLUMN its_generator_module.generate_task_log.task_id IS '任务ID（逻辑外键）';
COMMENT ON COLUMN its_generator_module.generate_task_log.status IS '当前日志记录的状态';
COMMENT ON COLUMN its_generator_module.generate_task_log.message IS '说明或异常信息';
