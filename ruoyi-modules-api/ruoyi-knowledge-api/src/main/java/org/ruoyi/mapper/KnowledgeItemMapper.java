package org.ruoyi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.domain.KnowledgeItem;
import org.ruoyi.domain.vo.KnowledgeItemVo;

/**
 * 知识条目Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Mapper
public interface KnowledgeItemMapper extends BaseMapperPlus<KnowledgeItem, KnowledgeItemVo> {

    /**
     * 根据itemUuid查询知识条目
     *
     * @param itemUuid 知识条目UUID
     * @return KnowledgeItem
     */
    KnowledgeItem selectByItemUuid(@Param("itemUuid") String itemUuid);

    /**
     * 更新知识库的条目数量（基于实际统计）
     *
     * @param kid 知识库ID
     */
    void updateKnowledgeItemCount(@Param("kid") String kid);

    /**
     * 更新知识库的片段数量（基于实际统计）
     *
     * @param kid 知识库ID
     */
    void updateKnowledgeFragmentCount(@Param("kid") String kid);

    /**
     * 更新知识库的存储大小（基于实际统计）
     *
     * @param kid 知识库ID
     * @param dataSize 存储大小（字节）
     */
    void updateKnowledgeDataSize(@Param("kid") String kid, @Param("dataSize") Long dataSize);
}
