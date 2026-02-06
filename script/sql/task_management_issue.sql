-- ----------------------------
-- 任务管理Issue表结构
-- ----------------------------

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for task_management_issue
-- ----------------------------
DROP TABLE IF EXISTS `task_management_issue`;
CREATE TABLE `task_management_issue`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `task_id`         bigint(20) NOT NULL COMMENT '任务ID',
    `file_name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名',
    `issue_name`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问题名称',
    `severity`        varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '严重程度：Low-低, Medium-中, High-高, Critical-严重',
    `line_number`     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '行号（可能是单个行号或范围，如"45"或"45-48"）',
    `description`     text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '问题描述',
    `fix_suggestion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '修复建议',
    `create_time`     datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag`        char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_task_id`(`task_id`) USING BTREE,
    INDEX `idx_severity`(`severity`) USING BTREE,
    INDEX `idx_file_name`(`file_name`) USING BTREE,
    INDEX `idx_create_time`(`create_time`) USING BTREE,
    CONSTRAINT `fk_task_management_issue_task` FOREIGN KEY (`task_id`) REFERENCES `task_management` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '任务管理Issue表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

