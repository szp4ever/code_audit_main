package org.ruoyi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.domain.KnowledgeItemHistory;
import org.ruoyi.domain.vo.KnowledgeItemHistoryVo;

import java.util.List;

/**
 * 知识条目版本历史Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Mapper
public interface KnowledgeItemHistoryMapper extends BaseMapperPlus<KnowledgeItemHistory, KnowledgeItemHistoryVo> {

    /**
     * 根据itemUuid查询版本历史列表
     *
     * @param itemUuid 知识条目UUID
     * @return 版本历史列表
     */
    List<KnowledgeItemHistoryVo> selectByItemUuid(@Param("itemUuid") String itemUuid);

    /**
     * 根据itemUuid和version查询版本历史
     *
     * @param itemUuid 知识条目UUID
     * @param version 版本号
     * @return KnowledgeItemHistory
     */
    KnowledgeItemHistory selectByItemUuidAndVersion(@Param("itemUuid") String itemUuid, @Param("version") Integer version);
}
