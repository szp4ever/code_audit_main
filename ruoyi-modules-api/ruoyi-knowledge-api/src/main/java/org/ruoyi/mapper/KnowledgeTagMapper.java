package org.ruoyi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.domain.KnowledgeTag;
import org.ruoyi.domain.vo.KnowledgeTagVo;

import java.util.List;

/**
 * 知识标签Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Mapper
public interface KnowledgeTagMapper extends BaseMapperPlus<KnowledgeTag, KnowledgeTagVo> {

    /**
     * 根据itemUuid查询标签列表
     *
     * @param itemUuid 知识条目UUID
     * @return 标签列表
     */
    List<KnowledgeTagVo> selectByItemUuid(@Param("itemUuid") String itemUuid);
}
