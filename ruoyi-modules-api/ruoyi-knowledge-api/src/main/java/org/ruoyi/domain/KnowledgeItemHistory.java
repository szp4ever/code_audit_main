package org.ruoyi.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;

import java.io.Serial;

/**
 * 知识条目版本历史对象 knowledge_item_history
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("knowledge_item_history")
public class KnowledgeItemHistory extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 知识条目UUID（关联 knowledge_item.item_uuid）
     */
    private String itemUuid;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 是否当前版本（0-否 1-是）
     */
    private String isCurrent;

    /**
     * 条目标题
     */
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
    private String changeReason;

    /**
     * 变更类型（create-创建、update-更新、delete-删除）
     */
    private String changeType;

    /**
     * 变更人ID
     */
    private Long changedBy;

    /**
     * 变更人姓名
     */
    private String changedByName;

    /**
     * 变更时间
     */
    private java.util.Date changedAt;
}
