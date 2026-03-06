package org.ruoyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.ruoyi.common.core.exception.ServiceException;
import org.ruoyi.common.core.utils.MapstructUtils;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.common.core.utils.SpringUtils;
import org.ruoyi.common.core.service.DictService;
import org.ruoyi.service.impl.HighlightJsService;
import org.ruoyi.common.satoken.utils.LoginHelper;
import cn.hutool.http.HttpStatus;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.KnowledgeItem;
import org.ruoyi.domain.KnowledgeItemTag;
import org.ruoyi.domain.KnowledgeItemVulnerabilityType;
import org.ruoyi.domain.KnowledgeTag;
import org.ruoyi.domain.bo.KnowledgeItemBo;
import org.ruoyi.domain.vo.ClusterWithItemsVo;
import org.ruoyi.domain.vo.CweClusterMappingVo;
import org.ruoyi.domain.vo.CweClusterVo;
import org.ruoyi.domain.vo.CweReferenceVo;
import org.ruoyi.domain.vo.FacetStatsVo;
import org.ruoyi.domain.vo.KnowledgeItemPageVo;
import org.ruoyi.domain.vo.KnowledgeItemVo;
import org.ruoyi.domain.vo.KnowledgeInfoVo;
import org.ruoyi.domain.vo.KnowledgeTagVo;
import org.ruoyi.domain.CweReference;
import org.ruoyi.domain.vo.BatchDeleteResultVo;
import org.ruoyi.domain.vo.BatchUpdateResultVo;
import org.ruoyi.domain.vo.DeleteFailureVo;
import org.ruoyi.domain.vo.UpdateFailureVo;
import org.ruoyi.domain.vo.ExportPreviewVo;
import org.ruoyi.domain.vo.FieldInfoVo;
import org.ruoyi.domain.bo.BatchUpdateRequestBo;
import org.ruoyi.domain.bo.ExportPreviewRequestBo;
import org.ruoyi.domain.bo.ExportRequestBo;
import org.ruoyi.domain.bo.ExcelOptionsBo;
import org.ruoyi.domain.bo.PdfOptionsBo;
import org.ruoyi.utils.CvssScoreCalculator;
import org.ruoyi.mapper.CweClusterMapper;
import org.ruoyi.mapper.CweClusterMappingMapper;
import org.ruoyi.mapper.CweReferenceMapper;
import org.ruoyi.mapper.KnowledgeItemMapper;
import org.ruoyi.mapper.KnowledgeItemTagMapper;
import org.ruoyi.mapper.KnowledgeItemVulnerabilityTypeMapper;
import org.ruoyi.mapper.KnowledgeTagMapper;
import org.ruoyi.mapper.KnowledgeFragmentMapper;
import org.ruoyi.mapper.KnowledgeAttachProcessMapper;
import org.ruoyi.mapper.KnowledgeAttachMapper;
import org.ruoyi.mapper.KnowledgeInfoMapper;
import org.ruoyi.domain.KnowledgeFragment;
import org.ruoyi.domain.KnowledgeAttachProcess;
import org.ruoyi.domain.KnowledgeAttach;
import org.ruoyi.domain.KnowledgeInfo;
import org.ruoyi.domain.enums.ProcessingStatus;
import org.ruoyi.service.IKnowledgeItemService;
import org.ruoyi.system.service.ISysOssService;
import org.ruoyi.system.service.ISysUserService;
import org.ruoyi.system.service.ISysDeptService;
import org.ruoyi.system.domain.vo.SysOssVo;
import org.ruoyi.system.domain.vo.SysUserVo;
import org.ruoyi.system.domain.vo.SysDeptVo;
import org.ruoyi.common.oss.core.OssClient;
import org.ruoyi.common.oss.factory.OssFactory;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONArray;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import com.itextpdf.kernel.pdf.PdfReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import java.awt.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.TabAlignment;
import com.itextpdf.layout.properties.Property;
import com.itextpdf.layout.properties.OverflowPropertyValue;
import com.itextpdf.layout.properties.ParagraphOrphansControl;
import com.itextpdf.layout.properties.ParagraphWidowsControl;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.layout.renderer.TextRenderer;
import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.ReaderProperties;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.StringWriter;
import java.util.Properties;

