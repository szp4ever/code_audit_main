package org.ruoyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.ruoyi.common.core.exception.ServiceException;
import org.ruoyi.common.core.utils.MapstructUtils;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.common.satoken.utils.LoginHelper;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.KnowledgeTag;
import org.ruoyi.domain.bo.KnowledgeTagBo;
import org.ruoyi.domain.vo.KnowledgeTagVo;
import org.ruoyi.mapper.KnowledgeItemTagMapper;
import org.ruoyi.mapper.KnowledgeTagMapper;
import org.ruoyi.service.IKnowledgeTagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 知识标签Service业务层处理
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@RequiredArgsConstructor
@Service
public class KnowledgeTagServiceImpl implements IKnowledgeTagService {

    private final KnowledgeTagMapper baseMapper;
    private final KnowledgeItemTagMapper knowledgeItemTagMapper;

    @Override
    public KnowledgeTagVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<KnowledgeTagVo> queryPageList(KnowledgeTagBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KnowledgeTag> lqw = buildQueryWrapper(bo);
        Page<KnowledgeTagVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        fillUsageCount(result.getRecords());
        return TableDataInfo.build(result);
    }

    @Override
    public List<KnowledgeTagVo> queryList(KnowledgeTagBo bo) {
        LambdaQueryWrapper<KnowledgeTag> lqw = buildQueryWrapper(bo);
        List<KnowledgeTagVo> list = baseMapper.selectVoList(lqw);
        fillUsageCount(list);
        return list;
    }

    private void fillUsageCount(List<KnowledgeTagVo> tags) {
        if (tags == null || tags.isEmpty()) {
            return;
        }
        for (KnowledgeTagVo tag : tags) {
            Integer count = knowledgeItemTagMapper.countByTagId(tag.getId());
            tag.setUsageCount(count != null ? count : 0);
        }
    }

    @Override
    public List<KnowledgeTagVo> queryByItemUuid(String itemUuid) {
        return baseMapper.selectByItemUuid(itemUuid);
    }

