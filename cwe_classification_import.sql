-- ============================================================
-- CWE 分类结果导入 SQL
-- 生成时间: 2026-01-15 19:19:21
-- 分类记录数: 944
-- 标准类型数: 4
-- ============================================================

-- 注意：导入前请确保已创建表结构（见 cwe_classification_tables.sql）
-- 如果表中已有数据，建议先清空：
-- TRUNCATE TABLE `cwe_hierarchy`;
-- TRUNCATE TABLE `cwe_impact_mapping`;
-- TRUNCATE TABLE `cwe_standard_mapping`;
-- DELETE FROM `cwe_standard_type`;

-- ============================================================
-- 1. 插入标准分类类型字典表
-- ============================================================

INSERT INTO `cwe_standard_type` (
    `type_code`,
    `type_name`,
    `description`,
    `version`,
    `sort_order`,
    `tenant_id`
) VALUES (
    '7_pernicious_kingdoms',
    '7 Pernicious Kingdoms',
    NULL,
    NULL,
    0,
    0
) ON DUPLICATE KEY UPDATE
    `type_name` = VALUES(`type_name`),
    `description` = VALUES(`description`),
    `version` = VALUES(`version`);

INSERT INTO `cwe_standard_type` (
    `type_code`,
    `type_name`,
    `description`,
    `version`,
    `sort_order`,
    `tenant_id`
) VALUES (
    'clasp',
    'Clasp',
    NULL,
    NULL,
    0,
    0
) ON DUPLICATE KEY UPDATE
    `type_name` = VALUES(`type_name`),
    `description` = VALUES(`description`),
    `version` = VALUES(`version`);

INSERT INTO `cwe_standard_type` (
    `type_code`,
    `type_name`,
    `description`,
    `version`,
    `sort_order`,
    `tenant_id`
) VALUES (
    'landwehr',
    'Landwehr',
    NULL,
    NULL,
    0,
    0
) ON DUPLICATE KEY UPDATE
    `type_name` = VALUES(`type_name`),
    `description` = VALUES(`description`),
    `version` = VALUES(`version`);

INSERT INTO `cwe_standard_type` (
    `type_code`,
    `type_name`,
    `description`,
    `version`,
    `sort_order`,
    `tenant_id`
) VALUES (
    'plover',
    'Plover',
    NULL,
    NULL,
    0,
    0
) ON DUPLICATE KEY UPDATE
    `type_name` = VALUES(`type_name`),
    `description` = VALUES(`description`),
    `version` = VALUES(`version`);


