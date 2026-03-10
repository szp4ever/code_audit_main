package org.ruoyi.knowledge.curation.convert;

import org.mapstruct.Mapper;
import org.ruoyi.knowledge.curation.domain.KnowledgeFavorite;
import org.ruoyi.knowledge.curation.domain.bo.KnowledgeFavoriteBo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeFavoriteVo;

@Mapper(componentModel = "spring")
public interface KnowledgeFavoriteConvert {
    KnowledgeFavorite toEntity(KnowledgeFavoriteBo bo);
    KnowledgeFavoriteVo toVo(KnowledgeFavorite entity);
    KnowledgeFavorite voToEntity(KnowledgeFavoriteVo vo);
}
