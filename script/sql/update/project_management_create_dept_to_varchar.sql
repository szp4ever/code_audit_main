-- ----------------------------
-- 修改project_management表的create_dept字段类型从bigint改为varchar
-- ----------------------------

SET NAMES utf8mb4;

-- 修改create_dept字段类型为varchar(255)，用于存储部门名称字符串
ALTER TABLE `project_management`
    MODIFY COLUMN `create_dept` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建部门（存储部门名称字符串）';

