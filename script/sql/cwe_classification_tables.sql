-- ============================================================
-- CWE 分类相关表（中层：分类层）
-- 说明：存储 CWE 的分类结果，包括业务分类、标准分类、影响分类、层级关系
-- ============================================================

-- ----------------------------
-- 1. 业务分类字典表
-- 说明：定义业务分类的元数据（与 classify_cwe_data.py 中的 BUSINESS_CATEGORIES 对应）
-- ----------------------------
DROP TABLE IF EXISTS `cwe_business_category`;
CREATE TABLE `cwe_business_category`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `category_code` varchar(64) NOT NULL COMMENT '分类代码（如 injection, authentication）',
    `name_zh`       varchar(128) NULL DEFAULT NULL COMMENT '中文名称',
    `name_en`       varchar(128) NULL DEFAULT NULL COMMENT '英文名称',
    `description`   varchar(512) NULL DEFAULT NULL COMMENT '分类描述',
    `keywords`      text NULL DEFAULT NULL COMMENT '关键词列表（JSON数组格式）',
    `sort_order`   int(11) NULL DEFAULT 0 COMMENT '排序顺序',
    `create_time`   datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`     bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID（预留，通常为 0）',
    
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_cwe_business_category_code`(`category_code`) USING BTREE,
    INDEX `idx_cwe_business_category_sort`(`sort_order`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'CWE 业务分类字典表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 2. CWE 业务分类映射表
-- 说明：CWE 与业务分类的多对多关系（一个 CWE 可以属于多个业务分类）
-- ----------------------------
DROP TABLE IF EXISTS `cwe_business_category_mapping`;
CREATE TABLE `cwe_business_category_mapping`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `cwe_id`        varchar(20) NOT NULL COMMENT 'CWE 编号（关联 cwe_reference.cwe_id）',
    `category_code` varchar(64) NOT NULL COMMENT '业务分类代码（关联 cwe_business_category.category_code）',
    `classification_method` varchar(32) NULL DEFAULT NULL COMMENT '分类方法（llm/keywords/rule）',
    `confidence`   decimal(5,2) NULL DEFAULT NULL COMMENT '置信度（0.00-1.00，LLM分类时可能提供）',
    `create_time`   datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`     bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID（预留，通常为 0）',
    
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_cwe_business_category_mapping`(`cwe_id`, `category_code`) USING BTREE,
    INDEX `idx_cwe_business_category_mapping_cwe`(`cwe_id`) USING BTREE,
    INDEX `idx_cwe_business_category_mapping_category`(`category_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'CWE 业务分类映射表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 3. 标准分类类型字典表
-- 说明：定义标准分类的类型（如 OWASP Top 10, CWE Top 25 等）
-- ----------------------------
DROP TABLE IF EXISTS `cwe_standard_type`;
CREATE TABLE `cwe_standard_type`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `type_code`     varchar(64) NOT NULL COMMENT '标准类型代码（如 owasp_top10, cwe_top25）',
    `type_name`     varchar(128) NULL DEFAULT NULL COMMENT '标准类型名称（如 OWASP Top 10）',
    `description`   varchar(512) NULL DEFAULT NULL COMMENT '标准类型描述',
    `version`       varchar(32) NULL DEFAULT NULL COMMENT '标准版本（如 2021, 2023）',
    `sort_order`   int(11) NULL DEFAULT 0 COMMENT '排序顺序',
    `create_time`   datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`     bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID（预留，通常为 0）',
    
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_cwe_standard_type_code`(`type_code`) USING BTREE,
    INDEX `idx_cwe_standard_type_sort`(`sort_order`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'CWE 标准分类类型字典表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 4. CWE 标准分类映射表
-- 说明：CWE 与标准分类的映射关系（从 Taxonomy Mappings 解析）
-- ----------------------------
DROP TABLE IF EXISTS `cwe_standard_mapping`;
CREATE TABLE `cwe_standard_mapping`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `cwe_id`        varchar(20) NOT NULL COMMENT 'CWE 编号（关联 cwe_reference.cwe_id）',
    `type_code`     varchar(64) NOT NULL COMMENT '标准类型代码（关联 cwe_standard_type.type_code）',
    `entry_code`    varchar(64) NULL DEFAULT NULL COMMENT '标准条目代码（如 A01, A02）',
    `entry_name`    varchar(256) NULL DEFAULT NULL COMMENT '标准条目名称',
    `create_time`   datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`     bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID（预留，通常为 0）',
    
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_cwe_standard_mapping_cwe`(`cwe_id`) USING BTREE,
    INDEX `idx_cwe_standard_mapping_type`(`type_code`) USING BTREE,
    INDEX `idx_cwe_standard_mapping_entry`(`entry_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'CWE 标准分类映射表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 5. CWE 影响类型映射表
-- 说明：CWE 与影响类型的映射关系（从 Common Consequences 解析，CIA三元组）
-- ----------------------------
DROP TABLE IF EXISTS `cwe_impact_mapping`;
CREATE TABLE `cwe_impact_mapping`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `cwe_id`        varchar(20) NOT NULL COMMENT 'CWE 编号（关联 cwe_reference.cwe_id）',
    `impact_type`  varchar(32) NOT NULL COMMENT '影响类型（confidentiality/integrity/availability）',
    `create_time`   datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`     bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID（预留，通常为 0）',
    
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_cwe_impact_mapping`(`cwe_id`, `impact_type`) USING BTREE,
    INDEX `idx_cwe_impact_mapping_cwe`(`cwe_id`) USING BTREE,
    INDEX `idx_cwe_impact_mapping_type`(`impact_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'CWE 影响类型映射表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 6. CWE 层级关系表
-- 说明：CWE 之间的层级关系（从 Related Weaknesses 解析）
-- ----------------------------
DROP TABLE IF EXISTS `cwe_hierarchy`;
CREATE TABLE `cwe_hierarchy`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `cwe_id`        varchar(20) NOT NULL COMMENT 'CWE 编号（子节点，关联 cwe_reference.cwe_id）',
    `parent_cwe_id` varchar(20) NOT NULL COMMENT '父 CWE 编号（关联 cwe_reference.cwe_id）',
    `relationship_type` varchar(32) NULL DEFAULT NULL COMMENT '关系类型（ChildOf/CanPrecede/CanFollow等）',
    `create_time`   datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`     bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID（预留，通常为 0）',
    
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_cwe_hierarchy_cwe`(`cwe_id`) USING BTREE,
    INDEX `idx_cwe_hierarchy_parent`(`parent_cwe_id`) USING BTREE,
    INDEX `idx_cwe_hierarchy_relationship`(`relationship_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'CWE 层级关系表' ROW_FORMAT = DYNAMIC;
