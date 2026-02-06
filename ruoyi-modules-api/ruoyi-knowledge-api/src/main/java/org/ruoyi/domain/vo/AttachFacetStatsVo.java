package org.ruoyi.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 附件分面统计视图对象
 * 包含筛选选项（基于原始数据）和统计计数（基于筛选结果）
 */
@Data
public class AttachFacetStatsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 筛选选项（基于原始数据，不受筛选影响）
     */
    private FacetOptionsVo options = new FacetOptionsVo();

    /**
     * 统计计数（基于筛选结果，动态更新）
     */
    private FacetCountsVo counts = new FacetCountsVo();

    @Data
    public static class FacetOptionsVo implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 文件类型选项
         */
        private Set<String> docTypes = new java.util.HashSet<>();

        /**
         * 创建人选项
         */
        private Set<String> creators = new java.util.HashSet<>();

        /**
         * 条目数量范围选项（动态生成）
         */
        private java.util.List<ItemCountRangeVo> itemCountRanges = new java.util.ArrayList<>();
    }

    @Data
    public static class FacetCountsVo implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 文件类型计数
         */
        private Map<String, Long> docTypes = new HashMap<>();

        /**
         * 拆解图片状态计数
         */
        private Map<Integer, Long> picStatuses = new HashMap<>();

        /**
         * 分析图片状态计数
         */
        private Map<Integer, Long> picAnysStatuses = new HashMap<>();

        /**
         * 向量化状态计数
         */
        private Map<Integer, Long> vectorStatuses = new HashMap<>();

        /**
         * 创建人计数
         */
        private Map<String, Long> creators = new HashMap<>();

        /**
         * 条目数量范围计数
         */
        private Map<String, Long> itemCounts = new HashMap<>();
    }

    @Data
    public static class ItemCountRangeVo implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 范围标签
         */
        private String label;

        /**
         * 范围最小值
         */
        private Integer min;

        /**
         * 范围最大值
         */
        private Integer max;
    }
}