-- ============================================================
-- 2. 插入标准分类映射表
-- ============================================================

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '14',
    '7_pernicious_kingdoms',
    NULL,
    'Insecure Compiler Optimization',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '15',
    '7_pernicious_kingdoms',
    NULL,
    'Setting Manipulation',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '20',
    '7_pernicious_kingdoms',
    NULL,
    'Input validation and representation',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '22',
    'plover',
    NULL,
    'Path Traversal',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '23',
    'plover',
    NULL,
    'Relative Path Traversal',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '24',
    'plover',
    NULL,
    '''../filedir',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '25',
    'plover',
    NULL,
    '''/../filedir',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '26',
    'plover',
    NULL,
    '''/directory/../filename',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '27',
    'plover',
    NULL,
    '''directory/../../filename',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '28',
    'plover',
    NULL,
    '''..filename'' (''dot dot backslash'')',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '29',
    'plover',
    NULL,
    '''..filename'' (''leading dot dot backslash'')',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '30',
    'plover',
    NULL,
    '7 - ''directory..filename',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '31',
    'plover',
    NULL,
    '8 - ''directory....filename',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '32',
    'plover',
    NULL,
    '''...'' (triple dot)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '33',
    'plover',
    NULL,
    '''....'' (multiple dot)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '34',
    'plover',
    NULL,
    '''....//'' (doubled dot dot slash)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '35',
    'plover',
    NULL,
    '''.../...//''',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '36',
    'plover',
    NULL,
    'Absolute Path Traversal',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '37',
    'plover',
    NULL,
    '/absolute/pathname/here',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '38',
    'plover',
    NULL,
    'absolutepathnamehere (''backslash absolute path'')',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '40',
    'plover',
    NULL,
    '''UNCsharename'' (Windows UNC share)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '41',
    'plover',
    NULL,
    'Path Equivalence',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '42',
    'plover',
    NULL,
    'Trailing Dot - ''filedir.''',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '43',
    'plover',
    NULL,
    'Multiple Trailing Dot - ''filedir....''',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '44',
    'plover',
    NULL,
    'Internal Dot - ''file.ordir''',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '45',
    'plover',
    NULL,
    'Multiple Internal Dot - ''file...dir''',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '46',
    'plover',
    NULL,
    'Trailing Space - ''filedir ''',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '47',
    'plover',
    NULL,
    'Leading Space - '' filedir''',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '48',
    'plover',
    NULL,
    'file(SPACE)name (internal space)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '49',
    'plover',
    NULL,
    'filedir/ (trailing slash, trailing /)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '50',
    'plover',
    NULL,
    '//multiple/leading/slash (''multiple leading slash'')',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '51',
    'plover',
    NULL,
    '/multiple//internal/slash (''multiple internal slash'')',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '52',
    'plover',
    NULL,
    '/multiple/trailing/slash// (''multiple trailing slash'')',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '53',
    'plover',
    NULL,
    'multipleinternalbackslash',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '54',
    'plover',
    NULL,
    'filedir (trailing backslash)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '55',
    'plover',
    NULL,
    '/./ (single dot directory)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '56',
    'plover',
    NULL,
    'filedir* (asterisk / wildcard)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '57',
    'plover',
    NULL,
    'dirname/fakechild/../realchild/filename',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '58',
    'plover',
    NULL,
    'Windows 8.3 Filename',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '59',
    'plover',
    NULL,
    'Link Following',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '61',
    'plover',
    NULL,
    'UNIX symbolic link following',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '62',
    'plover',
    NULL,
    'UNIX hard link',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '64',
    'plover',
    NULL,
    'Windows Shortcut Following (.LNK)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '65',
    'plover',
    NULL,
    'Windows hard link',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '66',
    'plover',
    NULL,
    'Virtual Files',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '67',
    'plover',
    NULL,
    'Windows MS-DOS device names',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '69',
    'plover',
    NULL,
    'Windows',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '73',
    '7_pernicious_kingdoms',
    NULL,
    'Path Manipulation',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '74',
    'clasp',
    NULL,
    'Injection problem (''data'' used as something else)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '75',
    'plover',
    NULL,
    'Special Element Injection',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '76',
    'plover',
    NULL,
    'Equivalent Special Element Injection',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '77',
    '7_pernicious_kingdoms',
    NULL,
    'Command Injection',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '78',
    'plover',
    NULL,
    'OS Command Injection',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '79',
    'plover',
    NULL,
    'Cross-site scripting (XSS)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '79',
    'clasp',
    NULL,
    'Cross-site scripting',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '80',
    'plover',
    NULL,
    'Basic XSS',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '81',
    'plover',
    NULL,
    'XSS in error pages',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '82',
    'plover',
    NULL,
    'Script in IMG tags',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '83',
    'plover',
    NULL,
    'XSS using Script in Attributes',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '84',
    'plover',
    NULL,
    'XSS using Script Via Encoded URI Schemes',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '85',
    'plover',
    NULL,
    'DOUBLE - Doubled character XSS manipulations, e.g. <script',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '86',
    'plover',
    NULL,
    'Invalid Characters in Identifiers',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '87',
    'plover',
    NULL,
    'Alternate XSS syntax',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '88',
    'plover',
    NULL,
    'Argument Injection or Modification',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '89',
    'plover',
    NULL,
    'SQL injection',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '89',
    'clasp',
    NULL,
    'SQL injection',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '90',
    'plover',
    NULL,
    'LDAP injection',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '91',
    'plover',
    NULL,
    'XML injection (aka Blind Xpath injection)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '93',
    'plover',
    NULL,
    'CRLF Injection',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '95',
    'plover',
    NULL,
    'Direct Dynamic Code Evaluation (''Eval Injection'')',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '96',
    'plover',
    NULL,
    'Direct Static Code Injection',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '97',
    'plover',
    NULL,
    'Server-Side Includes (SSI) Injection',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '98',
    'plover',
    NULL,
    'PHP File Include',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '99',
    '7_pernicious_kingdoms',
    NULL,
    'Resource Injection',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '111',
    '7_pernicious_kingdoms',
    NULL,
    'Unsafe JNI',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '112',
    '7_pernicious_kingdoms',
    NULL,
    'Missing XML Validation',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '113',
    'plover',
    NULL,
    'HTTP response splitting',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '114',
    '7_pernicious_kingdoms',
    NULL,
    'Process Control',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '115',
    'plover',
    NULL,
    'Misinterpretation Error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '117',
    '7_pernicious_kingdoms',
    NULL,
    'Log Forging',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '120',
    'plover',
    NULL,
    'Unbounded Transfer (''classic overflow'')',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '120',
    'clasp',
    NULL,
    'Buffer overflow',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '121',
    'clasp',
    NULL,
    'Stack overflow',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '122',
    'clasp',
    NULL,
    'Heap overflow',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '123',
    'clasp',
    NULL,
    'Write-what-where condition',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '124',
    'plover',
    NULL,
    'UNDER - Boundary beginning violation (''buffer underflow''?)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '125',
    'plover',
    NULL,
    'Out-of-bounds Read',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '126',
    'plover',
    NULL,
    'Buffer over-read',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '127',
    'plover',
    NULL,
    'Buffer under-read',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '128',
    'clasp',
    NULL,
    'Wrap-around error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '129',
    'clasp',
    NULL,
    'Unchecked array indexing',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '130',
    'plover',
    NULL,
    'Length Parameter Inconsistency',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '131',
    'plover',
    NULL,
    'Other length calculation error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '134',
    'plover',
    NULL,
    'Format string vulnerability',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '134',
    'clasp',
    NULL,
    'Format string problem',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '135',
    'clasp',
    NULL,
    'Improper string length checking',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '138',
    'plover',
    NULL,
    'Special Elements (Characters or Reserved Words)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '140',
    'plover',
    NULL,
    'Delimiter Problems',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '141',
    'plover',
    NULL,
    'Parameter Delimiter',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '142',
    'plover',
    NULL,
    'Value Delimiter',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '143',
    'plover',
    NULL,
    'Record Delimiter',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '144',
    'plover',
    NULL,
    'Line Delimiter',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '145',
    'plover',
    NULL,
    'Section Delimiter',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '146',
    'plover',
    NULL,
    'Delimiter between Expressions or Commands',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '147',
    'plover',
    NULL,
    'Input Terminator',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '148',
    'plover',
    NULL,
    'Input Leader',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '149',
    'plover',
    NULL,
    'Quoting Element',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '150',
    'plover',
    NULL,
    'Escape, Meta, or Control Character / Sequence',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '151',
    'plover',
    NULL,
    'Comment Element',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '152',
    'plover',
    NULL,
    'Macro Symbol',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '153',
    'plover',
    NULL,
    'Substitution Character',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '154',
    'plover',
    NULL,
    'Variable Name Delimiter',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '155',
    'plover',
    NULL,
    'Wildcard or Matching Element',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '157',
    'plover',
    NULL,
    'Grouping Element / Paired Delimiter',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '158',
    'plover',
    NULL,
    'Null Character / Null Byte',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '159',
    'plover',
    NULL,
    'Common Special Element Manipulations',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '160',
    'plover',
    NULL,
    'Leading Special Element',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '161',
    'plover',
    NULL,
    'Multiple Leading Special Elements',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '162',
    'plover',
    NULL,
    'Trailing Special Element',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '163',
    'plover',
    NULL,
    'Multiple Trailing Special Elements',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '164',
    'plover',
    NULL,
    'Internal Special Element',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '165',
    'plover',
    NULL,
    'Multiple Internal Special Element',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '166',
    'plover',
    NULL,
    'Missing Special Element',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '167',
    'plover',
    NULL,
    'Extra Special Element',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '168',
    'plover',
    NULL,
    'Inconsistent Special Elements',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '170',
    'plover',
    NULL,
    'Improper Null Termination',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '170',
    'clasp',
    NULL,
    'Miscalculated null termination',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '172',
    'plover',
    NULL,
    'Encoding Error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '173',
    'plover',
    NULL,
    'Alternate Encoding',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '174',
    'plover',
    NULL,
    'Double Encoding',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '175',
    'plover',
    NULL,
    'Mixed Encoding',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '176',
    'plover',
    NULL,
    'Unicode Encoding',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '177',
    'plover',
    NULL,
    'URL Encoding (Hex Encoding)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '178',
    'plover',
    NULL,
    'Case Sensitivity (lowercase, uppercase, mixed case)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '179',
    'plover',
    NULL,
    'Early Validation Errors',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '180',
    'plover',
    NULL,
    'Validate-Before-Canonicalize',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '181',
    'plover',
    NULL,
    'Validate-Before-Filter',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '182',
    'plover',
    NULL,
    'Collapse of Data into Unsafe Value',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '183',
    'plover',
    NULL,
    'Permissive Whitelist',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '184',
    'plover',
    NULL,
    'Incomplete Blacklist',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '185',
    'plover',
    NULL,
    'Regular Expression Error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '186',
    'plover',
    NULL,
    'Overly Restrictive Regular Expression',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '187',
    'plover',
    NULL,
    'Partial Comparison',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '188',
    'clasp',
    NULL,
    'Reliance on data layout',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '190',
    'plover',
    NULL,
    'Integer overflow (wrap or wraparound)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '190',
    'clasp',
    NULL,
    'Integer overflow',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '191',
    'plover',
    NULL,
    'Integer underflow (wrap or wraparound)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '192',
    'clasp',
    NULL,
    'Integer coercion error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '193',
    'plover',
    NULL,
    'Off-by-one Error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '194',
    'clasp',
    NULL,
    'Sign extension error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '195',
    'clasp',
    NULL,
    'Signed to unsigned conversion error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '196',
    'clasp',
    NULL,
    'Unsigned to signed conversion error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '197',
    'plover',
    NULL,
    'Numeric truncation error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '198',
    'plover',
    NULL,
    'Numeric Byte Ordering Error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '200',
    'plover',
    NULL,
    'Information Leak (information disclosure)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '201',
    'clasp',
    NULL,
    'Accidental leaking of sensitive information through sent data',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '202',
    'clasp',
    NULL,
    'Accidental leaking of sensitive information through data queries',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '203',
    'plover',
    NULL,
    'Discrepancy Information Leaks',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '204',
    'plover',
    NULL,
    'Response discrepancy infoleak',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '205',
    'plover',
    NULL,
    'Behavioral Discrepancy Infoleak',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '206',
    'plover',
    NULL,
    'Internal behavioral inconsistency infoleak',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '207',
    'plover',
    NULL,
    'External behavioral inconsistency infoleak',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '208',
    'plover',
    NULL,
    'Timing discrepancy infoleak',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '209',
    'clasp',
    NULL,
    'Accidental leaking of sensitive information through error messages',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '210',
    'plover',
    NULL,
    'Product-Generated Error Message Infoleak',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '211',
    'plover',
    NULL,
    'Product-External Error Message Infoleak',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '212',
    'plover',
    NULL,
    'Cross-Boundary Cleansing Infoleak',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '213',
    'plover',
    NULL,
    'Intended information leak',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '214',
    'plover',
    NULL,
    'Process information infoleak to other processes',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '215',
    'plover',
    NULL,
    'Infoleak Using Debug Information',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '219',
    'plover',
    NULL,
    'Sensitive Data Under Web Root',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '220',
    'plover',
    NULL,
    'Sensitive Data Under FTP Root',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '221',
    'plover',
    NULL,
    'Information loss or omission',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '222',
    'plover',
    NULL,
    'Truncation of Security-relevant Information',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '223',
    'plover',
    NULL,
    'Omission of Security-relevant Information',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '224',
    'plover',
    NULL,
    'Obscured Security-relevant Information by Alternate Name',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '226',
    'plover',
    NULL,
    'Sensitive Information Uncleared Before Use',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '228',
    'plover',
    NULL,
    'Structure and Validity Problems',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '230',
    'plover',
    NULL,
    'Missing Value Error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '231',
    'plover',
    NULL,
    'Extra Value Error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '232',
    'plover',
    NULL,
    'Undefined Value Error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '233',
    'plover',
    NULL,
    'Parameter Problems',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '234',
    'plover',
    NULL,
    'Missing Parameter Error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '235',
    'plover',
    NULL,
    'Extra Parameter Error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '236',
    'plover',
    NULL,
    'Undefined Parameter Error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '237',
    'plover',
    NULL,
    'Element Problems',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '238',
    'plover',
    NULL,
    'Missing Element Error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '239',
    'plover',
    NULL,
    'Incomplete Element',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '240',
    'plover',
    NULL,
    'Inconsistent Elements',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '241',
    'plover',
    NULL,
    'Wrong Data Type',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '242',
    '7_pernicious_kingdoms',
    NULL,
    'Dangerous Functions',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '243',
    '7_pernicious_kingdoms',
    NULL,
    'Directory Restriction',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '244',
    '7_pernicious_kingdoms',
    NULL,
    'Heap Inspection',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '252',
    '7_pernicious_kingdoms',
    NULL,
    'Unchecked Return Value',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '253',
    'clasp',
    NULL,
    'Misinterpreted function return value',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '256',
    '7_pernicious_kingdoms',
    NULL,
    'Password Management',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '257',
    'clasp',
    NULL,
    'Storing passwords in a recoverable format',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '259',
    'clasp',
    NULL,
    'Use of hard-coded password',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '262',
    'clasp',
    NULL,
    'Not allowing password aging',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '263',
    'clasp',
    NULL,
    'Allowing password aging',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '266',
    'plover',
    NULL,
    'Incorrect Privilege Assignment',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '267',
    'plover',
    NULL,
    'Unsafe Privilege',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '268',
    'plover',
    NULL,
    'Privilege Chaining',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '269',
    'plover',
    NULL,
    'Privilege Management Error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '270',
    'plover',
    NULL,
    'Privilege Context Switching Error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '271',
    'plover',
    NULL,
    'Privilege Dropping / Lowering Errors',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '272',
    '7_pernicious_kingdoms',
    NULL,
    'Least Privilege Violation',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '273',
    'clasp',
    NULL,
    'Failure to check whether privileges were dropped successfully',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '274',
    'plover',
    NULL,
    'Insufficient privileges',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '276',
    'plover',
    NULL,
    'Insecure Default Permissions',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '277',
    'plover',
    NULL,
    'Insecure inherited permissions',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '278',
    'plover',
    NULL,
    'Insecure preserved inherited permissions',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '279',
    'plover',
    NULL,
    'Insecure execution-assigned permissions',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '280',
    'plover',
    NULL,
    'Fails poorly due to insufficient permissions',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '281',
    'plover',
    NULL,
    'Permission preservation failure',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '282',
    'plover',
    NULL,
    'Ownership errors',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '283',
    'plover',
    NULL,
    'Unverified Ownership',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '284',
    'plover',
    NULL,
    'Access Control List (ACL) errors',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '284',
    '7_pernicious_kingdoms',
    NULL,
    'Missing Access Control',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '285',
    '7_pernicious_kingdoms',
    NULL,
    'Missing Access Control',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '286',
    'plover',
    NULL,
    'User management errors',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '287',
    'plover',
    NULL,
    'Authentication Error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '288',
    'plover',
    NULL,
    'Authentication Bypass by Alternate Path/Channel',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '289',
    'plover',
    NULL,
    'Authentication bypass by alternate name',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '290',
    'plover',
    NULL,
    'Authentication bypass by spoofing',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '291',
    'clasp',
    NULL,
    'Trusting self-reported IP address',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '293',
    'clasp',
    NULL,
    'Using referrer field for authentication',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '294',
    'plover',
    NULL,
    'Authentication bypass by replay',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '296',
    'clasp',
    NULL,
    'Failure to follow chain of trust in certificate validation',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '297',
    'clasp',
    NULL,
    'Failure to validate host-specific certificate data',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '298',
    'clasp',
    NULL,
    'Failure to validate certificate expiration',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '299',
    'clasp',
    NULL,
    'Failure to check for certificate revocation',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '300',
    'plover',
    NULL,
    'Man-in-the-middle (MITM)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '301',
    'clasp',
    NULL,
    'Reflection attack in an auth protocol',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '302',
    'plover',
    NULL,
    'Authentication Bypass via Assumed-Immutable Data',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '303',
    'plover',
    NULL,
    'Authentication Logic Error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '304',
    'plover',
    NULL,
    'Missing Critical Step in Authentication',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '305',
    'plover',
    NULL,
    'Authentication Bypass by Primary Weakness',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '306',
    'plover',
    NULL,
    'No Authentication for Critical Function',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '308',
    'clasp',
    NULL,
    'Using single-factor authentication',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '309',
    'clasp',
    NULL,
    'Using password systems',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '311',
    'clasp',
    NULL,
    'Failure to encrypt data',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '312',
    'plover',
    NULL,
    'Plaintext Storage of Sensitive Information',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '313',
    'plover',
    NULL,
    'Plaintext Storage in File or on Disk',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '314',
    'plover',
    NULL,
    'Plaintext Storage in Registry',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '315',
    'plover',
    NULL,
    'Plaintext Storage in Cookie',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '316',
    'plover',
    NULL,
    'Plaintext Storage in Memory',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '317',
    'plover',
    NULL,
    'Plaintext Storage in GUI',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '318',
    'plover',
    NULL,
    'Plaintext Storage in Executable',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '319',
    'plover',
    NULL,
    'Plaintext Transmission of Sensitive Information',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '321',
    'clasp',
    NULL,
    'Use of hard-coded cryptographic key',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '322',
    'clasp',
    NULL,
    'Key exchange without entity authentication',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '323',
    'clasp',
    NULL,
    'Reusing a nonce, key pair in encryption',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '324',
    'clasp',
    NULL,
    'Using a key past its expiration date',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '325',
    'plover',
    NULL,
    'Missing Required Cryptographic Step',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '326',
    'plover',
    NULL,
    'Weak Encryption',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '327',
    'clasp',
    NULL,
    'Using a broken or risky cryptographic algorithm',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '328',
    'plover',
    NULL,
    'Reversible One-Way Hash',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '329',
    'clasp',
    NULL,
    'Not using a random IV with CBC mode',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '330',
    'plover',
    NULL,
    'Randomness and Predictability',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '331',
    'plover',
    NULL,
    'Insufficient Entropy',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '332',
    'clasp',
    NULL,
    'Insufficient entropy in PRNG',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '333',
    'clasp',
    NULL,
    'Failure of TRNG',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '334',
    'plover',
    NULL,
    'Small Space of Random Values',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '335',
    'plover',
    NULL,
    'PRNG Seed Error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '336',
    'plover',
    NULL,
    'Same Seed in PRNG',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '337',
    'plover',
    NULL,
    'Predictable Seed in PRNG',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '338',
    'clasp',
    NULL,
    'Non-cryptographic PRNG',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '339',
    'plover',
    NULL,
    'Small Seed Space in PRNG',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '340',
    'plover',
    NULL,
    'Predictability problems',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '341',
    'plover',
    NULL,
    'Predictable from Observable State',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '342',
    'plover',
    NULL,
    'Predictable Exact Value from Previous Values',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '343',
    'plover',
    NULL,
    'Predictable Value Range from Previous Values',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '344',
    'plover',
    NULL,
    'Static Value in Unpredictable Context',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '345',
    'plover',
    NULL,
    'Insufficient Verification of Data',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '346',
    'plover',
    NULL,
    'Origin Validation Error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '347',
    'plover',
    NULL,
    'Improperly Verified Signature',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '348',
    'plover',
    NULL,
    'Use of Less Trusted Source',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '349',
    'plover',
    NULL,
    'Untrusted Data Appended with Trusted Data',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '350',
    'plover',
    NULL,
    'Improperly Trusted Reverse DNS',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '351',
    'plover',
    NULL,
    'Insufficient Type Distinction',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '352',
    'plover',
    NULL,
    'Cross-Site Request Forgery (CSRF)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '353',
    'clasp',
    NULL,
    'Failure to add integrity check value',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '354',
    'clasp',
    NULL,
    'Failure to check integrity check value',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '356',
    'plover',
    NULL,
    'Product UI does not warn user of unsafe actions',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '357',
    'plover',
    NULL,
    'Insufficient UI warning of dangerous operations',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '358',
    'plover',
    NULL,
    'Improperly Implemented Security Check for Standard',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '359',
    '7_pernicious_kingdoms',
    NULL,
    'Privacy Violation',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '360',
    'clasp',
    NULL,
    'Trust of system event data',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '362',
    'plover',
    NULL,
    'Race Conditions',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '363',
    'plover',
    NULL,
    'Race condition enabling link following',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '364',
    'plover',
    NULL,
    'Signal handler race condition',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '364',
    'clasp',
    NULL,
    'Race condition in signal handler',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '366',
    'clasp',
    NULL,
    'Race condition within a thread',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '367',
    'plover',
    NULL,
    'Time-of-check Time-of-use race condition',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '367',
    'clasp',
    NULL,
    'Time of check, time of use race condition',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '368',
    'plover',
    NULL,
    'Context Switching Race Condition',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '370',
    'clasp',
    NULL,
    'Race condition in checking for certificate revocation',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '372',
    'plover',
    NULL,
    'Incomplete Internal State Distinction',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '374',
    'clasp',
    NULL,
    'Passing mutable objects to an untrusted method',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '375',
    'clasp',
    NULL,
    'Mutable object returned',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '377',
    '7_pernicious_kingdoms',
    NULL,
    'Insecure Temporary File',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '378',
    'clasp',
    NULL,
    'Improper temp file opening',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '379',
    'clasp',
    NULL,
    'Guessed or visible temporary file',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '384',
    '7_pernicious_kingdoms',
    NULL,
    'Session Fixation',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '385',
    'landwehr',
    NULL,
    'Timing',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '386',
    'clasp',
    NULL,
    'Symbolic name not mapping to correct object',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '390',
    'clasp',
    NULL,
    'Improper error handling',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '391',
    'plover',
    NULL,
    'Unchecked Return Value',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '391',
    'clasp',
    NULL,
    'Uncaught exception',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '392',
    'plover',
    NULL,
    'Missing Error Status Code',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '393',
    'plover',
    NULL,
    'Wrong Status Code',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '394',
    'plover',
    NULL,
    'Unexpected Status Code or Return Value',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '395',
    '7_pernicious_kingdoms',
    NULL,
    'Catching NullPointerException',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '396',
    '7_pernicious_kingdoms',
    NULL,
    'Overly-Broad Catch Block',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '397',
    '7_pernicious_kingdoms',
    NULL,
    'Overly-Broad Throws Declaration',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '400',
    'clasp',
    NULL,
    'Resource exhaustion (file descriptor, disk space, sockets, ...)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '401',
    'plover',
    NULL,
    'Memory leak',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '401',
    'clasp',
    NULL,
    'Failure to deallocate data',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '402',
    'plover',
    NULL,
    'Resource leaks',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '403',
    'plover',
    NULL,
    'UNIX file descriptor leak',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '404',
    'plover',
    NULL,
    'Improper resource shutdown or release',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '405',
    'plover',
    NULL,
    'Asymmetric resource consumption (amplification)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '406',
    'plover',
    NULL,
    'Network Amplification',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '407',
    'plover',
    NULL,
    'Algorithmic Complexity',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '408',
    'plover',
    NULL,
    'Early Amplification',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '409',
    'plover',
    NULL,
    'Data Amplification',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '410',
    'plover',
    NULL,
    'Insufficient Resource Pool',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '412',
    'plover',
    NULL,
    'Unrestricted Critical Resource Lock',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '413',
    'plover',
    NULL,
    'Insufficient Resource Locking',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '414',
    'plover',
    NULL,
    'Missing Lock Check',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '415',
    'plover',
    NULL,
    'DFREE - Double-Free Vulnerability',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '415',
    'clasp',
    NULL,
    'Doubly freeing memory',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '416',
    '7_pernicious_kingdoms',
    NULL,
    'Use After Free',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '419',
    'plover',
    NULL,
    'Unprotected Primary Channel',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '420',
    'plover',
    NULL,
    'Unprotected Alternate Channel',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '421',
    'plover',
    NULL,
    'Alternate Channel Race Condition',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '422',
    'plover',
    NULL,
    'Unprotected Windows Messaging Channel (''Shatter'')',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '424',
    'plover',
    NULL,
    'Alternate Path Errors',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '425',
    'plover',
    NULL,
    'Direct Request aka ''Forced Browsing''',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '426',
    'plover',
    NULL,
    'Untrusted Search Path',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '427',
    'plover',
    NULL,
    'Uncontrolled Search Path Element',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '428',
    'plover',
    NULL,
    'Unquoted Search Path or Element',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '430',
    'plover',
    NULL,
    'Improper Handler Deployment',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '431',
    'plover',
    NULL,
    'Missing Handler',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '432',
    'plover',
    NULL,
    'Dangerous handler not cleared/disabled during sensitive operations',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '433',
    'plover',
    NULL,
    'Unparsed Raw Web Content Delivery',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '434',
    'plover',
    NULL,
    'Unrestricted File Upload',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '435',
    'plover',
    NULL,
    'Interaction Errors',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '436',
    'plover',
    NULL,
    'Multiple Interpretation Error (MIE)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '437',
    'plover',
    NULL,
    'Extra Unhandled Features',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '439',
    'plover',
    NULL,
    'CHANGE Behavioral Change',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '440',
    'plover',
    NULL,
    'Expected behavior violation',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '441',
    'plover',
    NULL,
    'Unintended proxy/intermediary',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '444',
    'plover',
    NULL,
    'HTTP Request Smuggling',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '446',
    'plover',
    NULL,
    'User interface inconsistency',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '447',
    'plover',
    NULL,
    'Unimplemented or unsupported feature in UI',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '448',
    'plover',
    NULL,
    'Obsolete feature in UI',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '449',
    'plover',
    NULL,
    'The UI performs the wrong action',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '450',
    'plover',
    NULL,
    'Multiple Interpretations of UI Input',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '451',
    'plover',
    NULL,
    'UI Misrepresentation of Critical Information',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '453',
    'plover',
    NULL,
    'Insecure default variable initialization',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '454',
    'plover',
    NULL,
    'External initialization of trusted variables or values',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '455',
    'plover',
    NULL,
    'Non-exit on Failed Initialization',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '456',
    'plover',
    NULL,
    'Missing Initialization',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '457',
    'clasp',
    NULL,
    'Uninitialized variable',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '459',
    'plover',
    NULL,
    'Incomplete Cleanup',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '460',
    'clasp',
    NULL,
    'Improper cleanup on thrown exception',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '462',
    'clasp',
    NULL,
    'Duplicate key in associative list (alist)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '463',
    'clasp',
    NULL,
    'Deletion of data-structure sentinel',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '464',
    'clasp',
    NULL,
    'Addition of data-structure sentinel',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '466',
    '7_pernicious_kingdoms',
    NULL,
    'Illegal Pointer Value',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '467',
    'clasp',
    NULL,
    'Use of sizeof() on a pointer type',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '468',
    'clasp',
    NULL,
    'Unintentional pointer scaling',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '469',
    'clasp',
    NULL,
    'Improper pointer subtraction',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '470',
    '7_pernicious_kingdoms',
    NULL,
    'Unsafe Reflection',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '471',
    'plover',
    NULL,
    'Modification of Assumed-Immutable Data',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '472',
    'plover',
    NULL,
    'Web Parameter Tampering',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '473',
    'plover',
    NULL,
    'PHP External Variable Modification',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '474',
    '7_pernicious_kingdoms',
    NULL,
    'Inconsistent Implementations',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '475',
    '7_pernicious_kingdoms',
    NULL,
    'Undefined Behavior',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '476',
    '7_pernicious_kingdoms',
    NULL,
    'Null Dereference',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '476',
    'plover',
    NULL,
    'Null Dereference (Null Pointer Dereference)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '477',
    '7_pernicious_kingdoms',
    NULL,
    'Obsolete',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '478',
    'clasp',
    NULL,
    'Failure to account for default case in switch',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '479',
    'clasp',
    NULL,
    'Unsafe function call from a signal handler',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '480',
    'clasp',
    NULL,
    'Using the wrong operator',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '481',
    'clasp',
    NULL,
    'Assigning instead of comparing',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '482',
    'clasp',
    NULL,
    'Comparing instead of assigning',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '483',
    'clasp',
    NULL,
    'Incorrect block delimitation',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '484',
    'clasp',
    NULL,
    'Omitted break statement',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '486',
    '7_pernicious_kingdoms',
    NULL,
    'Comparing Classes by Name',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '487',
    'clasp',
    NULL,
    'Relying on package-level scope',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '488',
    '7_pernicious_kingdoms',
    NULL,
    'Data Leaking Between Users',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '489',
    '7_pernicious_kingdoms',
    NULL,
    'Leftover Debug Code',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '492',
    'clasp',
    NULL,
    'Publicizing of private data when using inner classes',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '493',
    'clasp',
    NULL,
    'Failure to provide confidentiality for stored data',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '494',
    'clasp',
    NULL,
    'Invoking untrusted mobile code',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '495',
    '7_pernicious_kingdoms',
    NULL,
    'Private Array-Typed Field Returned From A Public Method',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '496',
    '7_pernicious_kingdoms',
    NULL,
    'Public Data Assigned to Private Array-Typed Field',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '497',
    '7_pernicious_kingdoms',
    NULL,
    'System Information Leak',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '498',
    'clasp',
    NULL,
    'Information leak through class cloning',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '499',
    'clasp',
    NULL,
    'Information leak through serialization',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '500',
    'clasp',
    NULL,
    'Overflow of static internal buffer',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '501',
    '7_pernicious_kingdoms',
    NULL,
    'Trust Boundary Violation',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '502',
    'clasp',
    NULL,
    'Deserialization of untrusted data',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '506',
    'landwehr',
    NULL,
    'Malicious',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '507',
    'landwehr',
    NULL,
    'Trojan Horse',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '508',
    'landwehr',
    NULL,
    'Non-Replicating',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '509',
    'landwehr',
    NULL,
    'Replicating (virus)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '510',
    'landwehr',
    NULL,
    'Trapdoor',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '511',
    'landwehr',
    NULL,
    'Logic/Time Bomb',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '514',
    'landwehr',
    NULL,
    'Covert Channel',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '515',
    'landwehr',
    NULL,
    'Storage',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '616',
    'plover',
    NULL,
    'Incomplete Identification of Uploaded File Variables (PHP)',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '662',
    'clasp',
    NULL,
    'State synchronization error',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '665',
    'plover',
    NULL,
    'Incorrect initialization',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '676',
    '7_pernicious_kingdoms',
    NULL,
    'Dangerous Functions',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '766',
    'clasp',
    NULL,
    'Failure to protect stored data from modification',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '767',
    'clasp',
    NULL,
    'Failure to protect stored data from modification',
    0
);

