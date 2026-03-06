package org.ruoyi.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.domain.KnowledgeItemHistory;

import java.io.Serial;
import java.io.Serializable;

/**
 * 知识条目版本历史视图对象 knowledge_item_history
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = KnowledgeItemHistory.class)
public class KnowledgeItemHistoryVo implements Serializable {

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
     * 版本号
     */
    @ExcelProperty(value = "版本号")
    private Integer version;

    /**
     * 是否当前版本
     */
    @ExcelProperty(value = "是否当前版本")
    private String isCurrent;

    /**
     * 条目标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 漏洞类型
     */
    private String vulnerabilityType;

    /**
     * 适用语言
     */
    private String language;

    /**
     * 风险等级
     */
    private String severity;

    /**
     * 规则ID
     */
    private String ruleId;

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
     * 变更原因
     */
    @ExcelProperty(value = "变更原因")
    private String changeReason;

    /**
     * 变更类型
     */
    @ExcelProperty(value = "变更类型")
    private String changeType;

    /**
     * 变更人ID
     */
    @ExcelProperty(value = "变更人ID")
    private Long changedBy;

    /**
     * 变更人姓名
     */
    @ExcelProperty(value = "变更人姓名")
    private String changedByName;

    /**
     * 变更时间
     */
    @ExcelProperty(value = "变更时间")
    private java.util.Date changedAt;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;
}
