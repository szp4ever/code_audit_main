-- ============================================================
-- CWE 聚类结果导入 SQL
-- 生成时间: 2026-01-18 08:35:46
-- 聚类方法: kmeans
-- 聚类数量: 12
-- ============================================================

-- 设置字符集（重要：确保中文正确导入）
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

-- 注意：导入前请确保已创建表结构（见 cwe_clustering_tables.sql）
-- 如果表中已有数据，建议先清空：
-- TRUNCATE TABLE `cwe_cluster_mapping`;
-- TRUNCATE TABLE `cwe_cluster`;

-- ============================================================
-- 1. 插入聚类主表数据
-- ============================================================

INSERT INTO `cwe_cluster` (
    `cluster_id`,
    `cluster_method`,
    `cluster_name_zh`,
    `cluster_name_en`,
    `category_code`,
    `description`,
    `keywords`,
    `cwe_count`,
    `silhouette_score`,
    `calinski_harabasz_score`,
    `llm_interpretation`,
    `tenant_id`
) VALUES (
    0,
    'kmeans',
    '并发与同步类',
    'Concurrency & Synchronization',
    'concurrency_synchronization',
    '涉及资源同步、锁定机制和竞争条件的漏洞，可能导致未授权访问、数据竞争或死锁。',
    '["concurrency", "synchronization", "race condition", "locking", "toctou"]',
    77,
    0.05094237055069627,
    17.637875642224365,
    '{"category_code": "concurrency_synchronization", "name_zh": "并发与同步类", "name_en": "Concurrency & Synchronization", "description": "涉及资源同步、锁定机制和竞争条件的漏洞，可能导致未授权访问、数据竞争或死锁。", "keywords": ["concurrency", "synchronization", "race condition", "locking", "toctou"]}',
    0
);

INSERT INTO `cwe_cluster` (
    `cluster_id`,
    `cluster_method`,
    `cluster_name_zh`,
    `cluster_name_en`,
    `category_code`,
    `description`,
    `keywords`,
    `cwe_count`,
    `silhouette_score`,
    `calinski_harabasz_score`,
    `llm_interpretation`,
    `tenant_id`
) VALUES (
    1,
    'kmeans',
    '输入解析类',
    'Input Parsing',
    'input_parsing',
    '与文件名、路径、编码和特殊字符解析相关的漏洞，可能导致路径遍历、编码混淆或注入攻击。',
    '["input parsing", "path resolution", "file name", "encoding", "special characters"]',
    39,
    0.05094237055069627,
    17.637875642224365,
    '{"category_code": "input_parsing", "name_zh": "输入解析类", "name_en": "Input Parsing", "description": "与文件名、路径、编码和特殊字符解析相关的漏洞，可能导致路径遍历、编码混淆或注入攻击。", "keywords": ["input parsing", "path resolution", "file name", "encoding", "special characters"]}',
    0
);

INSERT INTO `cwe_cluster` (
    `cluster_id`,
    `cluster_method`,
    `cluster_name_zh`,
    `cluster_name_en`,
    `category_code`,
    `description`,
    `keywords`,
    `cwe_count`,
    `silhouette_score`,
    `calinski_harabasz_score`,
    `llm_interpretation`,
    `tenant_id`
) VALUES (
    2,
    'kmeans',
    '信息泄露类',
    'Information Leak',
    'information_leak',
    '敏感信息在未授权情况下被暴露或传输',
    '["information leak", "sensitive data", "data exposure", "security breach", "data leakage"]',
    85,
    0.05094237055069627,
    17.637875642224365,
    '{"category_code": "information_leak", "name_zh": "信息泄露类", "name_en": "Information Leak", "description": "敏感信息在未授权情况下被暴露或传输", "keywords": ["information leak", "sensitive data", "data exposure", "security breach", "data leakage"]}',
    0
);

INSERT INTO `cwe_cluster` (
    `cluster_id`,
    `cluster_method`,
    `cluster_name_zh`,
    `cluster_name_en`,
    `category_code`,
    `description`,
    `keywords`,
    `cwe_count`,
    `silhouette_score`,
    `calinski_harabasz_score`,
    `llm_interpretation`,
    `tenant_id`
) VALUES (
    3,
    'kmeans',
    '路径遍历类',
    'Path Traversal',
    'path_traversal',
    '通过特殊字符访问受限目录或文件的漏洞',
    '["path traversal", "directory traversal", "file access", "relative path", "../"]',
    45,
    0.05094237055069627,
    17.637875642224365,
    '{"category_code": "path_traversal", "name_zh": "路径遍历类", "name_en": "Path Traversal", "description": "通过特殊字符访问受限目录或文件的漏洞", "keywords": ["path traversal", "directory traversal", "file access", "relative path", "../"]}',
    0
);

