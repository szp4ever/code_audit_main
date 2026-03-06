-- ----------------------------
-- 知识库管理模块数据库表结构
-- 基于知识库管理架构设计文档 v2.1
-- 创建日期: 2026-01-13
-- ----------------------------

-- 设置字符集，确保中文字符正确存储
SET NAMES utf8mb4;
SET CHARACTER_SET_CLIENT = utf8mb4;
SET CHARACTER_SET_CONNECTION = utf8mb4;
SET CHARACTER_SET_RESULTS = utf8mb4;
SET COLLATION_CONNECTION = utf8mb4_unicode_ci;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 1. 修改现有表：knowledge_info（添加分类、统计字段）
-- ----------------------------
ALTER TABLE `knowledge_info`
ADD COLUMN `category` varchar(50) NULL DEFAULT NULL COMMENT '知识库分类（用途类型）' AFTER `description`,
ADD COLUMN `data_size` bigint(20) NOT NULL DEFAULT 0 COMMENT '占用字节数（所有知识条目内容的总大小）' AFTER `category`,
ADD COLUMN `item_count` int(11) NOT NULL DEFAULT 0 COMMENT '知识条目数量' AFTER `data_size`,
ADD COLUMN `fragment_count` int(11) NOT NULL DEFAULT 0 COMMENT '知识片段数量' AFTER `item_count`,
ADD INDEX `idx_category`(`category`) USING BTREE;

-- ----------------------------
-- 2. 修改现有表：knowledge_fragment（添加 item_uuid、vector_id 等字段，确保兼容性）
-- ----------------------------
ALTER TABLE `knowledge_fragment`
ADD COLUMN `item_uuid` varchar(64) NULL DEFAULT NULL COMMENT '知识条目UUID（关联 knowledge_item.item_uuid）' AFTER `kid`,
ADD COLUMN `vector_id` varchar(64) NULL DEFAULT NULL COMMENT '向量库UUID（Weaviate返回的向量ID，删除时必须使用此ID）' AFTER `content`,
ADD COLUMN `vulnerability_type` varchar(50) NULL DEFAULT NULL COMMENT '漏洞类型（冗余字段，用于快速过滤）' AFTER `vector_id`,
ADD COLUMN `language` varchar(20) NULL DEFAULT NULL COMMENT '适用语言（冗余字段）' AFTER `vulnerability_type`,
ADD INDEX `idx_item_uuid`(`item_uuid`) USING BTREE,
ADD INDEX `idx_vulnerability_type`(`vulnerability_type`) USING BTREE,
ADD INDEX `idx_language`(`language`) USING BTREE;