INSERT INTO `cwe_standard_mapping` (
    `cwe_id`,
    `type_code`,
    `entry_code`,
    `entry_name`,
    `tenant_id`
) VALUES (
    '768',
    'clasp',
    NULL,
    'Failure to protect stored data from modification',
    0
);


-- ============================================================
-- 3. 插入影响类型映射表
-- ============================================================

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '5',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '5',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '7',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '8',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '8',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '11',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '12',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '14',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '20',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '20',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '20',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '22',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '22',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '22',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '23',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '23',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '23',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '24',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '24',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '25',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '25',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '26',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '26',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '27',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '27',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '28',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '28',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '29',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '29',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '30',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '30',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '31',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '31',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '32',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '32',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '33',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '33',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '34',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '34',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '35',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '35',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '36',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '36',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '36',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '37',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '37',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '38',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '38',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '39',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '39',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '39',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '40',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '40',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '41',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '41',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '43',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '43',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '44',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '44',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '45',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '45',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '46',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '46',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '47',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '47',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '48',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '48',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '49',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '49',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '50',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '50',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '51',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '51',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '52',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '52',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '53',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '53',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '54',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '54',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '55',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '55',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '56',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '56',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '57',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '57',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '58',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '58',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '59',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '59',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '61',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '61',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '62',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '62',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '64',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '64',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '65',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '65',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '67',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '67',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '72',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '72',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '73',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '73',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '73',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '74',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '74',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '75',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '75',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '75',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '77',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '77',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '77',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '78',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '78',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '78',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '79',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '79',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '79',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '80',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '80',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '80',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '81',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '81',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '81',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '82',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '82',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '82',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '83',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '83',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '83',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '84',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '85',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '85',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '85',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '86',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '86',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '86',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '87',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '87',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '87',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '88',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '88',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '88',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '89',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '89',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '89',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '90',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '90',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '90',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '91',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '91',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '91',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '93',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '94',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '94',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '94',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '95',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '95',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '95',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '96',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '96',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '96',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '97',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '97',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '97',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '98',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '98',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '98',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '99',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '99',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '102',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '103',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '103',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '103',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '104',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '104',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '104',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '105',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '106',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '108',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '108',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '108',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '112',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '113',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '114',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '114',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '114',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '115',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '116',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '116',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '116',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '117',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '117',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '117',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '119',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '119',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '119',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '120',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '120',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '120',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '121',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '121',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '121',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '122',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '122',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '122',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '123',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '123',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '123',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '124',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '124',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '124',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '125',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '125',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '126',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '126',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '126',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '127',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '128',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '128',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '128',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '129',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '129',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '129',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '130',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '130',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '131',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '131',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '131',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '134',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '134',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '134',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '135',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '135',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '135',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '138',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '138',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '138',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '140',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '141',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '142',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '143',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '144',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '145',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '146',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '146',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '146',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '147',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '148',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '149',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '150',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '151',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '152',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '153',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '154',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '155',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '156',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '157',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '158',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '159',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '160',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '161',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '162',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '163',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '164',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '165',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '166',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '167',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '168',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '170',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '170',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '170',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '172',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '174',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '174',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '174',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '175',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '176',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '177',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '179',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '187',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '188',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '188',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '190',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '190',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '190',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '191',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '191',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '191',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '192',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '192',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '192',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '193',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '193',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '193',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '194',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '194',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '194',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '195',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '196',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '196',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '196',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '197',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '198',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '200',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '201',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '202',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '203',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '204',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '205',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '206',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '207',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '208',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '209',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '210',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '211',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '212',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '213',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '214',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '215',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '219',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '220',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '226',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '228',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '228',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '229',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '230',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '231',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '232',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '233',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '234',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '234',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '234',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '235',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '236',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '237',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '238',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '239',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '240',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '241',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '243',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '244',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '248',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '248',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '250',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '250',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '250',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '252',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '252',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '253',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '253',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '257',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '272',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '276',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '276',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '277',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '277',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '278',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '278',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '279',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '279',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '281',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '281',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '285',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '285',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '287',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '287',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '287',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '295',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '296',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '296',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '296',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '298',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '299',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '299',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '300',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '300',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '304',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '304',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '311',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '311',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '312',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '313',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '314',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '315',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '316',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '317',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '318',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '319',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '319',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '322',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '325',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '325',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '326',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '327',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '327',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '329',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '330',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '332',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '333',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '345',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '347',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '347',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '349',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '352',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '352',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '352',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '353',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '354',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '359',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '360',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '360',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '360',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '362',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '362',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '362',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '363',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '363',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '364',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '364',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '364',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '366',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '367',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '368',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '368',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '369',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '370',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '370',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '372',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '374',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '375',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '377',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '377',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '378',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '378',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '379',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '382',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '385',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '386',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '386',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '390',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '391',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '392',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '393',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '394',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '395',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '400',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '401',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '402',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '403',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '403',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '404',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '404',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '405',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '406',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '407',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '408',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '409',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '410',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '410',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '412',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '413',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '413',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '414',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '414',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '415',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '415',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '415',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '416',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '416',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '416',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '425',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '425',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '425',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '426',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '426',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '426',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '427',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '427',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '427',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '428',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '428',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '428',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '430',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '432',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '433',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '434',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '434',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '434',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '435',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '436',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '437',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '444',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '453',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '454',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '455',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '456',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '457',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '457',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '459',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '459',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '463',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '464',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '466',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '466',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '467',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '467',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '468',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '468',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '469',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '469',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '469',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '470',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '470',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '470',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '471',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '472',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '473',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '476',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '476',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '476',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '478',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '479',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '479',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '479',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '482',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '482',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '483',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '483',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '483',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '486',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '486',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '486',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '487',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '487',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '488',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '489',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '489',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '489',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '491',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '492',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '493',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '493',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '494',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '494',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '494',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '495',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '496',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '497',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '499',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '500',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '500',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '502',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '502',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '506',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '506',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '506',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '507',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '507',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '507',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '508',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '508',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '508',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '509',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '509',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '509',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '510',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '510',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '510',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '511',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '512',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '514',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '515',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '515',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '524',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '525',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '526',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '527',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '528',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '529',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '530',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '531',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '532',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '535',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '536',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '537',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '538',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '539',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '540',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '541',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '543',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '544',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '548',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '550',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '552',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '552',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '553',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '553',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '553',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '554',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '558',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '560',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '560',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '562',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '562',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '562',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '564',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '564',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '565',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '565',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '565',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '566',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '566',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '567',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '567',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '567',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '578',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '578',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '578',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '580',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '581',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '582',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '583',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '583',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '583',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '586',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '587',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '587',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '587',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '588',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '588',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '590',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '590',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '590',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '591',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '593',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '594',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '594',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '598',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '599',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '600',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '600',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '601',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '602',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '605',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '605',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '606',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '607',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '608',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '608',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '609',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '610',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '610',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '611',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '611',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '611',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '612',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '614',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '615',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '616',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '616',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '617',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '619',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '619',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '621',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '622',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '623',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '623',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '623',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '624',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '624',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '624',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '626',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '627',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '627',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '627',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '638',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '638',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '638',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '640',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '640',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '641',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '641',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '641',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '642',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '642',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '643',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '644',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '644',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '644',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '645',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '646',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '646',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '647',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '648',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '648',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '648',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '649',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '650',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '650',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '651',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '652',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '656',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '656',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '656',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '662',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '662',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '663',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '663',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '665',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '665',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '667',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '668',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '668',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '669',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '669',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '672',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '672',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '672',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '674',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '674',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '680',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '680',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '680',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '681',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '682',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '682',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '682',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '689',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '689',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '690',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '690',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '690',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '692',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '692',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '692',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '696',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '698',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '698',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '698',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '703',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '703',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '703',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '706',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '706',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '708',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '708',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '732',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '732',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '749',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '749',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '749',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '754',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '754',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '756',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '761',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '761',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '761',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '762',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '762',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '762',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '763',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '763',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '763',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '764',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '764',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '765',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '765',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '766',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '766',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '767',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '768',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '768',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '768',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '770',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '771',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '772',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '773',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '774',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '775',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '776',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '777',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '777',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '779',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '781',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '781',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '781',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '782',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '782',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '782',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '783',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '783',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '783',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '785',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '785',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '785',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '786',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '786',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '786',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '787',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '787',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '788',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '788',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '788',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '789',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '790',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '791',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '792',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '793',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '794',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '795',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '796',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '797',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '798',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '798',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '798',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '799',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '805',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '805',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '805',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '806',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '806',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '806',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '807',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '807',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '820',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '820',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '821',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '821',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '822',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '822',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '822',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '823',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '823',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '823',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '824',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '824',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '824',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '825',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '825',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '825',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '826',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '826',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '826',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '827',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '827',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '827',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '828',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '828',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '828',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '829',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '829',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '829',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '830',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '830',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '830',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '831',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '831',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '831',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '832',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '832',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '832',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '833',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '834',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '835',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '838',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '838',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '838',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '839',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '839',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '839',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '843',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '843',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '843',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '862',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '862',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '862',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '863',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '863',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '863',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '908',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '908',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '909',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '909',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '910',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '910',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '911',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '912',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '913',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '914',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '915',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '917',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '917',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '918',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '918',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '920',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '921',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '921',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '922',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '922',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '923',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '923',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '924',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '924',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '925',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '926',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '926',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '926',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '927',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '927',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '942',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '942',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '942',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '943',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '943',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '943',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1004',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1004',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1007',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1007',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1022',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1023',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1037',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1038',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1039',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1039',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1039',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1050',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1057',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1067',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1173',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1174',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1176',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1189',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1191',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1191',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1204',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1209',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1209',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1209',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1220',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1220',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1220',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1221',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1221',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1221',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1224',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1224',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1224',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1230',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1235',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1236',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1239',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1240',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1241',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1242',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1242',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1242',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1243',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1244',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1244',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1245',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1246',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1247',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1247',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1247',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1248',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1251',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1251',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1251',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1252',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1252',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1253',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1253',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1253',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1254',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1255',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1255',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1255',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1256',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1257',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1257',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1257',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1258',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1259',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1259',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1259',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1260',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1260',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1260',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1261',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1262',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1262',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1263',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1263',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1264',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1265',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1266',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1267',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1267',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1267',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1268',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1268',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1268',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1269',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1269',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1269',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1270',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1270',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1270',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1272',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1272',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1272',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1273',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1273',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1273',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1274',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1275',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1275',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1276',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1276',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1276',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1277',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1277',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1278',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1279',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1279',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1279',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1280',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1280',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1281',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1281',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1282',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1283',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1284',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1284',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1290',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1290',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1290',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1291',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1291',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1291',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1292',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1292',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1292',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1293',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1293',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1294',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1294',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1294',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1295',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1295',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1295',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1296',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1296',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1296',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1297',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1297',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1297',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1299',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1299',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1299',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1300',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1301',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1302',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1302',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1302',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1303',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1304',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1304',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1311',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1311',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1312',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1312',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1313',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1313',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1313',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1314',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1316',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1316',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1317',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1317',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1317',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1318',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1318',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1318',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1319',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1319',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1319',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1320',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1321',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1321',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1321',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1322',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1323',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1325',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1327',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1328',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1328',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1329',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1329',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1330',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1331',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1331',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1332',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1332',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1333',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1334',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1334',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1335',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1336',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1338',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1339',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1339',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1339',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1341',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1341',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1342',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1342',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1351',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1384',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1384',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1384',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1385',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1385',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1385',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1386',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1386',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1386',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1389',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1389',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1390',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1390',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1390',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1395',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1395',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1395',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1419',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1419',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1420',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1421',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1422',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1423',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1426',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1427',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1427',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1427',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1428',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1428',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1429',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1429',
    'integrity',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1429',
    'availability',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1431',
    'confidentiality',
    0
);

