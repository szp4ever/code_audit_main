package org.ruoyi.service;

import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.bo.KnowledgeFavoriteBo;
import org.ruoyi.domain.vo.KnowledgeFavoriteVo;

import java.util.Collection;
import java.util.List;

/**
 * 知识收藏Service接口
 *
 * @author ruoyi
 * @date 2026-01-14
 */
public interface IKnowledgeFavoriteService {

    /**
     * 查询知识收藏
     */
    KnowledgeFavoriteVo queryById(Long id);

    /**
     * 查询知识收藏列表
     */
    TableDataInfo<KnowledgeFavoriteVo> queryPageList(KnowledgeFavoriteBo bo, PageQuery pageQuery);

    /**
     * 查询我的收藏列表
     */
    List<KnowledgeFavoriteVo> queryMyFavorites(Long userId);

    /**
     * 检查是否已收藏
     */
    Boolean checkFavorite(Long userId, String itemUuid);

    /**
     * 新增知识收藏
     */
    Boolean insertByBo(KnowledgeFavoriteBo bo);

    /**
     * 取消收藏
     */
    Boolean deleteByItemUuid(Long userId, String itemUuid);

    /**
     * 校验并批量删除知识收藏信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
