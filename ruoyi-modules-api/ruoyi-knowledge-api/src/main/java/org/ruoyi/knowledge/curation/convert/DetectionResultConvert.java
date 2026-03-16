package org.ruoyi.knowledge.curation.convert;

import org.mapstruct.Mapper;
import org.ruoyi.knowledge.curation.domain.DetectionResult;
import org.ruoyi.knowledge.curation.domain.bo.DetectionResultBo;
import org.ruoyi.knowledge.curation.domain.vo.DetectionResultVo;

@Mapper(componentModel = "spring")
public interface DetectionResultConvert {
    DetectionResult toEntity(DetectionResultBo bo);
    DetectionResultVo toVo(DetectionResult entity);
    DetectionResult voToEntity(DetectionResultVo vo);
}
