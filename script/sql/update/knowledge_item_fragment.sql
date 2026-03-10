-- ============================================================
-- 知识条目-片段 N:M 关联表
-- 替代原来 knowledge_fragment.item_uuid 的 1:1 直接绑定
-- ============================================================

CREATE TABLE IF NOT EXISTS `knowledge_item_fragment` (
    `id`              BIGINT(20)    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `item_uuid`       VARCHAR(50)   NOT NULL                COMMENT '知识条目 UUID',
    `fragment_id`     BIGINT(20)    NOT NULL                COMMENT '片段 ID',
    `relevance_score` DOUBLE        DEFAULT NULL            COMMENT '关联相关度评分（0-1，AI 匹配时填写）',
    `created_by`      VARCHAR(20)   NOT NULL DEFAULT 'manual' COMMENT '关联来源：manual（人工）/ ai（AI 建议）',
    `create_time`     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_item_fragment` (`item_uuid`, `fragment_id`),
    KEY `idx_item_uuid` (`item_uuid`),
    KEY `idx_fragment_id` (`fragment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='知识条目-片段关联表';

-- ============================================================
-- 数据迁移：从 knowledge_fragment.item_uuid 迁移到关联表
-- 注意：执行前请先备份 knowledge_fragment 表
-- ============================================================

INSERT INTO `knowledge_item_fragment` (`item_uuid`, `fragment_id`, `relevance_score`, `created_by`, `create_time`)
SELECT
    kf.item_uuid,
    kf.id,
    NULL,
    'manual',
    COALESCE(kf.create_time, NOW())
FROM `knowledge_fragment` kf
WHERE kf.item_uuid IS NOT NULL
  AND kf.item_uuid != ''
ON DUPLICATE KEY UPDATE `id` = `id`;

-- ============================================================
-- 可选：迁移完成并验证后，删除 knowledge_fragment.item_uuid 列
-- ALTER TABLE `knowledge_fragment` DROP COLUMN `item_uuid`;
-- ============================================================
