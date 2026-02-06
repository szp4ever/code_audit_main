-- ----------------------------
-- 附件处理状态管理表
-- 基于LLM与状态改革设计文档 v1.0
-- 创建日期: 2026-01-24
-- ----------------------------

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 表：knowledge_attach_process（附件处理状态表）
-- ----------------------------
DROP TABLE IF EXISTS `knowledge_attach_process`;
CREATE TABLE `knowledge_attach_process` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `attach_id` BIGINT NOT NULL COMMENT '附件ID（关联knowledge_attach.id）',
    `doc_id` VARCHAR(50) NOT NULL COMMENT '文档ID（关联knowledge_attach.doc_id）',
    `current_status` VARCHAR(50) NOT NULL COMMENT '当前状态（UPLOADING/PARSING/CHUNKING/MATCHING/USER_REVIEW_MATCHING/CREATING_ITEMS/USER_REVIEW_ITEMS/VECTORIZING/COMPLETED/FAILED/CANCELLED）',
    `status_data` JSON COMMENT '状态数据（存储匹配结果、LLM生成结果等，不存储完整片段内容）',
    `progress` INT DEFAULT 0 COMMENT '总进度（0-100）',
    `error_message` TEXT COMMENT '错误信息',
    `create_dept` BIGINT NULL DEFAULT NULL COMMENT '创建部门',
    `create_by` BIGINT NULL DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` BIGINT NULL DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag` CHAR(1) NULL DEFAULT '0' COMMENT '删除标志（0-存在 2-删除）',
    INDEX `idx_attach_id` (`attach_id`) USING BTREE,
    INDEX `idx_doc_id` (`doc_id`) USING BTREE,
    INDEX `idx_status` (`current_status`) USING BTREE,
    INDEX `idx_create_time` (`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '附件处理状态表';

SET FOREIGN_KEY_CHECKS = 1;
