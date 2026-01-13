-- ----------------------------
-- 任务管理模块数据库表结构
-- ----------------------------

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for task_management
-- ----------------------------
DROP TABLE IF EXISTS `task_management`;
CREATE TABLE `task_management`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `title`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务标题',
    `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '任务描述',
    `priority`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'medium' COMMENT '任务优先级：low-低, medium-中, high-高, urgent-紧急',
    `task_type`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务类型：code_standard_check-编码规范检查, data_security_audit-数据安全审计, dependency_analysis-依赖关系分析, compliance_audit-合规审计, other-其他',
    `status`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'pending' COMMENT '任务状态：pending-待处理, in_progress-进行中, completed-已完成, cancelled-已取消',
    `create_dept` bigint(20) NULL DEFAULT NULL COMMENT '创建部门',
    `create_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark`      varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
    `del_flag`    char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    `tenant_id`   bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_task_type`(`task_type`) USING BTREE,
    INDEX `idx_status`(`status`) USING BTREE,
    INDEX `idx_priority`(`priority`) USING BTREE,
    INDEX `idx_create_time`(`create_time`) USING BTREE,
    INDEX `idx_create_by`(`create_by`) USING BTREE,
    INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '任务管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for task_management_file
-- ----------------------------
DROP TABLE IF EXISTS `task_management_file`;
CREATE TABLE `task_management_file`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `task_id`       bigint(20) NULL DEFAULT NULL COMMENT '任务ID',
    `name`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
    `url`           varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件URL',
    `size`          bigint(20) NULL DEFAULT NULL COMMENT '文件大小（字节）',
    `type`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类型（MIME类型）',
    `file_category` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'input' COMMENT '文件类别：input-输入文件, output-输出文件',
    `upload_time`   datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    `create_by`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time`   datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag`      char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_task_id`(`task_id`) USING BTREE,
    INDEX `idx_file_category`(`file_category`) USING BTREE,
    INDEX `idx_upload_time`(`upload_time`) USING BTREE,
    CONSTRAINT `fk_task_management_file_task` FOREIGN KEY (`task_id`) REFERENCES `task_management` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX `idx_task_id_null`(`task_id`) USING BTREE COMMENT '用于查询未关联任务的文件'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '任务管理文件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for task_management_tag
-- ----------------------------
DROP TABLE IF EXISTS `task_management_tag`;
CREATE TABLE `task_management_tag`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `task_id`     bigint(20) NOT NULL COMMENT '任务ID',
    `tag_name`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名称',
    `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_task_id`(`task_id`) USING BTREE,
    INDEX `idx_tag_name`(`tag_name`) USING BTREE,
    UNIQUE INDEX `uk_task_management_tag`(`task_id`, `tag_name`) USING BTREE COMMENT '任务和标签唯一索引',
    CONSTRAINT `fk_task_management_tag_task` FOREIGN KEY (`task_id`) REFERENCES `task_management` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '任务管理标签关联表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;


