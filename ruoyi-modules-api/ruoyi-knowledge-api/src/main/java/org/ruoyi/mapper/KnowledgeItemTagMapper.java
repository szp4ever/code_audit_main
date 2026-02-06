package org.ruoyi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.domain.KnowledgeItemTag;

import java.util.List;
import java.util.Map;

/**
 * 知识条目标签关联Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Mapper
public interface KnowledgeItemTagMapper extends BaseMapperPlus<KnowledgeItemTag, KnowledgeItemTag> {

    /**
     * 根据itemUuid删除关联关系
     *
     * @param itemUuid 知识条目UUID
     * @return 删除数量
     */
    int deleteByItemUuid(@Param("itemUuid") String itemUuid);

    /**
     * 根据tagId删除关联关系
     *
     * @param tagId 标签ID
     * @return 删除数量
     */
    int deleteByTagId(@Param("tagId") Long tagId);

    /**
     * 根据知识库ID统计标签数量
     *
     * @param kid 知识库ID
     * @return 标签统计列表
     */
    @Select("SELECT kt.tag_name as tagName, COUNT(kit.item_uuid) as itemCount " +
            "FROM knowledge_item_tag kit " +
            "INNER JOIN knowledge_item ki ON kit.item_uuid = ki.item_uuid " +
            "INNER JOIN knowledge_tag kt ON kit.tag_id = kt.id " +
            "WHERE ki.kid = #{kid} AND ki.del_flag = '0' " +
            "GROUP BY kt.tag_name")
    List<Map<String, Object>> selectTagStatsByKid(@Param("kid") String kid);

    /**
     * 根据标签ID统计使用次数
     *
     * @param tagId 标签ID
     * @return 使用次数
     */
    @Select("SELECT COUNT(*) FROM knowledge_item_tag WHERE tag_id = #{tagId}")
    Integer countByTagId(@Param("tagId") Long tagId);
}