INSERT INTO `cwe_cluster` (
    `cluster_id`,
    `cluster_method`,
    `cluster_name_zh`,
    `cluster_name_en`,
    `category_code`,
    `description`,
    `keywords`,
    `cwe_count`,
    `silhouette_score`,
    `calinski_harabasz_score`,
    `llm_interpretation`,
    `tenant_id`
) VALUES (
    4,
    'kmeans',
    '输入处理不当',
    'Improper Input Handling',
    'improper_input_handling',
    '由于对用户输入缺乏有效验证或过滤，导致安全漏洞的出现',
    '["input validation", "sanitization", "input filtering", "disallowed inputs", "allowed inputs"]',
    146,
    0.05094237055069627,
    17.637875642224365,
    '{"category_code": "improper_input_handling", "name_zh": "输入处理不当", "name_en": "Improper Input Handling", "description": "由于对用户输入缺乏有效验证或过滤，导致安全漏洞的出现", "keywords": ["input validation", "sanitization", "input filtering", "disallowed inputs", "allowed inputs"]}',
    0
);

INSERT INTO `cwe_cluster` (
    `cluster_id`,
    `cluster_method`,
    `cluster_name_zh`,
    `cluster_name_en`,
    `category_code`,
    `description`,
    `keywords`,
    `cwe_count`,
    `silhouette_score`,
    `calinski_harabasz_score`,
    `llm_interpretation`,
    `tenant_id`
) VALUES (
    5,
    'kmeans',
    '密码管理类',
    'Password Management',
    'password_management',
    '与密码存储、配置和使用相关的安全问题',
    '["password", "configuration", "storage", "hardcoded", "encoding"]',
    70,
    0.05094237055069627,
    17.637875642224365,
    '{"category_code": "password_management", "name_zh": "密码管理类", "name_en": "Password Management", "description": "与密码存储、配置和使用相关的安全问题", "keywords": ["password", "configuration", "storage", "hardcoded", "encoding"]}',
    0
);

INSERT INTO `cwe_cluster` (
    `cluster_id`,
    `cluster_method`,
    `cluster_name_zh`,
    `cluster_name_en`,
    `category_code`,
    `description`,
    `keywords`,
    `cwe_count`,
    `silhouette_score`,
    `calinski_harabasz_score`,
    `llm_interpretation`,
    `tenant_id`
) VALUES (
    6,
    'kmeans',
    '输入验证类',
    'Input Validation',
    'input_validation',
    '由于输入验证不当导致的安全漏洞，包括未正确验证或处理用户输入数据。',
    '["input validation", "validation", "input sanitization", "data validation", "misinterpretation"]',
    156,
    0.05094237055069627,
    17.637875642224365,
    '{"category_code": "input_validation", "name_zh": "输入验证类", "name_en": "Input Validation", "description": "由于输入验证不当导致的安全漏洞，包括未正确验证或处理用户输入数据。", "keywords": ["input validation", "validation", "input sanitization", "data validation", "misinterpretation"]}',
    0
);

INSERT INTO `cwe_cluster` (
    `cluster_id`,
    `cluster_method`,
    `cluster_name_zh`,
    `cluster_name_en`,
    `category_code`,
    `description`,
    `keywords`,
    `cwe_count`,
    `silhouette_score`,
    `calinski_harabasz_score`,
    `llm_interpretation`,
    `tenant_id`
) VALUES (
    7,
    'kmeans',
    '异常处理类',
    'Exception Management',
    'exception_management',
    '与异常处理机制相关的漏洞，包括未捕获的异常、通用异常声明等',
    '["exception", "uncaught", "generic exception", "error handling", "exception declaration"]',
    78,
    0.05094237055069627,
    17.637875642224365,
    '{"category_code": "exception_management", "name_zh": "异常处理类", "name_en": "Exception Management", "description": "与异常处理机制相关的漏洞，包括未捕获的异常、通用异常声明等", "keywords": ["exception", "uncaught", "generic exception", "error handling", "exception declaration"]}',
    0
);

