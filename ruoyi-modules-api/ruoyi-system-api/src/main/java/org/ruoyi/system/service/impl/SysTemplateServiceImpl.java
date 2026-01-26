package org.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.constant.CacheNames;
import org.ruoyi.common.core.exception.ServiceException;
import org.ruoyi.common.core.utils.MapstructUtils;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.common.json.utils.JsonUtils;
import org.ruoyi.common.oss.constant.OssConstant;
import org.ruoyi.common.oss.factory.OssFactory;
import org.ruoyi.common.oss.properties.OssProperties;
import org.ruoyi.common.redis.utils.CacheUtils;
import org.ruoyi.common.redis.utils.RedisUtils;
import org.ruoyi.common.satoken.utils.LoginHelper;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.system.domain.SysTemplate;
import org.ruoyi.system.domain.bo.SysTemplateBo;
import org.ruoyi.system.domain.vo.SysTemplateVo;
import org.ruoyi.system.mapper.SysTemplateMapper;
import org.ruoyi.system.service.ISysTemplateService;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SysTemplateServiceImpl extends ServiceImpl<SysTemplateMapper, SysTemplate> implements ISysTemplateService {

    @Override
    public SysTemplateVo queryById(Long templateId) {
        return baseMapper.selectVoById(templateId);
    }

    @Override
    public TableDataInfo<SysTemplateVo> queryPageList(SysTemplateBo bo, PageQuery pageQuery) {
        // 1. 构建分页对象 (注意：这里泛型使用实体类 SysTemplate)
        Page<SysTemplate> page = pageQuery.build();

        // 2. 构建查询条件
        LambdaQueryWrapper<SysTemplate> lqw = buildQueryWrapper(bo);

        // 3. 处理默认排序
        if (StringUtils.isBlank(pageQuery.getOrderByColumn())) {
            lqw.orderByDesc(SysTemplate::getCreateTime);
        }

        // 4. [核心修改] 使用标准 MP 的 selectPage 查询实体列表
        // 避开 baseMapper.selectVoPage，解决报错
        baseMapper.selectPage(page, lqw);

        // 5. [核心修改] 手动将 实体列表(SysTemplate) 转换为 VO列表(SysTemplateVo)
        List<SysTemplateVo> voList = MapstructUtils.convert(page.getRecords(), SysTemplateVo.class);

        // 6. 处理 URL 签名
        for (SysTemplateVo vo : voList) {
            handleSignedUrl(vo);
        }

        // 7. 手动构建返回结果
        // 这样写可以完美避开 TableDataInfo.build(page) 可能产生的泛型不匹配问题
        TableDataInfo<SysTemplateVo> tableDataInfo = new TableDataInfo<>();
        tableDataInfo.setCode(200);
        tableDataInfo.setMsg("查询成功");
        tableDataInfo.setRows(voList);
        tableDataInfo.setTotal(page.getTotal());

        return tableDataInfo;
    }

    @Override
    public List<SysTemplateVo> queryList(SysTemplateBo bo) {
        LambdaQueryWrapper<SysTemplate> lqw = buildQueryWrapper(bo);
        // 列表查询也加上默认排序
        lqw.orderByDesc(SysTemplate::getCreateTime);
        List<SysTemplateVo> list = baseMapper.selectVoList(lqw);
        for (SysTemplateVo vo : list) {
            handleSignedUrl(vo);
        }
        return list;
    }

    private LambdaQueryWrapper<SysTemplate> buildQueryWrapper(SysTemplateBo bo) {
        LambdaQueryWrapper<SysTemplate> lqw = Wrappers.lambdaQuery();

        lqw.eq(bo.getTemplateId() != null, SysTemplate::getTemplateId, bo.getTemplateId());
        lqw.like(StringUtils.isNotBlank(bo.getTemplateName()), SysTemplate::getTemplateName, bo.getTemplateName());
        lqw.eq(StringUtils.isNotBlank(bo.getTemplateCode()), SysTemplate::getTemplateCode, bo.getTemplateCode());
        lqw.eq(StringUtils.isNotBlank(bo.getTemplateType()), SysTemplate::getTemplateType, bo.getTemplateType());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), SysTemplate::getStatus, bo.getStatus());

        // [核心逻辑]
        // 数据库 create_by 是 Long (ID)，bo.getCreateByName 是 String (名字)
        // 通过 SQL 子查询：先查出名字对应的 user_id，再匹配 sys_template 表
        if (StringUtils.isNotBlank(bo.getCreateByName())) {
            lqw.inSql(SysTemplate::getCreateBy,
                    "SELECT user_id FROM sys_user WHERE nick_name LIKE '%" + bo.getCreateByName() + "%' OR user_name LIKE '%" + bo.getCreateByName() + "%'");
        }

        if (StringUtils.isNotBlank(bo.getUpdateByName())) {
            lqw.inSql(SysTemplate::getUpdateBy,
                    "SELECT user_id FROM sys_user WHERE nick_name LIKE '%" + bo.getUpdateByName() + "%' OR user_name LIKE '%" + bo.getUpdateByName() + "%'");
        }

        // 时间范围查询
        if (bo.getParams() != null) {
            lqw.between(
                    bo.getParams().get("beginCreateTime") != null && bo.getParams().get("endCreateTime") != null,
                    SysTemplate::getCreateTime,
                    bo.getParams().get("beginCreateTime"),
                    bo.getParams().get("endCreateTime")
            );
            lqw.between(
                    bo.getParams().get("beginUpdateTime") != null && bo.getParams().get("endUpdateTime") != null,
                    SysTemplate::getUpdateTime,
                    bo.getParams().get("beginUpdateTime"),
                    bo.getParams().get("endUpdateTime")
            );
        }

        return lqw;
    }

    // --- 以下保持不变 ---

    private void handleSignedUrl(SysTemplateVo vo) {
        String originalPath = vo.getFilePath();
        if (StringUtils.isBlank(originalPath)) return;
        try {
            String objectKey = originalPath;
            if (StringUtils.startsWithAny(originalPath, "http://", "https://")) {
                URL url = new URL(originalPath);
                String path = url.getPath();
                if (path.startsWith("/")) path = path.substring(1);
                try {
                    String bucketName = getBucketNameFromCache();
                    if (StringUtils.isNotBlank(bucketName) && path.startsWith(bucketName + "/")) {
                        path = path.substring(bucketName.length() + 1);
                    }
                } catch (Exception e) {}
                objectKey = path;
            }
            String signedUrl = OssFactory.instance().getPrivateUrl(objectKey, 3600);
            vo.setFilePath(signedUrl);
        } catch (Exception e) {
            log.error("生成OSS签名失败: id={}", vo.getTemplateId(), e);
        }
    }

    private String getBucketNameFromCache() {
        String configKey = RedisUtils.getCacheObject(OssConstant.DEFAULT_CONFIG_KEY);
        if (StringUtils.isEmpty(configKey)) return null;
        String json = CacheUtils.get(CacheNames.SYS_OSS_CONFIG, configKey);
        if (json == null) return null;
        OssProperties properties = JsonUtils.parseObject(json, OssProperties.class);
        return properties != null ? properties.getBucketName() : null;
    }

    @Override
    public Boolean insertByBo(SysTemplateBo bo) {
        handleTemplateTypeAndKind(bo);
        SysTemplate add = MapstructUtils.convert(bo, SysTemplate.class);
        add.setCreateBy(LoginHelper.getUserId());
        add.setCreateTime(new Date());
        add.setUpdateBy(LoginHelper.getUserId());
        add.setUpdateTime(new Date());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) bo.setTemplateId(add.getTemplateId());
        return flag;
    }

    @Override
    public Boolean updateByBo(SysTemplateBo bo) {
        handleTemplateTypeAndKind(bo);
        SysTemplate update = MapstructUtils.convert(bo, SysTemplate.class);
        update.setUpdateBy(LoginHelper.getUserId());
        update.setUpdateTime(new Date());
        return baseMapper.updateById(update) > 0;
    }

    private void handleTemplateTypeAndKind(SysTemplateBo bo) {
        String type = bo.getTemplateType();
        if ("1".equals(type)) {
            if (StringUtils.isBlank(bo.getTemplateContent())) throw new ServiceException("请输入模板内容");
            bo.setFilePath("");
            bo.setFileKind("text");
        } else if ("2".equals(type)) {
            if (StringUtils.isBlank(bo.getFilePath())) throw new ServiceException("请上传Word模板文件");
            bo.setFileKind("docx");
            bo.setTemplateContent("");
        } else {
            throw new ServiceException("不支持的模板类型");
        }
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}