-- 10. 初始化 RuoYi 字典数据
-- ----------------------------

-- 语言类型字典
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(100, '知识库语言类型', 'knowledge_language', '0', 1, NOW(), '知识库和知识条目使用的编程语言类型');

INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(1001, 1, 'Java', 'java', 'knowledge_language', '', 'primary', 'N', '0', 1, NOW(), 'Java 语言'),
(1002, 2, 'JavaScript', 'javascript', 'knowledge_language', '', 'primary', 'N', '0', 1, NOW(), 'JavaScript 语言'),
(1003, 3, 'Python', 'python', 'knowledge_language', '', 'primary', 'N', '0', 1, NOW(), 'Python 语言'),
(1004, 4, 'Go', 'go', 'knowledge_language', '', 'primary', 'N', '0', 1, NOW(), 'Go 语言'),
(1005, 5, 'Rust', 'rust', 'knowledge_language', '', 'primary', 'N', '0', 1, NOW(), 'Rust 语言'),
(1006, 6, 'PHP', 'php', 'knowledge_language', '', 'primary', 'N', '0', 1, NOW(), 'PHP 语言'),
(1007, 7, 'C#', 'csharp', 'knowledge_language', '', 'primary', 'N', '0', 1, NOW(), 'C# 语言'),
(1008, 8, 'C++', 'cpp', 'knowledge_language', '', 'primary', 'N', '0', 1, NOW(), 'C++ 语言');

-- 风险等级字典
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(101, '知识库风险等级', 'knowledge_severity', '0', 1, NOW(), '知识条目对应的风险等级（对齐 CVSS 定性严重性等级）');

INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(1011, 1, '无', 'none', 'knowledge_severity', '', 'default', 'N', '0', 1, NOW(), 'CVSS 无风险 (0.0)'),
(1012, 2, '低', 'low', 'knowledge_severity', '', 'info', 'N', '0', 1, NOW(), 'CVSS 低风险 (0.1 - 3.9)'),
(1013, 3, '中', 'medium', 'knowledge_severity', '', 'warning', 'N', '0', 1, NOW(), 'CVSS 中等风险 (4.0 - 6.9)'),
(1014, 4, '高', 'high', 'knowledge_severity', '', 'danger', 'N', '0', 1, NOW(), 'CVSS 高风险 (7.0 - 8.9)'),
(1015, 5, '严重', 'critical', 'knowledge_severity', '', 'danger', 'N', '0', 1, NOW(), 'CVSS 严重风险 (9.0 - 10.0)');

-- 漏洞类型字典
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(102, '知识库漏洞类型', 'knowledge_vulnerability_type', '0', 1, NOW(), '知识条目对应的漏洞类型（以 CWE 为主键，由标准表 cwe_reference 导入）');

-- 知识条目状态字典
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(103, '知识条目状态', 'knowledge_item_status', '0', 1, NOW(), '知识条目的生命周期状态');

INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `remark`)
VALUES 
(1031, 1, '草稿', 'draft', 'knowledge_item_status', '', 'info', 'N', '0', 1, NOW(), '草稿状态'),
(1032, 2, '审核中', 'review', 'knowledge_item_status', '', 'warning', 'N', '0', 1, NOW(), '审核中状态'),
(1033, 3, '已发布', 'published', 'knowledge_item_status', '', 'success', 'N', '0', 1, NOW(), '已发布状态'),
(1034, 4, '已归档', 'archived', 'knowledge_item_status', '', 'default', 'N', '0', 1, NOW(), '已归档状态');

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
