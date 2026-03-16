package org.ruoyi.chat.convert;

import org.mapstruct.Mapper;
import org.ruoyi.chat.domain.TaskManagement;
import org.ruoyi.chat.domain.vo.TaskManagementVo;

@Mapper(componentModel = "spring")
public interface TaskManagementConvert {
    TaskManagementVo toVo(TaskManagement entity);
    TaskManagement voToEntity(TaskManagementVo vo);
}
