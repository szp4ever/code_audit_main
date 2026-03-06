package org.ruoyi.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 相似度匹配结果视图对象
 *
 * @author ruoyi
 * @date 2026-01-20
 */
@Data
public class MatchResultVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 匹配到的片段数量（相似度 >= 阈值）
     */
    private Integer matchedCount;

    /**
     * 创建新条目的片段数量（相似度 < 阈值）
     */
    private Integer newItemCount;

    /**
     * 匹配详情列表
     */
    private List<MatchDetailVo> matchDetails;
}
