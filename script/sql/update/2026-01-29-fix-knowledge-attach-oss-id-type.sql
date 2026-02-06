-- ----------------------------
-- 修复knowledge_attach表oss_id字段类型
-- 问题：knowledge_attach.oss_id是int(11)，但sys_oss.oss_id是bigint，导致大值无法存储
-- 修复：将knowledge_attach.oss_id改为bigint(20)以匹配sys_oss.oss_id
-- 创建日期: 2026-01-29
-- ----------------------------

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 修改knowledge_attach表的oss_id字段类型为bigint
-- ----------------------------
ALTER TABLE `knowledge_attach` 
MODIFY COLUMN `oss_id` bigint(20) NULL DEFAULT NULL COMMENT '对象存储ID';

SET FOREIGN_KEY_CHECKS = 1;
