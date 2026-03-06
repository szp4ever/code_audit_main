package org.ruoyi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ruoyi.core.mapper.BaseMapperPlus;
import org.ruoyi.domain.KnowledgeFavorite;
import org.ruoyi.domain.vo.KnowledgeFavoriteVo;

import java.util.List;

/**
 * 知识收藏Mapper接口
 *
 * @author ruoyi
 * @date 2026-01-14
 */
@Mapper
public interface KnowledgeFavoriteMapper extends BaseMapperPlus<KnowledgeFavorite, KnowledgeFavoriteVo> {

    /**
     * 根据userId查询收藏列表
     *
     * @param userId 用户ID
     * @return 收藏列表
     */
    List<KnowledgeFavoriteVo> selectByUserId(@Param("userId") Long userId);

    /**
     * 检查用户是否已收藏
     *
     * @param userId 用户ID
     * @param itemUuid 知识条目UUID
     * @return 收藏记录
     */
    KnowledgeFavorite selectByUserIdAndItemUuid(@Param("userId") Long userId, @Param("itemUuid") String itemUuid);
}