INSERT INTO `cwe_impact_mapping` (
    `cwe_id`,
    `impact_type`,
    `tenant_id`
) VALUES (
    '1434',
    'integrity',
    0
);


-- ============================================================
-- 4. 插入层级关系表
-- ============================================================

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '5',
    'CWE-319',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '6',
    'CWE-334',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '7',
    'CWE-756',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '8',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '9',
    'CWE-266',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '11',
    'CWE-489',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '12',
    'CWE-756',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '13',
    'CWE-260',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '14',
    'CWE-733',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '15',
    'CWE-642',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '15',
    'CWE-610',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '15',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '20',
    'CWE-707',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '22',
    'CWE-706',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '23',
    'CWE-22',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '24',
    'CWE-23',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '25',
    'CWE-23',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '26',
    'CWE-23',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '27',
    'CWE-23',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '28',
    'CWE-23',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '29',
    'CWE-23',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '30',
    'CWE-23',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '31',
    'CWE-23',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '32',
    'CWE-23',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '33',
    'CWE-23',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '34',
    'CWE-23',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '35',
    'CWE-23',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '36',
    'CWE-22',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '37',
    'CWE-36',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '37',
    'CWE-160',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '38',
    'CWE-36',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '39',
    'CWE-36',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '40',
    'CWE-36',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '41',
    'CWE-706',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '41',
    'CWE-863',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '41',
    'CWE-1390',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '42',
    'CWE-41',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '42',
    'CWE-162',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '43',
    'CWE-42',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '43',
    'CWE-163',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '44',
    'CWE-41',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '45',
    'CWE-44',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '45',
    'CWE-165',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '46',
    'CWE-41',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '46',
    'CWE-162',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '47',
    'CWE-41',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '48',
    'CWE-41',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '49',
    'CWE-41',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '49',
    'CWE-162',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '50',
    'CWE-41',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '50',
    'CWE-161',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '51',
    'CWE-41',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '52',
    'CWE-41',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '52',
    'CWE-163',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '53',
    'CWE-41',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '53',
    'CWE-165',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '54',
    'CWE-41',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '54',
    'CWE-162',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '55',
    'CWE-41',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '56',
    'CWE-41',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '56',
    'CWE-155',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '57',
    'CWE-41',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '58',
    'CWE-41',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '59',
    'CWE-706',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '61',
    'CWE-59',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '62',
    'CWE-59',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '64',
    'CWE-59',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '65',
    'CWE-59',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '66',
    'CWE-706',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '67',
    'CWE-66',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '69',
    'CWE-66',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '72',
    'CWE-66',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '73',
    'CWE-642',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '73',
    'CWE-610',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '73',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '74',
    'CWE-707',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '75',
    'CWE-74',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '76',
    'CWE-75',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '77',
    'CWE-74',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '78',
    'CWE-77',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '78',
    'CWE-74',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '79',
    'CWE-74',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '80',
    'CWE-79',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '81',
    'CWE-79',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '82',
    'CWE-83',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '83',
    'CWE-79',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '84',
    'CWE-79',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '85',
    'CWE-79',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '86',
    'CWE-79',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '86',
    'CWE-436',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '87',
    'CWE-79',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '88',
    'CWE-77',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '88',
    'CWE-74',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '89',
    'CWE-943',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '89',
    'CWE-74',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '90',
    'CWE-943',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '91',
    'CWE-74',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '93',
    'CWE-74',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '94',
    'CWE-74',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '94',
    'CWE-913',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '95',
    'CWE-94',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '96',
    'CWE-94',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '97',
    'CWE-96',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '98',
    'CWE-706',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '98',
    'CWE-829',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '99',
    'CWE-74',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '102',
    'CWE-694',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '102',
    'CWE-1173',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '102',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '103',
    'CWE-573',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '103',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '104',
    'CWE-573',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '104',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '105',
    'CWE-1173',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '105',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '106',
    'CWE-1173',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '106',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '107',
    'CWE-1164',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '107',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '108',
    'CWE-1173',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '108',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '109',
    'CWE-1173',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '109',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '110',
    'CWE-1164',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '110',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '111',
    'CWE-695',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '111',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '112',
    'CWE-1286',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '112',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '113',
    'CWE-93',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '113',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '113',
    'CWE-436',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '114',
    'CWE-73',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '114',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '115',
    'CWE-436',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '116',
    'CWE-707',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '117',
    'CWE-116',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '117',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '118',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '119',
    'CWE-118',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '119',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '120',
    'CWE-787',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '120',
    'CWE-119',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '120',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '121',
    'CWE-788',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '121',
    'CWE-787',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '122',
    'CWE-788',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '122',
    'CWE-787',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '123',
    'CWE-787',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '123',
    'CWE-119',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '124',
    'CWE-786',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '124',
    'CWE-787',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '125',
    'CWE-119',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '126',
    'CWE-125',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '126',
    'CWE-788',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '127',
    'CWE-125',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '127',
    'CWE-786',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '128',
    'CWE-682',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '129',
    'CWE-1285',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '129',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '130',
    'CWE-240',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '130',
    'CWE-119',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '131',
    'CWE-682',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '134',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '134',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '135',
    'CWE-682',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '138',
    'CWE-707',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '140',
    'CWE-138',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '141',
    'CWE-140',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '142',
    'CWE-140',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '143',
    'CWE-140',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '144',
    'CWE-140',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '145',
    'CWE-140',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '146',
    'CWE-140',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '147',
    'CWE-138',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '148',
    'CWE-138',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '149',
    'CWE-138',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '150',
    'CWE-138',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '151',
    'CWE-138',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '152',
    'CWE-138',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '153',
    'CWE-138',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '154',
    'CWE-138',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '155',
    'CWE-138',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '156',
    'CWE-138',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '157',
    'CWE-138',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '158',
    'CWE-138',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '159',
    'CWE-138',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '160',
    'CWE-138',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '161',
    'CWE-160',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '162',
    'CWE-138',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '163',
    'CWE-162',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '164',
    'CWE-138',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '165',
    'CWE-164',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '166',
    'CWE-159',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '166',
    'CWE-228',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '167',
    'CWE-159',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '167',
    'CWE-228',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '168',
    'CWE-159',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '168',
    'CWE-228',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '170',
    'CWE-707',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '170',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '172',
    'CWE-707',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '173',
    'CWE-172',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '174',
    'CWE-172',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '174',
    'CWE-675',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '175',
    'CWE-172',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '176',
    'CWE-172',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '177',
    'CWE-172',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '178',
    'CWE-706',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '179',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '179',
    'CWE-696',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '180',
    'CWE-179',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '181',
    'CWE-179',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '182',
    'CWE-707',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '183',
    'CWE-697',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '184',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '184',
    'CWE-1023',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '185',
    'CWE-697',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '186',
    'CWE-185',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '187',
    'CWE-1023',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '188',
    'CWE-1105',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '188',
    'CWE-435',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '190',
    'CWE-682',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '190',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '191',
    'CWE-682',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '192',
    'CWE-681',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '193',
    'CWE-682',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '194',
    'CWE-681',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '195',
    'CWE-681',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '196',
    'CWE-681',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '197',
    'CWE-681',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '198',
    'CWE-188',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '200',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '201',
    'CWE-200',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '202',
    'CWE-1230',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '203',
    'CWE-200',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '204',
    'CWE-203',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '205',
    'CWE-203',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '206',
    'CWE-205',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '207',
    'CWE-205',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '208',
    'CWE-203',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '209',
    'CWE-200',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '209',
    'CWE-755',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '210',
    'CWE-209',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '211',
    'CWE-209',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '212',
    'CWE-669',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '213',
    'CWE-200',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '214',
    'CWE-497',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '215',
    'CWE-200',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '219',
    'CWE-552',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '220',
    'CWE-552',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '221',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '222',
    'CWE-221',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '223',
    'CWE-221',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '224',
    'CWE-221',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '226',
    'CWE-459',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '226',
    'CWE-212',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '228',
    'CWE-703',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '228',
    'CWE-707',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '229',
    'CWE-228',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '230',
    'CWE-229',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '231',
    'CWE-229',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '232',
    'CWE-229',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '233',
    'CWE-228',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '234',
    'CWE-233',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '235',
    'CWE-233',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '236',
    'CWE-233',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '237',
    'CWE-228',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '238',
    'CWE-237',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '239',
    'CWE-237',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '240',
    'CWE-237',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '240',
    'CWE-707',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '241',
    'CWE-228',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '242',
    'CWE-1177',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '243',
    'CWE-573',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '243',
    'CWE-669',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '244',
    'CWE-226',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '245',
    'CWE-695',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '246',
    'CWE-695',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '248',
    'CWE-705',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '248',
    'CWE-755',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '248',
    'CWE-703',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '250',
    'CWE-269',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '250',
    'CWE-657',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '252',
    'CWE-754',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '253',
    'CWE-573',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '253',
    'CWE-754',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '256',
    'CWE-522',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '257',
    'CWE-522',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '258',
    'CWE-260',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '258',
    'CWE-521',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '259',
    'CWE-798',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '260',
    'CWE-522',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '261',
    'CWE-522',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '262',
    'CWE-1390',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '263',
    'CWE-1390',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '266',
    'CWE-269',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '267',
    'CWE-269',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '268',
    'CWE-269',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '269',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '270',
    'CWE-269',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '271',
    'CWE-269',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '272',
    'CWE-271',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '273',
    'CWE-754',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '273',
    'CWE-271',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '274',
    'CWE-755',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '274',
    'CWE-269',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '276',
    'CWE-732',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '277',
    'CWE-732',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '278',
    'CWE-732',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '279',
    'CWE-732',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '280',
    'CWE-755',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '281',
    'CWE-732',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '282',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '283',
    'CWE-282',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '285',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '286',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '287',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '288',
    'CWE-306',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '288',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '289',
    'CWE-1390',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '290',
    'CWE-1390',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '290',
    'CWE-287',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '291',
    'CWE-290',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '291',
    'CWE-923',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '293',
    'CWE-290',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '294',
    'CWE-1390',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '294',
    'CWE-287',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '295',
    'CWE-287',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '296',
    'CWE-295',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '296',
    'CWE-573',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '297',
    'CWE-923',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '297',
    'CWE-295',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '298',
    'CWE-295',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '298',
    'CWE-672',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '299',
    'CWE-295',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '299',
    'CWE-404',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '300',
    'CWE-923',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '301',
    'CWE-1390',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '302',
    'CWE-1390',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '302',
    'CWE-807',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '303',
    'CWE-1390',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '304',
    'CWE-303',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '304',
    'CWE-573',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '305',
    'CWE-1390',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '306',
    'CWE-287',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '307',
    'CWE-1390',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '307',
    'CWE-287',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '307',
    'CWE-799',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '308',
    'CWE-1390',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '308',
    'CWE-654',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '309',
    'CWE-1390',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '309',
    'CWE-654',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '311',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '312',
    'CWE-311',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '312',
    'CWE-922',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '313',
    'CWE-312',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '314',
    'CWE-312',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '315',
    'CWE-312',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '316',
    'CWE-312',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '317',
    'CWE-312',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '318',
    'CWE-312',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '319',
    'CWE-311',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '321',
    'CWE-798',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '322',
    'CWE-306',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '323',
    'CWE-344',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '324',
    'CWE-672',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '325',
    'CWE-1240',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '325',
    'CWE-573',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '326',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '327',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '328',
    'CWE-326',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '328',
    'CWE-327',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '329',
    'CWE-1204',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '329',
    'CWE-573',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '330',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '331',
    'CWE-330',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '332',
    'CWE-331',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '333',
    'CWE-331',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '333',
    'CWE-755',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '334',
    'CWE-330',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '335',
    'CWE-330',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '336',
    'CWE-335',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '337',
    'CWE-335',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '338',
    'CWE-330',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '339',
    'CWE-335',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '340',
    'CWE-330',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '341',
    'CWE-340',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '342',
    'CWE-340',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '343',
    'CWE-340',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '344',
    'CWE-330',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '345',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '346',
    'CWE-345',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '346',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '347',
    'CWE-345',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '348',
    'CWE-345',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '349',
    'CWE-345',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '350',
    'CWE-290',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '350',
    'CWE-807',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '351',
    'CWE-345',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '352',
    'CWE-345',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '353',
    'CWE-345',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '354',
    'CWE-345',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '354',
    'CWE-754',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '356',
    'CWE-221',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '357',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '358',
    'CWE-573',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '358',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '359',
    'CWE-200',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '360',
    'CWE-345',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '362',
    'CWE-662',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '363',
    'CWE-367',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '364',
    'CWE-362',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '366',
    'CWE-362',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '366',
    'CWE-662',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '367',
    'CWE-362',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '368',
    'CWE-362',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '369',
    'CWE-682',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '370',
    'CWE-299',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '372',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '374',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '375',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '377',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '378',
    'CWE-377',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '379',
    'CWE-377',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '382',
    'CWE-705',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '383',
    'CWE-695',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '384',
    'CWE-610',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '385',
    'CWE-514',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '386',
    'CWE-706',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '390',
    'CWE-755',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '391',
    'CWE-754',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '391',
    'CWE-703',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '392',
    'CWE-755',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '392',
    'CWE-684',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '392',
    'CWE-703',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '393',
    'CWE-684',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '393',
    'CWE-703',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '394',
    'CWE-754',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '395',
    'CWE-705',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '395',
    'CWE-755',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '396',
    'CWE-705',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '396',
    'CWE-755',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '396',
    'CWE-221',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '397',
    'CWE-705',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '397',
    'CWE-221',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '397',
    'CWE-703',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '400',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '401',
    'CWE-772',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '401',
    'CWE-404',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '402',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '403',
    'CWE-402',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '404',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '405',
    'CWE-400',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '406',
    'CWE-405',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '407',
    'CWE-405',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '408',
    'CWE-405',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '408',
    'CWE-696',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '409',
    'CWE-405',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '410',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '412',
    'CWE-667',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '413',
    'CWE-667',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '414',
    'CWE-667',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '415',
    'CWE-825',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '415',
    'CWE-1341',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '415',
    'CWE-672',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '415',
    'CWE-666',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '416',
    'CWE-825',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '416',
    'CWE-672',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '419',
    'CWE-923',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '420',
    'CWE-923',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '421',
    'CWE-420',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '421',
    'CWE-362',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '422',
    'CWE-420',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '422',
    'CWE-360',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '424',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '424',
    'CWE-638',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '425',
    'CWE-862',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '425',
    'CWE-288',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '425',
    'CWE-424',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '426',
    'CWE-642',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '426',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '426',
    'CWE-673',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '427',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '428',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '430',
    'CWE-691',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '431',
    'CWE-691',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '432',
    'CWE-364',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '433',
    'CWE-219',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '434',
    'CWE-669',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '436',
    'CWE-435',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '437',
    'CWE-436',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '439',
    'CWE-435',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '440',
    'CWE-684',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '441',
    'CWE-610',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '444',
    'CWE-436',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '446',
    'CWE-684',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '447',
    'CWE-446',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '447',
    'CWE-671',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '448',
    'CWE-446',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '449',
    'CWE-446',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '450',
    'CWE-357',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '451',
    'CWE-684',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '451',
    'CWE-221',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '453',
    'CWE-1188',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '454',
    'CWE-1419',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '455',
    'CWE-665',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '455',
    'CWE-705',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '455',
    'CWE-636',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '456',
    'CWE-909',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '456',
    'CWE-665',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '457',
    'CWE-908',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '457',
    'CWE-665',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '459',
    'CWE-404',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '460',
    'CWE-459',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '460',
    'CWE-755',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '462',
    'CWE-694',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '463',
    'CWE-707',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '464',
    'CWE-138',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '466',
    'CWE-119',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '466',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '467',
    'CWE-131',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '468',
    'CWE-682',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '469',
    'CWE-682',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '470',
    'CWE-913',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '470',
    'CWE-610',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '470',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '471',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '472',
    'CWE-642',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '472',
    'CWE-471',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '473',
    'CWE-471',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '474',
    'CWE-758',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '475',
    'CWE-573',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '476',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '476',
    'CWE-754',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '477',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '478',
    'CWE-1023',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '479',
    'CWE-828',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '479',
    'CWE-663',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '480',
    'CWE-670',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '481',
    'CWE-480',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '482',
    'CWE-480',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '483',
    'CWE-670',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '484',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '484',
    'CWE-670',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '486',
    'CWE-1025',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '487',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '488',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '489',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '491',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '492',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '493',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '494',
    'CWE-345',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '494',
    'CWE-669',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '495',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '496',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '497',
    'CWE-200',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '498',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '499',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '500',
    'CWE-493',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '501',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '502',
    'CWE-913',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '506',
    'CWE-912',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '507',
    'CWE-506',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '508',
    'CWE-507',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '509',
    'CWE-507',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '510',
    'CWE-506',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '511',
    'CWE-506',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '512',
    'CWE-506',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '514',
    'CWE-1229',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '515',
    'CWE-514',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '520',
    'CWE-266',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '521',
    'CWE-1391',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '521',
    'CWE-287',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '522',
    'CWE-1390',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '522',
    'CWE-287',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '522',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '523',
    'CWE-522',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '524',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '525',
    'CWE-524',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '526',
    'CWE-312',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '527',
    'CWE-552',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '528',
    'CWE-552',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '529',
    'CWE-552',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '530',
    'CWE-552',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '531',
    'CWE-540',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '532',
    'CWE-538',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '532',
    'CWE-200',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '535',
    'CWE-211',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '536',
    'CWE-211',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '537',
    'CWE-211',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '538',
    'CWE-200',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '539',
    'CWE-552',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '540',
    'CWE-538',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '541',
    'CWE-540',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '543',
    'CWE-820',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '543',
    'CWE-662',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '544',
    'CWE-755',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '546',
    'CWE-1078',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '547',
    'CWE-1078',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '548',
    'CWE-497',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '549',
    'CWE-522',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '550',
    'CWE-209',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '551',
    'CWE-863',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '551',
    'CWE-696',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '552',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '552',
    'CWE-285',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '553',
    'CWE-552',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '554',
    'CWE-1173',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '555',
    'CWE-260',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '556',
    'CWE-266',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '558',
    'CWE-663',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '560',
    'CWE-687',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '561',
    'CWE-1164',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '562',
    'CWE-758',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '563',
    'CWE-1164',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '564',
    'CWE-89',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '565',
    'CWE-642',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '565',
    'CWE-669',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '565',
    'CWE-602',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '566',
    'CWE-639',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '567',
    'CWE-820',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '567',
    'CWE-662',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '568',
    'CWE-573',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '568',
    'CWE-459',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '570',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '571',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '572',
    'CWE-821',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '573',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '574',
    'CWE-695',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '574',
    'CWE-821',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '575',
    'CWE-695',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '576',
    'CWE-695',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '577',
    'CWE-573',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '578',
    'CWE-573',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '579',
    'CWE-573',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '580',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '580',
    'CWE-573',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '581',
    'CWE-573',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '581',
    'CWE-697',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '582',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '583',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '584',
    'CWE-705',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '585',
    'CWE-1071',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '586',
    'CWE-1076',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '587',
    'CWE-344',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '587',
    'CWE-758',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '588',
    'CWE-704',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '588',
    'CWE-758',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '589',
    'CWE-474',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '590',
    'CWE-762',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '591',
    'CWE-413',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '593',
    'CWE-666',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '593',
    'CWE-1390',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '594',
    'CWE-1076',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '595',
    'CWE-1025',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '597',
    'CWE-595',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '597',
    'CWE-480',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '598',
    'CWE-201',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '599',
    'CWE-295',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '600',
    'CWE-248',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '601',
    'CWE-610',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '602',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '603',
    'CWE-1390',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '603',
    'CWE-602',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '605',
    'CWE-675',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '605',
    'CWE-666',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '606',
    'CWE-1284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '607',
    'CWE-471',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '608',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '609',
    'CWE-667',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '610',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '611',
    'CWE-610',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '612',
    'CWE-1230',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '613',
    'CWE-672',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '614',
    'CWE-319',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '615',
    'CWE-540',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '616',
    'CWE-345',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '617',
    'CWE-705',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '617',
    'CWE-670',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '618',
    'CWE-749',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '619',
    'CWE-402',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '620',
    'CWE-1390',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '621',
    'CWE-914',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '622',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '623',
    'CWE-267',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '624',
    'CWE-77',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '625',
    'CWE-185',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '626',
    'CWE-147',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '626',
    'CWE-436',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '627',
    'CWE-914',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '628',
    'CWE-573',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '636',
    'CWE-657',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '636',
    'CWE-755',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '637',
    'CWE-657',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '638',
    'CWE-657',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '638',
    'CWE-862',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '639',
    'CWE-863',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '639',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '640',
    'CWE-1390',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '640',
    'CWE-287',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '641',
    'CWE-99',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '642',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '643',
    'CWE-943',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '643',
    'CWE-91',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '644',
    'CWE-116',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '645',
    'CWE-287',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '646',
    'CWE-345',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '647',
    'CWE-863',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '647',
    'CWE-180',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '648',
    'CWE-269',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '649',
    'CWE-345',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '650',
    'CWE-436',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '651',
    'CWE-538',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '652',
    'CWE-943',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '652',
    'CWE-91',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '653',
    'CWE-657',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '653',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '654',
    'CWE-657',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '654',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '655',
    'CWE-657',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '655',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '656',
    'CWE-657',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '656',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '657',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '662',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '662',
    'CWE-691',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '663',
    'CWE-662',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '665',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '666',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '667',
    'CWE-662',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '668',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '669',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '670',
    'CWE-691',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '671',
    'CWE-657',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '672',
    'CWE-666',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '673',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '674',
    'CWE-834',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '675',
    'CWE-573',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '676',
    'CWE-1177',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '680',
    'CWE-190',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '681',
    'CWE-704',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '683',
    'CWE-628',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '684',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '685',
    'CWE-628',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '686',
    'CWE-628',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '687',
    'CWE-628',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '688',
    'CWE-628',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '689',
    'CWE-362',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '690',
    'CWE-252',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '692',
    'CWE-184',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '694',
    'CWE-99',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '694',
    'CWE-573',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '695',
    'CWE-573',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '696',
    'CWE-691',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '698',
    'CWE-705',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '698',
    'CWE-670',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '704',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '705',
    'CWE-691',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '706',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '708',
    'CWE-282',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '732',
    'CWE-285',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '732',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '733',
    'CWE-1038',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '749',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '754',
    'CWE-703',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '755',
    'CWE-703',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '756',
    'CWE-755',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '757',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '758',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '759',
    'CWE-916',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '760',
    'CWE-916',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '761',
    'CWE-763',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '761',
    'CWE-404',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '762',
    'CWE-763',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '762',
    'CWE-404',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '763',
    'CWE-404',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '764',
    'CWE-667',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '764',
    'CWE-675',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '764',
    'CWE-662',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '765',
    'CWE-667',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '765',
    'CWE-675',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '766',
    'CWE-732',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '766',
    'CWE-1061',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '767',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '768',
    'CWE-691',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '770',
    'CWE-400',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '770',
    'CWE-665',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '771',
    'CWE-400',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '772',
    'CWE-404',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '773',
    'CWE-771',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '774',
    'CWE-770',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '775',
    'CWE-772',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '775',
    'CWE-404',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '776',
    'CWE-674',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '776',
    'CWE-405',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '777',
    'CWE-625',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '778',
    'CWE-223',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '779',
    'CWE-400',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '780',
    'CWE-327',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '781',
    'CWE-1285',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '782',
    'CWE-749',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '783',
    'CWE-670',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '784',
    'CWE-807',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '784',
    'CWE-565',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '785',
    'CWE-676',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '785',
    'CWE-120',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '785',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '786',
    'CWE-119',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '787',
    'CWE-119',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '788',
    'CWE-119',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '789',
    'CWE-770',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '790',
    'CWE-138',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '791',
    'CWE-790',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '792',
    'CWE-791',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '793',
    'CWE-792',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '794',
    'CWE-792',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '795',
    'CWE-791',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '796',
    'CWE-795',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '797',
    'CWE-795',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '798',
    'CWE-1391',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '798',
    'CWE-287',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '798',
    'CWE-344',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '798',
    'CWE-671',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '799',
    'CWE-691',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '804',
    'CWE-863',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '804',
    'CWE-1390',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '805',
    'CWE-119',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '806',
    'CWE-805',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '807',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '820',
    'CWE-662',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '821',
    'CWE-662',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '822',
    'CWE-119',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '823',
    'CWE-119',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '824',
    'CWE-119',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '825',
    'CWE-119',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '825',
    'CWE-672',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '826',
    'CWE-666',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '827',
    'CWE-706',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '827',
    'CWE-829',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '828',
    'CWE-364',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '829',
    'CWE-669',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '830',
    'CWE-829',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '831',
    'CWE-364',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '832',
    'CWE-667',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '833',
    'CWE-667',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '833',
    'CWE-662',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '834',
    'CWE-691',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '835',
    'CWE-834',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '836',
    'CWE-1390',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '837',
    'CWE-799',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '838',
    'CWE-116',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '839',
    'CWE-1023',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '841',
    'CWE-691',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '842',
    'CWE-286',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '843',
    'CWE-704',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '862',
    'CWE-285',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '862',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '863',
    'CWE-285',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '863',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '908',
    'CWE-665',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '909',
    'CWE-665',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '910',
    'CWE-672',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '911',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '912',
    'CWE-684',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '913',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '914',
    'CWE-99',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '914',
    'CWE-913',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '915',
    'CWE-913',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '916',
    'CWE-328',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '916',
    'CWE-327',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '917',
    'CWE-77',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '917',
    'CWE-74',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '918',
    'CWE-441',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '918',
    'CWE-610',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '920',
    'CWE-400',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '921',
    'CWE-922',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '922',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '923',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '924',
    'CWE-345',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '925',
    'CWE-940',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '926',
    'CWE-285',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '927',
    'CWE-285',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '927',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '939',
    'CWE-862',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '939',
    'CWE-940',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '940',
    'CWE-923',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '940',
    'CWE-346',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '941',
    'CWE-923',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '942',
    'CWE-863',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '942',
    'CWE-923',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '942',
    'CWE-183',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '943',
    'CWE-74',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1004',
    'CWE-732',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1007',
    'CWE-451',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1021',
    'CWE-441',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1021',
    'CWE-610',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1021',
    'CWE-451',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1022',
    'CWE-266',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1023',
    'CWE-697',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1024',
    'CWE-697',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1025',
    'CWE-697',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1037',
    'CWE-1038',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1038',
    'CWE-435',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1038',
    'CWE-758',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1039',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1039',
    'CWE-697',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1041',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1042',
    'CWE-1176',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1043',
    'CWE-1093',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1044',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1045',
    'CWE-1076',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1046',
    'CWE-1176',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1047',
    'CWE-1120',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1048',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1049',
    'CWE-1176',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1050',
    'CWE-405',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1051',
    'CWE-1419',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1052',
    'CWE-1419',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1053',
    'CWE-1059',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1054',
    'CWE-1061',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1055',
    'CWE-1093',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1056',
    'CWE-1120',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1057',
    'CWE-1061',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1058',
    'CWE-662',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1059',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1060',
    'CWE-1120',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1061',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1062',
    'CWE-1061',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1063',
    'CWE-1176',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1064',
    'CWE-1120',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1065',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1066',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1067',
    'CWE-1176',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1068',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1069',
    'CWE-1071',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1070',
    'CWE-1076',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1071',
    'CWE-1164',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1072',
    'CWE-405',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1073',
    'CWE-405',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1074',
    'CWE-1093',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1075',
    'CWE-1120',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1076',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1077',
    'CWE-697',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1078',
    'CWE-1076',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1079',
    'CWE-1076',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1080',
    'CWE-1120',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1082',
    'CWE-1076',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1083',
    'CWE-1061',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1084',
    'CWE-405',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1085',
    'CWE-1078',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1086',
    'CWE-1093',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1087',
    'CWE-1076',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1088',
    'CWE-821',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1089',
    'CWE-405',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1090',
    'CWE-1061',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1091',
    'CWE-772',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1091',
    'CWE-1076',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1092',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1093',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1094',
    'CWE-405',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1095',
    'CWE-1120',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1096',
    'CWE-820',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1096',
    'CWE-662',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1097',
    'CWE-1076',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1097',
    'CWE-595',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1098',
    'CWE-1076',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1099',
    'CWE-1078',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1100',
    'CWE-1061',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1101',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1102',
    'CWE-758',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1103',
    'CWE-758',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1104',
    'CWE-1357',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1105',
    'CWE-758',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1105',
    'CWE-1061',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1106',
    'CWE-1078',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1107',
    'CWE-1078',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1108',
    'CWE-1076',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1109',
    'CWE-1078',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1110',
    'CWE-1059',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1111',
    'CWE-1059',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1112',
    'CWE-1059',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1113',
    'CWE-1078',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1114',
    'CWE-1078',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1115',
    'CWE-1078',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1116',
    'CWE-1078',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1117',
    'CWE-1078',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1118',
    'CWE-1059',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1119',
    'CWE-1120',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1120',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1121',
    'CWE-1120',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1122',
    'CWE-1120',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1123',
    'CWE-1120',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1124',
    'CWE-1120',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1125',
    'CWE-1120',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1126',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1127',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1164',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1173',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1174',
    'CWE-1173',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1176',
    'CWE-405',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1177',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1188',
    'CWE-1419',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1188',
    'CWE-344',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1188',
    'CWE-665',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1189',
    'CWE-653',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1189',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1190',
    'CWE-696',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1191',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1192',
    'CWE-657',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1193',
    'CWE-696',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1204',
    'CWE-330',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1209',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1220',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1221',
    'CWE-1419',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1222',
    'CWE-1220',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1223',
    'CWE-362',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1224',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1229',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1230',
    'CWE-285',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1231',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1232',
    'CWE-667',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1233',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1233',
    'CWE-667',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1234',
    'CWE-667',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1235',
    'CWE-400',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1236',
    'CWE-74',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1239',
    'CWE-226',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1240',
    'CWE-327',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1241',
    'CWE-330',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1242',
    'CWE-912',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1243',
    'CWE-1263',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1244',
    'CWE-863',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1245',
    'CWE-684',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1246',
    'CWE-400',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1247',
    'CWE-1384',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1248',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1249',
    'CWE-1250',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1250',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1251',
    'CWE-1250',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1252',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1253',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1254',
    'CWE-208',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1254',
    'CWE-697',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1255',
    'CWE-1300',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1256',
    'CWE-285',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1257',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1258',
    'CWE-212',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1259',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1259',
    'CWE-1294',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1260',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1261',
    'CWE-1384',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1262',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1263',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1264',
    'CWE-821',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1265',
    'CWE-662',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1266',
    'CWE-404',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1267',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1268',
    'CWE-266',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1269',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1270',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1270',
    'CWE-1294',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1271',
    'CWE-909',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1272',
    'CWE-226',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1273',
    'CWE-200',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1274',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1275',
    'CWE-923',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1276',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1277',
    'CWE-1329',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1278',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1279',
    'CWE-696',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1279',
    'CWE-665',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1280',
    'CWE-696',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1280',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1281',
    'CWE-691',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1282',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1283',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1284',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1285',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1286',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1287',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1288',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1289',
    'CWE-20',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1290',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1290',
    'CWE-1294',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1291',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1292',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1292',
    'CWE-1294',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1293',
    'CWE-345',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1294',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1295',
    'CWE-200',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1296',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1297',
    'CWE-285',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1298',
    'CWE-362',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1299',
    'CWE-420',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1299',
    'CWE-288',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1300',
    'CWE-203',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1301',
    'CWE-226',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1302',
    'CWE-1294',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1303',
    'CWE-1189',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1303',
    'CWE-203',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1304',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1310',
    'CWE-1329',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1311',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1312',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1313',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1314',
    'CWE-862',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1315',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1316',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1317',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1318',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1319',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1320',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1321',
    'CWE-915',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1321',
    'CWE-913',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1322',
    'CWE-834',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1323',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1325',
    'CWE-770',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1326',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1327',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1328',
    'CWE-285',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1329',
    'CWE-1357',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1329',
    'CWE-664',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1330',
    'CWE-1301',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1331',
    'CWE-653',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1331',
    'CWE-668',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1332',
    'CWE-1384',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1333',
    'CWE-407',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1334',
    'CWE-284',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1335',
    'CWE-682',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1336',
    'CWE-94',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1338',
    'CWE-693',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1339',
    'CWE-682',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1341',
    'CWE-675',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1342',
    'CWE-226',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1351',
    'CWE-1384',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1357',
    'CWE-710',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1384',
    'CWE-703',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1385',
    'CWE-346',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1386',
    'CWE-59',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1389',
    'CWE-704',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1390',
    'CWE-287',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1391',
    'CWE-1390',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1392',
    'CWE-1391',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1393',
    'CWE-1392',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1394',
    'CWE-1392',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1395',
    'CWE-657',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1419',
    'CWE-665',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1420',
    'CWE-669',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1421',
    'CWE-1420',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1422',
    'CWE-1420',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1423',
    'CWE-1420',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1426',
    'CWE-707',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1427',
    'CWE-77',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1428',
    'CWE-319',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1429',
    'CWE-223',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1431',
    'CWE-200',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1434',
    'CWE-440',
    'ChildOf',
    0
);

INSERT INTO `cwe_hierarchy` (
    `cwe_id`,
    `parent_cwe_id`,
    `relationship_type`,
    `tenant_id`
) VALUES (
    '1434',
    'CWE-665',
    'ChildOf',
    0
);
