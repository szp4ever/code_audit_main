package org.ruoyi.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 匹配详情视图对象
 *
 * @author ruoyi
 * @date 2026-01-20
 */
@Data
public class MatchDetailVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 片段索引（在chunkList中的位置）
     */
    private Integer fragmentIndex;

    /**
     * 片段内容（前100字符）
     */
    private String fragmentPreview;

    /**
     * 匹配到的知识条目UUID
     */
    private String itemUuid;

    /**
     * 匹配到的知识条目标题
     */
    private String itemTitle;

    /**
     * 相似度（0.0-1.0）
     */
    private Double similarity;
}
