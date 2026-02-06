-- ============================================================
-- CWE 聚类表编码修复脚本
-- 说明：修复 cwe_cluster 表中的中文乱码问题
-- ============================================================

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 注意：此脚本假设数据在导入时使用了错误的编码（Latin-1 被当作 UTF-8）
-- 如果数据已经以错误的编码存储，需要先备份数据，然后尝试修复

-- 方法1: 如果数据是以 Latin-1 编码存储的 UTF-8 字节
-- 需要将 Latin-1 字节重新解释为 UTF-8
-- 注意：这种方法可能不适用于所有情况，请先备份数据

-- 查看当前数据（用于诊断）
-- SELECT cluster_id, cluster_name_zh, cluster_name_en FROM cwe_cluster LIMIT 10;

-- 如果数据已经错误存储，可能需要：
-- 1. 导出数据为正确的编码
-- 2. 重新导入

-- 建议的修复步骤：
-- 1. 备份表
-- CREATE TABLE cwe_cluster_backup AS SELECT * FROM cwe_cluster;
-- CREATE TABLE cwe_cluster_mapping_backup AS SELECT * FROM cwe_cluster_mapping;

-- 2. 如果数据在导入时编码错误，需要：
--    - 重新运行 import_cwe_clustering.py 生成新的 SQL（已修复编码问题）
--    - 清空表
--    TRUNCATE TABLE cwe_cluster_mapping;
--    TRUNCATE TABLE cwe_cluster;
--    - 重新导入（使用修复后的 SQL 文件，包含 SET NAMES utf8mb4）

-- 3. 如果数据已经在数据库中但编码错误，可能需要使用 Python 脚本修复：
--    见 fix_cwe_cluster_encoding.py

-- ============================================================
-- 验证修复结果
-- ============================================================
-- SELECT 
--     cluster_id,
--     cluster_name_zh,
--     cluster_name_en,
--     LENGTH(cluster_name_zh) as name_zh_length,
--     CHAR_LENGTH(cluster_name_zh) as name_zh_char_length
-- FROM cwe_cluster 
-- WHERE cluster_name_zh IS NOT NULL
-- LIMIT 10;
