package org.ruoyi.knowledge.curation.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 条目关联片段详情 VO（含片段内容、文档名称等，用于前端展示）
 */
@Data
public class ItemFragmentDetailVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 片段ID（用于移除关联等操作） */
    private Long id;

    /** 片段ID（与 id 相同，兼容前端 fragmentId 字段） */
    private Long fragmentId;

    /** 片段内容 */
    private String content;

    /** 来源文档名称 */
    private String documentName;

    /** 来源文档名称（别名，兼容 sourceName） */
    private String sourceName;

    /** 关联相关度评分（AI 匹配时） */
    private Double relevanceScore;

    /** 关联来源：manual（人工）/ ai（AI 建议） */
    private String associationType;

    /** 创建时间 */
    private Date createTime;
}
