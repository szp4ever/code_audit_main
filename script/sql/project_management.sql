-- ----------------------------
-- 项目管理模块数据库表结构
-- ----------------------------

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for project_management
-- ----------------------------
DROP TABLE IF EXISTS `project_management`;
CREATE TABLE `project_management`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目名称',
    `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '项目描述',
    `status`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'active' COMMENT '项目状态：active-进行中, completed-已完成, archived-已归档, cancelled-已取消（根据任务完成情况自动计算，也可手动设置）',
    `create_dept` bigint(20) NULL DEFAULT NULL COMMENT '创建部门',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_by_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    `del_flag`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    `tenant_id`   bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_status`(`status`) USING BTREE,
    INDEX `idx_create_time`(`create_time`) USING BTREE,
    INDEX `idx_create_by`(`create_by`) USING BTREE,
    INDEX `idx_create_by_id`(`create_by_id`) USING BTREE,
    INDEX `idx_tenant_id`(`tenant_id`) USING BTREE,
    INDEX `idx_name`(`name`) USING BTREE COMMENT '项目名称索引，用于关键词搜索'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for project_management_tag
-- ----------------------------
DROP TABLE IF EXISTS `project_management_tag`;
CREATE TABLE `project_management_tag`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `project_id`  bigint(20) NOT NULL COMMENT '项目ID',
    `tag_name`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名称',
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_project_id`(`project_id`) USING BTREE,
    INDEX `idx_tag_name`(`tag_name`) USING BTREE,
    UNIQUE INDEX `uk_project_management_tag`(`project_id`, `tag_name`) USING BTREE COMMENT '项目和标签唯一索引',
    CONSTRAINT `fk_project_management_tag_project` FOREIGN KEY (`project_id`) REFERENCES `project_management` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目管理标签关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- 修改任务管理表，添加项目ID字段
-- ----------------------------
ALTER TABLE `task_management`
    ADD COLUMN `project_id` bigint(20) NULL DEFAULT NULL COMMENT '项目ID' AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE COMMENT '项目ID索引',
    ADD CONSTRAINT `fk_task_management_project` FOREIGN KEY (`project_id`) REFERENCES `project_management` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

SET FOREIGN_KEY_CHECKS = 1;


