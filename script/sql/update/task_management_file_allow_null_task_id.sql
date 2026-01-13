-- ----------------------------
-- 修改 task_management_file 表，允许 task_id 为 NULL
-- 原因：文件可能在上传时还没有关联任务，创建任务时再关联
-- ----------------------------

SET NAMES utf8mb4;

-- 修改 task_id 字段，允许为 NULL
ALTER TABLE `task_management_file` 
MODIFY COLUMN `task_id` bigint(20) NULL DEFAULT NULL COMMENT '任务ID';

-- 注意：外键约束仍然存在，但允许 NULL 值
-- 当 task_id 不为 NULL 时，必须引用有效的 task_management.id



