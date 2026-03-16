package org.ruoyi.knowledge.curation.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.knowledge.curation.domain.KnowledgeInfo;
import org.ruoyi.knowledge.curation.domain.vo.DailyCountPointVo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeInfoVo;
import java.util.List;

/**
 * 知识库Mapper接口
 *
 * @author ageerle
 * @date 2025-04-08
 */
@Mapper
public interface KnowledgeInfoMapper extends BaseMapperPlus<KnowledgeInfo, KnowledgeInfoVo> {

    /**
     * 根据kid查询知识库
     *
     * @param kid 知识库id
     * @return KnowledgeInfo
     */
    KnowledgeInfo selectByKid(@Param("kid") String kid);

    /**
     * 根据kid查询知识库Vo
     *
     * @param kid 知识库id
     * @return KnowledgeInfoVo
     */
    KnowledgeInfoVo selectVoByKid(@Param("kid") String kid);

    /**
     * 近30天条目更新频率（按日）
     */
    List<DailyCountPointVo> selectItemUpdateFrequencyByKid(@Param("kid") String kid, @Param("days") Integer days);

    /**
     * 近30天存储增长（按日，基于附件创建时间）
     */
    List<DailyCountPointVo> selectAttachGrowthByKid(@Param("kid") String kid, @Param("days") Integer days);
}
