package org.ruoyi.chat.controller.knowledge;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.chat.config.KnowledgeRoleConfig;
import org.ruoyi.common.core.domain.R;
import org.ruoyi.common.core.validate.AddGroup;
import org.ruoyi.common.excel.utils.ExcelUtil;
import org.ruoyi.common.log.annotation.Log;
import org.ruoyi.common.log.enums.BusinessType;
import org.ruoyi.common.satoken.utils.LoginHelper;
import org.ruoyi.common.web.core.BaseController;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.domain.bo.KnowledgeAttachBo;
import org.ruoyi.domain.bo.KnowledgeFragmentBo;
import org.ruoyi.domain.bo.KnowledgeInfoBo;
import org.ruoyi.domain.bo.KnowledgeInfoUploadBo;
import org.ruoyi.domain.vo.KnowledgeAttachVo;
import org.ruoyi.domain.vo.KnowledgeFragmentPageVo;
import org.ruoyi.domain.vo.KnowledgeFragmentVo;
import org.ruoyi.domain.vo.KnowledgeInfoVo;
import org.ruoyi.service.IKnowledgeAttachService;
import org.ruoyi.service.IKnowledgeFragmentService;
import org.ruoyi.service.IKnowledgeInfoService;
import cn.hutool.core.collection.CollUtil;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * 知识库管理
 *
 * @author ageerle
 * @date 2025-05-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/knowledge")
@Slf4j
public class KnowledgeController extends BaseController {

    private final IKnowledgeInfoService knowledgeInfoService;

    private final IKnowledgeAttachService attachService;

    private final IKnowledgeFragmentService fragmentService;

    private final KnowledgeRoleConfig knowledgeRoleConfig;