/**
 * 知识条目Service业务层处理
 *
 * @author ruoyi
 * @date 2026-01-15
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class KnowledgeItemServiceImpl implements IKnowledgeItemService {

    private final KnowledgeItemMapper baseMapper;
    private final KnowledgeItemVulnerabilityTypeMapper vulnerabilityTypeMapper;
    private final KnowledgeItemTagMapper itemTagMapper;
    private final KnowledgeTagMapper knowledgeTagMapper;
    private final CweClusterMapper cweClusterMapper;
    private final CweClusterMappingMapper cweClusterMappingMapper;
    private final CweReferenceMapper cweReferenceMapper;
    private final KnowledgeFragmentMapper fragmentMapper;
    private final KnowledgeAttachProcessMapper attachProcessMapper;
    private final KnowledgeAttachMapper attachMapper;
    private final KnowledgeInfoMapper knowledgeInfoMapper;
    private final ISysOssService sysOssService;
    private final ISysUserService sysUserService;
    private final ISysDeptService sysDeptService;
    private final HighlightJsService highlightJsService;

    @Override
    public KnowledgeItemVo queryById(Long id) {
        KnowledgeItemVo vo = baseMapper.selectVoById(id);
        if (vo != null && StringUtils.isNotBlank(vo.getItemUuid())) {
            loadVulnerabilityTypes(vo);
        }
        return vo;
    }

    @Override
    public KnowledgeItemVo queryByItemUuid(String itemUuid) {
        KnowledgeItem entity = baseMapper.selectByItemUuid(itemUuid);
        if (entity == null) {
            return null;
        }
        KnowledgeItemVo vo = baseMapper.selectVoById(entity.getId());
        if (vo != null) {
            loadVulnerabilityTypes(vo);
        }
        return vo;
    }

    /**
     * 加载漏洞类型列表
     */
    private void loadVulnerabilityTypes(KnowledgeItemVo vo) {
        if (StringUtils.isNotBlank(vo.getItemUuid())) {
            List<String> cweIds = vulnerabilityTypeMapper.selectCweIdsByItemUuid(vo.getItemUuid());
            vo.setVulnerabilityTypes(cweIds);
            if (CollectionUtils.isNotEmpty(cweIds) && cweIds.size() == 1) {
                vo.setVulnerabilityType(cweIds.get(0));
            }
            List<String> tagNames = loadTagNamesByItemUuid(vo.getItemUuid());
            vo.setTags(tagNames);
        }
    }
    private List<String> loadTagNamesByItemUuid(String itemUuid) {
        if (StringUtils.isBlank(itemUuid)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<KnowledgeItemTag> itemTagLqw = Wrappers.lambdaQuery();
        itemTagLqw.eq(KnowledgeItemTag::getItemUuid, itemUuid);
        List<KnowledgeItemTag> itemTags = itemTagMapper.selectList(itemTagLqw);
        if (CollectionUtils.isEmpty(itemTags)) {
            return new ArrayList<>();
        }
        List<Long> tagIds = itemTags.stream()
            .map(KnowledgeItemTag::getTagId)
            .collect(Collectors.toList());
        LambdaQueryWrapper<KnowledgeTag> tagLqw = Wrappers.lambdaQuery();
        tagLqw.in(KnowledgeTag::getId, tagIds);
        List<KnowledgeTag> tags = knowledgeTagMapper.selectList(tagLqw);
        return tags.stream()
            .map(KnowledgeTag::getTagName)
            .collect(Collectors.toList());
    }

    /**
     * 批量填充片段数量
     */
    private void fillFragmentCounts(List<KnowledgeItemVo> items) {
        if (CollectionUtils.isEmpty(items)) {
            return;
        }
        List<String> itemUuids = items.stream()
            .map(KnowledgeItemVo::getItemUuid)
            .filter(StringUtils::isNotBlank)
            .distinct()
            .collect(Collectors.toList());
        if (itemUuids.isEmpty()) {
            return;
        }
        Map<String, Integer> fragmentCountMap = fragmentMapper.selectList(
            Wrappers.<KnowledgeFragment>lambdaQuery()
                .in(KnowledgeFragment::getItemUuid, itemUuids)
                .eq(KnowledgeFragment::getDelFlag, "0")
        ).stream()
        .collect(Collectors.groupingBy(
            KnowledgeFragment::getItemUuid,
            Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
        ));
        for (KnowledgeItemVo item : items) {
            if (StringUtils.isNotBlank(item.getItemUuid()) && fragmentCountMap.containsKey(item.getItemUuid())) {
                item.setFragmentCount(fragmentCountMap.get(item.getItemUuid()));
            } else {
                item.setFragmentCount(0);
            }
        }
    }

    @Override
    public TableDataInfo<KnowledgeItemVo> queryPageList(KnowledgeItemBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<KnowledgeItem> lqw = buildQueryWrapper(bo);
        Page<KnowledgeItemVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        
        if (result.getRecords() != null) {
            result.getRecords().forEach(this::loadVulnerabilityTypes);
            fillFragmentCounts(result.getRecords());
            if (StringUtils.isNotBlank(bo.getSearchKeyword())) {
                sortByRelevance(result.getRecords(), bo.getSearchKeyword());
            }
        }
        FacetStatsVo facetStats = calculateFacetStats(bo);
        List<ClusterWithItemsVo> groupedByClusters = calculateGroupedByClusters(facetStats, bo.getVulnerabilityTypeKeyword());
        KnowledgeItemPageVo pageVo = new KnowledgeItemPageVo();
        pageVo.setTotal(result.getTotal());
        pageVo.setRows(result.getRecords());
        pageVo.setCode(HttpStatus.HTTP_OK);
        pageVo.setMsg("查询成功");
        pageVo.setFacetStats(facetStats);
        pageVo.setGroupedByClusters(groupedByClusters);
        return pageVo;
    }

    @Override
    public List<KnowledgeItemVo> queryList(KnowledgeItemBo bo) {
        LambdaQueryWrapper<KnowledgeItem> lqw = buildQueryWrapper(bo);
        List<KnowledgeItemVo> list = baseMapper.selectVoList(lqw);
        // 加载漏洞类型列表和片段数量
        if (list != null) {
            list.forEach(this::loadVulnerabilityTypes);
            fillFragmentCounts(list);
        }
        return list;
    }

    private LambdaQueryWrapper<KnowledgeItem> buildQueryWrapper(KnowledgeItemBo bo) {
        LambdaQueryWrapper<KnowledgeItem> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getKid()), KnowledgeItem::getKid, bo.getKid());
        if (bo.getItemUuids() != null) {
            if (CollectionUtils.isNotEmpty(bo.getItemUuids())) {
                lqw.in(KnowledgeItem::getItemUuid, bo.getItemUuids());
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        } else {
            lqw.eq(StringUtils.isNotBlank(bo.getItemUuid()), KnowledgeItem::getItemUuid, bo.getItemUuid());
        }
        if (StringUtils.isNotBlank(bo.getSearchKeyword())) {
            String keyword = bo.getSearchKeyword();
            lqw.and(wrapper -> wrapper
                .like(KnowledgeItem::getTitle, keyword)
                .or()
                .like(KnowledgeItem::getSummary, keyword)
                .or()
                .like(KnowledgeItem::getProblemDescription, keyword)
                .or()
                .like(KnowledgeItem::getFixSolution, keyword)
            );
        } else {
            lqw.like(StringUtils.isNotBlank(bo.getTitle()), KnowledgeItem::getTitle, bo.getTitle());
            lqw.like(StringUtils.isNotBlank(bo.getSummary()), KnowledgeItem::getSummary, bo.getSummary());
            lqw.like(StringUtils.isNotBlank(bo.getProblemDescription()), KnowledgeItem::getProblemDescription, bo.getProblemDescription());
        }
        lqw.eq(StringUtils.isNotBlank(bo.getVulnerabilityType()), KnowledgeItem::getVulnerabilityType, bo.getVulnerabilityType());
        lqw.eq(StringUtils.isNotBlank(bo.getLanguage()), KnowledgeItem::getLanguage, bo.getLanguage());
        lqw.eq(StringUtils.isNotBlank(bo.getSeverity()), KnowledgeItem::getSeverity, bo.getSeverity());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), KnowledgeItem::getStatus, bo.getStatus());
        if (bo.getLanguages() != null) {
            if (CollectionUtils.isNotEmpty(bo.getLanguages())) {
                lqw.in(KnowledgeItem::getLanguage, bo.getLanguages());
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getSeverities() != null) {
            if (CollectionUtils.isNotEmpty(bo.getSeverities())) {
                lqw.in(KnowledgeItem::getSeverity, bo.getSeverities());
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getStatuses() != null) {
            if (CollectionUtils.isNotEmpty(bo.getStatuses())) {
                lqw.in(KnowledgeItem::getStatus, bo.getStatuses());
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getTags() != null) {
            if (CollectionUtils.isNotEmpty(bo.getTags())) {
                LambdaQueryWrapper<KnowledgeTag> tagQueryWrapper = Wrappers.lambdaQuery();
                tagQueryWrapper.in(KnowledgeTag::getTagName, bo.getTags());
                List<KnowledgeTag> tags = knowledgeTagMapper.selectList(tagQueryWrapper);
                if (CollectionUtils.isNotEmpty(tags)) {
                    List<Long> tagIds = tags.stream()
                            .map(KnowledgeTag::getId)
                            .collect(Collectors.toList());
                    LambdaQueryWrapper<KnowledgeItemTag> itemTagQueryWrapper = Wrappers.lambdaQuery();
                    itemTagQueryWrapper.select(KnowledgeItemTag::getItemUuid)
                            .in(KnowledgeItemTag::getTagId, tagIds);
                    List<KnowledgeItemTag> itemTags = itemTagMapper.selectList(itemTagQueryWrapper);
                    if (CollectionUtils.isNotEmpty(itemTags)) {
                        List<String> itemUuids = itemTags.stream()
                                .map(KnowledgeItemTag::getItemUuid)
                                .distinct()
                                .collect(Collectors.toList());
                        lqw.in(KnowledgeItem::getItemUuid, itemUuids);
                    } else {
                        lqw.isNull(KnowledgeItem::getItemUuid);
                    }
                } else {
                    lqw.isNull(KnowledgeItem::getItemUuid);
                }
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getVulnerabilityTypes() != null) {
            if (CollectionUtils.isNotEmpty(bo.getVulnerabilityTypes())) {
                LambdaQueryWrapper<KnowledgeItemVulnerabilityType> vulnLqw = Wrappers.lambdaQuery();
                vulnLqw.select(KnowledgeItemVulnerabilityType::getItemUuid)
                        .in(KnowledgeItemVulnerabilityType::getCweId, bo.getVulnerabilityTypes());
                List<KnowledgeItemVulnerabilityType> vulnList = vulnerabilityTypeMapper.selectList(vulnLqw);
                if (CollectionUtils.isNotEmpty(vulnList)) {
                    List<String> itemUuids = vulnList.stream()
                            .map(KnowledgeItemVulnerabilityType::getItemUuid)
                            .distinct()
                            .collect(Collectors.toList());
                    lqw.in(KnowledgeItem::getItemUuid, itemUuids);
                } else {
                    lqw.isNull(KnowledgeItem::getItemUuid);
                }
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getCvssScoreMin() != null) {
            lqw.ge(KnowledgeItem::getCvssScore, bo.getCvssScoreMin());
        }
        if (bo.getCvssScoreMax() != null) {
            lqw.le(KnowledgeItem::getCvssScore, bo.getCvssScoreMax());
        }
        if (bo.getCvssAttackVector() != null) {
            if (CollectionUtils.isNotEmpty(bo.getCvssAttackVector())) {
                lqw.and(wrapper -> {
                    for (String value : bo.getCvssAttackVector()) {
                        wrapper.or().like(KnowledgeItem::getCvssVector, "AV:" + value);
                    }
                });
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getCvssAttackComplexity() != null) {
            if (CollectionUtils.isNotEmpty(bo.getCvssAttackComplexity())) {
                lqw.and(wrapper -> {
                    for (String value : bo.getCvssAttackComplexity()) {
                        wrapper.or().like(KnowledgeItem::getCvssVector, "AC:" + value);
                    }
                });
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getCvssPrivilegesRequired() != null) {
            if (CollectionUtils.isNotEmpty(bo.getCvssPrivilegesRequired())) {
                lqw.and(wrapper -> {
                    for (String value : bo.getCvssPrivilegesRequired()) {
                        wrapper.or().like(KnowledgeItem::getCvssVector, "PR:" + value);
                    }
                });
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getCvssUserInteraction() != null) {
            if (CollectionUtils.isNotEmpty(bo.getCvssUserInteraction())) {
                lqw.and(wrapper -> {
                    for (String value : bo.getCvssUserInteraction()) {
                        wrapper.or().like(KnowledgeItem::getCvssVector, "UI:" + value);
                    }
                });
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getCvssScope() != null) {
            if (CollectionUtils.isNotEmpty(bo.getCvssScope())) {
                lqw.and(wrapper -> {
                    for (String value : bo.getCvssScope()) {
                        wrapper.or().like(KnowledgeItem::getCvssVector, "S:" + value);
                    }
                });
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getCvssConfidentiality() != null) {
            if (CollectionUtils.isNotEmpty(bo.getCvssConfidentiality())) {
                lqw.and(wrapper -> {
                    for (String value : bo.getCvssConfidentiality()) {
                        wrapper.or().like(KnowledgeItem::getCvssVector, "C:" + value);
                    }
                });
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getCvssIntegrity() != null) {
            if (CollectionUtils.isNotEmpty(bo.getCvssIntegrity())) {
                lqw.and(wrapper -> {
                    for (String value : bo.getCvssIntegrity()) {
                        wrapper.or().like(KnowledgeItem::getCvssVector, "I:" + value);
                    }
                });
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getCvssAvailability() != null) {
            if (CollectionUtils.isNotEmpty(bo.getCvssAvailability())) {
                lqw.and(wrapper -> {
                    for (String value : bo.getCvssAvailability()) {
                        wrapper.or().like(KnowledgeItem::getCvssVector, "A:" + value);
                    }
                });
            } else {
                lqw.isNull(KnowledgeItem::getItemUuid);
            }
        }
        if (bo.getCreateTimeStart() != null) {
            lqw.ge(KnowledgeItem::getCreateTime, bo.getCreateTimeStart());
        }
        if (bo.getCreateTimeEnd() != null) {
            lqw.le(KnowledgeItem::getCreateTime, bo.getCreateTimeEnd());
        }
        
        // 过滤掉未完成处理的新建条目：只过滤那些在llmCreatedItems中且关联的处理任务未完成的条目
        // 保留：没有关联片段的条目（手动创建）、片段没有关联处理任务的条目（旧数据）、处理任务已完成的条目、已有条目（不在llmCreatedItems中）
        String completedStatus = ProcessingStatus.COMPLETED.getCode();
        
        // 只过滤新建条目：检查该条目是否在某个未完成任务的llmCreatedItems中
        String notExistsSql = "NOT EXISTS (SELECT 1 FROM knowledge_fragment f " +
            "INNER JOIN knowledge_attach_process p ON f.doc_id = p.doc_id " +
            "WHERE f.item_uuid = knowledge_item.item_uuid " +
            "AND f.del_flag = '0' " +
            "AND p.current_status != '" + completedStatus + "' " +
            "AND JSON_SEARCH(p.status_data, 'one', knowledge_item.item_uuid, NULL, '$.llmCreatedItems[*].itemUuid') IS NOT NULL)";
        
        lqw.apply(notExistsSql);
        
        return lqw;
    }

    private void sortByRelevance(List<KnowledgeItemVo> items, String keyword) {
        if (items == null || items.isEmpty() || StringUtils.isBlank(keyword)) {
            return;
        }
        String lowerKeyword = keyword.toLowerCase().trim();
        String[] keywords = lowerKeyword.split("\\s+");
        items.sort((a, b) -> {
            double scoreA = calculateRelevanceScore(a, keywords);
            double scoreB = calculateRelevanceScore(b, keywords);
            return Double.compare(scoreB, scoreA);
        });
    }

    private double calculateRelevanceScore(KnowledgeItemVo item, String[] keywords) {
        double score = 0.0;
        for (String keyword : keywords) {
            score += countMatches(item.getTitle(), keyword) * 4.0;
            score += countMatches(item.getSummary(), keyword) * 2.0;
            score += countMatches(item.getProblemDescription(), keyword) * 2.0;
            score += countMatches(item.getFixSolution(), keyword) * 1.0;
            if (item.getTitle() != null && item.getTitle().toLowerCase().startsWith(keyword)) {
                score += 10.0;
            }
            if (item.getTitle() != null && item.getTitle().toLowerCase().equals(keyword)) {
                score += 20.0;
            }
        }
        return score;
    }

    private int countMatches(String text, String keyword) {
        if (text == null || keyword == null) {
            return 0;
        }
        String lowerText = text.toLowerCase();
        int count = 0;
        int index = 0;
        while ((index = lowerText.indexOf(keyword, index)) != -1) {
            count++;
            index += keyword.length();
        }
        return count;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(KnowledgeItemBo bo) {
        KnowledgeItem add = MapstructUtils.convert(bo, KnowledgeItem.class);
        if (StringUtils.isBlank(add.getItemUuid())) {
            add.setItemUuid(UUID.randomUUID().toString().replace("-", ""));
        }
        if (CollectionUtils.isNotEmpty(bo.getVulnerabilityTypes()) && StringUtils.isBlank(add.getVulnerabilityType())) {
            add.setVulnerabilityType(bo.getVulnerabilityTypes().get(0));
        }
        //显式设置 createBy，确保即使自动填充失效也能正确设置
        Long currentUserId = LoginHelper.getUserId();
        if (currentUserId != null && add.getCreateBy() == null) {
            add.setCreateBy(currentUserId);
        }
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            saveVulnerabilityTypes(add.getItemUuid(), bo.getVulnerabilityTypes());
            saveItemTags(add.getItemUuid(), bo.getTags());
            if (StringUtils.isNotBlank(add.getKid()) && !isItemInUncompletedProcess(add.getItemUuid())) {
                baseMapper.updateKnowledgeItemCount(add.getKid());
                updateKnowledgeDataSize(add.getKid());
            }
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(KnowledgeItemBo bo) {
        if (StringUtils.isBlank(bo.getItemUuid())) {
            throw new ServiceException("知识条目UUID不能为空");
        }
        KnowledgeItem existing = baseMapper.selectByItemUuid(bo.getItemUuid());
        if (existing == null) {
            throw new ServiceException("知识条目不存在");
        }
        Long currentUserId = LoginHelper.getUserId();
        boolean isSuperAdmin = LoginHelper.isSuperAdmin();
        if (!isSuperAdmin && !Objects.equals(existing.getCreateBy(), currentUserId)) {
            throw new ServiceException("无权限编辑此知识条目，仅作者或管理员可以编辑");
        }
        KnowledgeItem update = MapstructUtils.convert(bo, KnowledgeItem.class);
        update.setId(existing.getId());
        update.setItemUuid(existing.getItemUuid());
        if (CollectionUtils.isNotEmpty(bo.getVulnerabilityTypes()) && StringUtils.isBlank(update.getVulnerabilityType())) {
            update.setVulnerabilityType(bo.getVulnerabilityTypes().get(0));
        }
        validEntityBeforeSave(update);
        boolean flag = baseMapper.updateById(update) > 0;
        if (flag && StringUtils.isNotBlank(update.getItemUuid())) {
            saveVulnerabilityTypes(update.getItemUuid(), bo.getVulnerabilityTypes());
            saveItemTags(update.getItemUuid(), bo.getTags());
            if (StringUtils.isNotBlank(existing.getKid()) && !isItemInUncompletedProcess(update.getItemUuid())) {
                baseMapper.updateKnowledgeItemCount(existing.getKid());
                updateKnowledgeDataSize(existing.getKid());
            }
        }
        return flag;
    }

    /**
     * 保存漏洞类型关联关系
     */
    private void saveVulnerabilityTypes(String itemUuid, List<String> vulnerabilityTypes) {
        if (StringUtils.isBlank(itemUuid)) {
            return;
        }
        vulnerabilityTypeMapper.deleteByItemUuid(itemUuid);
        if (CollectionUtils.isNotEmpty(vulnerabilityTypes)) {
            // 查询所有可用的CWE ID（从数据库获取实际格式）
            List<CweReferenceVo> allCwes = cweReferenceMapper.selectVoList(Wrappers.lambdaQuery());
            // 创建映射：标准化格式 -> 数据库实际格式
            Map<String, String> cweIdMap = new HashMap<>();
            for (CweReferenceVo cwe : allCwes) {
                if (StringUtils.isNotBlank(cwe.getCweId())) {
                    String dbCweId = cwe.getCweId();
                    // 创建多个可能的匹配键
                    String normalized = dbCweId.trim().toUpperCase();
                    cweIdMap.put(normalized, dbCweId);
                    if (normalized.startsWith("CWE-")) {
                        String numericPart = normalized.replace("CWE-", "");
                        cweIdMap.put(numericPart, dbCweId);
                        cweIdMap.put("CWE-" + numericPart, dbCweId);
                    }
                }
            }
            
            String tenantId = LoginHelper.getTenantId();
            Long tenantIdLong = 0L;
            if (StringUtils.isNotBlank(tenantId)) {
                try {
                    tenantIdLong = Long.parseLong(tenantId);
                } catch (NumberFormatException e) {
                    tenantIdLong = 0L;
                }
            }
            final Long finalTenantId = tenantIdLong;
            List<KnowledgeItemVulnerabilityType> list = vulnerabilityTypes.stream()
                    .filter(StringUtils::isNotBlank)
                    .map(cweId -> {
                        // 标准化输入格式用于匹配
                        String normalized = cweId.trim().toUpperCase();
                        String matchedCweId = null;
                        
                        // 尝试精确匹配
                        if (cweIdMap.containsKey(normalized)) {
                            matchedCweId = cweIdMap.get(normalized);
                        } else if (normalized.startsWith("CWE-")) {
                            // 尝试数字部分匹配
                            String numericPart = normalized.replace("CWE-", "");
                            if (cweIdMap.containsKey(numericPart)) {
                                matchedCweId = cweIdMap.get(numericPart);
                            }
                        } else {
                            // 尝试添加CWE-前缀匹配
                            String withPrefix = "CWE-" + normalized;
                            if (cweIdMap.containsKey(withPrefix)) {
                                matchedCweId = cweIdMap.get(withPrefix);
                            } else if (cweIdMap.containsKey(normalized)) {
                                matchedCweId = cweIdMap.get(normalized);
                            }
                        }
                        
                        return matchedCweId;
                    })
                    .filter(cweId -> cweId != null)
                    .distinct()
                    .map(cweId -> {
                        KnowledgeItemVulnerabilityType item = new KnowledgeItemVulnerabilityType();
                        item.setItemUuid(itemUuid);
                        item.setCweId(cweId);
                        item.setTenantId(finalTenantId);
                        return item;
                    })
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(list)) {
                vulnerabilityTypeMapper.insertBatch(list);
            } else if (CollectionUtils.isNotEmpty(vulnerabilityTypes)) {
                log.warn("[保存漏洞类型] 所有CWE ID验证失败，未保存任何漏洞类型关联，itemUuid={}, 输入的CWE IDs={}", 
                    itemUuid, vulnerabilityTypes);
            }
        }
    }

    private void saveItemTags(String itemUuid, List<String> tagNames) {
        if (StringUtils.isBlank(itemUuid)) {
            return;
        }
        LambdaQueryWrapper<KnowledgeItemTag> delLqw = Wrappers.lambdaQuery();
        delLqw.eq(KnowledgeItemTag::getItemUuid, itemUuid);
        itemTagMapper.delete(delLqw);
        if (CollectionUtils.isNotEmpty(tagNames)) {
            LambdaQueryWrapper<KnowledgeTag> tagLqw = Wrappers.lambdaQuery();
            tagLqw.in(KnowledgeTag::getTagName, tagNames);
            List<KnowledgeTag> tags = knowledgeTagMapper.selectList(tagLqw);
            if (CollectionUtils.isNotEmpty(tags)) {
                String tenantId = LoginHelper.getTenantId();
                Long tenantIdLong = 0L;
                if (StringUtils.isNotBlank(tenantId)) {
                    try {
                        tenantIdLong = Long.parseLong(tenantId);
                    } catch (NumberFormatException e) {
                        tenantIdLong = 0L;
                    }
                }
                final Long finalTenantId = tenantIdLong;
                List<KnowledgeItemTag> itemTags = tags.stream()
                        .map(tag -> {
                            KnowledgeItemTag itemTag = new KnowledgeItemTag();
                            itemTag.setItemUuid(itemUuid);
                            itemTag.setTagId(tag.getId());
                            itemTag.setTenantId(finalTenantId);
                            return itemTag;
                        })
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(itemTags)) {
                    for (KnowledgeItemTag itemTag : itemTags) {
                        itemTagMapper.insert(itemTag);
                    }
                }
            }
        }
    }

    private boolean isItemInUncompletedProcess(String itemUuid) {
        if (StringUtils.isBlank(itemUuid)) {
            return false;
        }
        String completedStatus = ProcessingStatus.COMPLETED.getCode();
        List<KnowledgeFragment> fragments = fragmentMapper.selectList(
            Wrappers.<KnowledgeFragment>lambdaQuery()
                .eq(KnowledgeFragment::getItemUuid, itemUuid)
                .eq(KnowledgeFragment::getDelFlag, "0")
        );
        if (CollectionUtils.isEmpty(fragments)) {
            return false;
        }
        for (KnowledgeFragment fragment : fragments) {
            List<KnowledgeAttachProcess> processes = attachProcessMapper.selectList(
                Wrappers.<KnowledgeAttachProcess>lambdaQuery()
                    .eq(KnowledgeAttachProcess::getDocId, fragment.getDocId())
                    .ne(KnowledgeAttachProcess::getCurrentStatus, completedStatus)
            );
            if (CollectionUtils.isNotEmpty(processes)) {
                for (KnowledgeAttachProcess process : processes) {
                    String statusData = process.getStatusData();
                    if (StringUtils.isNotBlank(statusData)) {
                        try {
                            JSONObject statusDataObj = JSON.parseObject(statusData);
                            JSONArray llmCreatedItems = statusDataObj.getJSONArray("llmCreatedItems");
                            if (llmCreatedItems != null) {
                                for (int i = 0; i < llmCreatedItems.size(); i++) {
                                    JSONObject item = llmCreatedItems.getJSONObject(i);
                                    if (item != null && itemUuid.equals(item.getString("itemUuid"))) {
                                        return true;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            log.warn("解析statusData失败: processId={}, error={}", process.getId(), e.getMessage());
                        }
                    }
                }
            }
        }
        return false;
    }

    private void validEntityBeforeSave(KnowledgeItem entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        List<KnowledgeItem> items = baseMapper.selectBatchIds(ids);
        Set<String> affectedKids = items.stream()
                .map(KnowledgeItem::getKid)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
        List<String> itemUuids = items.stream()
                .map(KnowledgeItem::getItemUuid)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(itemUuids)) {
            for (String itemUuid : itemUuids) {
                vulnerabilityTypeMapper.deleteByItemUuid(itemUuid);
                LambdaQueryWrapper<KnowledgeItemTag> itemTagLqw = Wrappers.lambdaQuery();
                itemTagLqw.eq(KnowledgeItemTag::getItemUuid, itemUuid);
                itemTagMapper.delete(itemTagLqw);
            }
        }
        boolean flag = baseMapper.deleteBatchIds(ids) > 0;
        if (flag) {
            // 批量删除后更新所有受影响知识库的统计字段
            for (String kid : affectedKids) {
                baseMapper.updateKnowledgeItemCount(kid);
                updateKnowledgeDataSize(kid);
            }
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByItemUuid(String itemUuid) {
        KnowledgeItem entity = baseMapper.selectByItemUuid(itemUuid);
        if (entity == null) {
            throw new ServiceException("知识条目不存在");
        }
        Long currentUserId = LoginHelper.getUserId();
        boolean isSuperAdmin = LoginHelper.isSuperAdmin();
        if (!isSuperAdmin && !Objects.equals(entity.getCreateBy(), currentUserId)) {
            throw new ServiceException("无权限删除此知识条目，仅作者或管理员可以删除");
        }
        String kid = entity.getKid();
        vulnerabilityTypeMapper.deleteByItemUuid(itemUuid);
        LambdaQueryWrapper<KnowledgeItemTag> itemTagLqw = Wrappers.lambdaQuery();
        itemTagLqw.eq(KnowledgeItemTag::getItemUuid, itemUuid);
        itemTagMapper.delete(itemTagLqw);
        boolean flag = baseMapper.deleteById(entity.getId()) > 0;
        if (flag && StringUtils.isNotBlank(kid)) {
            baseMapper.updateKnowledgeItemCount(kid);
            updateKnowledgeDataSize(kid);
        }
        return flag;
    }

    private void parseCvssVector(String cvssVector, FacetStatsVo stats) {
        if (StringUtils.isBlank(cvssVector)) {
            return;
        }
        String[] parts = cvssVector.split("/");
        for (String part : parts) {
            if (part.contains(":")) {
                String[] kv = part.split(":");
                if (kv.length == 2) {
                    String metric = kv[0].trim();
                    String value = kv[1].trim();
                    switch (metric) {
                        case "AV":
                            stats.getCvssAttackVector().merge(value, 1L, Long::sum);
                            break;
                        case "AC":
                            stats.getCvssAttackComplexity().merge(value, 1L, Long::sum);
                            break;
                        case "PR":
                            stats.getCvssPrivilegesRequired().merge(value, 1L, Long::sum);
                            break;
                        case "UI":
                            stats.getCvssUserInteraction().merge(value, 1L, Long::sum);
                            break;
                        case "S":
                            stats.getCvssScope().merge(value, 1L, Long::sum);
                            break;
                        case "C":
                            stats.getCvssConfidentiality().merge(value, 1L, Long::sum);
                            break;
                        case "I":
                            stats.getCvssIntegrity().merge(value, 1L, Long::sum);
                            break;
                        case "A":
                            stats.getCvssAvailability().merge(value, 1L, Long::sum);
                            break;
                    }
                }
            }
        }
    }

    private FacetStatsVo calculateFacetStats(KnowledgeItemBo bo) {
        FacetStatsVo stats = new FacetStatsVo();
        LambdaQueryWrapper<KnowledgeItem> lqw = buildQueryWrapper(bo);
        List<KnowledgeItemVo> allItems = baseMapper.selectVoList(lqw);
        if (allItems != null) {
            allItems.forEach(this::loadVulnerabilityTypes);
            for (KnowledgeItemVo item : allItems) {
                if (StringUtils.isNotBlank(item.getSeverity())) {
                    stats.getSeverities().merge(item.getSeverity(), 1L, Long::sum);
                }
                if (StringUtils.isNotBlank(item.getLanguage())) {
                    stats.getLanguages().merge(item.getLanguage(), 1L, Long::sum);
                }
                if (StringUtils.isNotBlank(item.getStatus())) {
                    stats.getStatuses().merge(item.getStatus(), 1L, Long::sum);
                }
                if (CollectionUtils.isNotEmpty(item.getVulnerabilityTypes())) {
                    for (String type : item.getVulnerabilityTypes()) {
                        stats.getVulnerabilityTypes().merge(type, 1L, Long::sum);
                    }
                }
                if (StringUtils.isNotBlank(item.getCvssVector())) {
                    parseCvssVector(item.getCvssVector(), stats);
                }
            }
            List<String> itemUuids = allItems.stream()
                    .map(KnowledgeItemVo::getItemUuid)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(itemUuids)) {
                LambdaQueryWrapper<KnowledgeItemTag> itemTagLqw = Wrappers.lambdaQuery();
                itemTagLqw.in(KnowledgeItemTag::getItemUuid, itemUuids);
                List<KnowledgeItemTag> itemTags = itemTagMapper.selectList(itemTagLqw);
                if (CollectionUtils.isNotEmpty(itemTags)) {
                    List<Long> tagIds = itemTags.stream()
                            .map(KnowledgeItemTag::getTagId)
                            .distinct()
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(tagIds)) {
                        LambdaQueryWrapper<KnowledgeTag> tagLqw = Wrappers.lambdaQuery();
                        tagLqw.in(KnowledgeTag::getId, tagIds);
                        List<KnowledgeTag> tags = knowledgeTagMapper.selectList(tagLqw);
                        Map<Long, String> tagIdToNameMap = tags.stream()
                                .collect(Collectors.toMap(KnowledgeTag::getId, KnowledgeTag::getTagName));
                        for (KnowledgeItemTag itemTag : itemTags) {
                            String tagName = tagIdToNameMap.get(itemTag.getTagId());
                            if (StringUtils.isNotBlank(tagName)) {
                                stats.getTags().merge(tagName, 1L, Long::sum);
                            }
                        }
                    }
                }
            }
        }
        return stats;
    }

    private List<ClusterWithItemsVo> calculateGroupedByClusters(FacetStatsVo facetStats, String vulnerabilityTypeKeyword) {
        List<ClusterWithItemsVo> result = new ArrayList<>();
        boolean hasData = facetStats != null && facetStats.getVulnerabilityTypes() != null && 
                         !facetStats.getVulnerabilityTypes().isEmpty();
        Set<String> availableCwes = hasData ? facetStats.getVulnerabilityTypes().keySet() : null;
        String keyword = vulnerabilityTypeKeyword != null ? vulnerabilityTypeKeyword.toLowerCase().trim() : "";
        List<CweClusterVo> clusters = cweClusterMapper.selectByClusterMethod("kmeans");
        if (clusters == null || clusters.isEmpty()) {
            return result;
        }
        Map<String, String> cweDisplayNames = new HashMap<>();
        if (StringUtils.isNotBlank(keyword)) {
            List<CweReferenceVo> allCweRefs = cweReferenceMapper.selectVoList(Wrappers.lambdaQuery());
            for (CweReferenceVo ref : allCweRefs) {
                String displayName = ref.getCweId();
                if (StringUtils.isNotBlank(ref.getNameZh())) {
                    displayName = ref.getCweId() + ": " + ref.getNameZh();
                }
                cweDisplayNames.put(ref.getCweId(), displayName);
            }
        }
        for (CweClusterVo cluster : clusters) {
            ClusterWithItemsVo vo = new ClusterWithItemsVo();
            vo.setId(cluster.getId());
            vo.setClusterId(cluster.getClusterId());
            vo.setClusterMethod(cluster.getClusterMethod());
            vo.setClusterName(cluster.getClusterNameEn());
            vo.setClusterNameZh(cluster.getClusterNameZh());
            vo.setClusterDescription(cluster.getDescription());
            vo.setClusterDescriptionZh(cluster.getDescription());
            List<CweClusterMappingVo> mappings = cweClusterMappingMapper.selectByClusterIdAndMethod(
                cluster.getClusterId(), cluster.getClusterMethod()
            );
            List<String> cwes = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(mappings)) {
                for (CweClusterMappingVo mapping : mappings) {
                    String cweId = mapping.getCweId();
                    if (StringUtils.isNotBlank(keyword)) {
                        String displayName = cweDisplayNames.getOrDefault(cweId, cweId).toLowerCase();
                        if (!displayName.contains(keyword) && !cweId.toLowerCase().contains(keyword)) {
                            continue;
                        }
                    }
                    cwes.add(cweId);
                }
            }
            vo.setCwes(cwes);
            vo.setCount(cwes.size());
            long itemCount = 0L;
            if (facetStats != null && facetStats.getVulnerabilityTypes() != null) {
                for (String cweId : cwes) {
                    itemCount += facetStats.getVulnerabilityTypes().getOrDefault(cweId, 0L);
                }
            }
            vo.setItemCount(itemCount);
            result.add(vo);
        }
        return result;
    }

    public void updateKnowledgeDataSize(String kid) {
        if (StringUtils.isBlank(kid)) {
            return;
        }
        try {
            List<KnowledgeAttach> attaches = attachMapper.selectList(
                Wrappers.<KnowledgeAttach>lambdaQuery()
                    .eq(KnowledgeAttach::getKid, kid)
            );
            if (CollectionUtils.isEmpty(attaches)) {
                updateDataSizeToZero(kid);
                return;
            }
            Set<String> uncompletedDocIds = getUncompletedDocIds();
            long totalSize = 0L;
            OssClient storage = OssFactory.instance();
            List<Long> ossIds = attaches.stream()
                .map(KnowledgeAttach::getOssId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
            Map<Long, SysOssVo> ossMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(ossIds)) {
                List<SysOssVo> ossList = sysOssService.listByIds(ossIds);
                ossMap = ossList.stream()
                    .collect(Collectors.toMap(SysOssVo::getOssId, oss -> oss, (a, b) -> a));
            }
            for (KnowledgeAttach attach : attaches) {
                if (uncompletedDocIds.contains(attach.getDocId())) {
                    continue;
                }
                if (attach.getOssId() == null) {
                    continue;
                }
                SysOssVo oss = ossMap.get(attach.getOssId());
                if (oss == null || StringUtils.isBlank(oss.getUrl())) {
                    continue;
                }
                try {
                    String url = oss.getUrl();
                    String objectPath;
                    if (StringUtils.isNotBlank(oss.getFileName())) {
                        objectPath = oss.getFileName();
                    } else if (url.startsWith("http://") || url.startsWith("https://")) {
                        try {
                            URI uri = new URI(url);
                            String path = uri.getPath().startsWith("/") ? uri.getPath().substring(1) : uri.getPath();
                            String baseUrl = storage.getUrl();
                            String basePath = baseUrl.replace("http://", "").replace("https://", "");
                            if (path.startsWith(basePath + "/")) {
                                objectPath = path.substring(basePath.length() + 1);
                            } else {
                                String[] parts = path.split("/", 2);
                                if (parts.length > 1) {
                                    objectPath = parts[1];
                                } else {
                                    objectPath = path;
                                }
                            }
                        } catch (URISyntaxException e) {
                            String baseUrl = storage.getUrl();
                            objectPath = url.replace(baseUrl + "/", "").replace(baseUrl, "");
                            String bucketName = storage.getConfigKey();
                            if (objectPath.startsWith(bucketName + "/")) {
                                objectPath = objectPath.substring(bucketName.length() + 1);
                            }
                        }
                    } else {
                        objectPath = url;
                    }
                    ObjectMetadata metadata = storage.getObjectMetadata(objectPath);
                    if (metadata != null && metadata.getContentLength() > 0) {
                        long fileSize = metadata.getContentLength();
                        totalSize += fileSize;
                    }
                } catch (Exception e) {
                    log.warn("[KnowledgeItemServiceImpl] 获取文件大小失败: ossId={}, docId={}, error={}", attach.getOssId(), attach.getDocId(), e.getMessage());
                }
            }
            baseMapper.updateKnowledgeDataSize(kid, totalSize);
        } catch (Exception e) {
            log.error("[KnowledgeItemServiceImpl] 更新知识库存储大小失败: kid={}, error={}", kid, e.getMessage(), e);
        }
    }

    private Set<String> getUncompletedDocIds() {
        List<KnowledgeAttachProcess> processes = attachProcessMapper.selectList(
            Wrappers.<KnowledgeAttachProcess>lambdaQuery()
                .ne(KnowledgeAttachProcess::getCurrentStatus, ProcessingStatus.COMPLETED.getCode())
        );
        return processes.stream()
            .map(KnowledgeAttachProcess::getDocId)
            .filter(StringUtils::isNotBlank)
            .collect(Collectors.toSet());
    }

    private void updateDataSizeToZero(String kid) {
        baseMapper.updateKnowledgeDataSize(kid, 0L);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BatchDeleteResultVo batchDeleteByItemUuids(List<String> itemUuids) {
        if (CollectionUtils.isEmpty(itemUuids)) {
            return BatchDeleteResultVo.builder()
                .successCount(0)
                .failedCount(0)
                .failures(new ArrayList<>())
                .build();
        }
        if (itemUuids.size() > 1000) {
            throw new ServiceException("单次最多删除1000条");
        }
        Long currentUserId = LoginHelper.getUserId();
        boolean isSuperAdmin = LoginHelper.isSuperAdmin();
        List<DeleteFailureVo> failures = new ArrayList<>();
        int successCount = 0;
        Set<String> affectedKids = new java.util.HashSet<>();
        for (String itemUuid : itemUuids) {
            try {
                KnowledgeItem entity = baseMapper.selectByItemUuid(itemUuid);
                if (entity == null) {
                    failures.add(DeleteFailureVo.builder()
                        .itemUuid(itemUuid)
                        .reason("条目不存在")
                        .errorCode("RESOURCE_NOT_FOUND")
                        .build());
                    continue;
                }
                if (!isSuperAdmin && !Objects.equals(entity.getCreateBy(), currentUserId)) {
                    failures.add(DeleteFailureVo.builder()
                        .itemUuid(itemUuid)
                        .reason("无删除权限")
                        .errorCode("PERMISSION_DENIED")
                        .build());
                    continue;
                }
                String kid = entity.getKid();
                vulnerabilityTypeMapper.deleteByItemUuid(itemUuid);
                LambdaQueryWrapper<KnowledgeItemTag> itemTagLqw = Wrappers.lambdaQuery();
                itemTagLqw.eq(KnowledgeItemTag::getItemUuid, itemUuid);
                itemTagMapper.delete(itemTagLqw);
                boolean flag = baseMapper.deleteById(entity.getId()) > 0;
                if (flag) {
                    successCount++;
                    if (StringUtils.isNotBlank(kid)) {
                        affectedKids.add(kid);
                    }
                } else {
                    failures.add(DeleteFailureVo.builder()
                        .itemUuid(itemUuid)
                        .reason("删除失败")
                        .errorCode("DELETE_FAILED")
                        .build());
                }
            } catch (Exception e) {
                log.error("批量删除条目失败: itemUuid={}, error={}", itemUuid, e.getMessage(), e);
                failures.add(DeleteFailureVo.builder()
                    .itemUuid(itemUuid)
                    .reason("删除异常: " + e.getMessage())
                    .errorCode("EXCEPTION")
                    .build());
            }
        }
        for (String kid : affectedKids) {
            baseMapper.updateKnowledgeItemCount(kid);
            updateKnowledgeDataSize(kid);
        }
        return BatchDeleteResultVo.builder()
            .successCount(successCount)
            .failedCount(failures.size())
            .failures(failures)
            .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BatchUpdateResultVo batchUpdateByItemUuids(BatchUpdateRequestBo request) {
        if (CollectionUtils.isEmpty(request.getItemUuids())) {
            return BatchUpdateResultVo.builder()
                .successCount(0)
                .failedCount(0)
                .failures(new ArrayList<>())
                .build();
        }
        if (request.getItemUuids().size() > 1000) {
            throw new ServiceException("单次最多更新1000条");
        }
        String field = request.getField();
        Object value = request.getValue();
        if (StringUtils.isBlank(field) || value == null) {
            throw new ServiceException("字段名和值不能为空");
        }
        Set<String> allowedFields = new HashSet<>();
        allowedFields.add("language");
        allowedFields.add("severity");
        allowedFields.add("status");
        allowedFields.add("tags");
        allowedFields.add("riskAttackVector");
        allowedFields.add("riskComplexity");
        allowedFields.add("riskPrivileges");
        allowedFields.add("riskUserInteraction");
        if (!allowedFields.contains(field)) {
            throw new ServiceException("不允许批量更新字段: " + field);
        }
        Long currentUserId = LoginHelper.getUserId();
        boolean isSuperAdmin = LoginHelper.isSuperAdmin();
        List<UpdateFailureVo> failures = new ArrayList<>();
        int successCount = 0;
        Set<String> affectedKids = new HashSet<>();
        for (String itemUuid : request.getItemUuids()) {
            try {
                KnowledgeItem entity = baseMapper.selectByItemUuid(itemUuid);
                if (entity == null) {
                    failures.add(UpdateFailureVo.builder()
                        .itemUuid(itemUuid)
                        .reason("条目不存在")
                        .errorCode("RESOURCE_NOT_FOUND")
                        .build());
                    continue;
                }
                if (!isSuperAdmin && !Objects.equals(entity.getCreateBy(), currentUserId)) {
                    failures.add(UpdateFailureVo.builder()
                        .itemUuid(itemUuid)
                        .reason("无编辑权限")
                        .errorCode("PERMISSION_DENIED")
                        .build());
                    continue;
                }
                updateSingleItemField(entity, field, value);
                boolean flag = baseMapper.updateById(entity) > 0;
                if (flag) {
                    successCount++;
                    if (StringUtils.isNotBlank(entity.getKid())) {
                        affectedKids.add(entity.getKid());
                    }
                } else {
                    failures.add(UpdateFailureVo.builder()
                        .itemUuid(itemUuid)
                        .reason("更新失败")
                        .errorCode("UPDATE_FAILED")
                        .build());
                }
            } catch (Exception e) {
                log.error("批量更新条目失败: itemUuid={}, field={}, error={}", itemUuid, field, e.getMessage(), e);
                failures.add(UpdateFailureVo.builder()
                    .itemUuid(itemUuid)
                    .reason("更新异常: " + e.getMessage())
                    .errorCode("EXCEPTION")
                    .build());
            }
        }
        for (String kid : affectedKids) {
            baseMapper.updateKnowledgeItemCount(kid);
            updateKnowledgeDataSize(kid);
        }
        return BatchUpdateResultVo.builder()
            .successCount(successCount)
            .failedCount(failures.size())
            .failures(failures)
            .build();
    }

    private void updateSingleItemField(KnowledgeItem entity, String field, Object value) {
        switch (field) {
            case "language":
                entity.setLanguage(value != null ? value.toString() : null);
                break;
            case "severity":
                entity.setSeverity(value != null ? value.toString() : null);
                break;
            case "status":
                entity.setStatus(value != null ? value.toString() : null);
                if ("published".equals(value) && entity.getPublishTime() == null) {
                    entity.setPublishTime(new java.util.Date());
                } else if ("archived".equals(value) && entity.getArchiveTime() == null) {
                    entity.setArchiveTime(new java.util.Date());
                }
                break;
            case "tags":
                if (value instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<String> tagNames = (List<String>) value;
                    saveItemTags(entity.getItemUuid(), tagNames);
                }
                break;
            case "riskAttackVector":
            case "riskComplexity":
            case "riskPrivileges":
            case "riskUserInteraction":
                updateCvssFields(entity, field, value);
                break;
            default:
                throw new ServiceException("不支持的字段: " + field);
        }
    }

    private void updateCvssFields(KnowledgeItem entity, String field, Object value) {
        String attackVector = null;
        String attackComplexity = null;
        String privilegesRequired = null;
        String userInteraction = null;
        String cvssVector = entity.getCvssVector();
        if ("riskAttackVector".equals(field)) {
            attackVector = value != null ? value.toString() : null;
            attackComplexity = parseCvssField(cvssVector, "AC");
            privilegesRequired = parseCvssField(cvssVector, "PR");
            userInteraction = parseCvssField(cvssVector, "UI");
        } else if ("riskComplexity".equals(field)) {
            attackComplexity = value != null ? value.toString() : null;
            attackVector = parseCvssField(cvssVector, "AV");
            privilegesRequired = parseCvssField(cvssVector, "PR");
            userInteraction = parseCvssField(cvssVector, "UI");
        } else if ("riskPrivileges".equals(field)) {
            privilegesRequired = value != null ? value.toString() : null;
            attackVector = parseCvssField(cvssVector, "AV");
            attackComplexity = parseCvssField(cvssVector, "AC");
            userInteraction = parseCvssField(cvssVector, "UI");
        } else if ("riskUserInteraction".equals(field)) {
            userInteraction = value != null ? value.toString() : null;
            attackVector = parseCvssField(cvssVector, "AV");
            attackComplexity = parseCvssField(cvssVector, "AC");
            privilegesRequired = parseCvssField(cvssVector, "PR");
        }
        if (StringUtils.isNotBlank(attackVector) && StringUtils.isNotBlank(attackComplexity)
            && StringUtils.isNotBlank(privilegesRequired) && StringUtils.isNotBlank(userInteraction)) {
            List<String> impact = parseCvssImpact(cvssVector);
            if (impact.isEmpty()) {
                impact.add("C");
                impact.add("I");
                impact.add("A");
            }
            java.math.BigDecimal cvssScore = CvssScoreCalculator.calculateCvssScore(
                attackVector, attackComplexity, privilegesRequired, userInteraction, impact);
            if (cvssScore != null) {
                entity.setCvssScore(cvssScore);
            }
            //severity字段只保存用户手动设置的值，不保存CVSS计算出的severity
            //CVSS计算出的severity仅用于前端显示，不存储到数据库
            StringBuilder cvssBuilder = new StringBuilder("CVSS:4.0");
            cvssBuilder.append("/AV:").append(attackVector);
            cvssBuilder.append("/AC:").append(attackComplexity);
            cvssBuilder.append("/AT:N");
            cvssBuilder.append("/PR:").append(privilegesRequired);
            cvssBuilder.append("/UI:").append(userInteraction);
            boolean hasC = impact.contains("C");
            boolean hasI = impact.contains("I");
            boolean hasA = impact.contains("A");
            cvssBuilder.append("/VC:").append(hasC ? "H" : "N");
            cvssBuilder.append("/VI:").append(hasI ? "H" : "N");
            cvssBuilder.append("/VA:").append(hasA ? "H" : "N");
            cvssBuilder.append("/SC:N/SI:N/SA:N");
            String newCvssVector = cvssBuilder.toString();
            if (newCvssVector.length() > 255) {
                newCvssVector = newCvssVector.substring(0, 255);
            }
            entity.setCvssVector(newCvssVector);
            entity.setCvssVersion("4.0");
        }
    }

    private String parseCvssField(String cvssVector, String metric) {
        if (StringUtils.isBlank(cvssVector)) {
            return null;
        }
        String[] parts = cvssVector.split("/");
        for (String part : parts) {
            if (part.startsWith(metric + ":")) {
                return part.substring(metric.length() + 1);
            }
        }
        return null;
    }

    private String getCvssComponentLabel(String cvssVector, String metric) {
        if (StringUtils.isBlank(cvssVector)) {
            return "";
        }
        String value = parseCvssField(cvssVector, metric);
        if (StringUtils.isBlank(value)) {
            return "";
        }
        Map<String, String> metricLabels = new HashMap<>();
        metricLabels.put("AV:N", "网络");
        metricLabels.put("AV:A", "网络相邻");
        metricLabels.put("AV:L", "本地");
        metricLabels.put("AV:P", "物理");
        metricLabels.put("AC:L", "低");
        metricLabels.put("AC:H", "高");
        metricLabels.put("PR:N", "无");
        metricLabels.put("PR:L", "低");
        metricLabels.put("PR:H", "高");
        metricLabels.put("UI:N", "无");
        metricLabels.put("UI:R", "必需");
        metricLabels.put("UI:A", "活跃");
        metricLabels.put("VC:H", "高");
        metricLabels.put("VC:L", "低");
        metricLabels.put("VC:N", "无");
        metricLabels.put("VI:H", "高");
        metricLabels.put("VI:L", "低");
        metricLabels.put("VI:N", "无");
        metricLabels.put("VA:H", "高");
        metricLabels.put("VA:L", "低");
        metricLabels.put("VA:N", "无");
        String key = metric + ":" + value;
        return metricLabels.getOrDefault(key, value);
    }

    private List<String> parseCvssImpact(String cvssVector) {
        List<String> impact = new ArrayList<>();
        if (StringUtils.isBlank(cvssVector)) {
            return impact;
        }
        String[] parts = cvssVector.split("/");
        for (String part : parts) {
            if (part.startsWith("VC:") && "H".equals(part.substring(3))) {
                impact.add("C");
            } else if (part.startsWith("VI:") && "H".equals(part.substring(3))) {
                impact.add("I");
            } else if (part.startsWith("VA:") && "H".equals(part.substring(3))) {
                impact.add("A");
            }
        }
        return impact;
    }

    @Override
    public ExportPreviewVo exportPreview(ExportPreviewRequestBo request) {
        int previewLimit = Math.min(10, Math.max(5, (int) Math.min(getExportDataCount(request), 10)));
        List<KnowledgeItemVo> sampleData = getExportData(request, previewLimit);
        long totalCount = getExportDataCount(request);
        List<FieldInfoVo> fieldInfos = buildFieldInfos(request.getSelectedFields(), request.getExpandedFields());
        long estimatedFileSize = estimateFileSize(sampleData, request.getFormat(), fieldInfos.size());
        int estimatedTime = estimateTime(totalCount, request.getFormat());
        String previewHtml = null;
        String pdfFormatType = null;
        if ("pdf".equals(request.getFormat())) {
            PdfOptionsBo pdfOptions = request.getPdfOptions();
            boolean useReportFormat;
            if (pdfOptions != null && StringUtils.isNotBlank(pdfOptions.getFormatType())) {
                useReportFormat = "report".equals(pdfOptions.getFormatType());
            } else {
                useReportFormat = shouldUseReportFormat(fieldInfos);
            }
            pdfFormatType = useReportFormat ? "report" : "table";
            previewHtml = generatePreviewHtml(sampleData, fieldInfos, request.getFieldFormats(), useReportFormat);
        }
        return ExportPreviewVo.builder()
            .sampleData(convertToMapList(sampleData, request.getSelectedFields(), request.getExpandedFields(), request.getFieldFormats()))
            .totalCount(totalCount)
            .selectedFields(fieldInfos)
            .estimatedFileSize(estimatedFileSize)
            .estimatedTime(estimatedTime)
            .previewHtml(previewHtml)
            .pdfFormatType(pdfFormatType)
            .build();
    }

    @Override
    public void export(ExportRequestBo request, HttpServletResponse response) {
        List<String> warnings = new ArrayList<>();
        try {
            String fileName = StringUtils.isNotBlank(request.getFileName()) 
                ? request.getFileName() 
                : generateDefaultFileName(request.getFormat());
            if (StringUtils.isBlank(request.getFormat())) {
                throw new ServiceException("导出格式不能为空");
            }
            if (CollectionUtils.isEmpty(request.getSelectedFields())) {
                throw new ServiceException("请至少选择一个导出字段");
            }
            if ("excel".equals(request.getFormat())) {
                long totalCount = getExportDataCount(request);
                if (totalCount == 0) {
                    throw new ServiceException("没有可导出的数据");
                }
                List<KnowledgeItemVo> data = getExportData(request, null);
                exportToExcel(data, request, fileName, response);
            } else if ("pdf".equals(request.getFormat())) {
                long totalCount = getExportDataCount(request);
                if (totalCount == 0) {
                    throw new ServiceException("没有可导出的数据");
                }
                exportToPdfStreaming(request, fileName, response, warnings);
            } else {
                throw new ServiceException("不支持的导出格式: " + request.getFormat());
            }
            if (!warnings.isEmpty()) {
                String warningsJson = String.join(";", warnings);
                response.setHeader("X-Export-Warnings", URLEncoder.encode(warningsJson, StandardCharsets.UTF_8));
            }
        } catch (ServiceException e) {
            log.error("导出失败: {}", e.getMessage());
            try {
                if (!response.isCommitted()) {
                    response.reset();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.setContentType("application/json;charset=utf-8");
                    response.getWriter().write("{\"code\":500,\"msg\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}");
                    response.getWriter().flush();
                }
            } catch (IOException ioException) {
                log.error("写入错误响应失败", ioException);
            }
            throw e;
        } catch (Exception e) {
            log.error("导出失败", e);
            try {
                if (!response.isCommitted()) {
                    response.reset();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.setContentType("application/json;charset=utf-8");
                    String errorMsg = "导出失败: " + e.getMessage();
                    response.getWriter().write("{\"code\":500,\"msg\":\"" + errorMsg.replace("\"", "\\\"").replace("\n", "\\n") + "\"}");
                    response.getWriter().flush();
                }
            } catch (IOException ioException) {
                log.error("写入错误响应失败", ioException);
            }
            throw new ServiceException("导出失败: " + e.getMessage());
        }
    }

    private List<KnowledgeItemVo> getExportData(ExportPreviewRequestBo request, Integer limit) {
        KnowledgeItemBo bo = new KnowledgeItemBo();
        if ("selected".equals(request.getExportRange()) && CollectionUtils.isNotEmpty(request.getItemUuids())) {
            bo.setItemUuids(request.getItemUuids());
            List<KnowledgeItemVo> list = queryList(bo);
            if (limit != null && limit > 0 && list != null && list.size() > limit) {
                return list.subList(0, limit);
            }
            return list != null ? list : new ArrayList<>();
        } else if ("currentPage".equals(request.getExportRange()) && request.getPageNum() != null && request.getPageSize() != null) {
            PageQuery pageQuery = new PageQuery(request.getPageSize(), request.getPageNum());
            if (request.getFilters() != null) {
                bo = request.getFilters();
            }
            TableDataInfo<KnowledgeItemVo> pageData = queryPageList(bo, pageQuery);
            List<KnowledgeItemVo> list = pageData.getRows();
            if (limit != null && limit > 0 && list != null && list.size() > limit) {
                return list.subList(0, limit);
            }
            return list != null ? list : new ArrayList<>();
        } else {
            if (request.getFilters() != null) {
                bo = request.getFilters();
            }
            List<KnowledgeItemVo> list = queryList(bo);
            if (limit != null && limit > 0 && list != null && list.size() > limit) {
                return list.subList(0, limit);
            }
            return list != null ? list : new ArrayList<>();
        }
    }

    private long getExportDataCount(ExportPreviewRequestBo request) {
        if ("selected".equals(request.getExportRange()) && CollectionUtils.isNotEmpty(request.getItemUuids())) {
            return request.getItemUuids().size();
        } else if ("currentPage".equals(request.getExportRange()) && request.getPageNum() != null && request.getPageSize() != null) {
            KnowledgeItemBo bo = new KnowledgeItemBo();
            if (request.getFilters() != null) {
                bo = request.getFilters();
            }
            PageQuery pageQuery = new PageQuery(request.getPageSize(), request.getPageNum());
            TableDataInfo<KnowledgeItemVo> pageData = queryPageList(bo, pageQuery);
            return pageData.getRows() != null ? pageData.getRows().size() : 0;
        }
        KnowledgeItemBo bo = new KnowledgeItemBo();
        if (request.getFilters() != null) {
            bo = request.getFilters();
        }
        LambdaQueryWrapper<KnowledgeItem> lqw = buildQueryWrapper(bo);
        return baseMapper.selectCount(lqw);
    }

    private List<FieldInfoVo> buildFieldInfos(List<String> selectedFields, Map<String, List<String>> expandedFields) {
        Map<String, String> fieldLabels = getFieldLabels();
        List<FieldInfoVo> fieldInfos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(selectedFields)) {
            for (String field : selectedFields) {
                //过滤掉已废弃的字段
                if ("vulnerabilityType".equals(field)) {
                    continue;
                }
                fieldInfos.add(FieldInfoVo.builder()
                    .key(field)
                    .label(fieldLabels.getOrDefault(field, field))
                    .type("base")
                    .build());
                if (expandedFields != null && expandedFields.containsKey(field)) {
                    for (String expandedField : expandedFields.get(field)) {
                        fieldInfos.add(FieldInfoVo.builder()
                            .key(expandedField)
                            .label(fieldLabels.getOrDefault(expandedField, expandedField))
                            .type("expanded")
                            .parentField(field)
                            .build());
                    }
                }
            }
        }
        return fieldInfos;
    }

    private Map<String, String> getFieldLabels() {
        Map<String, String> labels = new HashMap<>();
        labels.put("title", "标题");
        labels.put("summary", "摘要");
        labels.put("problemDescription", "问题描述");
        labels.put("fixSolution", "修复方案");
        labels.put("exampleCode", "示例代码");
        labels.put("referenceLink", "参考链接");
        labels.put("severity", "风险等级");
        labels.put("vulnerabilityTypes", "漏洞类型");
        labels.put("vulnerabilityTypeName", "漏洞类型名称（中文）");
        labels.put("vulnerabilityTypeNameEn", "漏洞类型名称（英文）");
        labels.put("vulnerabilityTypeDescription", "漏洞类型描述（中文）");
        labels.put("vulnerabilityTypeDescriptionEn", "漏洞类型描述（英文）");
        labels.put("language", "编程语言");
        labels.put("cvssScore", "CVSS评分");
        labels.put("cvssAttackVector", "CVSS攻击方式");
        labels.put("cvssAttackComplexity", "CVSS利用复杂度");
        labels.put("cvssPrivilegesRequired", "CVSS权限需求");
        labels.put("cvssUserInteraction", "CVSS用户交互");
        labels.put("cvssConfidentialityImpact", "CVSS机密性影响");
        labels.put("cvssIntegrityImpact", "CVSS完整性影响");
        labels.put("cvssAvailabilityImpact", "CVSS可用性影响");
        labels.put("status", "状态");
        labels.put("tags", "标签");
        labels.put("fragmentCount", "片段数量");
        labels.put("createTime", "创建时间");
        labels.put("updateTime", "更新时间");
        labels.put("createBy", "创建人");
        labels.put("updateBy", "更新人");
        labels.put("kid", "知识库");
        return labels;
    }

    private long estimateFileSize(List<KnowledgeItemVo> sampleData, String format, int fieldCount) {
        if (CollectionUtils.isEmpty(sampleData)) {
            return 0;
        }
        long avgSize = sampleData.stream()
            .mapToLong(item -> estimateItemSize(item))
            .sum() / sampleData.size();
        return avgSize * fieldCount * 2;
    }

    private long estimateItemSize(KnowledgeItemVo item) {
        long size = 0;
        if (StringUtils.isNotBlank(item.getTitle())) size += item.getTitle().length();
        if (StringUtils.isNotBlank(item.getSummary())) size += item.getSummary().length();
        if (StringUtils.isNotBlank(item.getProblemDescription())) size += item.getProblemDescription().length();
        if (StringUtils.isNotBlank(item.getFixSolution())) size += item.getFixSolution().length();
        if (StringUtils.isNotBlank(item.getExampleCode())) size += item.getExampleCode().length();
        return size;
    }

    private int estimateTime(long totalCount, String format) {
        int baseTime = "excel".equals(format) ? 1 : 2;
        return (int) Math.max(1, baseTime + totalCount / 1000);
    }

    private String generatePreviewHtml(List<KnowledgeItemVo> sampleData, List<FieldInfoVo> fieldInfos, Map<String, String> fieldFormats, boolean useReportFormat) {
        if (CollectionUtils.isEmpty(sampleData)) {
            return "<p>暂无数据</p>";
        }
        if (useReportFormat) {
            return generateReportFormatPreviewHtml(sampleData, fieldInfos, fieldFormats);
        } else {
            return generateTableFormatPreviewHtml(sampleData, fieldInfos, fieldFormats);
        }
    }
    private String generateTableFormatPreviewHtml(List<KnowledgeItemVo> sampleData, List<FieldInfoVo> fieldInfos, Map<String, String> fieldFormats) {
        if (CollectionUtils.isEmpty(sampleData)) {
            return "<p>暂无数据</p>";
        }
        List<String> selectedFields = fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList());
        Map<String, List<String>> expandedFields = extractExpandedFields(fieldInfos);
        Map<String, CweReferenceVo> cweMap = buildCweMap(sampleData, expandedFields, fieldFormats);
        Map<String, KnowledgeTagVo> tagMap = buildTagMap(sampleData, expandedFields, fieldFormats);
        Map<Long, String> userMap = buildUserMap(sampleData, selectedFields);
        Map<String, String> knowledgeBaseMap = buildKnowledgeBaseMap(sampleData, selectedFields, expandedFields);
        StringBuilder html = new StringBuilder("<div style='font-family: \"Microsoft YaHei\", \"SimSun\", \"Helvetica Neue\", Arial, sans-serif;'>");
        html.append("<table style='border-collapse: collapse; width: 100%; font-size: 12px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); background-color: #fff;'>");
        html.append("<thead><tr style='background-color: #f5f5f5;'>");
        for (FieldInfoVo fieldInfo : fieldInfos) {
            html.append("<th style='padding: 10px 8px; border: 1px solid #c8c8c8; font-weight: 600; text-align: center; color: #333; white-space: nowrap;'>")
                .append(escapeHtml(fieldInfo.getLabel())).append("</th>");
        }
        html.append("</tr></thead><tbody>");
        for (int i = 0; i < sampleData.size(); i++) {
            KnowledgeItemVo item = sampleData.get(i);
            String rowStyle = i % 2 == 0 
                ? "background-color: #ffffff; transition: background-color 0.2s;" 
                : "background-color: #f9f9f9; transition: background-color 0.2s;";
            html.append("<tr style='").append(rowStyle).append("' onmouseover=\"this.style.backgroundColor='#f0f7ff'\" onmouseout=\"this.style.backgroundColor='").append(i % 2 == 0 ? "#ffffff" : "#f9f9f9").append("'\">");
            for (FieldInfoVo fieldInfo : fieldInfos) {
                String value = getFieldValue(item, fieldInfo.getKey(), cweMap, tagMap, userMap, knowledgeBaseMap, fieldFormats);
                if (value.length() > 100) {
                    value = value.substring(0, 100) + "...";
                }
                html.append("<td style='padding: 10px 8px; border: 1px solid #c8c8c8; color: #666; word-wrap: break-word;'>").append(escapeHtml(value)).append("</td>");
            }
            html.append("</tr>");
        }
        html.append("</tbody></table></div>");
        return html.toString();
    }
    private String generateReportFormatPreviewHtml(List<KnowledgeItemVo> sampleData, List<FieldInfoVo> fieldInfos, Map<String, String> fieldFormats) {
        if (CollectionUtils.isEmpty(sampleData)) {
            return "<p>暂无数据</p>";
        }
        List<String> selectedFields = fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList());
        Map<String, List<String>> expandedFields = extractExpandedFields(fieldInfos);
        Map<String, CweReferenceVo> cweMap = buildCweMap(sampleData, expandedFields, fieldFormats);
        Map<String, KnowledgeTagVo> tagMap = buildTagMap(sampleData, expandedFields, fieldFormats);
        Map<Long, String> userMap = buildUserMap(sampleData, selectedFields);
        Map<String, String> knowledgeBaseMap = buildKnowledgeBaseMap(sampleData, selectedFields, expandedFields);
        DictService dictService = SpringUtils.getBean(DictService.class);
        StringBuilder html = new StringBuilder("<div style='font-family: \"Microsoft YaHei\", \"SimSun\", \"Helvetica Neue\", Arial, sans-serif; background-color: #fafafa; padding: 16px;'>");
        for (int i = 0; i < sampleData.size(); i++) {
            KnowledgeItemVo item = sampleData.get(i);
            String severity = item.getSeverity();
            if (StringUtils.isBlank(severity) && item.getCvssScore() != null) {
                severity = CvssScoreCalculator.mapSeverityByScore(item.getCvssScore());
            }
            String borderColor = getSeverityColor(severity);
            html.append("<div style='background-color: #ffffff; border: 1px solid #e5e5e5; border-left: 4px solid #404040; border-radius: 4px; padding: 16px; margin-bottom: 16px; box-shadow: 0 1px 3px rgba(0,0,0,0.1);'>");
            String title = StringUtils.isNotBlank(item.getTitle()) ? item.getTitle() : ("条目 " + (i + 1));
            html.append("<h3 style='color: #404040; font-size: 14px; font-weight: 600; margin: 0 0 12px 0; line-height: 1.4;'>")
                .append((i + 1)).append(". ").append(escapeHtml(title)).append("</h3>");
            html.append("<div style='margin-bottom: 12px; display: flex; flex-wrap: wrap; gap: 6px;'>");
            if (StringUtils.isNotBlank(severity)) {
                String severityLabel = dictService.getDictLabel("knowledge_severity", severity);
                String severityColorHex = getSeverityColor(severity);
                html.append("<span style='background-color: ").append(severityColorHex).append("; color: #fff; padding: 4px 8px; border-radius: 4px; font-size: 9px; font-weight: 600; display: inline-block;'>")
                    .append(escapeHtml(severityLabel)).append("</span>");
            }
            if (StringUtils.isNotBlank(item.getLanguage())) {
                String languageLabel = dictService.getDictLabel("knowledge_language", item.getLanguage());
                html.append("<span style='background-color: #f5f5f5; color: #666; padding: 4px 8px; border-radius: 4px; font-size: 9px; border: 1px solid #d9d9d9; display: inline-block;'>")
                    .append(escapeHtml(languageLabel)).append("</span>");
            }
            if (item.getVulnerabilityTypes() != null && !item.getVulnerabilityTypes().isEmpty()) {
                for (String vulnType : item.getVulnerabilityTypes()) {
                    if (cweMap != null && cweMap.containsKey(vulnType)) {
                        CweReferenceVo cwe = cweMap.get(vulnType);
                        String vulnName = StringUtils.isNotBlank(cwe.getNameZh()) ? cwe.getNameZh() : cwe.getNameEn();
                        if (vulnName.length() > 25) {
                            vulnName = vulnName.substring(0, 25) + "...";
                        }
                        html.append("<span style='background-color: #f5f5f5; color: #666; padding: 4px 8px; border-radius: 4px; font-size: 9px; border: 1px solid #d9d9d9; display: inline-block;'>")
                            .append(escapeHtml(vulnName)).append("</span>");
                    }
                }
            }
            html.append("</div>");
            Map<String, String> basicMeta = new LinkedHashMap<>();
            Map<String, String> cvssMeta = new LinkedHashMap<>();
            for (FieldInfoVo fieldInfo : fieldInfos) {
                String key = fieldInfo.getKey();
                if ("title".equals(key) || "summary".equals(key) || "problemDescription".equals(key) || 
                    "fixSolution".equals(key) || "exampleCode".equals(key)) {
                    continue;
                }
                String value = String.valueOf(getFieldValue(item, key, cweMap, tagMap, userMap, knowledgeBaseMap, fieldFormats));
                if (StringUtils.isNotBlank(value) && !"null".equals(value)) {
                    if (key.startsWith("cvss") || "cvssScore".equals(key)) {
                        cvssMeta.put(fieldInfo.getLabel(), value);
                    } else if (!"createTime".equals(key) && !"updateTime".equals(key) && 
                              !"createBy".equals(key) && !"updateBy".equals(key) &&
                              !"createByName".equals(key) && !"updateByName".equals(key) &&
                              !"kid".equals(key) && !"tags".equals(key)) {
                        basicMeta.put(fieldInfo.getLabel(), value);
                    }
                }
            }
            if (!basicMeta.isEmpty() || !cvssMeta.isEmpty()) {
                html.append("<div style='margin-bottom: 12px;'>");
                if (!basicMeta.isEmpty()) {
                    html.append("<div style='margin-bottom: 8px;'>");
                    html.append("<div style='font-size: 10px; color: #666; margin-bottom: 4px; font-weight: 600;'>基本信息</div>");
                    html.append("<table style='width: 100%; border-collapse: collapse; background-color: #f5f5f5; border: 1px solid #f5f5f5; font-size: 9px;'>");
                    for (Map.Entry<String, String> entry : basicMeta.entrySet()) {
                        html.append("<tr>");
                        html.append("<td style='width: 40%; padding: 6px 8px; border: 0.5px solid #e5e5e5; color: #666;'>").append(escapeHtml(entry.getKey())).append(":</td>");
                        html.append("<td style='width: 60%; padding: 6px 8px; border: 0.5px solid #e5e5e5; color: #333;'>").append(escapeHtml(entry.getValue())).append("</td>");
                        html.append("</tr>");
                    }
                    html.append("</table>");
                    html.append("</div>");
                }
                if (!cvssMeta.isEmpty()) {
                    html.append("<div style='margin-bottom: 8px;'>");
                    html.append("<div style='font-size: 10px; color: #666; margin-bottom: 4px; font-weight: 600;'>CVSS评分</div>");
                    html.append("<table style='width: 100%; border-collapse: collapse; background-color: #f5f5f5; border: 1px solid #e5e5e5; font-size: 9px;'>");
                    int colIndex = 0;
                    for (Map.Entry<String, String> entry : cvssMeta.entrySet()) {
                        if (colIndex % 6 == 0) {
                            if (colIndex > 0) html.append("</tr>");
                            html.append("<tr>");
                        }
                        html.append("<td style='padding: 6px 8px; border: 0.5px solid #e5e5e5; color: #666;'>").append(escapeHtml(entry.getKey())).append(":</td>");
                        html.append("<td style='padding: 6px 8px; border: 0.5px solid #e5e5e5; color: #333;'>").append(escapeHtml(entry.getValue())).append("</td>");
                        colIndex += 2;
                    }
                    if (colIndex > 0) html.append("</tr>");
                    html.append("</table>");
                    html.append("</div>");
                }
                html.append("</div>");
            }
            for (FieldInfoVo fieldInfo : fieldInfos) {
                String key = fieldInfo.getKey();
                if ("title".equals(key)) {
                    continue;
                }
                if ("summary".equals(key) || "problemDescription".equals(key) || 
                    "fixSolution".equals(key) || "exampleCode".equals(key)) {
                    String value = String.valueOf(getFieldValue(item, key, cweMap, tagMap, userMap, knowledgeBaseMap, fieldFormats));
                    if (StringUtils.isNotBlank(value) && !"null".equals(value)) {
                        html.append("<div style='margin-bottom: 12px;'>");
                        html.append("<div style='font-weight: 600; color: #333; font-size: 11px; margin-bottom: 4px;'>")
                            .append(escapeHtml(fieldInfo.getLabel())).append("</div>");
                        html.append("<div style='border-top: 1px solid #e5e5e5; margin-bottom: 8px;'></div>");
                        if ("exampleCode".equals(key)) {
                            html.append("<pre style='background-color: #f8f8f8; border: 1px solid #dcdfe6; border-radius: 4px; padding: 8px; margin: 0; font-size: 10px; line-height: 1.5; overflow-x: auto; white-space: pre; font-family: \"Courier New\", monospace;'>")
                                .append(escapeHtml(value)).append("</pre>");
                        } else {
                            html.append("<div style='color: #333; font-size: 10px; line-height: 1.5; white-space: pre-wrap; word-wrap: break-word;'>")
                            .append(escapeHtml(value)).append("</div>");
                        }
                        html.append("</div>");
                    }
                }
            }
            if (item.getTags() != null && !item.getTags().isEmpty()) {
                html.append("<div style='margin-bottom: 12px;'>");
                html.append("<div style='font-weight: 600; color: #333; font-size: 11px; margin-bottom: 4px;'>标签</div>");
                html.append("<div style='border-top: 1px solid #e5e5e5; margin-bottom: 8px;'></div>");
                html.append("<div style='display: flex; flex-wrap: wrap; gap: 6px;'>");
                for (String tagName : item.getTags()) {
                    html.append("<span style='background-color: #f6ffed; color: #52c41a; padding: 4px 8px; border-radius: 4px; font-size: 9px; border: 1px solid #52c41a; display: inline-block;'>")
                        .append(escapeHtml(tagName)).append("</span>");
                }
                html.append("</div>");
                html.append("</div>");
            }
            List<String> footerInfo = new ArrayList<>();
            for (FieldInfoVo fieldInfo : fieldInfos) {
                String key = fieldInfo.getKey();
                if ("createTime".equals(key) && item.getCreateTime() != null) {
                    footerInfo.add("创建时间：" + formatDateTime(item.getCreateTime()));
                } else if ("updateTime".equals(key) && item.getUpdateTime() != null) {
                    footerInfo.add("更新时间：" + formatDateTime(item.getUpdateTime()));
                } else if (("createBy".equals(key) || "createByName".equals(key)) && item.getCreateBy() != null && userMap != null) {
                    String creator = userMap.get(item.getCreateBy());
                    if (StringUtils.isNotBlank(creator)) {
                        footerInfo.add("创建人：" + creator);
                    }
                } else if (("updateBy".equals(key) || "updateByName".equals(key)) && item.getUpdateBy() != null && userMap != null) {
                    String updater = userMap.get(item.getUpdateBy());
                    if (StringUtils.isNotBlank(updater)) {
                        footerInfo.add("更新人：" + updater);
                    }
                } else if ("kid".equals(key) && StringUtils.isNotBlank(item.getKid()) && knowledgeBaseMap != null) {
                    String kbName = knowledgeBaseMap.get(item.getKid());
                    if (StringUtils.isNotBlank(kbName)) {
                        footerInfo.add("知识库：" + kbName);
                    }
                }
            }
            if (!footerInfo.isEmpty()) {
                html.append("<div style='margin-top: 8px; padding-top: 8px; border-top: 1px solid #f5f5f5;'>");
                html.append("<div style='color: #999; font-size: 9px;'>").append(String.join(" | ", footerInfo)).append("</div>");
                html.append("</div>");
            }
            html.append("</div>");
        }
        html.append("</div>");
        return html.toString();
    }

    private Map<String, List<String>> extractExpandedFields(List<FieldInfoVo> fieldInfos) {
        Map<String, List<String>> expandedFields = new HashMap<>();
        if (CollectionUtils.isEmpty(fieldInfos)) {
            return expandedFields;
        }
        for (FieldInfoVo fieldInfo : fieldInfos) {
            if ("expanded".equals(fieldInfo.getType()) && StringUtils.isNotBlank(fieldInfo.getParentField())) {
                expandedFields.computeIfAbsent(fieldInfo.getParentField(), k -> new ArrayList<>())
                    .add(fieldInfo.getKey());
            }
        }
        return expandedFields;
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }

    public String getFieldValueForTemplate(KnowledgeItemVo item, String fieldKey, Map<String, CweReferenceVo> cweMap, Map<String, KnowledgeTagVo> tagMap, Map<Long, String> userMap, Map<String, String> knowledgeBaseMap, Map<String, String> fieldFormats) {
        return getFieldValue(item, fieldKey, cweMap, tagMap, userMap, knowledgeBaseMap, fieldFormats);
    }
    private String getFieldValue(KnowledgeItemVo item, String fieldKey, Map<String, CweReferenceVo> cweMap, Map<String, KnowledgeTagVo> tagMap, Map<Long, String> userMap, Map<String, String> knowledgeBaseMap, Map<String, String> fieldFormats) {
        DictService dictService = SpringUtils.getBean(DictService.class);
        switch (fieldKey) {
            case "title": return StringUtils.isNotBlank(item.getTitle()) ? item.getTitle() : "";
            case "summary": return StringUtils.isNotBlank(item.getSummary()) ? item.getSummary() : "";
            case "problemDescription": return StringUtils.isNotBlank(item.getProblemDescription()) ? item.getProblemDescription() : "";
            case "fixSolution": return StringUtils.isNotBlank(item.getFixSolution()) ? item.getFixSolution() : "";
            case "exampleCode": return StringUtils.isNotBlank(item.getExampleCode()) ? item.getExampleCode() : "";
            case "referenceLink": return StringUtils.isNotBlank(item.getReferenceLink()) ? item.getReferenceLink() : "";
            case "severity": 
                if (StringUtils.isBlank(item.getSeverity())) return "";
                return dictService.getDictLabel("knowledge_severity", item.getSeverity());
            case "vulnerabilityTypes": 
                String vulnFormat = fieldFormats != null ? fieldFormats.get("vulnerabilityTypes") : null;
                return formatVulnerabilityTypes(item.getVulnerabilityTypes(), cweMap, vulnFormat);
            case "vulnerabilityTypeName":
                String vulnType = StringUtils.isNotBlank(item.getVulnerabilityType()) ? item.getVulnerabilityType() : 
                    (item.getVulnerabilityTypes() != null && !item.getVulnerabilityTypes().isEmpty() ? item.getVulnerabilityTypes().get(0) : null);
                if (vulnType != null && cweMap != null) {
                    CweReferenceVo cwe = cweMap.get(vulnType);
                    if (cwe != null && StringUtils.isNotBlank(cwe.getNameZh())) {
                        return cwe.getNameZh();
                    }
                }
                return "";
            case "vulnerabilityTypeNameEn":
                String vulnTypeEn = StringUtils.isNotBlank(item.getVulnerabilityType()) ? item.getVulnerabilityType() : 
                    (item.getVulnerabilityTypes() != null && !item.getVulnerabilityTypes().isEmpty() ? item.getVulnerabilityTypes().get(0) : null);
                if (vulnTypeEn != null && cweMap != null) {
                    CweReferenceVo cwe = cweMap.get(vulnTypeEn);
                    if (cwe != null && StringUtils.isNotBlank(cwe.getNameEn())) {
                        return cwe.getNameEn();
                    }
                }
                return "";
            case "vulnerabilityTypeDescription":
                String vulnTypeDesc = StringUtils.isNotBlank(item.getVulnerabilityType()) ? item.getVulnerabilityType() : 
                    (item.getVulnerabilityTypes() != null && !item.getVulnerabilityTypes().isEmpty() ? item.getVulnerabilityTypes().get(0) : null);
                if (vulnTypeDesc != null && cweMap != null) {
                    CweReferenceVo cwe = cweMap.get(vulnTypeDesc);
                    if (cwe != null && StringUtils.isNotBlank(cwe.getDescriptionZh())) {
                        return cwe.getDescriptionZh();
                    }
                }
                return "";
            case "vulnerabilityTypeDescriptionEn":
                String vulnTypeDescEn = StringUtils.isNotBlank(item.getVulnerabilityType()) ? item.getVulnerabilityType() : 
                    (item.getVulnerabilityTypes() != null && !item.getVulnerabilityTypes().isEmpty() ? item.getVulnerabilityTypes().get(0) : null);
                if (vulnTypeDescEn != null && cweMap != null) {
                    CweReferenceVo cwe = cweMap.get(vulnTypeDescEn);
                    if (cwe != null && StringUtils.isNotBlank(cwe.getDescriptionEn())) {
                        return cwe.getDescriptionEn();
                    }
                }
                return "";
            case "language": 
                if (StringUtils.isBlank(item.getLanguage())) return "";
                return dictService.getDictLabel("knowledge_language", item.getLanguage());
            case "cvssScore": return item.getCvssScore() != null ? item.getCvssScore().toString() : "";
            case "cvssAttackVector": return getCvssComponentLabel(item.getCvssVector(), "AV");
            case "cvssAttackComplexity": return getCvssComponentLabel(item.getCvssVector(), "AC");
            case "cvssPrivilegesRequired": return getCvssComponentLabel(item.getCvssVector(), "PR");
            case "cvssUserInteraction": return getCvssComponentLabel(item.getCvssVector(), "UI");
            case "cvssConfidentialityImpact": return getCvssComponentLabel(item.getCvssVector(), "VC");
            case "cvssIntegrityImpact": return getCvssComponentLabel(item.getCvssVector(), "VI");
            case "cvssAvailabilityImpact": return getCvssComponentLabel(item.getCvssVector(), "VA");
            case "status": 
                if (StringUtils.isBlank(item.getStatus())) return "";
                return dictService.getDictLabel("knowledge_item_status", item.getStatus());
            case "tags": 
                String tagFormat = fieldFormats != null ? fieldFormats.get("tags") : null;
                return formatTags(item.getTags(), tagMap, tagFormat);
            case "fragmentCount": return item.getFragmentCount() != null ? item.getFragmentCount().toString() : "";
            case "createTime": return item.getCreateTime() != null ? formatDateTime(item.getCreateTime()) : "";
            case "updateTime": return item.getUpdateTime() != null ? formatDateTime(item.getUpdateTime()) : "";
            case "createBy":
                if (item.getCreateBy() != null && userMap != null) {
                    return userMap.getOrDefault(item.getCreateBy(), "");
                }
                return "";
            case "kid":
                if (StringUtils.isNotBlank(item.getKid()) && knowledgeBaseMap != null) {
                    return knowledgeBaseMap.getOrDefault(item.getKid(), "");
                }
                return "";
            case "createByName":
                if (item.getCreateBy() != null && userMap != null) {
                    return userMap.getOrDefault(item.getCreateBy(), "");
                }
                return "";
            case "updateBy":
                if (item.getUpdateBy() != null && userMap != null) {
                    return userMap.getOrDefault(item.getUpdateBy(), "");
                }
                return "";
            case "updateByName":
                if (item.getUpdateBy() != null && userMap != null) {
                    return userMap.getOrDefault(item.getUpdateBy(), "");
                }
                return "";
            default: return "";
        }
    }

    private String formatDateTime(java.util.Date date) {
        if (date == null) return "";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    private String formatDataSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }

    private String parseCvssVector(String cvssVector) {
        if (StringUtils.isBlank(cvssVector)) {
            return "";
        }
        Map<String, String> metricLabels = new HashMap<>();
        metricLabels.put("AV:N", "网络");
        metricLabels.put("AV:A", "网络相邻");
        metricLabels.put("AV:L", "本地");
        metricLabels.put("AV:P", "物理");
        metricLabels.put("AC:L", "低");
        metricLabels.put("AC:H", "高");
        metricLabels.put("PR:N", "无");
        metricLabels.put("PR:L", "低");
        metricLabels.put("PR:H", "高");
        metricLabels.put("UI:N", "无");
        metricLabels.put("UI:R", "必需");
        metricLabels.put("UI:A", "活跃");
        metricLabels.put("AT:N", "无");
        metricLabels.put("AT:P", "存在");
        metricLabels.put("VC:H", "高");
        metricLabels.put("VC:L", "低");
        metricLabels.put("VC:N", "无");
        metricLabels.put("VI:H", "高");
        metricLabels.put("VI:L", "低");
        metricLabels.put("VI:N", "无");
        metricLabels.put("VA:H", "高");
        metricLabels.put("VA:L", "低");
        metricLabels.put("VA:N", "无");
        metricLabels.put("SC:H", "高");
        metricLabels.put("SC:L", "低");
        metricLabels.put("SC:N", "无");
        metricLabels.put("SI:H", "高");
        metricLabels.put("SI:L", "低");
        metricLabels.put("SI:N", "无");
        metricLabels.put("SA:H", "高");
        metricLabels.put("SA:L", "低");
        metricLabels.put("SA:N", "无");
        Map<String, String> metricNames = new HashMap<>();
        metricNames.put("AV", "攻击方式");
        metricNames.put("AC", "利用复杂度");
        metricNames.put("PR", "权限需求");
        metricNames.put("UI", "用户交互");
        metricNames.put("AT", "攻击要求");
        metricNames.put("VC", "机密性影响");
        metricNames.put("VI", "完整性影响");
        metricNames.put("VA", "可用性影响");
        metricNames.put("SC", "后续机密性影响");
        metricNames.put("SI", "后续完整性影响");
        metricNames.put("SA", "后续可用性影响");
        List<String> components = new ArrayList<>();
        String[] parts = cvssVector.split("/");
        for (String part : parts) {
            if (part.contains(":")) {
                String[] kv = part.split(":");
                if (kv.length == 2) {
                    String metric = kv[0].trim();
                    String value = kv[1].trim();
                    String metricName = metricNames.getOrDefault(metric, metric);
                    String label = metricLabels.getOrDefault(metric + ":" + value, value);
                    components.add(metricName + "：" + label);
                }
            }
        }
        return String.join("；", components);
    }

    private List<Map<String, Object>> convertToMapList(List<KnowledgeItemVo> items, List<String> selectedFields, Map<String, List<String>> expandedFields, Map<String, String> fieldFormats) {
        if (CollectionUtils.isEmpty(items)) {
            return new ArrayList<>();
        }
        List<FieldInfoVo> fieldInfos = buildFieldInfos(selectedFields, expandedFields);
        Map<String, CweReferenceVo> cweMap = buildCweMap(items, expandedFields, fieldFormats);
        Map<String, KnowledgeTagVo> tagMap = buildTagMap(items, expandedFields, fieldFormats);
        Map<Long, String> userMap = buildUserMap(items, selectedFields);
        Map<String, String> knowledgeBaseMap = buildKnowledgeBaseMap(items, selectedFields, expandedFields);
        List<Map<String, Object>> result = new ArrayList<>();
        for (KnowledgeItemVo item : items) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (FieldInfoVo fieldInfo : fieldInfos) {
                String fieldKey = fieldInfo.getKey();
                Object fieldValue = getFieldValue(item, fieldKey, cweMap, tagMap, userMap, knowledgeBaseMap, fieldFormats);
                map.put(fieldKey, fieldValue);
            }
            result.add(map);
        }
        return result;
    }

    private Map<String, CweReferenceVo> buildCweMap(List<KnowledgeItemVo> items, Map<String, List<String>> expandedFields, Map<String, String> fieldFormats) {
        Map<String, CweReferenceVo> cweMap = new HashMap<>();
        boolean needCwe = false;
        if (expandedFields != null && (expandedFields.containsKey("vulnerabilityType") || expandedFields.containsKey("vulnerabilityTypes"))) {
            needCwe = true;
        }
        if (fieldFormats != null && (fieldFormats.containsKey("vulnerabilityTypes") || fieldFormats.containsKey("vulnerabilityType"))) {
            String format = fieldFormats.get("vulnerabilityTypes");
            if (format == null) format = fieldFormats.get("vulnerabilityType");
            if (format != null) {
                needCwe = true;
            }
        }
        if (!needCwe) {
            return cweMap;
        }
        Set<String> cweIds = new HashSet<>();
        for (KnowledgeItemVo item : items) {
            if (item.getVulnerabilityTypes() != null) {
                cweIds.addAll(item.getVulnerabilityTypes());
            }
            if (StringUtils.isNotBlank(item.getVulnerabilityType())) {
                cweIds.add(item.getVulnerabilityType());
            }
        }
        if (!cweIds.isEmpty()) {
            List<CweReferenceVo> cweList = cweReferenceMapper.selectVoList(
                Wrappers.<CweReference>lambdaQuery().in(CweReference::getCweId, cweIds)
            );
            for (CweReferenceVo cwe : cweList) {
                cweMap.put(cwe.getCweId(), cwe);
            }
        }
        return cweMap;
    }

    private Map<String, KnowledgeTagVo> buildTagMap(List<KnowledgeItemVo> items, Map<String, List<String>> expandedFields, Map<String, String> fieldFormats) {
        Map<String, KnowledgeTagVo> tagMap = new HashMap<>();
        boolean needTag = false;
        if (expandedFields != null && expandedFields.containsKey("tags")) {
            needTag = true;
        }
        if (fieldFormats != null && fieldFormats.containsKey("tags")) {
            String format = fieldFormats.get("tags");
            if (format != null && "full".equals(format)) {
                needTag = true;
            }
        }
        if (!needTag) {
            return tagMap;
        }
        Set<String> tagNames = new HashSet<>();
        for (KnowledgeItemVo item : items) {
            if (item.getTags() != null) {
                tagNames.addAll(item.getTags());
            }
        }
        if (!tagNames.isEmpty()) {
            List<KnowledgeTagVo> tagList = knowledgeTagMapper.selectVoList(
                Wrappers.<KnowledgeTag>lambdaQuery().in(KnowledgeTag::getTagName, tagNames)
            );
            for (KnowledgeTagVo tag : tagList) {
                tagMap.put(tag.getTagName(), tag);
            }
        }
        return tagMap;
    }

    private Map<Long, String> buildUserMap(List<KnowledgeItemVo> items, List<String> selectedFields) {
        Map<Long, String> userMap = new HashMap<>();
        if (selectedFields == null) {
            return userMap;
        }
        boolean needUser = selectedFields.contains("createBy") || selectedFields.contains("updateBy");
        if (!needUser) {
            return userMap;
        }
        Set<Long> userIds = new HashSet<>();
        for (KnowledgeItemVo item : items) {
            if (item.getCreateBy() != null) {
                userIds.add(item.getCreateBy());
            }
            if (item.getUpdateBy() != null) {
                userIds.add(item.getUpdateBy());
            }
        }
        if (!userIds.isEmpty()) {
            for (Long userId : userIds) {
                SysUserVo user = sysUserService.selectUserById(userId);
                if (user != null && StringUtils.isNotBlank(user.getUserName())) {
                    userMap.put(userId, user.getUserName());
                }
            }
        }
        return userMap;
    }


    private Map<String, String> buildKnowledgeBaseMap(List<KnowledgeItemVo> items, List<String> selectedFields, Map<String, List<String>> expandedFields) {
        Map<String, String> knowledgeBaseMap = new HashMap<>();
        //如果选择了kid字段，需要构建映射
        boolean needKnowledgeBase = (selectedFields != null && selectedFields.contains("kid")) 
            || (expandedFields != null && expandedFields.containsKey("kid"));
        if (!needKnowledgeBase) {
            return knowledgeBaseMap;
        }
        Set<String> kids = new HashSet<>();
        for (KnowledgeItemVo item : items) {
            if (StringUtils.isNotBlank(item.getKid())) {
                kids.add(item.getKid());
            }
        }
        if (!kids.isEmpty()) {
            for (String kid : kids) {
                KnowledgeInfoVo info = knowledgeInfoMapper.selectVoByKid(kid);
                if (info != null && StringUtils.isNotBlank(info.getKname())) {
                    knowledgeBaseMap.put(kid, info.getKname());
                }
            }
        }
        return knowledgeBaseMap;
    }

    private String formatVulnerabilityTypes(List<String> vulnerabilityTypes, Map<String, CweReferenceVo> cweMap, String format) {
        if (CollectionUtils.isEmpty(vulnerabilityTypes)) {
            return "";
        }
        List<String> formatted = new ArrayList<>();
        for (String cweId : vulnerabilityTypes) {
            CweReferenceVo cwe = cweMap != null ? cweMap.get(cweId) : null;
            if (format == null || "name_only".equals(format)) {
                if (cwe != null && StringUtils.isNotBlank(cwe.getNameZh())) {
                    formatted.add(cwe.getNameZh());
                } else {
                    formatted.add(cweId);
                }
            } else if ("id_name".equals(format)) {
                if (cwe != null && StringUtils.isNotBlank(cwe.getNameZh())) {
                    formatted.add(cweId + ": " + cwe.getNameZh());
                } else {
                    formatted.add(cweId);
                }
            } else if ("full".equals(format)) {
                StringBuilder sb = new StringBuilder();
                if (cwe != null && StringUtils.isNotBlank(cwe.getNameZh())) {
                    sb.append(cwe.getNameZh());
                    if (StringUtils.isNotBlank(cwe.getDescriptionZh())) {
                        String desc = cwe.getDescriptionZh();
                        if (desc.length() > 50) {
                            desc = desc.substring(0, 50) + "...";
                        }
                        sb.append("（").append(desc).append("）");
                    }
                } else {
                    sb.append(cweId);
                }
                formatted.add(sb.toString());
            } else {
                if (cwe != null && StringUtils.isNotBlank(cwe.getNameZh())) {
                    formatted.add(cwe.getNameZh());
                } else {
                    formatted.add(cweId);
                }
            }
        }
        return String.join(", ", formatted);
    }

    private String formatTags(List<String> tags, Map<String, KnowledgeTagVo> tagMap, String format) {
        if (CollectionUtils.isEmpty(tags)) {
            return "";
        }
        List<String> formatted = new ArrayList<>();
        for (String tagName : tags) {
            KnowledgeTagVo tag = tagMap != null ? tagMap.get(tagName) : null;
            if (format == null || "name_only".equals(format)) {
                formatted.add(tagName);
            } else if ("full".equals(format)) {
                StringBuilder sb = new StringBuilder(tagName);
                if (tag != null) {
                    if (StringUtils.isNotBlank(tag.getTagCategory())) {
                        sb.append(" [").append(tag.getTagCategory()).append("]");
                    }
                    if (StringUtils.isNotBlank(tag.getDescription())) {
                        String desc = tag.getDescription();
                        if (desc.length() > 30) {
                            desc = desc.substring(0, 30) + "...";
                        }
                        sb.append("（").append(desc).append("）");
                    }
                }
                formatted.add(sb.toString());
            } else {
                formatted.add(tagName);
            }
        }
        return String.join(", ", formatted);
    }

    private String generateDefaultFileName(String format) {
        String extension = "excel".equals(format) ? "xlsx" : "pdf";
        return "知识条目导出_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + "." + extension;
    }

    private void exportToExcel(List<KnowledgeItemVo> data, ExportRequestBo request, String fileName, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
        List<Map<String, Object>> exportData = convertToMapList(data, request.getSelectedFields(), request.getExpandedFields(), request.getFieldFormats());
        List<FieldInfoVo> fieldInfos = buildFieldInfos(request.getSelectedFields(), request.getExpandedFields());
        ExcelOptionsBo excelOptions = request.getExcelOptions();
        boolean freezeHeader = excelOptions != null && Boolean.TRUE.equals(excelOptions.getFreezeHeader());
        boolean includeFilter = excelOptions != null && Boolean.TRUE.equals(excelOptions.getIncludeFilter());
        boolean conditionalFormatting = excelOptions != null && Boolean.TRUE.equals(excelOptions.getConditionalFormatting());
        Map<String, Integer> columnWidths = request.getColumnWidths() != null ? request.getColumnWidths() : new HashMap<>();
        OutputStream outputStream = response.getOutputStream();
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("知识条目");
            int rowIndex = 0;
            int columnCount = fieldInfos.size();
            XSSFCellStyle headerStyle = createHeaderStyle(workbook);
            XSSFCellStyle oddRowStyle = createOddRowStyle(workbook);
            XSSFCellStyle evenRowStyle = createEvenRowStyle(workbook);
            Row headerRow = sheet.createRow(rowIndex++);
            for (int i = 0; i < fieldInfos.size(); i++) {
                FieldInfoVo fieldInfo = fieldInfos.get(i);
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(fieldInfo.getLabel());
                cell.setCellStyle(headerStyle);
                int excelColumnWidth = convertPixelToExcelWidth(columnWidths.getOrDefault(fieldInfo.getKey(), calculateDefaultColumnWidth(fieldInfo, exportData)));
                sheet.setColumnWidth(i, excelColumnWidth);
            }
            for (int dataIndex = 0; dataIndex < exportData.size(); dataIndex++) {
                Map<String, Object> item = exportData.get(dataIndex);
                Row row = sheet.createRow(rowIndex++);
                boolean isOddRow = (dataIndex + 1) % 2 == 1;
                XSSFCellStyle baseStyle = isOddRow ? oddRowStyle : evenRowStyle;
                for (int i = 0; i < fieldInfos.size(); i++) {
                    FieldInfoVo fieldInfo = fieldInfos.get(i);
                    org.apache.poi.ss.usermodel.Cell cell = row.createCell(i);
                    Object value = item.getOrDefault(fieldInfo.getKey(), "");
                    setCellValue(cell, value);
                    XSSFCellStyle cellStyle = workbook.createCellStyle();
                    cellStyle.cloneStyleFrom(baseStyle);
                    if (conditionalFormatting && ("severity".equals(fieldInfo.getKey()) || "severityLabel".equals(fieldInfo.getKey()))) {
                        applySeverityConditionalFormatting(cell, cellStyle, workbook, String.valueOf(value));
                    }
                    cell.setCellStyle(cellStyle);
                }
            }
            if (freezeHeader) {
                sheet.createFreezePane(0, 1);
            }
            if (includeFilter && columnCount > 0) {
                CellRangeAddress filterRange = new CellRangeAddress(0, 0, 0, columnCount - 1);
                sheet.setAutoFilter(filterRange);
            }
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e) {
            log.error("导出Excel异常", e);
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            throw new RuntimeException("导出Excel异常: " + e.getMessage(), e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    log.warn("关闭Workbook异常", e);
                }
            }
        }
    }

    //创建表头样式：深色背景+白色文字
    private XSSFCellStyle createHeaderStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(new XSSFColor(new Color(0, 51, 102), null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setTopBorderColor(new XSSFColor(Color.BLACK, null));
        style.setBottomBorderColor(new XSSFColor(Color.BLACK, null));
        style.setLeftBorderColor(new XSSFColor(Color.BLACK, null));
        style.setRightBorderColor(new XSSFColor(Color.BLACK, null));
        XSSFFont font = workbook.createFont();
        font.setColor(new XSSFColor(Color.WHITE, null));
        font.setBold(true);
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        return style;
    }
    //创建偶数行样式：白色背景
    private XSSFCellStyle createEvenRowStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(new XSSFColor(Color.WHITE, null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setTopBorderColor(new XSSFColor(Color.BLACK, null));
        style.setBottomBorderColor(new XSSFColor(Color.BLACK, null));
        style.setLeftBorderColor(new XSSFColor(Color.BLACK, null));
        style.setRightBorderColor(new XSSFColor(Color.BLACK, null));
        XSSFFont font = workbook.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        return style;
    }
    //创建奇数行样式：浅灰色背景
    private XSSFCellStyle createOddRowStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(new XSSFColor(new Color(242, 242, 242), null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setTopBorderColor(new XSSFColor(Color.BLACK, null));
        style.setBottomBorderColor(new XSSFColor(Color.BLACK, null));
        style.setLeftBorderColor(new XSSFColor(Color.BLACK, null));
        style.setRightBorderColor(new XSSFColor(Color.BLACK, null));
        XSSFFont font = workbook.createFont();
        font.setFontName("微软雅黑");
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        return style;
    }
    //设置单元格值（处理不同类型）
    private void setCellValue(org.apache.poi.ss.usermodel.Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
            return;
        }
        if (value instanceof Number) {
            if (value instanceof Double || value instanceof Float) {
                cell.setCellValue(((Number) value).doubleValue());
            } else if (value instanceof Long || value instanceof Integer || value instanceof Short || value instanceof Byte) {
                cell.setCellValue(((Number) value).longValue());
            } else {
                cell.setCellValue(value.toString());
            }
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof java.util.Date) {
            cell.setCellValue((java.util.Date) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }
    //应用风险等级条件格式
    private void applySeverityConditionalFormatting(org.apache.poi.ss.usermodel.Cell cell, XSSFCellStyle cellStyle, XSSFWorkbook workbook, String cellValue) {
        if (StringUtils.isBlank(cellValue)) {
            return;
        }
        XSSFFont font = workbook.createFont();
        XSSFFont baseFont = (XSSFFont) cellStyle.getFont();
        if (baseFont != null) {
            font.setFontName(baseFont.getFontName());
            font.setFontHeightInPoints(baseFont.getFontHeightInPoints());
        } else {
            font.setFontName("微软雅黑");
            font.setFontHeightInPoints((short) 11);
        }
        String lowerValue = cellValue.toLowerCase();
        if (lowerValue.contains("高") || lowerValue.contains("high")) {
            cellStyle.setFillForegroundColor(new XSSFColor(new Color(255, 230, 230), null));
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            font.setColor(new XSSFColor(new Color(204, 0, 0), null));
            cellStyle.setFont(font);
        } else if (lowerValue.contains("中") || lowerValue.contains("medium")) {
            cellStyle.setFillForegroundColor(new XSSFColor(new Color(255, 244, 230), null));
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            font.setColor(new XSSFColor(new Color(230, 115, 0), null));
            cellStyle.setFont(font);
        } else if (lowerValue.contains("低") || lowerValue.contains("low")) {
            cellStyle.setFillForegroundColor(new XSSFColor(new Color(230, 247, 230), null));
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            font.setColor(new XSSFColor(new Color(0, 102, 0), null));
            cellStyle.setFont(font);
        }
    }
    //将像素宽度转换为Excel列宽单位
    //Excel列宽单位：1个单位 = 1/256字符宽度，默认字符宽度约7像素
    //转换公式：excelWidth = (pixelWidth / 7) * 256
    private int convertPixelToExcelWidth(int pixelWidth) {
        if (pixelWidth <= 0) {
            return 2560;
        }
        double excelWidth = (pixelWidth / 7.0) * 256;
        int result = (int) Math.round(excelWidth);
        return Math.max(256, Math.min(result, 65535));
    }
    //计算默认列宽（与前端逻辑保持一致）
    private int calculateDefaultColumnWidth(FieldInfoVo fieldInfo, List<Map<String, Object>> exportData) {
        String fieldKey = fieldInfo.getKey();
        String fieldLabel = fieldInfo.getLabel();
        int headerWidth = estimateTextWidth(fieldLabel, 13);
        int headerPadding = 60;
        int baseWidth = Math.max(headerWidth + headerPadding, 100);
        if (exportData != null && !exportData.isEmpty()) {
            int maxContentWidth = headerWidth;
            for (Map<String, Object> row : exportData) {
                Object value = row.get(fieldKey);
                if (value != null) {
                    String contentText = String.valueOf(value);
                    int contentWidth = estimateTextWidth(contentText, 12);
                    if (contentWidth > maxContentWidth) {
                        maxContentWidth = contentWidth;
                    }
                }
            }
            if (maxContentWidth > headerWidth) {
                int contentPadding = 60;
                int contentBasedWidth = maxContentWidth + contentPadding;
                baseWidth = Math.max(baseWidth, contentBasedWidth);
            }
        }
        Map<String, Integer> specialMinWidths = new HashMap<>();
        specialMinWidths.put("title", 180);
        specialMinWidths.put("summary", 300);
        specialMinWidths.put("itemUuid", 280);
        specialMinWidths.put("problemDescription", 400);
        specialMinWidths.put("fixSolution", 400);
        specialMinWidths.put("exampleCode", 400);
        specialMinWidths.put("referenceLink", 300);
        if (specialMinWidths.containsKey(fieldKey)) {
            baseWidth = Math.max(baseWidth, specialMinWidths.get(fieldKey));
        }
        Map<String, Integer> maxWidths = new HashMap<>();
        maxWidths.put("title", 400);
        maxWidths.put("summary", 600);
        maxWidths.put("problemDescription", 800);
        maxWidths.put("fixSolution", 800);
        maxWidths.put("exampleCode", 800);
        maxWidths.put("referenceLink", 500);
        int maxWidth = maxWidths.getOrDefault(fieldKey, 600);
        return Math.max(100, Math.min(baseWidth, maxWidth));
    }
    //估算文本宽度（像素）
    private int estimateTextWidth(String text, int fontSize) {
        if (StringUtils.isBlank(text)) {
            return 0;
        }
        int width = 0;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch >= 0x4e00 && ch <= 0x9fa5 || (ch >= 0x3000 && ch <= 0x303f) || (ch >= 0xff00 && ch <= 0xffef)) {
                width += fontSize * 1.2;
            } else {
                width += fontSize * 0.6;
            }
        }
        return width;
    }
    //流式PDF生成：使用Velocity模板+OpenHTML to PDF方案
    private void exportToPdfStreaming(ExportRequestBo request, String fileName, HttpServletResponse response, List<String> warnings) throws IOException {
        response.setContentType("application/pdf");
        String originalFileName = fileName;
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFileName);
        List<FieldInfoVo> fieldInfos = buildFieldInfos(request.getSelectedFields(), request.getExpandedFields());
        if (CollectionUtils.isEmpty(fieldInfos)) {
            throw new ServiceException("请至少选择一个导出字段");
        }
        PdfOptionsBo pdfOptions = request.getPdfOptions();
        boolean useReportFormat;
        if (pdfOptions != null && StringUtils.isNotBlank(pdfOptions.getFormatType())) {
            useReportFormat = "report".equals(pdfOptions.getFormatType());
        } else {
            useReportFormat = shouldUseReportFormat(fieldInfos);
        }
        boolean includeHeaderFooter = pdfOptions == null || Boolean.TRUE.equals(pdfOptions.getIncludeHeaderFooter());
        boolean includeTOC = pdfOptions != null && Boolean.TRUE.equals(pdfOptions.getIncludeTOC());
        boolean codeHighlight = pdfOptions == null || Boolean.TRUE.equals(pdfOptions.getCodeHighlight());
        log.info("PDF流式导出(Velocity+OpenHTML) - 初始化参数: useReportFormat={}, includeHeaderFooter={}, includeTOC={}, codeHighlight={}", useReportFormat, includeHeaderFooter, includeTOC, codeHighlight);
        OutputStream outputStream = response.getOutputStream();
        log.info("PDF流式导出(Velocity+OpenHTML) - 开始获取导出数据");
        List<KnowledgeItemVo> allData = getExportData(request, null);
        if (CollectionUtils.isEmpty(allData)) {
            throw new ServiceException("没有可导出的数据");
        }
        log.info("PDF流式导出(Velocity+OpenHTML) - 获取到{}条数据", allData.size());
        List<String> selectedFields = fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList());
        Map<String, List<String>> expandedFields = extractExpandedFields(fieldInfos);
        Map<String, CweReferenceVo> cweMap = buildCweMap(allData, expandedFields, request.getFieldFormats());
        Map<String, KnowledgeTagVo> tagMap = buildTagMap(allData, expandedFields, request.getFieldFormats());
        Map<Long, String> userMap = buildUserMap(allData, selectedFields);
        Map<String, String> knowledgeBaseMap = buildKnowledgeBaseMap(allData, selectedFields, expandedFields);
        DictService dictService = SpringUtils.getBean(DictService.class);
        HighlightJsService highlightJsService = SpringUtils.getBean(HighlightJsService.class);
        log.info("PDF流式导出(Velocity+OpenHTML) - 数据准备完成，开始初始化Velocity");
        initializeVelocity();
        PdfTemplateUtils utils = new PdfTemplateUtils();
        utils.setServiceInstance(this);
        VelocityContext context = new VelocityContext();
        context.put("items", allData);
        context.put("fieldInfos", fieldInfos);
        context.put("cweMap", cweMap);
        context.put("tagMap", tagMap);
        context.put("userMap", userMap);
        context.put("knowledgeBaseMap", knowledgeBaseMap);
        context.put("fieldFormats", request.getFieldFormats());
        context.put("dictService", dictService);
        context.put("utils", utils);
        context.put("highlightJsService", highlightJsService);
        context.put("includeTOC", includeTOC);
        context.put("codeHighlight", codeHighlight);
        String headerText = includeHeaderFooter ? originalFileName : "";
        context.put("headerText", headerText);
        context.put("includeHeaderFooter", includeHeaderFooter);
        List<Map<String, Object>> tocItems = new ArrayList<>();
        if (includeTOC && useReportFormat) {
            for (int i = 0; i < allData.size(); i++) {
                KnowledgeItemVo item = allData.get(i);
                Map<String, Object> tocItem = new HashMap<>();
                tocItem.put("title", item.getTitle() != null ? item.getTitle() : ("条目 " + (i + 1)));
                tocItem.put("index", i + 1);
                tocItem.put("anchor", "item-" + (i + 1));
                tocItems.add(tocItem);
            }
        }
        context.put("tocItems", tocItems);
        String templatePath = useReportFormat ? "templates/pdf/report-format.vm" : "templates/pdf/table-format.vm";
        log.info("PDF流式导出(Velocity+OpenHTML) - 开始渲染Velocity模板: {}", templatePath);
        String html = renderVelocityTemplate(templatePath, context);
        log.info("PDF流式导出(Velocity+OpenHTML) - Velocity模板渲染完成，HTML长度: {}", html.length());
        log.info("PDF流式导出(Velocity+OpenHTML) - 开始使用OpenHTML to PDF转换");
        convertHtmlToPdfWithOpenHtml(html, outputStream, includeHeaderFooter, headerText);
        log.info("PDF流式导出(Velocity+OpenHTML) - PDF生成完成，共处理{}条数据", allData.size());
    }
    //旧版iText原生API方案（保留作为备用）
    @SuppressWarnings("unused")
    private void exportToPdfStreamingOld(ExportRequestBo request, String fileName, HttpServletResponse response, List<String> warnings) throws IOException {
        response.setContentType("application/pdf");
        response.setCharacterEncoding("utf-8");
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
        List<FieldInfoVo> fieldInfos = buildFieldInfos(request.getSelectedFields(), request.getExpandedFields());
        if (CollectionUtils.isEmpty(fieldInfos)) {
            throw new ServiceException("请至少选择一个导出字段");
        }
        PdfOptionsBo pdfOptions = request.getPdfOptions();
        boolean useReportFormat;
        if (pdfOptions != null && StringUtils.isNotBlank(pdfOptions.getFormatType())) {
            useReportFormat = "report".equals(pdfOptions.getFormatType());
        } else {
            useReportFormat = shouldUseReportFormat(fieldInfos);
        }
        boolean includeTOC = pdfOptions != null && Boolean.TRUE.equals(pdfOptions.getIncludeTOC());
        boolean includeHeaderFooter = pdfOptions == null || Boolean.TRUE.equals(pdfOptions.getIncludeHeaderFooter());
        boolean codeHighlight = pdfOptions != null && Boolean.TRUE.equals(pdfOptions.getCodeHighlight());
        boolean needTOC = includeTOC && useReportFormat;
        log.info("PDF流式导出 - 初始化参数: needTOC={}, useReportFormat={}, codeHighlight={}", needTOC, useReportFormat, codeHighlight);
        OutputStream finalOutputStream = response.getOutputStream();
        ByteArrayOutputStream tempOutputStream = needTOC ? new ByteArrayOutputStream() : null;
        OutputStream outputStream = needTOC ? tempOutputStream : finalOutputStream;
        log.info("PDF流式导出 - 输出流初始化完成: needTOC={}, tempOutputStream={}", needTOC, tempOutputStream != null);
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        log.info("PDF流式导出 - PdfDocument创建完成");
        pdfDoc.setTagged();
        log.info("PDF流式导出 - setTagged()设置完成");
        if (needTOC) {
            pdfDoc.initializeOutlines();
            log.info("PDF流式导出 - initializeOutlines()调用完成");
        }
        Document document = new Document(pdfDoc, PageSize.A4, !needTOC);
        log.info("PDF流式导出 - Document创建完成: immediateFlush={}", !needTOC);
        document.setMargins(36, 36, 36, 36);
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont fontChinese = null;
        PdfFont fontChineseBold = null;
        String[] fontPaths = {
            "/usr/share/fonts/opentype/noto/NotoSansCJK-Regular.ttc",
            "/usr/share/fonts/opentype/noto/NotoSerifCJK-Regular.ttc",
            "/usr/share/fonts/opentype/noto/NotoSansCJK-Bold.ttc",
            "/usr/share/fonts/opentype/noto/NotoSerifCJK-Bold.ttc",
            "/usr/share/fonts/wenquanyi/wqy-zenhei/wqy-zenhei.ttc",
            "/usr/share/fonts/truetype/wqy/wqy-zenhei.ttc",
            "/usr/share/fonts/truetype/msttcore/SimSun.ttf",
            "/usr/share/fonts/truetype/msttcore/SimHei.ttf",
            "C:/Windows/Fonts/simsun.ttc",
            "C:/Windows/Fonts/simhei.ttf",
            "C:/Windows/Fonts/msyh.ttf",
            System.getProperty("java.home") + "/lib/fonts/SimSun.ttf"
        };
        boolean fontLoaded = false;
        String loadedFontPath = null;
        log.info("开始查找中文字体文件，共{}个路径", fontPaths.length);
        for (String fontPath : fontPaths) {
            try {
                java.io.File fontFile = new java.io.File(fontPath);
                boolean exists = fontFile.exists();
                boolean isFile = fontFile.isFile();
                log.debug("检查字体文件: {}, 存在: {}, 是文件: {}", fontPath, exists, isFile);
                if (exists && isFile) {
                    if (fontPath.toLowerCase().endsWith(".ttc")) {
                        FontProgram fontProgram = FontProgramFactory.createFont(fontPath, 0, false);
                        fontChinese = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                        loadedFontPath = fontPath;
                        fontLoaded = true;
                        log.info("成功从TTC文件加载中文字体: {}", fontPath);
                        break;
                    } else {
                        fontChinese = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                        loadedFontPath = fontPath;
                        fontLoaded = true;
                        log.info("成功从TTF文件加载中文字体: {}", fontPath);
                        break;
                    }
                } else {
                    log.debug("字体文件不存在或不是文件: {}", fontPath);
                }
            } catch (Exception e) {
                log.warn("尝试从文件加载字体失败: {}, 错误: {}", fontPath, e.getMessage());
            }
        }
        if (!fontLoaded) {
            log.warn("所有字体文件路径都未找到字体文件，将尝试其他方式加载");
        }
        if (fontLoaded && loadedFontPath != null) {
            String boldFontPath = loadedFontPath.replace("Regular", "Bold");
            try {
                java.io.File boldFontFile = new java.io.File(boldFontPath);
                if (boldFontFile.exists() && boldFontFile.isFile()) {
                    if (boldFontPath.toLowerCase().endsWith(".ttc")) {
                        FontProgram boldFontProgram = FontProgramFactory.createFont(boldFontPath, 0, false);
                        fontChineseBold = PdfFontFactory.createFont(boldFontProgram, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                    } else {
                        fontChineseBold = PdfFontFactory.createFont(boldFontPath, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                    }
                    log.info("成功加载中文字体粗体: {}", boldFontPath);
                } else {
                    fontChineseBold = fontChinese;
                    log.info("未找到对应的粗体字体，使用常规字体作为粗体");
                }
            } catch (Exception e) {
                log.warn("加载粗体字体失败，使用常规字体: {}", e.getMessage());
                fontChineseBold = fontChinese;
            }
        }
        if (!fontLoaded) {
            try {
                InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/simsun.ttf");
                if (fontStream == null) {
                    fontStream = getClass().getClassLoader().getResourceAsStream("fonts/SimSun.ttf");
                }
                if (fontStream != null) {
                    byte[] fontBytes = fontStream.readAllBytes();
                    fontStream.close();
                    fontChinese = PdfFontFactory.createFont(fontBytes, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                    fontChineseBold = fontChinese;
                    fontLoaded = true;
                    log.info("从resources加载中文字体成功");
                }
            } catch (Exception e) {
                log.debug("从resources加载字体失败");
            }
        }
        if (!fontLoaded) {
            log.warn("尝试使用字体名称加载（可能无法正确渲染中文）");
            try {
                fontChinese = PdfFontFactory.createFont("STSong-Light", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                fontChineseBold = PdfFontFactory.createFont("STSongStd-Light", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                fontLoaded = true;
                log.warn("使用字体名称加载成功（但可能无法正确渲染中文）: STSong-Light");
            } catch (Exception e1) {
                try {
                    fontChinese = PdfFontFactory.createFont("SimSun", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                    fontChineseBold = PdfFontFactory.createFont("SimHei", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                    fontLoaded = true;
                } catch (Exception e2) {
                    try {
                        fontChinese = PdfFontFactory.createFont("SimHei", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                        fontChineseBold = fontChinese;
                        fontLoaded = true;
                    } catch (Exception e3) {
                        try {
                            fontChinese = PdfFontFactory.createFont("Microsoft YaHei", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                            fontChineseBold = fontChinese;
                            fontLoaded = true;
                        } catch (Exception e4) {
                            log.error("无法加载中文字体，将使用Helvetica（中文可能无法正确显示）", e4);
                            fontChinese = font;
                            fontChineseBold = fontBold;
                        }
                    }
                }
            }
        }
        if (fontChinese == null || fontChineseBold == null) {
            log.error("中文字体加载失败，PDF中的中文内容可能无法正确显示");
            fontChinese = font;
            fontChineseBold = fontBold;
        } else {
            String fontName = fontChinese.getFontProgram().getFontNames().getFontName();
            String boldFontName = fontChineseBold.getFontProgram().getFontNames().getFontName();
            log.info("中文字体加载成功: regular={}, bold={}", fontName, boldFontName);
            try {
                String testText = "测试";
                float width = fontChinese.getWidth(testText, 12);
                if (width <= 0) {
                    log.warn("字体可能不支持中文字符，测试文本宽度为0");
                } else {
                    log.info("字体中文字符测试通过，测试文本宽度: {}", width);
                }
            } catch (Exception e) {
                log.warn("字体中文字符测试失败", e);
            }
        }
        if (includeHeaderFooter) {
            HeaderFooterHandler handler = new HeaderFooterHandler(font, fontChinese, fileName);
            pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, handler);
        }
        long totalCount = getExportDataCount(request);
        log.info("PDF流式导出开始，总数据量: {}", totalCount);
        if (needTOC && totalCount > 1000) {
            log.info("数据量过大({}条)，跳过目录生成", totalCount);
            warnings.add("数据量过大(" + totalCount + "条)，已跳过目录生成");
            needTOC = false;
        }
        Map<String, Integer> pageNumberMap = new HashMap<>();
        if (needTOC && totalCount > 0 && totalCount <= 1000) {
            log.info("将使用两遍渲染策略：先收集页码，再生成目录");
        }
        final int BATCH_SIZE = 100;
        int processedCount = 0;
        int itemIndex = 0;
        KnowledgeItemBo bo = buildExportQueryBo(request);
        Table sharedTable = null;
        if (!useReportFormat) {
            sharedTable = new Table(fieldInfos.size()).useAllAvailableWidth();
            sharedTable.setMarginBottom(20);
            sharedTable.setKeepTogether(false);
            for (FieldInfoVo fieldInfo : fieldInfos) {
                PdfFont headerFont = fontChinese != null && fontChinese != font ? fontChinese : fontBold;
                com.itextpdf.layout.element.Cell headerCell = new com.itextpdf.layout.element.Cell().add(new Paragraph(fieldInfo.getLabel())
                    .setFont(headerFont)
                    .setFontSize(10));
                headerCell.setBackgroundColor(new DeviceRgb(245, 245, 245));
                headerCell.setBorder(new SolidBorder(new DeviceRgb(200, 200, 200), 0.5f));
                headerCell.setTextAlignment(TextAlignment.CENTER);
                headerCell.setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE);
                headerCell.setPadding(5);
                sharedTable.addHeaderCell(headerCell);
            }
            document.add(sharedTable);
        }
        if ("selected".equals(request.getExportRange()) && CollectionUtils.isNotEmpty(request.getItemUuids())) {
            List<String> itemUuids = request.getItemUuids();
            int totalBatches = (itemUuids.size() + BATCH_SIZE - 1) / BATCH_SIZE;
            for (int batchIndex = 0; batchIndex < totalBatches; batchIndex++) {
                int start = batchIndex * BATCH_SIZE;
                int end = Math.min(start + BATCH_SIZE, itemUuids.size());
                List<String> batchUuids = itemUuids.subList(start, end);
                bo.setItemUuids(batchUuids);
                List<KnowledgeItemVo> batchData = queryList(bo);
                if (CollectionUtils.isNotEmpty(batchData)) {
                    if (useReportFormat) {
                        generateReportFormatBatch(document, pdfDoc, batchData, fieldInfos, request.getFieldFormats(), font, fontChineseBold, fontChinese, codeHighlight, itemIndex, pageNumberMap);
                    } else {
                        generateTableFormatBatch(sharedTable, batchData, fieldInfos, request.getFieldFormats(), font, fontBold, fontChinese);
                    }
                    itemIndex += batchData.size();
                    processedCount += batchData.size();
                    log.info("PDF流式导出进度: {}/{}", processedCount, totalCount);
                    batchData = null;
                    System.gc();
                }
            }
        } else if ("currentPage".equals(request.getExportRange()) && request.getPageNum() != null && request.getPageSize() != null) {
            PageQuery pageQuery = new PageQuery(request.getPageSize(), request.getPageNum());
            TableDataInfo<KnowledgeItemVo> pageData = queryPageList(bo, pageQuery);
            List<KnowledgeItemVo> batchData = pageData.getRows();
            if (CollectionUtils.isNotEmpty(batchData)) {
                if (useReportFormat) {
                    generateReportFormatBatch(document, pdfDoc, batchData, fieldInfos, request.getFieldFormats(), font, fontChineseBold, fontChinese, codeHighlight, itemIndex, pageNumberMap);
                } else {
                    generateTableFormatBatch(sharedTable, batchData, fieldInfos, request.getFieldFormats(), font, fontBold, fontChinese);
                }
                processedCount += batchData.size();
                log.info("PDF流式导出进度: {}/{}", processedCount, totalCount);
            }
        } else {
            int pageNum = 1;
            int pageSize = BATCH_SIZE;
            while (true) {
                PageQuery pageQuery = new PageQuery(pageSize, pageNum);
                TableDataInfo<KnowledgeItemVo> pageData = queryPageList(bo, pageQuery);
                List<KnowledgeItemVo> batchData = pageData.getRows();
                if (CollectionUtils.isEmpty(batchData)) {
                    break;
                }
                if (useReportFormat) {
                    generateReportFormatBatch(document, pdfDoc, batchData, fieldInfos, request.getFieldFormats(), font, fontChineseBold, fontChinese, codeHighlight, itemIndex, pageNumberMap);
                } else {
                    generateTableFormatBatch(sharedTable, batchData, fieldInfos, request.getFieldFormats(), font, fontBold, fontChinese);
                }
                itemIndex += batchData.size();
                processedCount += batchData.size();
                log.info("PDF流式导出进度: {}/{}", processedCount, totalCount);
                if (batchData.size() < pageSize) {
                    break;
                }
                pageNum++;
                batchData = null;
                System.gc();
            }
        }
        log.info("PDF流式导出 - 数据渲染完成，needTOC={}, pageNumberMap大小={}", needTOC, pageNumberMap.size());
        if (needTOC) {
            log.info("PDF流式导出 - 需要生成目录，开始flush document以触发页码收集");
            document.flush();
            log.info("PDF流式导出 - document.flush()完成，触发页码收集，当前pageNumberMap大小={}", pageNumberMap.size());
            if (pageNumberMap != null && !pageNumberMap.isEmpty()) {
                log.debug("PDF流式导出 - pageNumberMap内容: {}", pageNumberMap);
            }
            if (pageNumberMap.isEmpty()) {
                log.warn("PDF流式导出 - flush后pageNumberMap仍为空，无法生成目录");
                warnings.add("页码收集失败，已跳过目录生成");
            } else {
                log.info("PDF流式导出 - 开始生成目录，pageNumberMap包含{}个条目", pageNumberMap.size());
                try {
                    List<KnowledgeItemVo> allData;
                    if ("currentPage".equals(request.getExportRange()) && request.getPageNum() != null && request.getPageSize() != null) {
                        PageQuery pageQuery = new PageQuery(request.getPageSize(), request.getPageNum());
                        TableDataInfo<KnowledgeItemVo> pageData = queryPageList(bo, pageQuery);
                        allData = pageData.getRows() != null ? pageData.getRows() : new ArrayList<>();
                        log.info("PDF流式导出 - 目录生成：从currentPage查询数据，共{}条", allData.size());
                    } else {
                        allData = getExportData(request, null);
                        log.info("PDF流式导出 - 目录生成：从getExportData查询数据，共{}条", allData != null ? allData.size() : 0);
                    }
                    log.info("PDF流式导出 - 开始调用addTableOfContentsWithPageNumbers，数据条数={}", allData.size());
                    addTableOfContentsWithPageNumbers(document, pdfDoc, allData, fontChineseBold, fontChinese, pageNumberMap, warnings);
                    log.info("PDF流式导出 - 目录生成成功，当前PDF页数: {}", pdfDoc.getNumberOfPages());
                    allData = null;
                    System.gc();
                } catch (Exception e) {
                    log.error("PDF流式导出 - 生成目录失败", e);
                    warnings.add("目录生成失败: " + e.getMessage());
                }
            }
        }
        try {
            log.info("PDF流式导出 - 开始关闭document和输出流");
            document.close();
            log.info("PDF流式导出 - document.close()完成");
            if (needTOC && tempOutputStream != null) {
                log.info("PDF流式导出 - 开始重新排序页面，将目录页移到第一页");
                try {
                    byte[] tempPdfBytes = tempOutputStream.toByteArray();
                    log.info("PDF流式导出 - 临时PDF字节数组大小: {} bytes", tempPdfBytes.length);
                    tempOutputStream.close();
                    log.info("PDF流式导出 - tempOutputStream关闭完成");
                    log.info("PDF流式导出 - 开始读取临时PDF");
                    PdfDocument tempPdfDoc = new PdfDocument(new PdfReader(new ByteArrayInputStream(tempPdfBytes), new ReaderProperties()));
                    log.info("PDF流式导出 - 临时PDF读取成功");
                    tempPdfDoc.initializeOutlines();
                    log.info("PDF流式导出 - 临时PDF initializeOutlines()完成");
                    int totalPages = tempPdfDoc.getNumberOfPages();
                    int tocPageNum = totalPages;
                    log.info("PDF流式导出 - 临时PDF总页数: {}, 目录页页码: {}", totalPages, tocPageNum);
                    if (totalPages <= 0) {
                        log.error("PDF流式导出 - 错误：临时PDF页数为0或负数");
                        throw new ServiceException("临时PDF页数异常: " + totalPages);
                    }
                    log.info("PDF流式导出 - 开始创建最终PDF");
                    PdfDocument finalPdfDoc = new PdfDocument(new PdfWriter(finalOutputStream));
                    log.info("PDF流式导出 - 最终PDF创建成功");
                    finalPdfDoc.initializeOutlines();
                    log.info("PDF流式导出 - 最终PDF initializeOutlines()完成");
                    PdfPageFormCopier formCopier = new PdfPageFormCopier();
                    log.info("PDF流式导出 - PdfPageFormCopier创建完成");
                    List<Integer> pageOrder = new ArrayList<>();
                    pageOrder.add(tocPageNum);
                    for (int i = 1; i < tocPageNum; i++) {
                        pageOrder.add(i);
                    }
                    log.info("PDF流式导出 - 页面顺序: {}, 共{}页", pageOrder, pageOrder.size());
                    log.info("PDF流式导出 - 开始执行copyPagesTo，从临时PDF复制{}页到最终PDF", pageOrder.size());
                    tempPdfDoc.copyPagesTo(pageOrder, finalPdfDoc, formCopier);
                    log.info("PDF流式导出 - copyPagesTo执行完成");
                    log.info("PDF流式导出 - 最终PDF当前页数: {}", finalPdfDoc.getNumberOfPages());
                    log.info("PDF流式导出 - 开始关闭临时PDF");
                    tempPdfDoc.close();
                    log.info("PDF流式导出 - 临时PDF关闭完成");
                    log.info("PDF流式导出 - 开始关闭最终PDF");
                    finalPdfDoc.close();
                    log.info("PDF流式导出 - 最终PDF关闭完成");
                    log.info("PDF流式导出 - 页面重新排序完成，最终PDF应包含{}页", pageOrder.size());
                } catch (Exception e) {
                    log.error("PDF流式导出 - 页面重新排序过程中发生异常", e);
                    throw e;
                }
            } else {
                log.info("PDF流式导出 - 无需重新排序页面，直接刷新输出流");
                outputStream.flush();
                log.info("PDF流式导出 - outputStream.flush()完成");
            }
            log.info("PDF流式导出完成，共处理 {} 条数据", processedCount);
        } catch (Exception e) {
            log.error("PDF流式导出关闭失败", e);
            log.error("PDF流式导出 - 异常详情: 类型={}, 消息={}, 堆栈=", e.getClass().getName(), e.getMessage(), e);
            if (!response.isCommitted()) {
                response.reset();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write("{\"code\":500,\"msg\":\"PDF生成失败: " + e.getMessage().replace("\"", "\\\"").replace("\n", "\\n") + "\"}");
                response.getWriter().flush();
            }
            throw new IOException("PDF生成失败: " + e.getMessage(), e);
        }
    }
    private KnowledgeItemBo buildExportQueryBo(ExportRequestBo request) {
        KnowledgeItemBo bo = new KnowledgeItemBo();
        if (request.getFilters() != null) {
            bo = request.getFilters();
        }
        return bo;
    }
    private void generateReportFormatBatch(Document document, PdfDocument pdfDoc, List<KnowledgeItemVo> batchData, List<FieldInfoVo> fieldInfos,
                                          Map<String, String> fieldFormats, PdfFont font, PdfFont fontChineseBold, PdfFont fontChinese, 
                                          boolean codeHighlight, int startIndex, Map<String, Integer> pageNumberMap) throws IOException {
        Map<String, CweReferenceVo> cweMap = buildCweMap(batchData, extractExpandedFields(fieldInfos), fieldFormats);
        Map<String, KnowledgeTagVo> tagMap = buildTagMap(batchData, extractExpandedFields(fieldInfos), fieldFormats);
        Map<Long, String> userMap = buildUserMap(batchData, fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList()));
        Map<String, String> knowledgeBaseMap = buildKnowledgeBaseMap(batchData, 
            fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList()),
            extractExpandedFields(fieldInfos));
        DictService dictService = SpringUtils.getBean(DictService.class);
        for (int i = 0; i < batchData.size(); i++) {
            KnowledgeItemVo item = batchData.get(i);
            int itemIndex = startIndex + i;
            Div card = createItemCard(pdfDoc, item, itemIndex, fieldInfos, fieldFormats, cweMap, tagMap, userMap, knowledgeBaseMap, 
                font, fontChineseBold, fontChinese, codeHighlight, dictService, pageNumberMap);
            document.add(card);
        }
    }
    private Div createItemCard(PdfDocument pdfDoc, KnowledgeItemVo item, int itemIndex, List<FieldInfoVo> fieldInfos, Map<String, String> fieldFormats,
                              Map<String, CweReferenceVo> cweMap, Map<String, KnowledgeTagVo> tagMap, Map<Long, String> userMap,
                              Map<String, String> knowledgeBaseMap, PdfFont font, PdfFont fontChineseBold, PdfFont fontChinese,
                              boolean codeHighlight, DictService dictService, Map<String, Integer> pageNumberMap) throws IOException {
        Div card = new Div();
        card.setBackgroundColor(new DeviceRgb(255, 255, 255));
        card.setBorder(new SolidBorder(new DeviceRgb(229, 229, 229), 1));
        card.setBorderLeft(new SolidBorder(new DeviceRgb(64, 64, 64), 4));
        card.setPadding(16);
        card.setPaddingLeft(20);
        card.setMarginBottom(16);
        String title = StringUtils.isNotBlank(item.getTitle()) ? item.getTitle() : ("条目 " + (itemIndex + 1));
        if (StringUtils.isBlank(title)) {
            title = "条目 " + (itemIndex + 1);
        }
        String titleText = String.format("%d. %s", itemIndex + 1, title);
        String destinationId = "item_" + itemIndex;
        Paragraph titlePara = new Paragraph(titleText)
            .setFont(fontChineseBold != null ? fontChineseBold : font)
            .setFontSize(14)
            .setFontColor(new DeviceRgb(64, 64, 64))
            .setMarginBottom(12);
        titlePara.setDestination(destinationId);
        if (pdfDoc != null) {
            PdfOutline rootOutLine = pdfDoc.getOutlines(false);
            PdfOutline outline = rootOutLine.addOutline(destinationId);
            outline.addDestination(PdfDestination.makeDestination(new PdfString(destinationId)));
        }
        if (pageNumberMap != null) {
            titlePara.setNextRenderer(new PageNumberCollectorRenderer(titlePara, destinationId, pageNumberMap));
        }
        card.add(titlePara);
        if (pageNumberMap != null && itemIndex == 0) {
            log.debug("PDF流式导出 - 第一个条目已添加，destinationId={}", destinationId);
        }
        Div tagsContainer = createTagsRow(item, cweMap, dictService, fontChinese);
        if (tagsContainer != null) {
            card.add(tagsContainer);
        }
        Div metaInfoSection = createMetaInfoSection(item, fieldInfos, fieldFormats, cweMap, tagMap, userMap, knowledgeBaseMap, 
            font, fontChineseBold, fontChinese, dictService);
        if (metaInfoSection != null) {
            card.add(metaInfoSection);
        }
        Div contentSection = createContentSection(item, fieldInfos, fieldFormats, cweMap, tagMap, userMap, knowledgeBaseMap,
            font, fontChineseBold, fontChinese, codeHighlight);
        if (contentSection != null) {
            card.add(contentSection);
        }
        Div footerMeta = createFooterMeta(item, fieldInfos, fieldFormats, userMap, knowledgeBaseMap, fontChinese, font);
        if (footerMeta != null) {
            card.add(footerMeta);
        }
        return card;
    }
    private Div createTagsRow(KnowledgeItemVo item, Map<String, CweReferenceVo> cweMap, DictService dictService, PdfFont fontChinese) {
        List<Div> tags = new ArrayList<>();
        String severity = item.getSeverity();
        if (StringUtils.isBlank(severity) && item.getCvssScore() != null) {
            severity = CvssScoreCalculator.mapSeverityByScore(item.getCvssScore());
        }
        if (StringUtils.isNotBlank(severity)) {
            String severityLabel = dictService.getDictLabel("knowledge_severity", severity);
            String severityColorHex = getSeverityColor(severity);
            DeviceRgb severityColor = parseHexColor(severityColorHex);
            Div severityTag = createTag(severityLabel, severityColor, severityColor, fontChinese);
            if (!severityTag.getChildren().isEmpty() && severityTag.getChildren().get(0) instanceof Paragraph) {
                ((Paragraph) severityTag.getChildren().get(0)).setFontColor(new DeviceRgb(255, 255, 255));
            }
            tags.add(severityTag);
        }
        if (StringUtils.isNotBlank(item.getLanguage())) {
            String languageLabel = dictService.getDictLabel("knowledge_language", item.getLanguage());
            Div languageTag = createTag(languageLabel, new DeviceRgb(217, 217, 217), new DeviceRgb(245, 245, 245), fontChinese);
            if (!languageTag.getChildren().isEmpty() && languageTag.getChildren().get(0) instanceof Paragraph) {
                ((Paragraph) languageTag.getChildren().get(0)).setFontColor(new DeviceRgb(102, 102, 102));
            }
            tags.add(languageTag);
        }
        if (item.getVulnerabilityTypes() != null && !item.getVulnerabilityTypes().isEmpty()) {
            for (String vulnType : item.getVulnerabilityTypes()) {
                if (cweMap != null && cweMap.containsKey(vulnType)) {
                    CweReferenceVo cwe = cweMap.get(vulnType);
                    String vulnName = StringUtils.isNotBlank(cwe.getNameZh()) ? cwe.getNameZh() : cwe.getNameEn();
                    if (vulnName.length() > 25) {
                        vulnName = vulnName.substring(0, 25) + "...";
                    }
                    Div vulnTag = createTag(vulnName, new DeviceRgb(217, 217, 217), new DeviceRgb(245, 245, 245), fontChinese);
                    if (!vulnTag.getChildren().isEmpty() && vulnTag.getChildren().get(0) instanceof Paragraph) {
                        ((Paragraph) vulnTag.getChildren().get(0)).setFontColor(new DeviceRgb(96, 96, 96));
                    }
                    tags.add(vulnTag);
                }
            }
        }
        if (tags.isEmpty()) {
            return null;
        }
        Div tagsContainer = new Div();
        tagsContainer.setMarginBottom(12);
        tagsContainer.setProperty(Property.OVERFLOW_X, OverflowPropertyValue.VISIBLE);
        for (Div tag : tags) {
            tagsContainer.add(tag);
        }
        return tagsContainer;
    }
    private Div createTag(String text, DeviceRgb borderColor, DeviceRgb bgColor, PdfFont font) {
        Div tag = new Div();
        tag.setBorder(new SolidBorder(borderColor, 1));
        tag.setBackgroundColor(bgColor);
        tag.setPadding(4);
        tag.setPaddingLeft(8);
        tag.setPaddingRight(8);
        tag.setMarginRight(6);
        tag.setMarginBottom(4);
        tag.setWidth(UnitValue.createPointValue(0));
        Paragraph tagText = new Paragraph(text)
            .setFont(font)
            .setFontSize(9)
            .setFontColor(borderColor)
            .setMargin(0);
        tag.add(tagText);
        return tag;
    }
    private DeviceRgb parseHexColor(String hex) {
        if (hex == null || !hex.startsWith("#")) {
            return new DeviceRgb(64, 64, 64);
        }
        try {
            int r = Integer.parseInt(hex.substring(1, 3), 16);
            int g = Integer.parseInt(hex.substring(3, 5), 16);
            int b = Integer.parseInt(hex.substring(5, 7), 16);
            return new DeviceRgb(r, g, b);
        } catch (Exception e) {
            return new DeviceRgb(64, 64, 64);
        }
    }
    private Div createMetaInfoSection(KnowledgeItemVo item, List<FieldInfoVo> fieldInfos, Map<String, String> fieldFormats,
                                     Map<String, CweReferenceVo> cweMap, Map<String, KnowledgeTagVo> tagMap, Map<Long, String> userMap,
                                     Map<String, String> knowledgeBaseMap, PdfFont font, PdfFont fontChineseBold, PdfFont fontChinese,
                                     DictService dictService) {
        Map<String, Object> basicMeta = new LinkedHashMap<>();
        Map<String, Object> cvssMeta = new LinkedHashMap<>();
        for (FieldInfoVo fieldInfo : fieldInfos) {
            String key = fieldInfo.getKey();
            if ("title".equals(key) || "summary".equals(key) || "problemDescription".equals(key) || 
                "fixSolution".equals(key) || "exampleCode".equals(key)) {
                continue;
            }
            Object value = getFieldValue(item, key, cweMap, tagMap, userMap, knowledgeBaseMap, fieldFormats);
            if (value != null && StringUtils.isNotBlank(String.valueOf(value))) {
                if (key.startsWith("cvss") || "cvssScore".equals(key)) {
                    cvssMeta.put(fieldInfo.getLabel(), value);
                } else if (!"createTime".equals(key) && !"updateTime".equals(key) && 
                          !"createBy".equals(key) && !"updateBy".equals(key) &&
                          !"createByName".equals(key) && !"updateByName".equals(key) &&
                          !"kid".equals(key) && !"tags".equals(key)) {
                    basicMeta.put(fieldInfo.getLabel(), value);
                }
            }
        }
        if (basicMeta.isEmpty() && cvssMeta.isEmpty()) {
            return null;
        }
        Div metaSection = new Div();
        metaSection.setMarginBottom(12);
        if (!basicMeta.isEmpty()) {
            Div basicMetaDiv = createMetaTable("基本信息", basicMeta, font, fontChineseBold, fontChinese);
            metaSection.add(basicMetaDiv);
        }
        if (!cvssMeta.isEmpty()) {
            Div cvssMetaDiv = createMetaTable("CVSS评分", cvssMeta, font, fontChineseBold, fontChinese);
            metaSection.add(cvssMetaDiv);
        }
        return metaSection;
    }
    private Div createMetaTable(String title, Map<String, Object> metaData, PdfFont font, PdfFont fontChineseBold, PdfFont fontChinese) {
        Div metaGroup = new Div();
        metaGroup.setMarginBottom(8);
        Paragraph groupTitle = new Paragraph(title)
            .setFont(fontChineseBold != null ? fontChineseBold : font)
            .setFontSize(10)
            .setFontColor(new DeviceRgb(102, 102, 102))
            .setMarginBottom(4);
        metaGroup.add(groupTitle);
        Table metaTable = new Table(UnitValue.createPercentArray(new float[]{0.4f, 0.6f})).useAllAvailableWidth();
        metaTable.setBorder(new SolidBorder(new DeviceRgb(245, 245, 245), 1));
        metaTable.setBackgroundColor(new DeviceRgb(245, 245, 245));
        metaTable.setKeepTogether(false);
        for (Map.Entry<String, Object> entry : metaData.entrySet()) {
            com.itextpdf.layout.element.Cell labelCell = new com.itextpdf.layout.element.Cell()
                .add(new Paragraph(entry.getKey() + ": ")
                    .setFont(fontChinese != null ? fontChinese : font)
                    .setFontSize(9)
                    .setFontColor(new DeviceRgb(102, 102, 102)))
                .setBorder(new SolidBorder(new DeviceRgb(229, 229, 229), 0.5f))
                .setPadding(6)
                .setPaddingLeft(8);
            com.itextpdf.layout.element.Cell valueCell = new com.itextpdf.layout.element.Cell()
                .add(new Paragraph(String.valueOf(entry.getValue()))
                    .setFont(fontChinese != null ? fontChinese : font)
                    .setFontSize(9)
                    .setFontColor(new DeviceRgb(51, 51, 51)))
                .setBorder(new SolidBorder(new DeviceRgb(229, 229, 229), 0.5f))
                .setPadding(6)
                .setPaddingRight(8);
            metaTable.addCell(labelCell);
            metaTable.addCell(valueCell);
        }
        metaGroup.add(metaTable);
        return metaGroup;
    }
    private Div createContentSection(KnowledgeItemVo item, List<FieldInfoVo> fieldInfos, Map<String, String> fieldFormats,
                                    Map<String, CweReferenceVo> cweMap, Map<String, KnowledgeTagVo> tagMap, Map<Long, String> userMap,
                                    Map<String, String> knowledgeBaseMap, PdfFont font, PdfFont fontChineseBold, PdfFont fontChinese,
                                    boolean codeHighlight) throws IOException {
        Div contentSection = new Div();
        boolean hasContent = false;
        for (FieldInfoVo fieldInfo : fieldInfos) {
            String key = fieldInfo.getKey();
            if ("title".equals(key)) {
                continue;
            }
            if ("summary".equals(key) || "problemDescription".equals(key) || 
                "fixSolution".equals(key) || "exampleCode".equals(key)) {
                Object value = getFieldValue(item, key, cweMap, tagMap, userMap, knowledgeBaseMap, fieldFormats);
                if (value != null && StringUtils.isNotBlank(String.valueOf(value))) {
                    String text = String.valueOf(value);
                    String label = fieldInfo.getLabel();
                    Div contentItem = new Div();
                    contentItem.setMarginBottom(12);
                    Paragraph sectionTitle = new Paragraph(label)
                        .setFont(fontChineseBold != null ? fontChineseBold : font)
                        .setFontSize(11)
                        .setFontColor(new DeviceRgb(51, 51, 51))
                        .setMarginBottom(4);
                    Div divider = new Div();
                    divider.setBorder(new SolidBorder(new DeviceRgb(229, 229, 229), 1));
                    divider.setMarginBottom(8);
                    contentItem.add(sectionTitle);
                    contentItem.add(divider);
                    if ("exampleCode".equals(key) && codeHighlight) {
                        Div codeBlock = createCodeBlock(text, font, fontChinese);
                        contentItem.add(codeBlock);
                    } else {
                        Paragraph contentPara = new Paragraph(text)
                            .setFont(fontChinese != null ? fontChinese : font)
                            .setFontSize(10)
                            .setFontColor(new DeviceRgb(51, 51, 51))
                            .setMargin(0)
                            .setOrphansControl(new ParagraphOrphansControl(2))
                            .setWidowsControl(new ParagraphWidowsControl(2, 1, true));
                        contentItem.add(contentPara);
                    }
                    contentSection.add(contentItem);
                    hasContent = true;
                }
            }
        }
        if (item.getTags() != null && !item.getTags().isEmpty()) {
            Div tagsSection = new Div();
            tagsSection.setMarginBottom(12);
            Paragraph tagsTitle = new Paragraph("标签")
                .setFont(fontChineseBold != null ? fontChineseBold : font)
                .setFontSize(11)
                .setFontColor(new DeviceRgb(51, 51, 51))
                .setMarginBottom(4);
            Div divider = new Div();
            divider.setBorder(new SolidBorder(new DeviceRgb(229, 229, 229), 1));
            divider.setMarginBottom(8);
            tagsSection.add(tagsTitle);
            tagsSection.add(divider);
            Div tagsContainer = new Div();
            for (String tagName : item.getTags()) {
                Div tag = createTag(tagName, new DeviceRgb(82, 196, 26), new DeviceRgb(246, 255, 237), fontChinese);
                if (!tag.getChildren().isEmpty() && tag.getChildren().get(0) instanceof Paragraph) {
                    ((Paragraph) tag.getChildren().get(0)).setFontColor(new DeviceRgb(82, 196, 26));
                }
                tagsContainer.add(tag);
            }
            tagsSection.add(tagsContainer);
            contentSection.add(tagsSection);
            hasContent = true;
        }
        return hasContent ? contentSection : null;
    }
    private Div createFooterMeta(KnowledgeItemVo item, List<FieldInfoVo> fieldInfos, Map<String, String> fieldFormats,
                                Map<Long, String> userMap, Map<String, String> knowledgeBaseMap, PdfFont fontChinese, PdfFont font) {
        List<String> footerInfo = new ArrayList<>();
        for (FieldInfoVo fieldInfo : fieldInfos) {
            String key = fieldInfo.getKey();
            if ("createTime".equals(key) && item.getCreateTime() != null) {
                footerInfo.add("创建时间：" + formatDateTime(item.getCreateTime()));
            } else if ("updateTime".equals(key) && item.getUpdateTime() != null) {
                footerInfo.add("更新时间：" + formatDateTime(item.getUpdateTime()));
            } else if (("createBy".equals(key) || "createByName".equals(key)) && item.getCreateBy() != null && userMap != null) {
                String creator = userMap.get(item.getCreateBy());
                if (StringUtils.isNotBlank(creator)) {
                    footerInfo.add("创建人：" + creator);
                }
            } else if (("updateBy".equals(key) || "updateByName".equals(key)) && item.getUpdateBy() != null && userMap != null) {
                String updater = userMap.get(item.getUpdateBy());
                if (StringUtils.isNotBlank(updater)) {
                    footerInfo.add("更新人：" + updater);
                }
            } else if ("kid".equals(key) && StringUtils.isNotBlank(item.getKid()) && knowledgeBaseMap != null) {
                String kbName = knowledgeBaseMap.get(item.getKid());
                if (StringUtils.isNotBlank(kbName)) {
                    footerInfo.add("知识库：" + kbName);
                }
            }
        }
        if (footerInfo.isEmpty()) {
            return null;
        }
        Div footerMeta = new Div();
        footerMeta.setMarginTop(8);
        footerMeta.setPaddingTop(8);
        footerMeta.setBorderTop(new SolidBorder(new DeviceRgb(245, 245, 245), 1));
        String footerText = String.join(" | ", footerInfo);
        PdfFont footerFont = fontChinese != null ? fontChinese : font;
        Paragraph footerPara = new Paragraph(footerText)
            .setFont(footerFont)
            .setFontSize(9)
            .setFontColor(new DeviceRgb(102, 102, 102))
            .setMargin(0);
        footerMeta.add(footerPara);
        return footerMeta;
    }
    private void generateTableFormatBatch(Table table, List<KnowledgeItemVo> batchData, List<FieldInfoVo> fieldInfos,
                                         Map<String, String> fieldFormats, PdfFont font, PdfFont fontBold, PdfFont fontChinese) throws IOException {
        List<Map<String, Object>> exportData = convertToMapList(batchData, 
            fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList()),
            extractExpandedFields(fieldInfos), fieldFormats);
        for (Map<String, Object> rowData : exportData) {
            for (FieldInfoVo fieldInfo : fieldInfos) {
                Object value = rowData.get(fieldInfo.getKey());
                String text = value != null ? String.valueOf(value) : "";
                Paragraph textPara = new Paragraph(text)
                    .setFont(fontChinese != null ? fontChinese : font)
                    .setFontSize(9)
                    .setMargin(0)
                    .setOrphansControl(new ParagraphOrphansControl(1))
                    .setWidowsControl(new ParagraphWidowsControl(1, 1, true));
                com.itextpdf.layout.element.Cell cell = new com.itextpdf.layout.element.Cell().add(textPara);
                cell.setBorder(new SolidBorder(new DeviceRgb(200, 200, 200), 0.5f));
                cell.setPadding(5);
                cell.setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE);
                table.addCell(cell);
            }
        }
    }
    private void exportToPdf(List<KnowledgeItemVo> data, ExportRequestBo request, String fileName, HttpServletResponse response) throws IOException {
        if (CollectionUtils.isEmpty(data)) {
            throw new ServiceException("没有可导出的数据");
        }
        response.setContentType("application/pdf");
        response.setCharacterEncoding("utf-8");
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName);
        List<FieldInfoVo> fieldInfos = buildFieldInfos(request.getSelectedFields(), request.getExpandedFields());
        if (CollectionUtils.isEmpty(fieldInfos)) {
            throw new ServiceException("请至少选择一个导出字段");
        }
        PdfOptionsBo pdfOptions = request.getPdfOptions();
        boolean useReportFormat;
        if (pdfOptions != null && StringUtils.isNotBlank(pdfOptions.getFormatType())) {
            useReportFormat = "report".equals(pdfOptions.getFormatType());
        } else {
            useReportFormat = shouldUseReportFormat(fieldInfos);
        }
        boolean includeTOC = pdfOptions != null && Boolean.TRUE.equals(pdfOptions.getIncludeTOC());
        boolean includeHeaderFooter = pdfOptions == null || Boolean.TRUE.equals(pdfOptions.getIncludeHeaderFooter());
        boolean useHtmlToPdf = false;
        if (useHtmlToPdf) {
            try {
                exportToPdfFromHtml(data, request, fileName, response, fieldInfos, useReportFormat, includeTOC, includeHeaderFooter);
                return;
            } catch (Exception e) {
                log.warn("HTML转PDF失败，回退到iText原生方案", e);
            }
        }
        OutputStream outputStream = response.getOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        pdfDoc.setTagged();
        Document document = new Document(pdfDoc, PageSize.A4);
        document.setMargins(36, 36, 36, 36);
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont fontChinese = null;
        PdfFont fontChineseBold = null;
        String[] fontPaths = {
            "/usr/share/fonts/opentype/noto/NotoSansCJK-Regular.ttc",
            "/usr/share/fonts/opentype/noto/NotoSerifCJK-Regular.ttc",
            "/usr/share/fonts/opentype/noto/NotoSansCJK-Bold.ttc",
            "/usr/share/fonts/opentype/noto/NotoSerifCJK-Bold.ttc",
            "/usr/share/fonts/wenquanyi/wqy-zenhei/wqy-zenhei.ttc",
            "/usr/share/fonts/truetype/wqy/wqy-zenhei.ttc",
            "/usr/share/fonts/truetype/msttcore/SimSun.ttf",
            "/usr/share/fonts/truetype/msttcore/SimHei.ttf",
            "C:/Windows/Fonts/simsun.ttc",
            "C:/Windows/Fonts/simhei.ttf",
            "C:/Windows/Fonts/msyh.ttf",
            System.getProperty("java.home") + "/lib/fonts/SimSun.ttf"
        };
        boolean fontLoaded = false;
        String loadedFontPath = null;
        log.info("开始查找中文字体文件，共{}个路径", fontPaths.length);
        for (String fontPath : fontPaths) {
            try {
                java.io.File fontFile = new java.io.File(fontPath);
                boolean exists = fontFile.exists();
                boolean isFile = fontFile.isFile();
                log.debug("检查字体文件: {}, 存在: {}, 是文件: {}", fontPath, exists, isFile);
                if (exists && isFile) {
                    if (fontPath.toLowerCase().endsWith(".ttc")) {
                        //TTC文件需要使用FontProgramFactory加载，索引0表示第一个字体
                        //cached=false减少内存占用，避免OutOfMemoryError
                        FontProgram fontProgram = FontProgramFactory.createFont(fontPath, 0, false);
                        fontChinese = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                        loadedFontPath = fontPath;
                        fontLoaded = true;
                        log.info("成功从TTC文件加载中文字体: {}", fontPath);
                        break;
                    } else {
                        //TTF文件可以直接使用PdfFontFactory加载
                        fontChinese = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                        loadedFontPath = fontPath;
                        fontLoaded = true;
                        log.info("成功从TTF文件加载中文字体: {}", fontPath);
                        break;
                    }
                } else {
                    log.debug("字体文件不存在或不是文件: {}", fontPath);
                }
            } catch (Exception e) {
                log.warn("尝试从文件加载字体失败: {}, 错误: {}", fontPath, e.getMessage());
            }
        }
        if (!fontLoaded) {
            log.warn("所有字体文件路径都未找到字体文件，将尝试其他方式加载");
        }
        if (fontLoaded && loadedFontPath != null) {
            String boldFontPath = loadedFontPath.replace("Regular", "Bold");
            try {
                java.io.File boldFontFile = new java.io.File(boldFontPath);
                if (boldFontFile.exists() && boldFontFile.isFile()) {
                    if (boldFontPath.toLowerCase().endsWith(".ttc")) {
                        //cached=false减少内存占用
                        FontProgram boldFontProgram = FontProgramFactory.createFont(boldFontPath, 0, false);
                        fontChineseBold = PdfFontFactory.createFont(boldFontProgram, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                    } else {
                        fontChineseBold = PdfFontFactory.createFont(boldFontPath, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                    }
                    log.info("成功加载中文字体粗体: {}", boldFontPath);
                } else {
                    fontChineseBold = fontChinese;
                    log.info("未找到对应的粗体字体，使用常规字体作为粗体");
                }
            } catch (Exception e) {
                log.warn("加载粗体字体失败，使用常规字体: {}", e.getMessage());
                fontChineseBold = fontChinese;
            }
        }
        if (!fontLoaded) {
            try {
                InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/simsun.ttf");
                if (fontStream == null) {
                    fontStream = getClass().getClassLoader().getResourceAsStream("fonts/SimSun.ttf");
                }
                if (fontStream != null) {
                    byte[] fontBytes = fontStream.readAllBytes();
                    fontStream.close();
                    fontChinese = PdfFontFactory.createFont(fontBytes, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                    fontChineseBold = fontChinese;
                    fontLoaded = true;
                    log.info("从resources加载中文字体成功");
                }
            } catch (Exception e) {
                log.debug("从resources加载字体失败");
            }
        }
        if (!fontLoaded) {
            log.warn("尝试使用字体名称加载（可能无法正确渲染中文）");
            try {
                fontChinese = PdfFontFactory.createFont("STSong-Light", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                fontChineseBold = PdfFontFactory.createFont("STSongStd-Light", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                fontLoaded = true;
                log.warn("使用字体名称加载成功（但可能无法正确渲染中文）: STSong-Light");
        } catch (Exception e1) {
            try {
                    fontChinese = PdfFontFactory.createFont("SimSun", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                    fontChineseBold = PdfFontFactory.createFont("SimHei", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                    fontLoaded = true;
            } catch (Exception e2) {
                try {
                        fontChinese = PdfFontFactory.createFont("SimHei", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                    fontChineseBold = fontChinese;
                        fontLoaded = true;
                } catch (Exception e3) {
                        try {
                            fontChinese = PdfFontFactory.createFont("Microsoft YaHei", PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                            fontChineseBold = fontChinese;
                            fontLoaded = true;
                        } catch (Exception e4) {
                            log.error("无法加载中文字体，将使用Helvetica（中文可能无法正确显示）", e4);
                    fontChinese = font;
                    fontChineseBold = fontBold;
                }
                    }
                }
            }
        }
        if (fontChinese == null || fontChineseBold == null) {
            log.error("中文字体加载失败，PDF中的中文内容可能无法正确显示");
            fontChinese = font;
            fontChineseBold = fontBold;
        } else {
            String fontName = fontChinese.getFontProgram().getFontNames().getFontName();
            String boldFontName = fontChineseBold.getFontProgram().getFontNames().getFontName();
            log.info("中文字体加载成功: regular={}, bold={}", fontName, boldFontName);
            try {
                String testText = "测试";
                float width = fontChinese.getWidth(testText, 12);
                if (width <= 0) {
                    log.warn("字体可能不支持中文字符，测试文本宽度为0");
                } else {
                    log.info("字体中文字符测试通过，测试文本宽度: {}", width);
                }
            } catch (Exception e) {
                log.warn("字体中文字符测试失败", e);
            }
        }
        if (includeHeaderFooter) {
            HeaderFooterHandler handler = new HeaderFooterHandler(font, fontChinese, fileName);
            pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, handler);
        }
        boolean codeHighlight = pdfOptions != null && Boolean.TRUE.equals(pdfOptions.getCodeHighlight());
        Map<String, Integer> pageNumberMap = new HashMap<>();
        if (useReportFormat) {
            generateReportFormat(document, pdfDoc, data, fieldInfos, request.getFieldFormats(), font, fontChineseBold, fontChinese, codeHighlight, pageNumberMap);
        } else {
            generateTableFormat(document, data, fieldInfos, request.getFieldFormats(), font, fontBold, fontChinese);
        }
        if (includeTOC && useReportFormat && !pageNumberMap.isEmpty()) {
            List<String> warnings = new ArrayList<>();
            addTableOfContentsWithPageNumbers(document, pdfDoc, data, fontChineseBold, fontChinese, pageNumberMap, warnings);
        }
        document.close();
        outputStream.flush();
    }
    private boolean shouldUseReportFormat(List<FieldInfoVo> fieldInfos) {
        if (CollectionUtils.isEmpty(fieldInfos)) {
            return false;
        }
        int fieldCount = fieldInfos.size();
        boolean hasLongTextFields = false;
        for (FieldInfoVo fieldInfo : fieldInfos) {
            String key = fieldInfo.getKey();
            if ("summary".equals(key) || "problemDescription".equals(key) || 
                "fixSolution".equals(key) || "exampleCode".equals(key)) {
                hasLongTextFields = true;
                break;
            }
        }
        return hasLongTextFields || fieldCount <= 6;
    }
    private static class HeaderFooterHandler implements IEventHandler {
        private final PdfFont font;
        private final PdfFont fontChinese;
        private final String fileName;
        public HeaderFooterHandler(PdfFont font, PdfFont fontChinese, String fileName) {
            this.font = font;
            this.fontChinese = fontChinese;
            this.fileName = fileName;
        }
        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdfDoc = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            int pageNumber = pdfDoc.getPageNumber(page);
            Rectangle pageSize = page.getPageSize();
            PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
            canvas.saveState();
            canvas.setFontAndSize(font, 9);
            canvas.setFillColor(ColorConstants.GRAY);
            String headerText = "知识条目导出报告";
            String footerText = String.format("第 %d 页", pageNumber);
            String dateText = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            float headerY = pageSize.getTop() - 20;
            float footerY = 20;
            canvas.setFontAndSize(fontChinese, 9);
            canvas.beginText();
            canvas.moveText(pageSize.getWidth() / 2 - 50, headerY);
            canvas.showText(headerText);
            canvas.endText();
            canvas.beginText();
            canvas.moveText(pageSize.getWidth() / 2 - 30, footerY);
            canvas.showText(footerText);
            canvas.endText();
            canvas.setFontAndSize(font, 9);
            canvas.beginText();
            canvas.moveText(20, footerY);
            canvas.showText(dateText);
            canvas.endText();
            canvas.restoreState();
        }
    }
    private void addTableOfContentsWithPageNumbers(Document document, PdfDocument pdfDoc, List<KnowledgeItemVo> data, 
                                                   PdfFont fontChineseBold, PdfFont fontChinese, Map<String, Integer> pageNumberMap, List<String> warnings) throws IOException {
        log.info("PDF流式导出 - addTableOfContentsWithPageNumbers开始，数据条数={}, pageNumberMap大小={}", data.size(), pageNumberMap.size());
        PdfFont tocBoldFont = fontChineseBold != null ? fontChineseBold : PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont tocFont = fontChinese != null ? fontChinese : PdfFontFactory.createFont(StandardFonts.HELVETICA);
        document.add(new Paragraph("\n"));
        Paragraph tocTitle = new Paragraph("目录")
            .setFont(tocBoldFont)
            .setFontSize(18)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(24);
        document.add(tocTitle);
        document.add(new Paragraph("\n"));
        List<TabStop> tabStops = new ArrayList<>();
        tabStops.add(new TabStop(550, TabAlignment.RIGHT, new DottedLine(1, 2)));
        int tocItemCount = 0;
        for (int i = 0; i < Math.min(data.size(), 1000); i++) {
            KnowledgeItemVo item = data.get(i);
            String title = StringUtils.isNotBlank(item.getTitle()) ? item.getTitle() : ("条目 " + (i + 1));
            if (title.length() > 50) {
                title = title.substring(0, 50) + "...";
            }
            String destinationId = "item_" + i;
            int pageNumber = pageNumberMap.getOrDefault(destinationId, 0);
            if (pageNumber == 0) {
                log.debug("PDF流式导出 - 目录项跳过：destinationId={} 未找到页码", destinationId);
                continue;
            }
            tocItemCount++;
            Paragraph tocItem = new Paragraph();
            tocItem.addTabStops(tabStops);
            Link titleLink = new Link(String.format("%d. %s", i + 1, title), PdfAction.createGoTo(destinationId));
            titleLink.setFont(tocFont);
            titleLink.setFontSize(11);
            titleLink.setFontColor(new DeviceRgb(51, 51, 51));
            tocItem.add(titleLink);
            tocItem.add(new Tab());
            Text pageText = new Text(String.valueOf(pageNumber));
            pageText.setFont(tocFont);
            pageText.setFontSize(11);
            pageText.setFontColor(new DeviceRgb(102, 102, 102));
            tocItem.add(pageText);
            tocItem.setMarginBottom(10);
            document.add(tocItem);
        }
        document.add(new Paragraph("\n"));
        log.info("PDF流式导出 - 目录项添加完成，共{}个有效目录项", tocItemCount);
        int currentPageCount = pdfDoc.getNumberOfPages();
        log.info("PDF流式导出 - 添加目录后当前PDF总页数: {}", currentPageCount);
        if (currentPageCount > 0) {
            int tocPageNum = pdfDoc.getPageNumber(pdfDoc.getLastPage());
            log.info("PDF流式导出 - 目录页当前页码: {}", tocPageNum);
            document.flush();
            int pageCountAfterFlush = pdfDoc.getNumberOfPages();
            log.info("PDF流式导出 - flush后PDF总页数: {}", pageCountAfterFlush);
            log.info("PDF流式导出 - addTableOfContentsWithPageNumbers完成，目录页页码: {}", tocPageNum);
        } else {
            log.warn("PDF流式导出 - 警告：PDF页数为0，无法确定目录页页码");
            document.flush();
            log.info("PDF流式导出 - addTableOfContentsWithPageNumbers完成（页数为0）");
        }
    }
    private void generateTableFormat(Document document, List<KnowledgeItemVo> data, List<FieldInfoVo> fieldInfos, 
                                     Map<String, String> fieldFormats, PdfFont font, PdfFont fontBold, PdfFont fontChinese) throws IOException {
        List<Map<String, Object>> exportData = convertToMapList(data, 
            fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList()),
            extractExpandedFields(fieldInfos), fieldFormats);
        Table table = new Table(UnitValue.createPercentArray(fieldInfos.size())).useAllAvailableWidth();
        table.setBorder(new SolidBorder(new DeviceRgb(200, 200, 200), 0.5f));
        table.setKeepTogether(false);
        PdfFont headerFont = fontChinese != null && fontChinese != font ? fontChinese : fontBold;
        for (FieldInfoVo fieldInfo : fieldInfos) {
            com.itextpdf.layout.element.Cell headerCell = new com.itextpdf.layout.element.Cell().add(new Paragraph(fieldInfo.getLabel())
                .setFont(headerFont)
                .setFontSize(10));
            headerCell.setBackgroundColor(new DeviceRgb(245, 245, 245));
            headerCell.setBorder(new SolidBorder(new DeviceRgb(200, 200, 200), 0.5f));
            headerCell.setTextAlignment(TextAlignment.CENTER);
            headerCell.setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE);
            headerCell.setPadding(5);
            table.addHeaderCell(headerCell);
        }
        for (Map<String, Object> rowData : exportData) {
            for (FieldInfoVo fieldInfo : fieldInfos) {
                Object value = rowData.get(fieldInfo.getKey());
                String text = value != null ? String.valueOf(value) : "";
                Paragraph textPara = new Paragraph(text)
                    .setFont(fontChinese != null ? fontChinese : font)
                    .setFontSize(9)
                    .setMargin(0)
                    .setOrphansControl(new ParagraphOrphansControl(1))
                    .setWidowsControl(new ParagraphWidowsControl(1, 1, true));
                com.itextpdf.layout.element.Cell cell = new com.itextpdf.layout.element.Cell().add(textPara);
                cell.setBorder(new SolidBorder(new DeviceRgb(200, 200, 200), 0.5f));
                cell.setPadding(5);
                cell.setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE);
                table.addCell(cell);
            }
        }
        document.add(table);
    }
    private void generateReportFormat(Document document, PdfDocument pdfDoc, List<KnowledgeItemVo> data, List<FieldInfoVo> fieldInfos,
                                     Map<String, String> fieldFormats, PdfFont font, PdfFont fontChineseBold, PdfFont fontChinese, 
                                     boolean codeHighlight, Map<String, Integer> pageNumberMap) throws IOException {
        log.info("PDF导出 - 开始生成报告格式PDF，数据条数: {}, 字段数: {}", data.size(), fieldInfos.size());
        Map<String, CweReferenceVo> cweMap = buildCweMap(data, extractExpandedFields(fieldInfos), fieldFormats);
        Map<String, KnowledgeTagVo> tagMap = buildTagMap(data, extractExpandedFields(fieldInfos), fieldFormats);
        Map<Long, String> userMap = buildUserMap(data, fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList()));
        Map<String, String> knowledgeBaseMap = buildKnowledgeBaseMap(data, 
            fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList()),
            extractExpandedFields(fieldInfos));
        DictService dictService = SpringUtils.getBean(DictService.class);
        for (int i = 0; i < data.size(); i++) {
            KnowledgeItemVo item = data.get(i);
            Div card = createItemCard(pdfDoc, item, i, fieldInfos, fieldFormats, cweMap, tagMap, userMap, knowledgeBaseMap,
                font, fontChineseBold, fontChinese, codeHighlight, dictService, pageNumberMap);
            document.add(card);
        }
        log.info("PDF导出 - 报告格式PDF生成完成，共生成 {} 个条目", data.size());
    }
    private Div createCodeBlock(String code, PdfFont defaultFont, PdfFont chineseFont) throws IOException {
        PdfFont codeFont = PdfFontFactory.createFont(StandardFonts.COURIER);
        Div codeContainer = new Div();
        codeContainer.setBackgroundColor(new DeviceRgb(248, 248, 248));
        codeContainer.setBorder(new SolidBorder(new DeviceRgb(220, 220, 220), 1));
        codeContainer.setPadding(8);
        codeContainer.setMarginBottom(5);
        Paragraph codePara = new Paragraph();
        codePara.setFont(codeFont);
        codePara.setFontSize(9);
        codePara.setMargin(0);
        String language = detectCodeLanguage(code);
        String highlightedHtml = highlightJsService.highlight(code, language);
        parseHighlightedHtml(highlightedHtml, codePara, codeFont);
        codeContainer.add(codePara);
        return codeContainer;
    }
    private void parseHighlightedHtml(String html, Paragraph para, PdfFont font) {
        if (html == null || html.isEmpty()) {
            return;
        }
        int i = 0;
        int len = html.length();
        while (i < len) {
            if (html.charAt(i) == '<') {
                int tagEnd = html.indexOf('>', i);
                if (tagEnd == -1) {
                    break;
                }
                String tag = html.substring(i + 1, tagEnd);
                if (tag.startsWith("span")) {
                    int closeTag = html.indexOf("</span>", tagEnd);
                    if (closeTag == -1) {
                        i = tagEnd + 1;
                        continue;
                    }
                    String text = html.substring(tagEnd + 1, closeTag);
                    text = decodeHtmlEntities(text);
                    DeviceRgb color = getColorFromSpanClass(tag);
                    Text textElement = new Text(text);
                    textElement.setFont(font);
                    textElement.setFontSize(9);
                    textElement.setFontColor(color);
                    textElement.setNextRenderer(new PreserveWhitespaceTextRenderer(textElement));
                    para.add(textElement);
                    i = closeTag + 7;
                } else if (tag.equals("br") || tag.startsWith("br ")) {
                    Text newlineText = new Text("\n");
                    newlineText.setNextRenderer(new PreserveWhitespaceTextRenderer(newlineText));
                    para.add(newlineText);
                    i = tagEnd + 1;
                } else {
                    i = tagEnd + 1;
                }
            } else {
                int nextTag = html.indexOf('<', i);
                if (nextTag == -1) {
                    String text = decodeHtmlEntities(html.substring(i));
                    Text textElement = new Text(text);
                    textElement.setFont(font);
                    textElement.setFontSize(9);
                    textElement.setFontColor(new DeviceRgb(0, 0, 0));
                    textElement.setNextRenderer(new PreserveWhitespaceTextRenderer(textElement));
                    para.add(textElement);
                    break;
                }
                if (nextTag > i) {
                    String text = decodeHtmlEntities(html.substring(i, nextTag));
                    Text textElement = new Text(text);
                    textElement.setFont(font);
                    textElement.setFontSize(9);
                    textElement.setFontColor(new DeviceRgb(0, 0, 0));
                    textElement.setNextRenderer(new PreserveWhitespaceTextRenderer(textElement));
                    para.add(textElement);
                }
                i = nextTag;
            }
        }
    }
    private String decodeHtmlEntities(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")
            .replace("&nbsp;", " ");
    }
    private DeviceRgb getColorFromSpanClass(String spanTag) {
        String classAttr = extractClassFromTag(spanTag);
        if (classAttr == null) {
            return new DeviceRgb(0, 0, 0);
        }
        if (classAttr.contains("keyword") || classAttr.contains("built_in") || classAttr.contains("type")) {
            return new DeviceRgb(0, 0, 255);
        } else if (classAttr.contains("string") || classAttr.contains("attr")) {
            return new DeviceRgb(163, 21, 21);
        } else if (classAttr.contains("comment")) {
            return new DeviceRgb(0, 128, 0);
        } else if (classAttr.contains("number") || classAttr.contains("literal")) {
            return new DeviceRgb(0, 128, 128);
        } else if (classAttr.contains("function")) {
            return new DeviceRgb(121, 93, 163);
        } else if (classAttr.contains("variable")) {
            return new DeviceRgb(0, 0, 128);
        }
        return new DeviceRgb(0, 0, 0);
    }
    private String extractClassFromTag(String tag) {
        int classIdx = tag.indexOf("class=\"");
        if (classIdx == -1) {
            classIdx = tag.indexOf("class='");
            if (classIdx == -1) {
                return null;
            }
            int end = tag.indexOf("'", classIdx + 7);
            return end == -1 ? null : tag.substring(classIdx + 7, end);
        }
        int end = tag.indexOf("\"", classIdx + 7);
        return end == -1 ? null : tag.substring(classIdx + 7, end);
    }
    private String detectCodeLanguage(String code) {
        if (code == null || code.trim().isEmpty()) {
            return "text";
        }
        String trimmed = code.trim();
        if (trimmed.startsWith("<?xml") || trimmed.startsWith("<")) {
            return "xml";
        }
        if (trimmed.startsWith("package ") || trimmed.contains("public class") || trimmed.contains("import ")) {
            return "java";
        }
        if (trimmed.startsWith("def ") || trimmed.contains("import ") && trimmed.contains("print(")) {
            return "python";
        }
        if (trimmed.startsWith("function ") || trimmed.contains("const ") || trimmed.contains("let ")) {
            return "javascript";
        }
        if (trimmed.startsWith("SELECT ") || trimmed.startsWith("select ")) {
            return "sql";
        }
        if (trimmed.startsWith("#include") || trimmed.contains("int main")) {
            return "cpp";
        }
        return "text";
    }
    private List<CodeToken> tokenizeCode(String code, String language) {
        //预分配容量，减少ArrayList扩容次数，避免内存溢出
        int estimatedSize = code.length() / 10 + code.split("\n").length * 2;
        List<CodeToken> tokens = new ArrayList<>(Math.min(estimatedSize, 100000));
        if (code == null || code.isEmpty()) {
            return tokens;
        }
        String[] keywords = getKeywordsForLanguage(language);
        String[] lines = code.split("\n", -1);
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.trim().isEmpty()) {
                tokens.add(new CodeToken("\n", TokenType.DEFAULT));
                continue;
            }
            if (language.equals("java") || language.equals("cpp") || language.equals("javascript")) {
                tokenizeLine(line, keywords, tokens);
            } else if (language.equals("python")) {
                tokenizePythonLine(line, tokens);
            } else if (language.equals("xml")) {
                tokenizeXmlLine(line, tokens);
            } else {
                tokens.add(new CodeToken(line, TokenType.DEFAULT));
            }
            if (i < lines.length - 1) {
                tokens.add(new CodeToken("\n", TokenType.DEFAULT));
            }
        }
        return tokens;
    }
    private void tokenizeLine(String line, String[] keywords, List<CodeToken> tokens) {
        int i = 0;
        int lineLen = line.length();
        while (i < lineLen) {
            char c = line.charAt(i);
            if (c == '"' || c == '\'') {
                char quote = c;
                int end = line.indexOf(quote, i + 1);
                if (end == -1) {
                    tokens.add(new CodeToken(line.substring(i), TokenType.STRING));
                    break;
                }
                tokens.add(new CodeToken(line.substring(i, end + 1), TokenType.STRING));
                i = end + 1;
            } else if (i < lineLen - 1 && line.charAt(i) == '/' && line.charAt(i + 1) == '/') {
                tokens.add(new CodeToken(line.substring(i), TokenType.COMMENT));
                break;
            } else if (i < lineLen - 1 && line.charAt(i) == '/' && line.charAt(i + 1) == '*') {
                int end = line.indexOf("*/", i + 2);
                if (end == -1) {
                    tokens.add(new CodeToken(line.substring(i), TokenType.COMMENT));
                    break;
                }
                tokens.add(new CodeToken(line.substring(i, end + 2), TokenType.COMMENT));
                i = end + 2;
            } else {
                int next = findNextSpecialChar(line, i);
                String word = line.substring(i, next);
                if (isKeyword(word, keywords)) {
                    tokens.add(new CodeToken(word, TokenType.KEYWORD));
                } else if (isNumber(word)) {
                    tokens.add(new CodeToken(word, TokenType.NUMBER));
                } else {
                    tokens.add(new CodeToken(word, TokenType.DEFAULT));
                }
                i = next;
            }
        }
    }
    private void tokenizePythonLine(String line, List<CodeToken> tokens) {
        if (line.trim().startsWith("#")) {
            tokens.add(new CodeToken(line, TokenType.COMMENT));
            return;
        }
        if (line.trim().startsWith("\"\"\"") || line.trim().startsWith("'''")) {
            tokens.add(new CodeToken(line, TokenType.COMMENT));
            return;
        }
        tokenizeLine(line, getKeywordsForLanguage("python"), tokens);
    }
    private void tokenizeXmlLine(String line, List<CodeToken> tokens) {
        int i = 0;
        while (i < line.length()) {
            if (line.charAt(i) == '<') {
                int end = line.indexOf('>', i);
                if (end == -1) {
                    tokens.add(new CodeToken(line.substring(i), TokenType.KEYWORD));
                    break;
                }
                tokens.add(new CodeToken(line.substring(i, end + 1), TokenType.KEYWORD));
                i = end + 1;
            } else {
                int next = line.indexOf('<', i);
                if (next == -1) {
                    tokens.add(new CodeToken(line.substring(i), TokenType.DEFAULT));
                    break;
                }
                tokens.add(new CodeToken(line.substring(i, next), TokenType.DEFAULT));
                i = next;
            }
        }
    }
    private int findNextSpecialChar(String line, int start) {
        int len = line.length();
        for (int i = start; i < len; i++) {
            char c = line.charAt(i);
            if (c == '"' || c == '\'' || c == ' ' || c == '\t') {
                return i;
            }
            if (i < len - 1 && c == '/') {
                char next = line.charAt(i + 1);
                if (next == '/' || next == '*') {
                    return i;
                }
            }
        }
        return len;
    }
    private boolean isKeyword(String word, String[] keywords) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        String trimmed = word.trim();
        for (String keyword : keywords) {
            if (keyword.equals(trimmed)) {
                return true;
            }
        }
        return false;
    }
    private boolean isNumber(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(word.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private String[] getKeywordsForLanguage(String language) {
        switch (language) {
            case "java":
                return new String[]{"public", "private", "protected", "class", "interface", "extends", "implements",
                    "static", "final", "void", "return", "if", "else", "for", "while", "do", "switch", "case",
                    "break", "continue", "try", "catch", "finally", "throw", "throws", "new", "this", "super",
                    "import", "package", "boolean", "int", "long", "float", "double", "char", "String", "null"};
            case "python":
                return new String[]{"def", "class", "if", "elif", "else", "for", "while", "try", "except",
                    "finally", "with", "as", "import", "from", "return", "yield", "pass", "break", "continue",
                    "and", "or", "not", "in", "is", "None", "True", "False", "lambda"};
            case "javascript":
                return new String[]{"function", "var", "let", "const", "if", "else", "for", "while", "do",
                    "switch", "case", "break", "continue", "return", "try", "catch", "finally", "throw",
                    "new", "this", "class", "extends", "import", "export", "default", "async", "await"};
            case "cpp":
                return new String[]{"int", "char", "float", "double", "void", "bool", "if", "else", "for",
                    "while", "do", "switch", "case", "break", "continue", "return", "class", "struct",
                    "public", "private", "protected", "static", "const", "virtual", "new", "delete"};
            case "sql":
                return new String[]{"SELECT", "FROM", "WHERE", "INSERT", "UPDATE", "DELETE", "CREATE",
                    "ALTER", "DROP", "TABLE", "INDEX", "VIEW", "JOIN", "INNER", "OUTER", "LEFT", "RIGHT",
                    "ON", "GROUP", "BY", "ORDER", "HAVING", "UNION", "AND", "OR", "NOT", "NULL", "AS"};
            default:
                return new String[]{};
        }
    }
    private static class CodeToken {
        String text;
        TokenType type;
        CodeToken(String text, TokenType type) {
            this.text = text;
            this.type = type;
        }
    }
    private enum TokenType {
        KEYWORD, STRING, COMMENT, NUMBER, DEFAULT
    }
    private void exportToPdfFromHtml(List<KnowledgeItemVo> data, ExportRequestBo request, String fileName, HttpServletResponse response, 
                                     List<FieldInfoVo> fieldInfos, boolean useReportFormat, boolean includeTOC, boolean includeHeaderFooter) throws IOException {
        String html = generateFullHtmlForPdf(data, fieldInfos, request.getFieldFormats(), useReportFormat, includeTOC, includeHeaderFooter);
        log.info("PDF导出 - 生成的HTML长度: {}, 字段数量: {}", html.length(), fieldInfos.size());
        if (html.length() < 1000) {
            log.info("PDF导出 - 生成的HTML内容: {}", html);
        }
        OutputStream outputStream = response.getOutputStream();
        ConverterProperties properties = new ConverterProperties();
        DefaultFontProvider fontProvider = new DefaultFontProvider(true, true, true);
        String[] fontDirs = {
            "/usr/share/fonts/truetype/msttcore",
            "/usr/share/fonts/truetype",
            "C:/Windows/Fonts",
            System.getProperty("java.home") + "/lib/fonts"
        };
        String[] fontFiles = {
            "SimSun.ttf", "simsun.ttf", "simsun.ttc",
            "SimHei.ttf", "simhei.ttf",
            "msyh.ttf", "msyhbd.ttf"
        };
        boolean fontAdded = false;
        String loadedFontPath = null;
        for (String fontDir : fontDirs) {
            java.io.File dir = new java.io.File(fontDir);
            if (dir.exists() && dir.isDirectory()) {
                try {
                    fontProvider.addDirectory(fontDir);
                    fontAdded = true;
                    loadedFontPath = fontDir;
                    log.info("成功添加字体目录: {}", fontDir);
                    break;
                } catch (Exception e) {
                    log.debug("添加字体目录失败: {}", fontDir, e);
                }
            }
        }
        if (!fontAdded) {
            for (String fontDir : fontDirs) {
                for (String fontFile : fontFiles) {
                    String fontPath = fontDir + "/" + fontFile;
                    try {
                        java.io.File font = new java.io.File(fontPath);
                        if (font.exists() && font.isFile()) {
                            try {
                                FontProgram fontProgram = FontProgramFactory.createFont(fontPath);
                                fontProvider.addFont(fontProgram);
                                fontAdded = true;
                                loadedFontPath = fontPath;
                                log.info("成功加载中文字体: {}", fontPath);
                                break;
                            } catch (Exception e1) {
                                try {
                                    fontProvider.addFont(fontPath);
                                    fontAdded = true;
                                    loadedFontPath = fontPath;
                                    log.info("成功加载中文字体(直接路径): {}", fontPath);
                                    break;
                                } catch (Exception e2) {
                                    log.debug("尝试加载字体失败: {}", fontPath, e2);
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.debug("检查字体文件失败: {}", fontPath, e);
                    }
                }
                if (fontAdded) break;
            }
        }
        if (!fontAdded) {
            try {
                InputStream fontStream = getClass().getClassLoader().getResourceAsStream("fonts/simsun.ttf");
                if (fontStream == null) {
                    fontStream = getClass().getClassLoader().getResourceAsStream("fonts/SimSun.ttf");
                }
                if (fontStream != null) {
                    byte[] fontBytes = fontStream.readAllBytes();
                    fontStream.close();
                    FontProgram fontProgram = FontProgramFactory.createFont(fontBytes);
                    fontProvider.addFont(fontProgram);
                    fontAdded = true;
                    log.info("从resources加载中文字体成功");
                }
            } catch (Exception e) {
                log.warn("无法从resources加载中文字体", e);
            }
        }
        if (!fontAdded) {
            log.warn("未找到中文字体文件，html2pdf将尝试使用系统字体。如果中文无法显示，请将中文字体文件放在resources/fonts/目录下");
        } else {
            log.info("已加载中文字体支持，路径: {}", loadedFontPath);
        }
        properties.setFontProvider(fontProvider);
        HtmlConverter.convertToPdf(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)), outputStream, properties);
        outputStream.flush();
    }
    private String generateFullHtmlForPdf(List<KnowledgeItemVo> data, List<FieldInfoVo> fieldInfos, Map<String, String> fieldFormats, 
                                          boolean useReportFormat, boolean includeTOC, boolean includeHeaderFooter) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body style='font-family: SimSun, Microsoft YaHei, SimHei, Arial, sans-serif; margin: 0; padding: 20px; background-color: #fff;'>");
        if (includeTOC && useReportFormat) {
            html.append("<div style='margin-bottom: 30px; page-break-after: always;'><div style='font-size: 16px; font-weight: bold; text-align: center; margin-bottom: 20px;'>目录</div>");
            for (int i = 0; i < Math.min(data.size(), 50); i++) {
                KnowledgeItemVo item = data.get(i);
            String title = StringUtils.isNotBlank(item.getTitle()) ? item.getTitle() : ("条目 " + (i + 1));
                if (title.length() > 50) {
                    title = title.substring(0, 50) + "...";
                }
                html.append("<div style='font-size: 12px; margin-bottom: 5px;'>").append((i + 1)).append(". ").append(escapeHtml(title)).append("</div>");
            }
            html.append("</div>");
        }
        if (useReportFormat) {
            html.append(generateReportFormatHtmlForPdf(data, fieldInfos, fieldFormats));
        } else {
            html.append(generateTableFormatHtmlForPdf(data, fieldInfos, fieldFormats));
        }
        html.append("</body></html>");
        return html.toString();
    }
    private String getSeverityColor(String severity) {
        if (StringUtils.isBlank(severity)) {
            return "#1890ff";
        }
        String lowerSeverity = severity.toLowerCase();
        switch (lowerSeverity) {
            case "critical":
                return "#cf1322";
            case "high":
                return "#ff4d4f";
            case "medium":
                return "#faad14";
            case "low":
                return "#52c41a";
            case "none":
                return "#808080";
            default:
                return "#1890ff";
        }
    }
    private String getSeverityBgColor(String severity) {
        if (StringUtils.isBlank(severity)) {
            return "#e6f7ff";
        }
        String lowerSeverity = severity.toLowerCase();
        switch (lowerSeverity) {
            case "critical":
                return "#fff1f0";
            case "high":
                return "#fff1f0";
            case "medium":
                return "#fffbe6";
            case "low":
                return "#f6ffed";
            case "none":
                return "#f5f5f5";
            default:
                return "#e6f7ff";
        }
    }
    private String generateReportFormatHtmlForPdf(List<KnowledgeItemVo> data, List<FieldInfoVo> fieldInfos, Map<String, String> fieldFormats) {
        Map<String, CweReferenceVo> cweMap = buildCweMap(data, extractExpandedFields(fieldInfos), fieldFormats);
        Map<String, KnowledgeTagVo> tagMap = buildTagMap(data, extractExpandedFields(fieldInfos), fieldFormats);
        Map<Long, String> userMap = buildUserMap(data, fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList()));
        Map<String, String> knowledgeBaseMap = buildKnowledgeBaseMap(data, 
            fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList()),
            extractExpandedFields(fieldInfos));
        DictService dictService = SpringUtils.getBean(DictService.class);
        StringBuilder html = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            KnowledgeItemVo item = data.get(i);
            String severity = item.getSeverity();
            if (StringUtils.isBlank(severity) && item.getCvssScore() != null) {
                severity = CvssScoreCalculator.mapSeverityByScore(item.getCvssScore());
            }
            String borderColor = getSeverityColor(severity);
            String bgColor = getSeverityBgColor(severity);
            html.append("<div style='border: 1px solid #e5e5e5; border-left: 4px solid ").append(borderColor).append("; padding: 15px; margin-bottom: 15px; page-break-inside: avoid; background-color: ").append(bgColor).append("; border-radius: 6px; font-family: SimSun, Microsoft YaHei, SimHei, Arial, sans-serif;'>");
            String title = StringUtils.isNotBlank(item.getTitle()) ? item.getTitle() : ("条目 " + (i + 1));
            if (StringUtils.isBlank(title)) {
                title = "条目 " + (i + 1);
            }
            html.append("<div style='font-size: 14px; font-weight: bold; color: #404040; margin-bottom: 8px; font-family: SimSun, Microsoft YaHei, SimHei, Arial, sans-serif;'>").append((i + 1)).append(". ").append(escapeHtml(title)).append("</div>");
            List<String> metaInfo = new ArrayList<>();
            for (FieldInfoVo fieldInfo : fieldInfos) {
                String key = fieldInfo.getKey();
                if ("title".equals(key) || "summary".equals(key) || "problemDescription".equals(key) || 
                    "fixSolution".equals(key) || "exampleCode".equals(key)) {
                    continue;
                }
                Object value = getFieldValue(item, key, cweMap, tagMap, userMap, knowledgeBaseMap, fieldFormats);
                String valueStr = value != null ? String.valueOf(value) : "";
                if (StringUtils.isNotBlank(valueStr)) {
                    String labelText = escapeHtml(fieldInfo.getLabel());
                    String valueText = escapeHtml(valueStr);
                    if ("severity".equals(key) || "severityLabel".equals(key)) {
                        String severityValue = "severity".equals(key) ? severity : valueStr;
                        String severityColor = getSeverityColor(severityValue);
                        metaInfo.add("<span style='color: #495057;'>" + labelText + "</span>: <span style='background-color: " + severityColor + "; color: #fff; padding: 2px 8px; border-radius: 4px; font-weight: 600; display: inline-block;'>" + valueText + "</span>");
                    } else {
                        metaInfo.add("<span style='color: #495057;'>" + labelText + "</span>: <span style='color: #6c757d; font-weight: 500;'>" + valueText + "</span>");
                    }
                }
            }
            if (!metaInfo.isEmpty()) {
                html.append("<div style='font-size: 10px; color: #808080; margin-bottom: 8px; font-family: SimSun, Microsoft YaHei, SimHei, Arial, sans-serif;'>").append(String.join(" <span style='color: #dee2e6; margin: 0 8px;'>|</span> ", metaInfo)).append("</div>");
            }
            for (FieldInfoVo fieldInfo : fieldInfos) {
                String key = fieldInfo.getKey();
                if ("title".equals(key)) {
                    continue;
                }
                if ("summary".equals(key) || "problemDescription".equals(key) || 
                    "fixSolution".equals(key) || "exampleCode".equals(key)) {
                    Object value = getFieldValue(item, key, cweMap, tagMap, userMap, knowledgeBaseMap, fieldFormats);
                    if (value != null && StringUtils.isNotBlank(String.valueOf(value))) {
                        html.append("<div style='margin-bottom: 14px; padding: 12px; background-color: #f8f9fa; border-radius: 4px; border-left: 3px solid #52c41a; font-family: SimSun, Microsoft YaHei, SimHei, Arial, sans-serif;'>");
                        html.append("<div style='font-weight: 600; color: #212529; font-size: 13px; margin-bottom: 6px; font-family: SimSun, Microsoft YaHei, SimHei, Arial, sans-serif;'>").append(escapeHtml(fieldInfo.getLabel())).append("</div>");
                        html.append("<div style='color: #495057; font-size: 12px; line-height: 1.7; white-space: pre-wrap; word-wrap: break-word; font-family: SimSun, Microsoft YaHei, SimHei, Arial, sans-serif;'>").append(escapeHtml(String.valueOf(value))).append("</div>");
                        html.append("</div>");
                    }
                }
            }
            html.append("</div>");
        }
        return html.toString();
    }
    private String generateTableFormatHtmlForPdf(List<KnowledgeItemVo> data, List<FieldInfoVo> fieldInfos, Map<String, String> fieldFormats) {
        List<Map<String, Object>> exportData = convertToMapList(data, 
            fieldInfos.stream().map(FieldInfoVo::getKey).collect(Collectors.toList()),
            extractExpandedFields(fieldInfos), fieldFormats);
        StringBuilder html = new StringBuilder("<table style='border-collapse: collapse; width: 100%; font-size: 12px; font-family: SimSun, Microsoft YaHei, SimHei, Arial, sans-serif;'>");
        html.append("<thead><tr style='background-color: #f5f5f5;'>");
        for (FieldInfoVo fieldInfo : fieldInfos) {
            html.append("<th style='padding: 10px 8px; border: 1px solid #c8c8c8; font-weight: 600; text-align: center; font-family: SimSun, Microsoft YaHei, SimHei, Arial, sans-serif;'>")
                .append(escapeHtml(fieldInfo.getLabel())).append("</th>");
        }
        html.append("</tr></thead><tbody>");
        for (Map<String, Object> rowData : exportData) {
            html.append("<tr>");
            for (FieldInfoVo fieldInfo : fieldInfos) {
                Object value = rowData.get(fieldInfo.getKey());
                String text = value != null ? String.valueOf(value) : "";
                if (text.length() > 100) {
                    text = text.substring(0, 100) + "...";
                }
                html.append("<td style='padding: 5px; border: 1px solid #c8c8c8; font-family: SimSun, Microsoft YaHei, SimHei, Arial, sans-serif;'>").append(escapeHtml(text)).append("</td>");
            }
            html.append("</tr>");
        }
        html.append("</tbody></table>");
        return html.toString();
    }
    private void initializeVelocity() {
        try {
            Properties p = new Properties();
            p.setProperty("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            p.setProperty(Velocity.INPUT_ENCODING, StandardCharsets.UTF_8.name());
            Velocity.init(p);
            log.info("Velocity引擎初始化完成");
        } catch (Exception e) {
            log.error("Velocity引擎初始化失败", e);
            throw new RuntimeException("Velocity引擎初始化失败", e);
        }
    }
    private String renderVelocityTemplate(String templatePath, VelocityContext context) throws IOException {
        try {
            Template template = Velocity.getTemplate(templatePath, StandardCharsets.UTF_8.name());
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            return writer.toString();
        } catch (Exception e) {
            log.error("Velocity模板渲染失败: {}", templatePath, e);
            throw new IOException("Velocity模板渲染失败: " + templatePath, e);
        }
    }
    private void convertHtmlToPdfWithOpenHtml(String html, OutputStream outputStream, boolean includeHeaderFooter, String headerText) throws IOException {
        log.info("PDF流式导出 - 开始使用OpenHTML to PDF转换HTML到PDF");
        try {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.useDefaultPageSize(210, 297, PdfRendererBuilder.PageSizeUnits.MM);
            //优先从项目resources/fonts目录加载字体（跨平台兼容）
            //字体配置：[资源路径, 字体族名称]
            //支持多字重：Regular(400), Medium(500), Bold(700), Light(300), Thin(100), DemiLight(350), Black(900)
            String[][] classpathFontConfigs = {
                {"fonts/NotoSansCJKsc-Regular.ttf", "Noto Sans CJK SC"},
                {"fonts/NotoSansCJKsc-Medium.ttf", "Noto Sans CJK SC"},
                {"fonts/NotoSansCJKsc-Bold.ttf", "Noto Sans CJK SC"},
                {"fonts/NotoSansCJKsc-Light.ttf", "Noto Sans CJK SC"},
                {"fonts/NotoSansCJKsc-Thin.ttf", "Noto Sans CJK SC"},
                {"fonts/NotoSansCJKsc-DemiLight.ttf", "Noto Sans CJK SC"},
                {"fonts/NotoSansCJKsc-Black.ttf", "Noto Sans CJK SC"},
                {"fonts/NotoSerifCJK-Regular.ttf", "Noto Serif CJK SC"},
                {"fonts/NotoSerifCJK-Bold.ttf", "Noto Serif CJK SC"},
                {"fonts/WenQuanYiZenHei.ttf", "WenQuanYi Zen Hei"},
                {"fonts/SimSun.ttf", "SimSun"},
                {"fonts/SimHei.ttf", "SimHei"},
                {"fonts/MicrosoftYaHei.ttf", "Microsoft YaHei"}
            };
            //系统字体路径作为回退（兼容性）
            String[][] systemFontConfigs = {
                {"/usr/share/fonts/truetype/msttcore/SimSun.ttf", "SimSun"},
                {"/usr/share/fonts/truetype/msttcore/SimHei.ttf", "SimHei"},
                {"/usr/share/fonts/truetype/wqy/wqy-zenhei.ttc", "WenQuanYi Zen Hei"},
                {"/usr/share/fonts/wenquanyi/wqy-zenhei/wqy-zenhei.ttc", "WenQuanYi Zen Hei"},
                {"/usr/share/fonts/opentype/noto/NotoSansCJK-Regular.ttc", "Noto Sans CJK SC"},
                {"/usr/share/fonts/opentype/noto/NotoSerifCJK-Regular.ttc", "Noto Serif CJK SC"},
                {"C:/Windows/Fonts/simsun.ttc", "SimSun"},
                {"C:/Windows/Fonts/simhei.ttf", "SimHei"},
                {"C:/Windows/Fonts/msyh.ttf", "Microsoft YaHei"}
            };
            List<String> registeredFonts = new ArrayList<>();
            Set<String> registeredFontKeys = new HashSet<>();
            Set<String> registeredFamilyNamesFromClasspath = new HashSet<>();
            //优先从classpath加载字体（支持多字重）
            List<java.io.File> tempFontFiles = new ArrayList<>();
            for (String[] fontConfig : classpathFontConfigs) {
                String resourcePath = fontConfig[0];
                String fontFamilyName = fontConfig[1];
                String fontKey = resourcePath + "|" + fontFamilyName;
                if (registeredFontKeys.contains(fontKey)) {
                    continue;
                }
                try {
                    Resource resource = new ClassPathResource(resourcePath);
                    if (resource.exists()) {
                        java.io.File fontFile;
                        try {
                            fontFile = resource.getFile();
                        } catch (Exception e) {
                            //jar包内资源，需要复制到临时文件
                            java.io.File tempFile = java.io.File.createTempFile("font_", "_" + new java.io.File(resourcePath).getName());
                            tempFile.deleteOnExit();
                            tempFontFiles.add(tempFile);
                            try (InputStream is = resource.getInputStream();
                                 java.io.FileOutputStream fos = new java.io.FileOutputStream(tempFile)) {
                                is.transferTo(fos);
                            }
                            fontFile = tempFile;
                            log.debug("PDF流式导出 - 从jar包内复制字体到临时文件: {}", tempFile.getAbsolutePath());
                        }
                        builder.useFont(fontFile, fontFamilyName);
                        registeredFonts.add(fontFamilyName + " (" + resourcePath + ")");
                        registeredFontKeys.add(fontKey);
                        registeredFamilyNamesFromClasspath.add(fontFamilyName);
                        log.info("PDF流式导出 - OpenHTML to PDF从classpath加载字体: {} (字体族: {})", resourcePath, fontFamilyName);
                    }
                } catch (Exception e) {
                    log.debug("PDF流式导出 - OpenHTML to PDF从classpath加载字体失败: {}", resourcePath, e);
                }
            }
            //回退到系统字体路径（仅当classpath中未找到该字体族时）
            for (String[] fontConfig : systemFontConfigs) {
                String fontPath = fontConfig[0];
                String fontFamilyName = fontConfig[1];
                if (registeredFamilyNamesFromClasspath.contains(fontFamilyName)) {
                    log.debug("PDF流式导出 - 跳过系统路径字体 {}，因为classpath中已注册字体族: {}", fontPath, fontFamilyName);
                    continue;
                }
                String fontKey = fontPath + "|" + fontFamilyName;
                if (registeredFontKeys.contains(fontKey)) {
                    continue;
                }
                try {
                    java.io.File fontFile = new java.io.File(fontPath);
                    if (fontFile.exists() && fontFile.isFile()) {
                        builder.useFont(fontFile, fontFamilyName);
                        registeredFonts.add(fontFamilyName + " (" + fontPath + ")");
                        registeredFontKeys.add(fontKey);
                        log.info("PDF流式导出 - OpenHTML to PDF从系统路径加载字体: {} (字体族: {})", fontPath, fontFamilyName);
                    }
                } catch (Exception e) {
                    log.debug("PDF流式导出 - OpenHTML to PDF从系统路径加载字体失败: {}", fontPath, e);
                }
            }
            if (registeredFonts.isEmpty()) {
                log.warn("PDF流式导出 - OpenHTML to PDF未找到中文字体，将使用默认字体");
            } else {
                log.info("PDF流式导出 - OpenHTML to PDF已注册字体: {}", String.join(", ", registeredFonts));
            }
            builder.run();
            log.info("PDF流式导出 - OpenHTML to PDF转换完成");
            outputStream.flush();
            log.info("PDF流式导出 - 输出流已刷新");
        } catch (Exception e) {
            log.error("PDF流式导出 - OpenHTML to PDF转换失败", e);
            throw new IOException("OpenHTML to PDF转换失败", e);
        }
    }
}
