package org.ruoyi.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.domain.KnowledgeItem;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 知识条目视图对象 knowledge_item
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = KnowledgeItem.class)
public class KnowledgeItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "ID")
    private Long id;

    /**
     * 知识条目UUID
     */
    @ExcelProperty(value = "条目UUID")
    private String itemUuid;

    /**
     * 知识库ID
     */
    @ExcelProperty(value = "知识库ID")
    private String kid;

    /**
     * 条目标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 摘要
     */
    @ExcelProperty(value = "摘要")
    private String summary;

    /**
     * 漏洞类型（单个，保留用于兼容和快速查询）
     */
    @ExcelProperty(value = "漏洞类型")
    private String vulnerabilityType;

    /**
     * 漏洞类型列表（多个CWE ID，如 ["CWE-89", "CWE-79"]）
     */
    private List<String> vulnerabilityTypes;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 适用语言
     */
    @ExcelProperty(value = "适用语言")
    private String language;

    /**
     * 风险等级
     */
    @ExcelProperty(value = "风险等级")
    private String severity;

    /**
     * CVSS 向量字符串
     */
    private String cvssVector;

    /**
     * CVSS 数值分数
     */
    private BigDecimal cvssScore;

    /**
     * CVSS 版本号
     */
    private String cvssVersion;

    /**
     * 问题描述
     */
    private String problemDescription;

    /**
     * 修复方案
     */
    private String fixSolution;

    /**
     * 示例代码
     */
    private String exampleCode;

    /**
     * 参考链接
     */
    private String referenceLink;

    /**
     * 当前版本号
     */
    private Integer currentVersion;

    /**
     * 版本总数
     */
    private Integer versionCount;

    /**
     * 当前版本ID
     */
    private Long currentVersionId;

    /**
     * 占用字节数
     */
    private Long dataSize;

    /**
     * 知识片段数量
     */
    private Integer fragmentCount;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 发布时间
     */
    private java.util.Date publishTime;

    /**
     * 归档时间
     */
    private java.util.Date archiveTime;

    /**
     * 来源类型
     */
    private String sourceType;

    /**
     * 来源任务ID
     */
    private Long sourceTaskId;

    /**
     * 来源反馈ID
     */
    private Long sourceFeedbackId;

    /**
     * 删除标志（0-存在 1-删除）
     */
    private String delFlag;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private java.util.Date createTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private java.util.Date updateTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建部门
     */
    private Long createDept;

    /**
     * 更新人
     */
    private Long updateBy;
}