    private LambdaQueryWrapper<KnowledgeTag> buildQueryWrapper(KnowledgeTagBo bo) {
        LambdaQueryWrapper<KnowledgeTag> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getTagName()), KnowledgeTag::getTagName, bo.getTagName());
        lqw.eq(StringUtils.isNotBlank(bo.getTagType()), KnowledgeTag::getTagType, bo.getTagType());
        lqw.eq(StringUtils.isNotBlank(bo.getTagCategory()), KnowledgeTag::getTagCategory, bo.getTagCategory());
        
        //权限控制：系统标签全局可见，用户标签仅创建者可见
        Long currentUserId = LoginHelper.getUserId();
        boolean isSuperAdmin = LoginHelper.isSuperAdmin();
        
        if (!isSuperAdmin && currentUserId != null) {
            //普通用户：只能看到系统标签 + 自己创建的用户标签
            lqw.and(wrapper -> wrapper
                .eq(KnowledgeTag::getTagType, "system")
                .or()
                .and(subWrapper -> subWrapper
                    .eq(KnowledgeTag::getTagType, "user")
                    .eq(KnowledgeTag::getCreateBy, currentUserId)
                )
            );
        }
        //管理员可以看到所有标签，不需要额外过滤
        
        //租户隔离
        String tenantId = LoginHelper.getTenantId();
        if (StringUtils.isNotBlank(tenantId)) {
            try {
                Long tenantIdLong = Long.parseLong(tenantId);
                lqw.eq(KnowledgeTag::getTenantId, tenantIdLong);
            } catch (NumberFormatException e) {
                //忽略租户ID解析错误
            }
        }
        
        return lqw;
    }

    @Override
    public Boolean insertByBo(KnowledgeTagBo bo) {
        Long currentUserId = LoginHelper.getUserId();
        boolean isSuperAdmin = LoginHelper.isSuperAdmin();
        
        //权限检查：只有管理员可以创建系统标签
        if ("system".equals(bo.getTagType()) && !isSuperAdmin) {
            throw new ServiceException("无权限创建系统标签，仅管理员可以创建");
        }
        
        //如果未指定标签类型，默认为用户标签
        if (StringUtils.isBlank(bo.getTagType())) {
            bo.setTagType("user");
        }
        
        KnowledgeTag add = MapstructUtils.convert(bo, KnowledgeTag.class);
        
        //设置租户ID
        String tenantId = LoginHelper.getTenantId();
        if (StringUtils.isNotBlank(tenantId)) {
            try {
                add.setTenantId(Long.parseLong(tenantId));
            } catch (NumberFormatException e) {
                add.setTenantId(0L);
            }
        } else {
            add.setTenantId(0L);
        }
        
        //createBy 会由 BaseEntity 自动填充，但如果是系统标签且当前用户是管理员，确保设置正确
        if ("system".equals(bo.getTagType()) && isSuperAdmin && currentUserId != null) {
            add.setCreateBy(currentUserId);
        }
        
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(KnowledgeTagBo bo) {
        //先查询原标签信息
        KnowledgeTag existing = baseMapper.selectById(bo.getId());
        if (existing == null) {
            throw new ServiceException("标签不存在");
        }
        
        Long currentUserId = LoginHelper.getUserId();
        boolean isSuperAdmin = LoginHelper.isSuperAdmin();
        
        //权限检查
        if ("system".equals(existing.getTagType())) {
            //系统标签：只有管理员可以编辑
            if (!isSuperAdmin) {
                throw new ServiceException("无权限编辑系统标签，仅管理员可以编辑");
            }
        } else {
            //用户标签：只有创建者可以编辑
            if (!isSuperAdmin && !Objects.equals(existing.getCreateBy(), currentUserId)) {
                throw new ServiceException("无权限编辑此标签，仅创建者可以编辑");
            }
        }
        
        //不允许将系统标签改为用户标签，或用户标签改为系统标签（除非是管理员）
        if (!isSuperAdmin && StringUtils.isNotBlank(bo.getTagType()) && !bo.getTagType().equals(existing.getTagType())) {
            throw new ServiceException("无权限修改标签类型");
        }
        
        KnowledgeTag update = MapstructUtils.convert(bo, KnowledgeTag.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    private void validEntityBeforeSave(KnowledgeTag entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            Long currentUserId = LoginHelper.getUserId();
            boolean isSuperAdmin = LoginHelper.isSuperAdmin();
            
            //检查每个标签的权限和使用次数
            for (Long id : ids) {
                KnowledgeTag tag = baseMapper.selectById(id);
                if (tag == null) {
                    continue;
                }
                
                //检查使用次数
                Integer usageCount = knowledgeItemTagMapper.countByTagId(id);
                if (usageCount != null && usageCount > 0) {
                    throw new ServiceException(String.format("标签\"%s\"正在被%d个知识条目使用，无法删除", tag.getTagName(), usageCount));
                }
                
                if ("system".equals(tag.getTagType())) {
                    //系统标签：只有管理员可以删除
                    if (!isSuperAdmin) {
                        throw new ServiceException("无权限删除系统标签，仅管理员可以删除");
                    }
                } else {
                    //用户标签：只有创建者可以删除
                    if (!isSuperAdmin && !Objects.equals(tag.getCreateBy(), currentUserId)) {
                        throw new ServiceException("无权限删除此标签，仅创建者可以删除");
                    }
                }
            }
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addTagToItem(String itemUuid, Long tagId) {
        org.ruoyi.domain.KnowledgeItemTag itemTag = new org.ruoyi.domain.KnowledgeItemTag();
        itemTag.setItemUuid(itemUuid);
        itemTag.setTagId(tagId);
        return knowledgeItemTagMapper.insert(itemTag) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeTagFromItem(String itemUuid, Long tagId) {
        LambdaQueryWrapper<org.ruoyi.domain.KnowledgeItemTag> lqw = Wrappers.lambdaQuery();
        lqw.eq(org.ruoyi.domain.KnowledgeItemTag::getItemUuid, itemUuid);
        lqw.eq(org.ruoyi.domain.KnowledgeItemTag::getTagId, tagId);
        return knowledgeItemTagMapper.delete(lqw) > 0;
    }
}
