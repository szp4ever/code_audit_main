-- ----------------------------
-- 为任务管理文件表添加相对路径字段
-- 用于支持文件夹上传功能，保存文件在文件夹中的相对路径
-- ----------------------------

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 添加相对路径字段
ALTER TABLE `task_management_file` 
ADD COLUMN `relative_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '相对路径（用于文件夹上传，如：src/main/java/App.java）' 
AFTER `name`;

-- 为相对路径字段添加索引，方便按路径查询
ALTER TABLE `task_management_file` 
ADD INDEX `idx_relative_path`(`relative_path`) USING BTREE;

SET FOREIGN_KEY_CHECKS = 1;