INSERT INTO `cwe_cluster` (
    `cluster_id`,
    `cluster_method`,
    `cluster_name_zh`,
    `cluster_name_en`,
    `category_code`,
    `description`,
    `keywords`,
    `cwe_count`,
    `silhouette_score`,
    `calinski_harabasz_score`,
    `llm_interpretation`,
    `tenant_id`
) VALUES (
    8,
    'kmeans',
    '配置类',
    'Misconfiguration',
    'misconfiguration',
    '由于系统或应用配置不当导致的安全漏洞',
    '["misconfiguration", "configuration", "security settings", "default settings", "error handling"]',
    61,
    0.05094237055069627,
    17.637875642224365,
    '{"category_code": "misconfiguration", "name_zh": "配置类", "name_en": "Misconfiguration", "description": "由于系统或应用配置不当导致的安全漏洞", "keywords": ["misconfiguration", "configuration", "security settings", "default settings", "error handling"]}',
    0
);

INSERT INTO `cwe_cluster` (
    `cluster_id`,
    `cluster_method`,
    `cluster_name_zh`,
    `cluster_name_en`,
    `category_code`,
    `description`,
    `keywords`,
    `cwe_count`,
    `silhouette_score`,
    `calinski_harabasz_score`,
    `llm_interpretation`,
    `tenant_id`
) VALUES (
    9,
    'kmeans',
    '缓冲区溢出类',
    'Buffer Overflow',
    'buffer_overflow',
    '由于对内存缓冲区操作不当导致的越界访问或覆盖，可能引发系统崩溃或代码执行漏洞',
    '["buffer overflow", "stack overflow", "heap overflow", "out-of-bounds", "memory corruption"]',
    109,
    0.05094237055069627,
    17.637875642224365,
    '{"category_code": "buffer_overflow", "name_zh": "缓冲区溢出类", "name_en": "Buffer Overflow", "description": "由于对内存缓冲区操作不当导致的越界访问或覆盖，可能引发系统崩溃或代码执行漏洞", "keywords": ["buffer overflow", "stack overflow", "heap overflow", "out-of-bounds", "memory corruption"]}',
    0
);

INSERT INTO `cwe_cluster` (
    `cluster_id`,
    `cluster_method`,
    `cluster_name_zh`,
    `cluster_name_en`,
    `category_code`,
    `description`,
    `keywords`,
    `cwe_count`,
    `silhouette_score`,
    `calinski_harabasz_score`,
    `llm_interpretation`,
    `tenant_id`
) VALUES (
    10,
    'kmeans',
    '注入类',
    'Injection',
    'injection',
    '由于未正确转义或过滤特殊字符，导致攻击者可以将恶意内容注入到系统中并被执行',
    '["injection", "command injection", "xss", "sql injection", "script injection"]',
    63,
    0.05094237055069627,
    17.637875642224365,
    '{"category_code": "injection", "name_zh": "注入类", "name_en": "Injection", "description": "由于未正确转义或过滤特殊字符，导致攻击者可以将恶意内容注入到系统中并被执行", "keywords": ["injection", "command injection", "xss", "sql injection", "script injection"]}',
    0
);

INSERT INTO `cwe_cluster` (
    `cluster_id`,
    `cluster_method`,
    `cluster_name_zh`,
    `cluster_name_en`,
    `category_code`,
    `description`,
    `keywords`,
    `cwe_count`,
    `silhouette_score`,
    `calinski_harabasz_score`,
    `llm_interpretation`,
    `tenant_id`
) VALUES (
    11,
    'kmeans',
    '弱随机性类',
    'Weak Randomness',
    'weak_randomness',
    '与伪随机数生成器或熵不足相关的漏洞，导致可预测的值或标识符。',
    '["randomness", "prng", "entropy", "predictable", "seed"]',
    15,
    0.05094237055069627,
    17.637875642224365,
    '{"category_code": "weak_randomness", "name_zh": "弱随机性类", "name_en": "Weak Randomness", "description": "与伪随机数生成器或熵不足相关的漏洞，导致可预测的值或标识符。", "keywords": ["randomness", "prng", "entropy", "predictable", "seed"]}',
    0
);