-- ----------------------------
-- 3. 新建表：knowledge_item（知识条目表）
-- ----------------------------
DROP TABLE IF EXISTS `knowledge_item`;
CREATE TABLE `knowledge_item`
(
    `id`                   bigint(20) NOT NULL AUTO_INCREMENT,
    `item_uuid`            varchar(64) NOT NULL COMMENT '知识条目UUID（唯一标识）',
    `kid`                  varchar(50) NOT NULL COMMENT '知识库ID（关联 knowledge_info.kid）',
    `title`                varchar(255) NOT NULL COMMENT '条目标题（如：规则ID-101：禁止使用 strcpy）',
    `summary`              varchar(500) NULL DEFAULT NULL COMMENT '摘要',
    
    -- 结构化元数据（用于精确检索）
    `vulnerability_type`    varchar(50) NULL DEFAULT NULL COMMENT '漏洞类型（SQL注入、缓冲区溢出等）',
    `language`             varchar(20) NULL DEFAULT NULL COMMENT '适用语言（Java、C++、Python等）',
    `severity`             varchar(20) NULL DEFAULT NULL COMMENT '风险等级（高危、中危、低危）',
    `rule_id`              varchar(50) NULL DEFAULT NULL COMMENT '关联规则ID（对应检测引擎规则）',
    `rule_name`             varchar(255) NULL DEFAULT NULL COMMENT '规则名称',
    
    -- 内容字段（非结构化）
    `problem_description`  text NULL DEFAULT NULL COMMENT '问题描述（漏洞触发场景、原理说明）',
    `fix_solution`         text NULL DEFAULT NULL COMMENT '修复方案（修复代码、步骤说明）',
    `example_code`         text NULL DEFAULT NULL COMMENT '示例代码（错误示例、正确示例）',
    `reference_link`       varchar(500) NULL DEFAULT NULL COMMENT '参考链接',
    
    -- 版本控制
    `current_version`      int(11) NOT NULL DEFAULT 1 COMMENT '当前版本号',
    `version_count`        int(11) NOT NULL DEFAULT 1 COMMENT '版本总数',
    `current_version_id`   bigint(20) NULL DEFAULT NULL COMMENT '当前版本ID（关联 knowledge_item_history.id，用于快速查询）',
    
    -- 存储统计字段（用于监控）
    `data_size`            bigint(20) NOT NULL DEFAULT 0 COMMENT '占用字节数（问题描述+修复方案+示例代码的总大小）',
    `fragment_count`       int(11) NOT NULL DEFAULT 0 COMMENT '知识片段数量',
    
    -- 状态管理
    `status`               varchar(20) NOT NULL DEFAULT 'draft' COMMENT '状态（draft-草稿、published-已发布、archived-已归档）',
    `publish_time`         datetime NULL DEFAULT NULL COMMENT '发布时间',
    `archive_time`         datetime NULL DEFAULT NULL COMMENT '归档时间',
    
    -- 来源信息（用于追溯）
    `source_type`          varchar(20) NULL DEFAULT NULL COMMENT '来源类型（manual-手动录入、feedback-用户反馈、import-批量导入）',
    `source_task_id`       bigint(20) NULL DEFAULT NULL COMMENT '来源任务ID（如果是来自检测任务）',
    `source_feedback_id`   bigint(20) NULL DEFAULT NULL COMMENT '来源反馈ID（如果是来自用户反馈）',
    
    -- RuoYi 标准字段
    `create_dept`          bigint(20) NULL DEFAULT NULL COMMENT '创建部门',
    `create_by`            bigint(20) NULL DEFAULT NULL COMMENT '创建人',
    `create_time`          datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`            bigint(20) NULL DEFAULT NULL COMMENT '更新者',
    `update_time`          datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark`               varchar(500) NULL DEFAULT NULL COMMENT '备注',
    `del_flag`             char(1) NULL DEFAULT '0' COMMENT '删除标志（0-存在 1-删除）',
    `tenant_id`            bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID',
    
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_item_uuid`(`item_uuid`) USING BTREE,
    INDEX `idx_kid`(`kid`) USING BTREE,
    INDEX `idx_vulnerability_type`(`vulnerability_type`) USING BTREE,
    INDEX `idx_language`(`language`) USING BTREE,
    INDEX `idx_severity`(`severity`) USING BTREE,
    INDEX `idx_rule_id`(`rule_id`) USING BTREE,
    INDEX `idx_status`(`status`) USING BTREE,
    INDEX `idx_source_task_id`(`source_task_id`) USING BTREE,
    INDEX `idx_create_time`(`create_time`) USING BTREE,
    CONSTRAINT `fk_knowledge_item_kid` FOREIGN KEY (`kid`) REFERENCES `knowledge_info` (`kid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '知识条目表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 4. 新建表：knowledge_item_history（知识条目版本历史表）
-- ----------------------------
DROP TABLE IF EXISTS `knowledge_item_history`;
CREATE TABLE `knowledge_item_history`
(
    `id`                   bigint(20) NOT NULL AUTO_INCREMENT,
    `item_uuid`            varchar(64) NOT NULL COMMENT '知识条目UUID（关联 knowledge_item.item_uuid）',
    `version`              int(11) NOT NULL COMMENT '版本号',
    `is_current`           char(1) NOT NULL DEFAULT '0' COMMENT '是否当前版本（0-否 1-是）',
    
    -- 内容快照（与 knowledge_item 对应字段）
    `title`                varchar(255) NOT NULL COMMENT '条目标题',
    `summary`              varchar(500) NULL DEFAULT NULL COMMENT '摘要',
    `vulnerability_type`   varchar(50) NULL DEFAULT NULL COMMENT '漏洞类型',
    `language`             varchar(20) NULL DEFAULT NULL COMMENT '适用语言',
    `severity`             varchar(20) NULL DEFAULT NULL COMMENT '风险等级',
    `rule_id`              varchar(50) NULL DEFAULT NULL COMMENT '规则ID',
    `problem_description` text NULL DEFAULT NULL COMMENT '问题描述',
    `fix_solution`         text NULL DEFAULT NULL COMMENT '修复方案',
    `example_code`         text NULL DEFAULT NULL COMMENT '示例代码',
    
    -- 变更信息
    `change_reason`        varchar(500) NULL DEFAULT NULL COMMENT '变更原因',
    `change_type`         varchar(20) NULL DEFAULT NULL COMMENT '变更类型（create-创建、update-更新、delete-删除）',
    `changed_by`           bigint(20) NULL DEFAULT NULL COMMENT '变更人ID',
    `changed_by_name`      varchar(50) NULL DEFAULT NULL COMMENT '变更人姓名',
    `changed_at`           datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '变更时间',
    
    -- RuoYi 标准字段
    `remark`               varchar(500) NULL DEFAULT NULL COMMENT '备注',
    
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_item_uuid`(`item_uuid`) USING BTREE,
    INDEX `idx_version`(`item_uuid`, `version`) USING BTREE,
    INDEX `idx_is_current`(`item_uuid`, `is_current`) USING BTREE,
    INDEX `idx_changed_at`(`changed_at`) USING BTREE,
    CONSTRAINT `fk_knowledge_item_history_item_uuid` FOREIGN KEY (`item_uuid`) REFERENCES `knowledge_item` (`item_uuid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '知识条目版本历史表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 5. 新建表：knowledge_tag（知识标签表）
-- ----------------------------
DROP TABLE IF EXISTS `knowledge_tag`;
CREATE TABLE `knowledge_tag`
(
    `id`                   bigint(20) NOT NULL AUTO_INCREMENT,
    `tag_name`             varchar(50) NOT NULL COMMENT '标签名称（如：内存泄漏、IEC协议、TOD时间戳）',
    `tag_type`             varchar(20) NULL DEFAULT 'user' COMMENT '标签类型（system-系统标签、user-用户标签）',
    `tag_category`         varchar(50) NULL DEFAULT NULL COMMENT '标签分类（用于分组）',
    `description`          varchar(500) NULL DEFAULT NULL COMMENT '标签描述',
    `usage_count`          int(11) NOT NULL DEFAULT 0 COMMENT '使用次数',
    
    -- RuoYi 标准字段
    `create_dept`          bigint(20) NULL DEFAULT NULL COMMENT '创建部门',
    `create_by`            bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
    `create_time`          datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`            bigint(20) NULL DEFAULT NULL COMMENT '更新者',
    `update_time`          datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag`             char(1) NULL DEFAULT '0' COMMENT '删除标志',
    `tenant_id`            bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID',
    
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_tag_name_tenant`(`tag_name`, `tenant_id`) USING BTREE,
    INDEX `idx_tag_type`(`tag_type`) USING BTREE,
    INDEX `idx_tag_category`(`tag_category`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '知识标签表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 6. 新建表：knowledge_item_tag（知识条目标签关联表）
-- ----------------------------
DROP TABLE IF EXISTS `knowledge_item_tag`;
CREATE TABLE `knowledge_item_tag`
(
    `id`                   bigint(20) NOT NULL AUTO_INCREMENT,
    `item_uuid`            varchar(64) NOT NULL COMMENT '知识条目UUID（关联 knowledge_item.item_uuid）',
    `tag_id`               bigint(20) NOT NULL COMMENT '标签ID（关联 knowledge_tag.id）',
    
    -- RuoYi 标准字段
    `create_time`          datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `tenant_id`            bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID',
    
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_item_tag`(`item_uuid`, `tag_id`) USING BTREE,
    INDEX `idx_item_uuid`(`item_uuid`) USING BTREE,
    INDEX `idx_tag_id`(`tag_id`) USING BTREE,
    CONSTRAINT `fk_knowledge_item_tag_item` FOREIGN KEY (`item_uuid`) REFERENCES `knowledge_item` (`item_uuid`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `fk_knowledge_item_tag_tag` FOREIGN KEY (`tag_id`) REFERENCES `knowledge_tag` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '知识条目标签关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 7. 新建表：knowledge_favorite（知识收藏表）
-- ----------------------------
DROP TABLE IF EXISTS `knowledge_favorite`;
CREATE TABLE `knowledge_favorite`
(
    `id`                   bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id`              bigint(20) NOT NULL COMMENT '用户ID',
    `item_uuid`            varchar(64) NOT NULL COMMENT '知识条目UUID（关联 knowledge_item.item_uuid）',
    `kid`                  varchar(50) NULL DEFAULT NULL COMMENT '知识库ID（冗余字段，用于快速查询）',
    
    -- RuoYi 标准字段
    `create_time`          datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    `tenant_id`            bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID',
    
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_user_item`(`user_id`, `item_uuid`) USING BTREE,
    INDEX `idx_user_id`(`user_id`) USING BTREE,
    INDEX `idx_item_uuid`(`item_uuid`) USING BTREE,
    INDEX `idx_kid`(`kid`) USING BTREE,
    CONSTRAINT `fk_knowledge_favorite_item` FOREIGN KEY (`item_uuid`) REFERENCES `knowledge_item` (`item_uuid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '知识收藏表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 8. 新建表：cwe_reference（CWE 标准漏洞类型参考表）
-- 说明：用于承载来自 STANDARDS/CWE.csv 的完整 CWE 数据，业务表仅引用 cwe_id
-- ----------------------------
DROP TABLE IF EXISTS `cwe_reference`;
CREATE TABLE `cwe_reference`
(
    `id`                    bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `cwe_id`                varchar(20) NOT NULL COMMENT 'CWE 编号（如 CWE-89）',
    `name_en`               varchar(512) NULL DEFAULT NULL COMMENT '英文名称',
    `name_zh`               varchar(512) NULL DEFAULT NULL COMMENT '中文名称（通过翻译生成，可人工校正）',
    `weakness_abstraction`  varchar(64) NULL DEFAULT NULL COMMENT '弱点抽象层级（Base/Variant/Class/Compound 等）',
    `status`                varchar(64) NULL DEFAULT NULL COMMENT '状态（Draft/Incomplete/Stable/Deprecated 等）',
    `description_en`        text NULL DEFAULT NULL COMMENT '英文描述',
    `description_zh`        text NULL DEFAULT NULL COMMENT '中文描述（可选）',
    `raw_json`              text NULL DEFAULT NULL COMMENT '原始 CSV 行的结构化 JSON（便于保留完整信息）',
    `create_time`           datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '导入时间',
    `update_time`           datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`             bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID（预留，通常为 0）',
    
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_cwe_reference_cwe_id`(`cwe_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'CWE 标准漏洞类型参考表（从 STANDARDS/CWE.csv 导入）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 9. 新建表：detection_result（检测结果表）
-- 说明：此表依赖检测报告功能，当前检测报告功能完成度仅10%，表结构先创建
-- ----------------------------
DROP TABLE IF EXISTS `detection_result`;
CREATE TABLE `detection_result`
(
    `id`                   bigint(20) NOT NULL AUTO_INCREMENT,
    `result_uuid`           varchar(64) NOT NULL COMMENT '检测结果UUID',
    `task_id`              bigint(20) NOT NULL COMMENT '任务ID（关联 task_management.id）',
    `project_id`           bigint(20) NULL DEFAULT NULL COMMENT '项目ID（关联 project_management.id）',
    
    -- 检测信息
    `rule_id`              varchar(50) NOT NULL COMMENT '规则ID',
    `rule_name`            varchar(255) NULL DEFAULT NULL COMMENT '规则名称',
    `vulnerability_type`   varchar(50) NULL DEFAULT NULL COMMENT '漏洞类型',
    `severity`             varchar(20) NULL DEFAULT NULL COMMENT '风险等级',
    `language`             varchar(20) NULL DEFAULT NULL COMMENT '代码语言',
    
    -- 代码信息
    `file_path`            varchar(500) NULL DEFAULT NULL COMMENT '文件路径',
    `file_name`            varchar(255) NULL DEFAULT NULL COMMENT '文件名',
    `line_number`         int(11) NULL DEFAULT NULL COMMENT '行号',
    `code_snippet`         text NULL DEFAULT NULL COMMENT '代码片段（检测出的问题代码）',
    `context_code`         text NULL DEFAULT NULL COMMENT '上下文代码',
    
    -- 检测结果
    `description`          text NULL DEFAULT NULL COMMENT '问题描述',
    `recommendation`       text NULL DEFAULT NULL COMMENT '修复建议（系统自动生成）',
    `confidence`           decimal(5,2) NULL DEFAULT NULL COMMENT '置信度（0-100）',
    
    -- 状态
    `status`               varchar(20) NOT NULL DEFAULT 'detected' COMMENT '状态（detected-已检测、false_positive-误报、fixed-已修复、ignored-已忽略）',
    `is_false_positive`   char(1) NULL DEFAULT '0' COMMENT '是否误报（0-否 1-是）',
    `is_fixed`             char(1) NULL DEFAULT '0' COMMENT '是否已修复（0-否 1-是）',
    
    -- RuoYi 标准字段
    `create_time`          datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`          datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag`             char(1) NULL DEFAULT '0' COMMENT '删除标志',
    `tenant_id`            bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID',
    
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_result_uuid`(`result_uuid`) USING BTREE,
    INDEX `idx_task_id`(`task_id`) USING BTREE,
    INDEX `idx_project_id`(`project_id`) USING BTREE,
    INDEX `idx_rule_id`(`rule_id`) USING BTREE,
    INDEX `idx_vulnerability_type`(`vulnerability_type`) USING BTREE,
    INDEX `idx_status`(`status`) USING BTREE,
    CONSTRAINT `fk_detection_result_task` FOREIGN KEY (`task_id`) REFERENCES `task_management` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '检测结果表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 9. 新建表：knowledge_feedback（知识反馈表，缓冲区表）
-- ----------------------------
DROP TABLE IF EXISTS `knowledge_feedback`;
CREATE TABLE `knowledge_feedback`
(
    `id`                   bigint(20) NOT NULL AUTO_INCREMENT,
    `feedback_uuid`        varchar(64) NOT NULL COMMENT '反馈UUID',
    `result_uuid`          varchar(64) NULL DEFAULT NULL COMMENT '检测结果UUID（关联 detection_result.result_uuid，独立反馈入口可为空）',
    `task_id`              bigint(20) NULL DEFAULT NULL COMMENT '任务ID（关联 task_management.id，独立反馈入口可为空）',
    `project_id`           bigint(20) NULL DEFAULT NULL COMMENT '项目ID（关联 project_management.id）',
    
    -- 上下文快照（Critical: 冗余存储，避免任务删除后失去上下文）
    `project_name`         varchar(255) NULL DEFAULT NULL COMMENT '项目名称快照（冗余字段，即使任务被删除也能追溯）',
    `language`             varchar(20) NULL DEFAULT NULL COMMENT '代码语言快照（冗余字段，记录当时检测的语言）',
    
    -- 反馈类型（字典：1=误报, 2=推荐修复, 3=补充描述）
    `feedback_type`        varchar(20) NOT NULL COMMENT '反馈类型（false_positive-误报、fix_recommendation-修复建议、supplement-补充描述）',
    
    -- 上下文信息（Critical: 保留完整上下文）
    `file_path`            varchar(500) NULL DEFAULT NULL COMMENT '出问题的文件路径',
    `file_name`            varchar(255) NULL DEFAULT NULL COMMENT '文件名',
    `line_number`          int(11) NULL DEFAULT NULL COMMENT '行号',
    `code_snippet`         text NULL DEFAULT NULL COMMENT '出问题的代码片段（原始代码）',
    `context_code`         text NULL DEFAULT NULL COMMENT '上下文代码',
    
    -- 反馈内容
    `user_comment`         text NULL DEFAULT NULL COMMENT '用户评论',
    `corrected_code`       text NULL DEFAULT NULL COMMENT '修正后的代码（如果是误报，提供正确代码）',
    `fix_suggestion`       text NULL DEFAULT NULL COMMENT '修复建议（如果是修复建议）',
    
    -- 关联现有知识（用于修正现有知识）
    `target_item_uuid`     varchar(64) NULL DEFAULT NULL COMMENT '目标知识条目UUID（如果是对现有知识的修正，关联 knowledge_item.item_uuid）',
    
    -- 状态管理（Critical: 明确的缓冲区状态）
    `status`               varchar(20) NOT NULL DEFAULT 'pending' COMMENT '状态（pending-待处理、approved-已采纳入库、rejected-驳回）',
    `converted_item_uuid`  varchar(64) NULL DEFAULT NULL COMMENT '转换后的知识条目UUID（关联 knowledge_item.item_uuid，status=approved时才有值）',
    `approve_time`         datetime NULL DEFAULT NULL COMMENT '审核通过时间',
    `approve_by`           bigint(20) NULL DEFAULT NULL COMMENT '审核人ID',
    `reject_reason`        varchar(500) NULL DEFAULT NULL COMMENT '驳回原因（status=rejected时填写）',
    
    -- RuoYi 标准字段
    `create_by`            bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
    `create_time`          datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`          datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `del_flag`             char(1) NULL DEFAULT '0' COMMENT '删除标志',
    `tenant_id`            bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID',
    
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_feedback_uuid`(`feedback_uuid`) USING BTREE,
    INDEX `idx_result_uuid`(`result_uuid`) USING BTREE,
    INDEX `idx_task_id`(`task_id`) USING BTREE,
    INDEX `idx_status`(`status`) USING BTREE,
    INDEX `idx_target_item_uuid`(`target_item_uuid`) USING BTREE,
    INDEX `idx_converted_item_uuid`(`converted_item_uuid`) USING BTREE,
    INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '知识反馈表（缓冲区表，实现反馈闭环）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 10. 初始化 RuoYi 字典数据
-- ----------------------------

-- 语言类型字典
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(100, '知识库语言类型', 'knowledge_language', '0', 1, NOW(), '知识库和知识条目使用的编程语言类型')
ON DUPLICATE KEY UPDATE
    `dict_name` = VALUES(`dict_name`),
    `status` = VALUES(`status`),
    `remark` = VALUES(`remark`),
    `update_time` = CURRENT_TIMESTAMP;

INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(1001, 1, 'Java', 'java', 'knowledge_language', '', 'primary', 'N', '0', 1, NOW(), 'Java 语言'),
(1002, 2, 'JavaScript', 'javascript', 'knowledge_language', '', 'primary', 'N', '0', 1, NOW(), 'JavaScript 语言'),
(1003, 3, 'Python', 'python', 'knowledge_language', '', 'primary', 'N', '0', 1, NOW(), 'Python 语言'),
(1004, 4, 'Go', 'go', 'knowledge_language', '', 'primary', 'N', '0', 1, NOW(), 'Go 语言'),
(1005, 5, 'Rust', 'rust', 'knowledge_language', '', 'primary', 'N', '0', 1, NOW(), 'Rust 语言'),
(1006, 6, 'PHP', 'php', 'knowledge_language', '', 'primary', 'N', '0', 1, NOW(), 'PHP 语言'),
(1007, 7, 'C#', 'csharp', 'knowledge_language', '', 'primary', 'N', '0', 1, NOW(), 'C# 语言'),
(1008, 8, 'C++', 'cpp', 'knowledge_language', '', 'primary', 'N', '0', 1, NOW(), 'C++ 语言')
ON DUPLICATE KEY UPDATE
    `dict_sort` = VALUES(`dict_sort`),
    `dict_label` = VALUES(`dict_label`),
    `css_class` = VALUES(`css_class`),
    `list_class` = VALUES(`list_class`),
    `is_default` = VALUES(`is_default`),
    `status` = VALUES(`status`),
    `remark` = VALUES(`remark`),
    `update_time` = CURRENT_TIMESTAMP;

-- 风险等级字典
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(101, '知识库风险等级', 'knowledge_severity', '0', 1, NOW(), '知识条目对应的风险等级（对齐 CVSS 定性严重性等级）')
ON DUPLICATE KEY UPDATE
    `dict_name` = VALUES(`dict_name`),
    `status` = VALUES(`status`),
    `remark` = VALUES(`remark`),
    `update_time` = CURRENT_TIMESTAMP;

INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(1011, 1, '无', 'none', 'knowledge_severity', '', 'default', 'N', '0', 1, NOW(), 'CVSS 无风险 (0.0)'),
(1012, 2, '低', 'low', 'knowledge_severity', '', 'info', 'N', '0', 1, NOW(), 'CVSS 低风险 (0.1 - 3.9)'),
(1013, 3, '中', 'medium', 'knowledge_severity', '', 'warning', 'N', '0', 1, NOW(), 'CVSS 中等风险 (4.0 - 6.9)'),
(1014, 4, '高', 'high', 'knowledge_severity', '', 'danger', 'N', '0', 1, NOW(), 'CVSS 高风险 (7.0 - 8.9)'),
(1015, 5, '严重', 'critical', 'knowledge_severity', '', 'danger', 'N', '0', 1, NOW(), 'CVSS 严重风险 (9.0 - 10.0)')
ON DUPLICATE KEY UPDATE
    `dict_sort` = VALUES(`dict_sort`),
    `dict_label` = VALUES(`dict_label`),
    `css_class` = VALUES(`css_class`),
    `list_class` = VALUES(`list_class`),
    `is_default` = VALUES(`is_default`),
    `status` = VALUES(`status`),
    `remark` = VALUES(`remark`),
    `update_time` = CURRENT_TIMESTAMP;

-- 漏洞类型字典
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(102, '知识库漏洞类型', 'knowledge_vulnerability_type', '0', 1, NOW(), '知识条目对应的漏洞类型（以 CWE 为主键，由标准表 cwe_reference 导入）');

-- 知识条目状态字典
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(103, '知识条目状态', 'knowledge_item_status', '0', 1, NOW(), '知识条目的生命周期状态')
ON DUPLICATE KEY UPDATE
    `dict_name` = VALUES(`dict_name`),
    `status` = VALUES(`status`),
    `remark` = VALUES(`remark`),
    `update_time` = CURRENT_TIMESTAMP;

INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(1031, 1, '草稿', 'draft', 'knowledge_item_status', '', 'info', 'N', '0', 1, NOW(), '草稿状态'),
(1032, 2, '审核中', 'review', 'knowledge_item_status', '', 'warning', 'N', '0', 1, NOW(), '审核中状态'),
(1033, 3, '已发布', 'published', 'knowledge_item_status', '', 'success', 'N', '0', 1, NOW(), '已发布状态'),
(1034, 4, '已归档', 'archived', 'knowledge_item_status', '', 'default', 'N', '0', 1, NOW(), '已归档状态')
ON DUPLICATE KEY UPDATE
    `dict_sort` = VALUES(`dict_sort`),
    `dict_label` = VALUES(`dict_label`),
    `css_class` = VALUES(`css_class`),
    `list_class` = VALUES(`list_class`),
    `is_default` = VALUES(`is_default`),
    `status` = VALUES(`status`),
    `remark` = VALUES(`remark`),
    `update_time` = CURRENT_TIMESTAMP;

-- 反馈类型字典
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(104, '反馈类型', 'feedback_type', '0', 1, NOW(), '用户反馈的类型');

INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(1041, 1, '误报', 'false_positive', 'feedback_type', '', 'warning', 'N', '0', 1, NOW(), '标记为误报'),
(1042, 2, '修复建议', 'fix_recommendation', 'feedback_type', '', 'primary', 'N', '0', 1, NOW(), '提供修复建议'),
(1043, 3, '补充描述', 'supplement', 'feedback_type', '', 'info', 'N', '0', 1, NOW(), '补充描述信息');

-- 知识变更类型字典
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(105, '知识变更类型', 'knowledge_change_type', '0', 1, NOW(), 'knowledge_item_history.change_type 的取值');

INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(1051, 1, '创建', 'create', 'knowledge_change_type', '', 'info', 'N', '0', 1, NOW(), '创建知识条目时产生的版本'),
(1052, 2, '更新', 'update', 'knowledge_change_type', '', 'primary', 'N', '0', 1, NOW(), '编辑知识条目内容产生的版本'),
(1053, 3, '删除', 'delete', 'knowledge_change_type', '', 'danger', 'N', '0', 1, NOW(), '逻辑删除知识条目时产生的版本'),
(1054, 4, '回滚', 'rollback', 'knowledge_change_type', '', 'warning', 'N', '0', 1, NOW(), '从历史版本回滚产生的新版本');

-- 知识标签类型字典
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(106, '知识标签类型', 'knowledge_tag_type', '0', 1, NOW(), 'knowledge_tag.tag_type 的取值');

INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(1061, 1, '系统标签', 'system', 'knowledge_tag_type', '', 'primary', 'N', '0', 1, NOW(), '系统预置的标签'),
(1062, 2, '用户标签', 'user', 'knowledge_tag_type', '', 'info', 'N', '0', 1, NOW(), '用户自定义的标签');

-- 知识来源类型字典
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(107, '知识来源类型', 'knowledge_source_type', '0', 1, NOW(), 'knowledge_item.source_type 的取值');

INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(1071, 1, '手动录入', 'manual', 'knowledge_source_type', '', 'info', 'N', '0', 1, NOW(), '人工在后台手动录入'),
(1072, 2, '用户反馈', 'feedback', 'knowledge_source_type', '', 'primary', 'N', '0', 1, NOW(), '由用户反馈转换而来'),
(1073, 3, '批量导入', 'import', 'knowledge_source_type', '', 'warning', 'N', '0', 1, NOW(), '通过 Excel/CSV 等批量导入'),
(1074, 4, '迁移数据', 'migrated', 'knowledge_source_type', '', 'default', 'N', '0', 1, NOW(), '由历史数据迁移脚本自动生成');

-- 检测结果状态字典
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(108, '检测结果状态', 'detection_result_status', '0', 1, NOW(), 'detection_result.status 的取值');

INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(1081, 1, '已检测', 'detected', 'detection_result_status', '', 'info', 'N', '0', 1, NOW(), '系统新产生的告警，尚未人工处理'),
(1082, 2, '误报', 'false_positive', 'detection_result_status', '', 'warning', 'N', '0', 1, NOW(), '经人工确认认为不是实际问题'),
(1083, 3, '已修复', 'fixed', 'detection_result_status', '', 'success', 'N', '0', 1, NOW(), '问题已修复并通过复测验证'),
(1084, 4, '已忽略', 'ignored', 'detection_result_status', '', 'default', 'N', '0', 1, NOW(), '当前版本/场景下暂不处理');

-- 知识反馈处理状态字典
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(109, '知识反馈状态', 'knowledge_feedback_status', '0', 1, NOW(), 'knowledge_feedback.status 的取值');

INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(1091, 1, '待处理', 'pending', 'knowledge_feedback_status', '', 'info', 'N', '0', 1, NOW(), '等待安全专家/知识管理员审核'),
(1092, 2, '已采纳入库', 'approved', 'knowledge_feedback_status', '', 'success', 'N', '0', 1, NOW(), '反馈内容已转化为知识条目'),
(1093, 3, '已驳回', 'rejected', 'knowledge_feedback_status', '', 'danger', 'N', '0', 1, NOW(), '反馈被认为不合理或重复，被驳回');

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- 11. 扩展 cwe_reference 表：增加全字段中文镜像支持
-- 说明：在原有表结构基础上，增加 raw_json_zh 和 description_zh 字段
--       用于存储完整 CSV 行的中文翻译版本，实现全字段中英双镜像
-- ----------------------------

-- 增加 description_zh 字段（如果不存在）
ALTER TABLE `cwe_reference`
ADD COLUMN `description_zh` text NULL DEFAULT NULL COMMENT '中文描述（通过翻译生成，可人工校正）' AFTER `description_en`;

-- 增加 raw_json_zh 字段（完整 CSV 行的中文 JSON 镜像）
ALTER TABLE `cwe_reference`
ADD COLUMN `raw_json_zh` text NULL DEFAULT NULL COMMENT '原始 CSV 行的中文翻译 JSON（所有字段的中文版本）' AFTER `raw_json`;

-- ----------------------------
-- 12. CVSS 风险度量体系接入（对齐 CVSS v4.0，而非自创枚举）
-- 说明：在业务表中增加 CVSS 向量、分数、版本字段，并创建 CVSS Score 到定性等级的映射表
--       风险等级（knowledge_severity）仅作为 CVSS Base Score 映射后的展示档位
-- ----------------------------

-- 12.1 为 knowledge_item 表添加 CVSS 字段
ALTER TABLE `knowledge_item`
ADD COLUMN `cvss_vector` varchar(255) NULL DEFAULT NULL COMMENT 'CVSS 向量字符串（如 CVSS:4.0/AV:N/AC:L/AT:N/PR:N/UI:N/VC:H/VI:H/VA:H/SC:H/SI:H/SA:H）' AFTER `severity`,
ADD COLUMN `cvss_score` decimal(5,2) NULL DEFAULT NULL COMMENT 'CVSS 数值分数（0.0-10.0）' AFTER `cvss_vector`,
ADD COLUMN `cvss_version` varchar(10) NULL DEFAULT NULL COMMENT 'CVSS 版本号（如 4.0）' AFTER `cvss_score`,
ADD INDEX `idx_cvss_score`(`cvss_score`) USING BTREE,
ADD INDEX `idx_cvss_version`(`cvss_version`) USING BTREE;

-- 12.2 为 detection_result 表添加 CVSS 字段
ALTER TABLE `detection_result`
ADD COLUMN `cvss_vector` varchar(255) NULL DEFAULT NULL COMMENT 'CVSS 向量字符串（如 CVSS:4.0/AV:N/AC:L/AT:N/PR:N/UI:N/VC:H/VI:H/VA:H/SC:H/SI:H/SA:H）' AFTER `severity`,
ADD COLUMN `cvss_score` decimal(5,2) NULL DEFAULT NULL COMMENT 'CVSS 数值分数（0.0-10.0）' AFTER `cvss_vector`,
ADD COLUMN `cvss_version` varchar(10) NULL DEFAULT NULL COMMENT 'CVSS 版本号（如 4.0）' AFTER `cvss_score`,
ADD INDEX `idx_cvss_score`(`cvss_score`) USING BTREE,
ADD INDEX `idx_cvss_version`(`cvss_version`) USING BTREE;

-- 12.3 创建 CVSS 严重性等级映射表（CVSS Score 到定性等级的映射）
DROP TABLE IF EXISTS `cvss_severity_mapping`;
CREATE TABLE `cvss_severity_mapping`
(
    `id`                    bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `severity_level`        varchar(20) NOT NULL COMMENT '定性等级（none/low/medium/high/critical）',
    `score_min`             decimal(5,2) NOT NULL COMMENT '最小分数（包含）',
    `score_max`             decimal(5,2) NOT NULL COMMENT '最大分数（包含）',
    `cvss_version`          varchar(10) NOT NULL DEFAULT '4.0' COMMENT 'CVSS 版本号',
    `description`           varchar(255) NULL DEFAULT NULL COMMENT '等级说明',
    `create_time`           datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`           datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_cvss_severity_version_level`(`cvss_version`, `severity_level`) USING BTREE,
    INDEX `idx_score_range`(`score_min`, `score_max`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'CVSS 严重性等级映射表（CVSS Score 到定性等级的映射，基于 CVSS v4.0）' ROW_FORMAT = DYNAMIC;

-- 12.4 插入 CVSS v4.0 定性等级映射数据（基于 Table 22: Qualitative severity rating scale）
INSERT INTO `cvss_severity_mapping` (`severity_level`, `score_min`, `score_max`, `cvss_version`, `description`)
VALUES
('none', 0.0, 0.0, '4.0', 'CVSS Score = 0.0'),
('low', 0.1, 3.9, '4.0', 'CVSS Score = 0.1 - 3.9'),
('medium', 4.0, 6.9, '4.0', 'CVSS Score = 4.0 - 6.9'),
('high', 7.0, 8.9, '4.0', 'CVSS Score = 7.0 - 8.9'),
('critical', 9.0, 10.0, '4.0', 'CVSS Score = 9.0 - 10.0')
ON DUPLICATE KEY UPDATE
    `score_min` = VALUES(`score_min`),
    `score_max` = VALUES(`score_max`),
    `description` = VALUES(`description`),
    `update_time` = CURRENT_TIMESTAMP;

-- ----------------------------
-- 13. 知识库分类字典（knowledge_category）
-- 说明：知识库用途分类，采用"枚举 + 可扩展"方式管理
--       预设标准分类，允许管理员在字典管理中扩展
-- ----------------------------

-- 13.1 创建字典类型
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(110, '知识库分类', 'knowledge_category', '0', 1, NOW(), '知识库用途分类，管理员可在字典管理中维护')
ON DUPLICATE KEY UPDATE
    `dict_name` = VALUES(`dict_name`),
    `status` = VALUES(`status`),
    `remark` = VALUES(`remark`),
    `update_time` = CURRENT_TIMESTAMP;

-- 13.2 插入预设分类数据
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(1101, 1, '漏洞修复', 'vulnerability_fix', 'knowledge_category', '', 'primary', 'N', '0', 1, NOW(), '漏洞修复相关知识库'),
(1102, 2, '编码规范', 'coding_standard', 'knowledge_category', '', 'success', 'N', '0', 1, NOW(), '编码规范和最佳实践知识库'),
(1103, 3, '安全标准', 'security_standard', 'knowledge_category', '', 'warning', 'N', '0', 1, NOW(), '安全标准和合规要求知识库'),
(1104, 4, '审计案例', 'audit_case', 'knowledge_category', '', 'info', 'N', '0', 1, NOW(), '审计案例和经验总结知识库'),
(1105, 5, '政策法规', 'policy_regulation', 'knowledge_category', '', 'default', 'N', '0', 1, NOW(), '政策法规和行业标准知识库'),
(1106, 6, '工具使用', 'tool_usage', 'knowledge_category', '', 'primary', 'N', '0', 1, NOW(), '工具使用和操作指南知识库')
ON DUPLICATE KEY UPDATE
    `dict_sort` = VALUES(`dict_sort`),
    `dict_label` = VALUES(`dict_label`),
    `css_class` = VALUES(`css_class`),
    `list_class` = VALUES(`list_class`),
    `remark` = VALUES(`remark`),
    `update_time` = CURRENT_TIMESTAMP;
