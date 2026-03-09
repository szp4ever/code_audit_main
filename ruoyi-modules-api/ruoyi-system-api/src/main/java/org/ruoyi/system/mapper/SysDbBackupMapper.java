package org.ruoyi.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.system.domain.SysDbBackup;
import org.ruoyi.system.domain.vo.SysDbBackupVo;

/**
 * 数据库备份记录表 数据层
 *
 * @author GPT
 */
@Mapper
public interface SysDbBackupMapper extends BaseMapperPlus<SysDbBackup, SysDbBackupVo> {

}

