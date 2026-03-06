package org.ruoyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.ruoyi.common.core.utils.MapstructUtils;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.KnowledgeFavorite;
import org.ruoyi.domain.bo.KnowledgeFavoriteBo;
import org.ruoyi.domain.vo.KnowledgeFavoriteVo;
import org.ruoyi.mapper.KnowledgeFavoriteMapper;
import org.ruoyi.service.IKnowledgeFavoriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
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

    @Override
    public Boolean insertByBo(KnowledgeFavoriteBo bo) {
        KnowledgeFavorite add = MapstructUtils.convert(bo, KnowledgeFavorite.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    private void validEntityBeforeSave(KnowledgeFavorite entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByItemUuid(Long userId, String itemUuid) {
        KnowledgeFavorite favorite = baseMapper.selectByUserIdAndItemUuid(userId, itemUuid);
        if (favorite == null) {
            return false;
        }
        return baseMapper.deleteById(favorite.getId()) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
