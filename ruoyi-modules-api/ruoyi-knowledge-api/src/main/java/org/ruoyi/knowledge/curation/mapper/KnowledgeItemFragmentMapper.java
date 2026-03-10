package org.ruoyi.knowledge.curation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.knowledge.curation.domain.KnowledgeItemFragment;

import java.util.List;

/**
 * 知识条目-片段关联 Mapper
 */
@Mapper
public interface KnowledgeItemFragmentMapper extends BaseMapper<KnowledgeItemFragment> {

    /**
     * 根据条目 UUID 查询关联的片段 ID 列表
     */
    default List<KnowledgeItemFragment> selectByItemUuid(String itemUuid) {
        return selectList(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<KnowledgeItemFragment>()
            .eq(KnowledgeItemFragment::getItemUuid, itemUuid)
            .orderByDesc(KnowledgeItemFragment::getCreateTime));
    }

    /**
     * 根据片段 ID 查询关联的条目 UUID 列表
     */
    default List<KnowledgeItemFragment> selectByFragmentId(Long fragmentId) {
        return selectList(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<KnowledgeItemFragment>()
            .eq(KnowledgeItemFragment::getFragmentId, fragmentId)
            .orderByDesc(KnowledgeItemFragment::getCreateTime));
    }

    /**
     * 删除条目的所有片段关联
     */
    default int deleteByItemUuid(String itemUuid) {
        return delete(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<KnowledgeItemFragment>()
            .eq(KnowledgeItemFragment::getItemUuid, itemUuid));
    }

    /**
     * 删除片段的所有条目关联
     */
    default int deleteByFragmentId(Long fragmentId) {
        return delete(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<KnowledgeItemFragment>()
            .eq(KnowledgeItemFragment::getFragmentId, fragmentId));
    }

    /**
     * 检查关联是否已存在
     */
    default boolean existsAssociation(String itemUuid, Long fragmentId) {
        return exists(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<KnowledgeItemFragment>()
            .eq(KnowledgeItemFragment::getItemUuid, itemUuid)
            .eq(KnowledgeItemFragment::getFragmentId, fragmentId));
    }
}
