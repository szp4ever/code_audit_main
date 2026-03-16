-- 修复 SiliconFlow 向量模型 API 主机配置
-- LangChain4j 的 OpenAiEmbeddingModel 会自动追加 /embeddings 路径，
-- 所以 api_host 不应该包含 /embeddings 后缀

UPDATE chat_model
SET api_host = 'https://api.siliconflow.cn/v1'
WHERE id = 2007746350454992898;

-- ============================================
-- 添加知识库文档数量字段
-- ============================================

-- 1. 在 knowledge_info 表添加 attach_count 字段（文档/附件数量）
ALTER TABLE knowledge_info
ADD COLUMN IF NOT EXISTS attach_count INT DEFAULT 0 COMMENT '文档数量（附件数）' AFTER fragment_count;

-- 2. 初始化现有知识库的文档数量
UPDATE knowledge_info ki
SET attach_count = (
    SELECT COUNT(*)
    FROM knowledge_attach ka
    WHERE ka.kid = ki.kid
    AND ka.del_flag = '0'
)
WHERE EXISTS (
    SELECT 1 FROM knowledge_attach ka2 WHERE ka2.kid = ki.kid
);
