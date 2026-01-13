-- ----------------------------
-- 项目管理模块数据库迁移脚本
-- 用于在已有任务管理表的情况下，添加项目关联功能
-- 
-- 使用说明：
-- 1. 如果 project_id 字段已存在，执行此脚本会报错，可以忽略
-- 2. 如果外键约束已存在，执行此脚本会报错，可以忽略
-- 3. 建议先执行 project_management.sql 创建项目表，再执行此迁移脚本
-- ----------------------------

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 添加 project_id 字段到 task_management 表
-- 如果字段已存在，会报错，可以忽略
-- ----------------------------
ALTER TABLE `task_management`
    ADD COLUMN `project_id` bigint(20) NULL DEFAULT NULL COMMENT '项目ID' AFTER `id`;

-- ----------------------------
-- 添加索引
-- 如果索引已存在，会报错，可以忽略
-- ----------------------------
ALTER TABLE `task_management`
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE COMMENT '项目ID索引';

-- ----------------------------
-- 添加外键约束
-- 注意：需要先确保 project_management 表已创建
-- 如果外键已存在，会报错，可以忽略
-- ----------------------------
ALTER TABLE `task_management`
    ADD CONSTRAINT `fk_task_management_project` FOREIGN KEY (`project_id`) REFERENCES `project_management` (`id`) ON DELETE SET NULL ON UPDATE CASCADE COMMENT '任务关联项目外键';

SET FOREIGN_KEY_CHECKS = 1;