    /**
     * 根据用户信息查询本地知识库
     */
    @GetMapping("/list")
    public TableDataInfo<KnowledgeInfoVo> list(KnowledgeInfoBo bo, PageQuery pageQuery) {
        if (!StpUtil.isLogin()) {
            throw new SecurityException("请先去登录!");
        }
        if (!Objects.equals(LoginHelper.getUserId(), 1L)) {
            bo.setUid(LoginHelper.getUserId());
        }
        return knowledgeInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 根据用户信息及知识库角色查询本地知识库
     */
    @GetMapping("/listByRole")
    public TableDataInfo<KnowledgeInfoVo> listByRole(KnowledgeInfoBo bo, PageQuery pageQuery) {
        if (!StpUtil.isLogin()) {
            throw new SecurityException("请先去登录!");
        }

        bo.setUid(LoginHelper.getUserId());
        
        if (!knowledgeRoleConfig.getEnable()) {
            return knowledgeInfoService.queryPageList(bo, pageQuery);
        } else {
            return knowledgeInfoService.queryPageListByRole(bo, pageQuery);
        }
    }

    /**
     * 新增知识库
     */
    @Log(title = "知识库", businessType = BusinessType.INSERT)
    @PostMapping("/save")
    public R<Void> save(@Validated(AddGroup.class) @RequestBody KnowledgeInfoBo bo) {
        knowledgeInfoService.saveOne(bo);
        return R.ok();
    }

    /**
     * 删除知识库
     */
    @Log(title = "知识库", businessType = BusinessType.DELETE)
    @PostMapping("/remove/{id}")
    public R<Void> remove(@NotNull(message = "主键不能为空")
                         @PathVariable String id) {
        knowledgeInfoService.removeKnowledge(id);
        return R.ok();
    }

    /**
     * 修改知识库
     */
    @Log(title = "知识库", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public R<Void> update(@Validated(AddGroup.class) @RequestBody KnowledgeInfoBo bo) {
        knowledgeInfoService.updateByBo(bo);
        return R.ok();
    }

    /**
     * 查询知识库详情
     */
    @GetMapping("/info/{id}")
    public R<KnowledgeInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        Long longId;
        try {
            longId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return R.fail("主键格式错误");
        }
        return R.ok(knowledgeInfoService.queryById(longId));
    }

    /**
     * 上传知识库附件
     * 使用@RequestParam绑定multipart/form-data参数（包括MultipartFile）
     * 对于简单的FormData上传（key-value pairs + file），@RequestParam是标准做法
     * 参考：ChatController.audio() 和 uploadChunk() 的实现
     */
    @PostMapping(value = "/attach/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<KnowledgeAttachVo> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("kid") String kid,
            @RequestParam(value = "autoCreateItems", required = false) Boolean autoCreateItems,
            @RequestParam(value = "autoClassify", required = false) Boolean autoClassify) throws Exception {
        
        // 构建BO对象
        KnowledgeInfoUploadBo bo = new KnowledgeInfoUploadBo();
        bo.setFile(file);
        bo.setKid(kid);
        bo.setAutoCreateItems(autoCreateItems);
        bo.setAutoClassify(autoClassify);
        
        // 先创建附件记录和processId（同步，快速完成）
        KnowledgeAttachVo vo = knowledgeInfoService.uploadAndCreateAttach(bo);
        
        // 异步执行后续处理（解析、分块、匹配、创建条目、向量化等）
        knowledgeInfoService.processAttachAsync(vo.getId(), vo.getDocId(), kid, autoCreateItems, autoClassify);
        
        // 立即返回响应，包含attachId和processId，让前端可以开始轮询进度
        return R.ok(vo);
    }

    /**
     * 分片上传接口（前端使用，后端暂不支持分片，直接返回成功）
     * 注意：当前实现中，分片上传功能在前端实现，后端统一接收完整文件
     * 如需实现真正的分片上传，需要添加临时存储和合并逻辑
     */
    @PostMapping(value = "/attach/upload-chunk")
    public R<Void> uploadChunk(
            @RequestParam("file") MultipartFile chunk,
            @RequestParam("kid") String kid,
            @RequestParam("chunkIndex") Integer chunkIndex,
            @RequestParam("totalChunks") Integer totalChunks,
            @RequestParam("fileName") String fileName,
            @RequestParam("fileSize") Long fileSize,
            @RequestParam(value = "autoCreateItems", required = false) Boolean autoCreateItems,
            @RequestParam(value = "autoClassify", required = false) Boolean autoClassify) {
        // TODO: 实现分片上传逻辑
        // 1. 将分片保存到临时存储
        // 2. 返回成功，等待所有分片上传完成后调用合并接口
        return R.ok();
    }

    /**
     * 合并分片接口（前端使用，后端暂不支持分片，直接返回失败提示使用普通上传）
     * 注意：当前实现中，分片上传功能在前端实现，后端统一接收完整文件
     * 如需实现真正的分片上传，需要添加临时存储和合并逻辑
     */
    @PostMapping(value = "/attach/merge-chunks")
    public R<KnowledgeAttachVo> mergeChunks(
            @RequestParam("fileName") String fileName,
            @RequestParam("kid") String kid,
            @RequestParam("totalChunks") Integer totalChunks,
            @RequestParam(value = "autoCreateItems", required = false) Boolean autoCreateItems,
            @RequestParam(value = "autoClassify", required = false) Boolean autoClassify) {
        // TODO: 实现分片合并逻辑
        // 1. 从临时存储读取所有分片
        // 2. 合并分片为完整文件
        // 3. 调用 storeContent 处理文件
        // 4. 清理临时存储
        return R.fail("分片上传功能暂未实现，请使用普通上传方式");
    }

    /**
     * 获取知识库附件详细信息
     *
     * @param id 主键
     */
    @GetMapping("attach/info/{id}")
    public R<KnowledgeAttachVo> getAttachInfo(@NotNull(message = "主键不能为空")
                                              @PathVariable Long id) {
        return R.ok(attachService.queryById(id));
    }

    /**
     * 删除知识库附件（通过docId）
     */
    @PostMapping("attach/remove/{docId}")
    public R<Void> removeAttach(@NotEmpty(message = "文档ID不能为空")
                                @PathVariable String docId) {
        attachService.removeKnowledgeAttach(docId);
        return R.ok();
    }

    /**
     * 删除知识库附件（通过processId）
     * 用于上传过程中删除任务，此时可能还没有docId
     */
    @PostMapping("attach/remove-by-process/{processId}")
    public R<Void> removeAttachByProcessId(@NotEmpty(message = "处理任务ID不能为空")
                                           @PathVariable String processId) {
        attachService.removeKnowledgeAttachByProcessId(processId);
        return R.ok();
    }

    /**
     * 删除知识库附件（通过kid和docName）
     * 最保险的删除方式，因为文件名是用户最直观的标识，且始终存在于任务对象中
     */
    @PostMapping("attach/remove-by-name")
    public R<Void> removeAttachByKidAndName(@RequestParam("kid") @NotEmpty(message = "知识库ID不能为空") String kid,
                                            @RequestParam("docName") @NotEmpty(message = "文档名称不能为空") String docName) {
        attachService.removeKnowledgeAttachByKidAndName(kid, docName);
        return R.ok();
    }

    /**
     * 重新处理知识库附件
     */
    @PostMapping("attach/reprocess/{docId}")
    public R<Void> reprocessAttach(@NotEmpty(message = "文档ID不能为空")
                                  @PathVariable String docId) {
        attachService.reprocessAttach(docId);
        return R.ok();
    }

    /**
     * 获取附件条目数量分布统计（用于智能分箱）
     */
    @PostMapping("/attach/item-count-distribution")
    public R<List<Integer>> getItemCountDistribution(@RequestBody KnowledgeAttachBo bo) {
        List<Integer> distribution = attachService.getItemCountDistribution(bo);
        return R.ok(distribution);
    }

    /**
     * 获取附件分面统计（筛选选项和计数）
     */
    @PostMapping("/attach/facet-stats")
    public R<org.ruoyi.domain.vo.AttachFacetStatsVo> getFacetStats(@RequestBody KnowledgeAttachBo bo) {
        org.ruoyi.domain.vo.AttachFacetStatsVo stats = attachService.getFacetStats(bo);
        return R.ok(stats);
    }

    /**
     * 批量查询片段内容
     * 基于LLM与状态改革设计文档 v1.0
     * 用于审阅页面，需要包含未完成处理的片段
     */
    @PostMapping("/fragment/batch")
    public R<List<KnowledgeFragmentVo>> getFragmentBatch(
            @RequestBody List<org.ruoyi.domain.bo.FragmentBatchQueryBo> queries) {
        List<KnowledgeFragmentVo> fragments = new java.util.ArrayList<>();
        for (org.ruoyi.domain.bo.FragmentBatchQueryBo query : queries) {
            KnowledgeFragmentBo bo = new KnowledgeFragmentBo();
            bo.setDocId(query.getDocId());
            if (query.getIdx() != null) {
                bo.setIdx(query.getIdx().longValue());
            }
            //审阅页面需要包含未完成处理的片段
            bo.setIncludeIncomplete(true);
            List<KnowledgeFragmentVo> list = fragmentService.queryList(bo);
            if (CollUtil.isNotEmpty(list)) {
                fragments.addAll(list);
            }
        }
        return R.ok(fragments);
    }

    /**
     * 下载知识库附件
     */
    @GetMapping("attach/download/{docId}")
    public void downloadAttach(@NotEmpty(message = "文档ID不能为空")
                              @PathVariable String docId,
                              HttpServletResponse response) throws java.io.IOException {
        KnowledgeAttachBo attachBo = new KnowledgeAttachBo();
        attachBo.setDocId(docId);
        List<KnowledgeAttachVo> attachList = attachService.queryList(attachBo);
        if (attachList.isEmpty()) {
            throw new org.ruoyi.common.core.exception.ServiceException("附件不存在");
        }
        KnowledgeAttachVo attach = attachList.get(0);
        attachService.downloadAttach(attach.getId(), response);
    }

    /**
     * 查询知识库附件列表
     */
    @GetMapping("attach/list")
    public TableDataInfo<KnowledgeAttachVo> getAttachList(KnowledgeAttachBo bo, PageQuery pageQuery) {
        return attachService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询知识库片段列表
     */
    @GetMapping("fragment/list")
    public TableDataInfo<KnowledgeFragmentVo> getFragmentList(KnowledgeFragmentBo bo, PageQuery pageQuery) {
        return fragmentService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询知识条目下的片段列表（带分面统计）
     */
    @PostMapping("fragment/list-by-item")
    public R<KnowledgeFragmentPageVo> getFragmentListByItem(@RequestBody KnowledgeFragmentBo bo, PageQuery pageQuery) {
        KnowledgeFragmentPageVo result = fragmentService.queryPageListWithFacetStats(bo, pageQuery);
        return R.ok(result);
    }

    /**
     * 刷新所有知识库的统计字段（条目数、片段数、存储大小）
     * 不更新update_time字段
     */
    @PostMapping("/refresh-statistics")
    public R<Void> refreshStatistics() {
        knowledgeInfoService.refreshAllKnowledgeStatistics();
        return R.ok();
    }
}
