package org.ruoyi.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * 知识条目对象 knowledge_item
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("knowledge_item")
public class KnowledgeItem extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 知识条目UUID（唯一标识）
     */
    private String itemUuid;

    /**
     * 知识库ID（关联 knowledge_info.kid）
     */
    private String kid;

    /**
     * 条目标题
     */
    private String title;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 漏洞类型（SQL注入、缓冲区溢出等）
     */
    private String vulnerabilityType;

    /**
     * 适用语言（Java、C++、Python等）
     */
    private String language;

    /**
     * 风险等级（高危、中危、低危）
     */
    private String severity;

    /**
     * CVSS 向量字符串
     */
    private String cvssVector;

    /**
     * CVSS 数值分数（0.0-10.0）
     */
    private BigDecimal cvssScore;

    /**
     * CVSS 版本号（如 4.0）
     */
    private String cvssVersion;

    /**
     * 问题描述（漏洞触发场景、原理说明）
     */
    private String problemDescription;

    /**
     * 修复方案（修复代码、步骤说明）
     */
    private String fixSolution;

    /**
     * 示例代码（错误示例、正确示例）
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
     * 当前版本ID（关联 knowledge_item_history.id，用于快速查询）
     */
    private Long currentVersionId;

    /**
     * 占用字节数（问题描述+修复方案+示例代码的总大小）
     */
    private Long dataSize;

    /**
     * 知识片段数量
     */
    private Integer fragmentCount;

    /**
     * 状态（draft-草稿、published-已发布、archived-已归档）
     */
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
     * 来源类型（manual-手动录入、feedback-用户反馈、import-批量导入）
     */
    private String sourceType;

    /**
     * 来源任务ID（如果是来自检测任务）
     */
    private Long sourceTaskId;

    /**
     * 来源反馈ID（如果是来自用户反馈）
     */
    private Long sourceFeedbackId;

    /**
     * 删除标志（0-存在 1-删除）
     */
    @TableField("del_flag")
    private String delFlag;

    /**
     * 租户ID
     */
    private Long tenantId;
}