-- ============================================================
-- 2. 插入聚类映射表数据
-- ============================================================

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '59',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '99',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '118',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '271',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '282',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '283',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '362',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '363',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '366',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '367',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '400',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '401',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '402',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '404',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '405',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '410',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '413',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '414',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '459',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '511',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '543',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '558',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '567',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '585',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '609',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '610',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '638',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '641',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '662',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '664',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '665',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '666',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '667',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '668',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '669',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '672',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '674',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '675',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '689',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '694',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '706',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '708',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '732',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '762',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '763',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '764',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '765',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '770',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '771',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '772',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '773',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '775',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '820',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '821',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '826',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '832',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '833',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '835',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '843',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '908',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '909',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '911',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '913',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '920',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1050',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1051',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1065',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1067',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1072',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1088',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1096',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1188',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1229',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1269',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1325',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1341',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1419',
    0,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '66',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '67',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '72',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '130',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '157',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '159',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '166',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '167',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '168',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '173',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '175',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '176',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '177',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '178',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '228',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '229',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '230',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '231',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '232',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '233',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '235',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '236',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '237',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '238',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '239',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '240',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '241',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '274',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '280',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '409',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '430',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '431',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '544',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '703',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '755',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1118',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1246',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1261',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1384',
    1,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '200',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '201',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '202',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '209',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '210',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '211',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '212',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '213',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '214',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '215',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '219',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '220',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '222',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '223',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '224',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '226',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '244',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '311',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '312',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '313',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '314',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '315',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '316',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '317',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '318',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '319',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '349',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '359',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '377',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '385',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '403',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '432',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '463',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '464',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '488',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '492',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '497',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '498',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '499',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '512',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '514',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '515',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '524',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '525',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '526',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '527',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '528',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '529',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '530',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '531',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '532',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '538',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '539',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '540',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '541',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '548',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '550',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '553',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '591',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '598',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '612',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '614',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '615',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '642',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '651',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '749',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '766',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '921',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '922',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '927',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1004',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1230',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1243',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1258',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1266',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1272',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1275',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1297',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1323',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1330',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1342',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1420',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1421',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1422',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1423',
    2,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '22',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '23',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '24',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '25',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '26',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '27',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '28',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '29',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '30',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '31',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '32',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '33',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '34',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '35',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '36',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '37',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '38',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '39',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '40',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '41',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '42',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '43',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '44',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '45',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '46',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '47',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '48',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '49',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '50',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '51',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '52',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '53',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '54',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '55',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '56',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '57',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '58',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '61',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '62',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '64',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '65',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '73',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '426',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '427',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '428',
    3,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '15',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '69',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '75',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '98',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '114',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '183',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '184',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '242',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '243',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '250',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '266',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '267',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '268',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '269',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '270',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '272',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '276',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '277',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '278',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '281',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '284',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '300',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '307',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '350',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '356',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '357',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '360',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '368',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '378',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '379',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '391',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '406',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '412',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '419',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '420',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '421',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '422',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '424',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '434',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '446',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '447',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '450',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '451',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '455',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '470',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '471',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '520',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '552',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '583',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '617',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '623',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '636',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '645',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '646',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '647',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '648',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '653',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '654',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '655',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '671',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '673',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '676',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '692',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '693',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '695',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '733',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '774',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '782',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '807',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '827',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '828',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '829',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '830',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '912',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '923',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '939',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '942',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1037',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1038',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1177',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1189',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1190',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1191',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1192',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1193',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1209',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1220',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1222',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1223',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1224',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1231',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1232',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1233',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1234',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1239',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1242',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1244',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1245',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1247',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1248',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1252',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1253',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1255',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1256',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1257',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1259',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1260',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1262',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1263',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1264',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1268',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1270',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1271',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1274',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1276',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1278',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1280',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1290',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1292',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1294',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1298',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1299',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1300',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1302',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1303',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1304',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1310',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1311',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1312',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1313',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1314',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1315',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1316',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1317',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1318',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1319',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1320',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1326',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1328',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1331',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1332',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1334',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1338',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1351',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1386',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1429',
    4,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '13',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '174',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '256',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '257',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '258',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '259',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '260',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '261',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '262',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '263',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '288',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '289',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '290',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '291',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '294',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '301',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '302',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '304',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '305',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '306',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '308',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '309',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '321',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '322',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '323',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '324',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '325',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '326',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '327',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '328',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '348',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '384',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '510',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '521',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '522',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '523',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '547',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '549',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '565',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '566',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '593',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '599',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '602',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '603',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '620',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '639',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '640',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '649',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '656',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '757',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '759',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '760',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '780',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '784',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '798',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '836',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '916',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1204',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1240',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1267',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1273',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1279',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1291',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1390',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1391',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1392',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1393',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1394',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1428',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1431',
    5,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '20',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '112',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '115',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '116',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '129',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '170',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '172',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '179',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '180',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '181',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '182',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '185',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '188',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '193',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '195',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '196',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '198',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '203',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '204',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '205',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '206',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '207',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '208',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '221',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '252',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '253',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '273',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '279',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '285',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '286',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '287',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '295',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '296',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '297',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '298',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '299',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '303',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '330',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '331',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '345',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '346',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '347',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '351',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '353',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '354',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '358',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '370',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '372',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '390',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '392',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '393',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '394',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '407',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '408',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '435',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '436',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '437',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '440',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '441',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '448',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '449',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '453',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '456',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '460',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '480',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '501',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '502',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '573',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '581',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '589',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '606',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '611',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '622',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '625',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '626',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '628',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '637',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '657',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '670',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '682',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '683',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '684',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '685',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '686',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '687',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '688',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '696',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '697',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '704',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '705',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '710',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '754',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '756',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '758',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '768',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '778',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '779',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '781',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '783',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '799',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '837',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '838',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '841',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '842',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '862',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '863',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '914',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '915',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '924',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '925',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '940',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '941',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1007',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1023',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1024',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1039',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1053',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1059',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1060',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1061',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1066',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1068',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1076',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1100',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1103',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1104',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1110',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1111',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1112',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1113',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1173',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1176',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1221',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1249',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1250',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1251',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1254',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1277',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1281',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1284',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1285',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1286',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1287',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1288',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1289',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1293',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1295',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1296',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1301',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1321',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1329',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1339',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1357',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1395',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1426',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1434',
    6,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '94',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '186',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '248',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '396',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '397',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '457',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '474',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '477',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '478',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '483',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '484',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '489',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '494',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '506',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '508',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '509',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '546',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '561',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '584',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '624',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '691',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '776',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '834',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1025',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1041',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1043',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1044',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1047',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1048',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1049',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1054',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1055',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1056',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1058',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1062',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1063',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1064',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1069',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1071',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1073',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1074',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1075',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1078',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1080',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1082',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1084',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1085',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1086',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1089',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1092',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1093',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1094',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1099',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1101',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1102',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1105',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1106',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1107',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1108',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1109',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1114',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1115',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1116',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1117',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1119',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1120',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1121',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1122',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1123',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1124',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1125',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1126',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1127',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1164',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1235',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1265',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1322',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1333',
    7,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '5',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '6',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '7',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '8',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '9',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '11',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '12',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '85',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '91',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '102',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '103',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '104',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '105',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '106',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '107',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '108',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '109',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '110',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '111',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '245',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '246',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '293',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '352',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '382',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '383',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '425',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '433',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '444',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '472',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '473',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '487',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '507',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '535',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '536',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '537',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '551',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '554',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '555',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '556',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '564',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '574',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '575',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '576',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '577',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '578',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '579',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '594',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '600',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '601',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '605',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '608',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '613',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '618',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '650',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '698',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '918',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '926',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1021',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1022',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1174',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1385',
    8,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '14',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '119',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '120',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '121',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '122',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '123',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '124',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '125',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '126',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '127',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '128',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '131',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '134',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '135',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '187',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '190',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '191',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '192',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '194',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '197',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '234',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '344',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '364',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '369',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '374',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '375',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '386',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '395',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '415',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '416',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '439',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '454',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '462',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '466',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '467',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '468',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '469',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '475',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '476',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '479',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '481',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '482',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '486',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '491',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '493',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '495',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '496',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '500',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '560',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '562',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '563',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '568',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '570',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '571',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '572',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '580',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '582',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '586',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '587',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '588',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '590',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '595',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '597',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '607',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '616',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '619',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '621',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '627',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '663',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '680',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '681',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '690',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '761',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '767',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '777',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '785',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '786',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '787',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '788',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '789',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '805',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '806',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '822',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '823',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '824',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '825',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '831',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '839',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '910',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1042',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1045',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1046',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1052',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1057',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1070',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1077',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1079',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1083',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1087',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1090',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1091',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1095',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1097',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1098',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1282',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1283',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1327',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1335',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1389',
    9,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '74',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '76',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '77',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '78',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '79',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '80',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '81',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '82',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '83',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '84',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '86',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '87',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '88',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '89',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '90',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '93',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '95',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '96',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '97',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '113',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '117',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '138',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '140',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '141',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '142',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '143',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '144',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '145',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '146',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '147',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '148',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '149',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '150',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '151',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '152',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '153',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '154',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '155',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '156',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '158',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '160',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '161',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '162',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '163',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '164',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '165',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '643',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '644',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '652',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '707',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '790',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '791',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '792',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '793',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '794',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '795',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '796',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '797',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '917',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '943',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1236',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1336',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1427',
    10,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '329',
    11,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '332',
    11,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '333',
    11,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '334',
    11,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '335',
    11,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '336',
    11,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '337',
    11,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '338',
    11,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '339',
    11,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '340',
    11,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '341',
    11,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '342',
    11,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '343',
    11,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '804',
    11,
    'kmeans',
    NULL,
    0
);

INSERT INTO `cwe_cluster_mapping` (
    `cwe_id`,
    `cluster_id`,
    `cluster_method`,
    `distance_to_center`,
    `tenant_id`
) VALUES (
    '1241',
    11,
    'kmeans',
    NULL,
    0
);
