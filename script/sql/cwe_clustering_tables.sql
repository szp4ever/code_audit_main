-- ============================================================
-- CWE 聚类相关表（上层：聚类层）
-- 说明：存储 CWE 的聚类结果，包括聚类信息和LLM解释
-- ============================================================

-- ----------------------------
-- 1. CWE 聚类主表
-- 说明：存储聚类的基本信息和LLM解释结果
-- ----------------------------
DROP TABLE IF EXISTS `cwe_cluster`;
CREATE TABLE `cwe_cluster`
(
    `id`                bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `cluster_id`        int(11) NOT NULL COMMENT '聚类ID（算法生成的编号）',
    `cluster_method`    varchar(32) NOT NULL COMMENT '聚类方法（kmeans/hierarchical/dbscan）',
    `cluster_name_zh`   varchar(128) NULL DEFAULT NULL COMMENT '聚类中文名称（LLM生成）',
    `cluster_name_en`   varchar(128) NULL DEFAULT NULL COMMENT '聚类英文名称（LLM生成）',
    `category_code`     varchar(64) NULL DEFAULT NULL COMMENT '分类代码（LLM生成，如 injection）',
    `description`       text NULL DEFAULT NULL COMMENT '聚类描述（LLM生成）',
    `keywords`          text NULL DEFAULT NULL COMMENT '关键词列表（JSON数组格式，LLM生成）',
    `cwe_count`         int(11) NULL DEFAULT 0 COMMENT '包含的 CWE 数量',
    `silhouette_score`  decimal(10,6) NULL DEFAULT NULL COMMENT '轮廓系数（聚类质量指标）',
    `calinski_harabasz_score` decimal(10,2) NULL DEFAULT NULL COMMENT 'Calinski-Harabasz 分数（聚类质量指标）',
    `llm_interpretation` text NULL DEFAULT NULL COMMENT 'LLM 完整解释结果（JSON格式）',
    `create_time`       datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`         bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID（预留，通常为 0）',
    
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_cwe_cluster_id_method`(`cluster_id`, `cluster_method`) USING BTREE,
    INDEX `idx_cwe_cluster_method`(`cluster_method`) USING BTREE,
    INDEX `idx_cwe_cluster_category`(`category_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'CWE 聚类主表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 2. CWE 聚类映射表
-- 说明：CWE 与聚类的映射关系（一个 CWE 只能属于一个聚类）
-- ----------------------------
DROP TABLE IF EXISTS `cwe_cluster_mapping`;
CREATE TABLE `cwe_cluster_mapping`
(
    `id`            bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `cwe_id`        varchar(20) NOT NULL COMMENT 'CWE 编号（关联 cwe_reference.cwe_id）',
    `cluster_id`    int(11) NOT NULL COMMENT '聚类ID（关联 cwe_cluster.cluster_id）',
    `cluster_method` varchar(32) NOT NULL COMMENT '聚类方法（关联 cwe_cluster.cluster_method）',
    `distance_to_center` decimal(10,6) NULL DEFAULT NULL COMMENT '到聚类中心的距离（可选）',
    `create_time`   datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`     bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID（预留，通常为 0）',
    
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_cwe_cluster_mapping`(`cwe_id`, `cluster_method`) USING BTREE,
    INDEX `idx_cwe_cluster_mapping_cwe`(`cwe_id`) USING BTREE,
    INDEX `idx_cwe_cluster_mapping_cluster`(`cluster_id`, `cluster_method`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'CWE 聚类映射表' ROW_FORMAT = DYNAMIC;
