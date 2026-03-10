package org.ruoyi.knowledge.curation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.ruoyi.common.core.utils.MapstructUtils;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.knowledge.curation.domain.KnowledgeFavorite;
import org.ruoyi.knowledge.curation.domain.bo.KnowledgeFavoriteBo;
import org.ruoyi.knowledge.curation.domain.vo.KnowledgeFavoriteVo;
import org.ruoyi.knowledge.curation.mapper.KnowledgeFavoriteMapper;
import org.ruoyi.knowledge.curation.service.IKnowledgeFavoriteService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 知识收藏Service业务层处理
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@RequiredArgsConstructor
@Service
public class KnowledgeFavoriteServiceImpl implements IKnowledgeFavoriteService {

    private final KnowledgeFavoriteMapper baseMapper;

    @Override
    public KnowledgeFavoriteVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<KnowledgeFavoriteVo> queryPageList(KnowledgeFavoriteBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KnowledgeFavorite> lqw = buildQueryWrapper(bo);
        Page<KnowledgeFavoriteVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<KnowledgeFavoriteVo> queryMyFavorites(Long userId) {
        return baseMapper.selectByUserId(userId);
    }

    @Override
    public Boolean checkFavorite(Long userId, String itemUuid) {
        KnowledgeFavorite favorite = baseMapper.selectByUserIdAndItemUuid(userId, itemUuid);
        return favorite != null;
    }

    private LambdaQueryWrapper<KnowledgeFavorite> buildQueryWrapper(KnowledgeFavoriteBo bo) {
        LambdaQueryWrapper<KnowledgeFavorite> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, KnowledgeFavorite::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getItemUuid()), KnowledgeFavorite::getItemUuid, bo.getItemUuid());
        lqw.eq(StringUtils.isNotBlank(bo.getKid()), KnowledgeFavorite::getKid, bo.getKid());
        return lqw;
    }

    /**
     * 新增收藏记录（幂等设计）
     * <p>
     * 最佳实践：
     * 1. 同一用户对同一条目的收藏应幂等 - 重复收藏返回已存在记录
     * 2. 使用数据库唯一索引 (user_id, item_uuid) 保证并发安全
     * 3. 捕获 DuplicateKeyException 实现幂等，而非先查后插
     *
     * @param bo 收藏业务对象
     * @return true: 收藏成功（新创建或已存在）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(KnowledgeFavoriteBo bo) {
        // 先尝试查询是否已收藏
        KnowledgeFavorite existing = baseMapper.selectByUserIdAndItemUuid(bo.getUserId(), bo.getItemUuid());
        if (existing != null) {
            bo.setId(existing.getId());
            return true;
        }

        KnowledgeFavorite add = MapstructUtils.convert(bo, KnowledgeFavorite.class);
        if (add.getCreateTime() == null) {
            add.setCreateTime(new Date());
        }

        try {
            // 直接插入，依赖数据库唯一索引保证幂等性
            boolean flag = baseMapper.insert(add) > 0;
            if (flag) {
                bo.setId(add.getId());
            }
            return flag;
        } catch (DuplicateKeyException e) {
            // 并发场景下其他事务已插入，查询并返回已有记录
            KnowledgeFavorite concurrent = baseMapper.selectByUserIdAndItemUuid(bo.getUserId(), bo.getItemUuid());
            if (concurrent != null) {
                bo.setId(concurrent.getId());
            }
            return true;
        }
    }

    /**
     * 取消收藏（幂等设计）
     * <p>
     * 最佳实践：
     * 1. 删除操作天然幂等 - 无论记录是否存在，最终状态都是"未收藏"
     * 2. 不应因记录不存在而返回失败，符合幂等语义
     * 3. 根据业务ID直接删除，无需先查询
     *
     * @param userId   用户ID
     * @param itemUuid 条目UUID
     * @return true: 操作成功（删除或本来就没有）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByItemUuid(Long userId, String itemUuid) {
        // 幂等删除：直接执行删除，不检查记录是否存在
        KnowledgeFavorite toDelete = new KnowledgeFavorite();
        toDelete.setUserId(userId);
        toDelete.setItemUuid(itemUuid);

        int deleted = baseMapper.delete(
            Wrappers.<KnowledgeFavorite>lambdaQuery()
                .eq(KnowledgeFavorite::getUserId, userId)
                .eq(KnowledgeFavorite::getItemUuid, itemUuid)
        );

        // 删除0条也是成功（本来就没有收藏）
        return true;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        // 使用 deleteByIds 替代废弃的 deleteBatchIds
        return baseMapper.deleteByIds(ids) > 0;
    }
}
