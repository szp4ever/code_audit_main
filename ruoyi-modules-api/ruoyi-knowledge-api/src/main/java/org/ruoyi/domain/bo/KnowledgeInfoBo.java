package org.ruoyi.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.common.core.validate.AddGroup;
import org.ruoyi.common.core.validate.EditGroup;
import org.ruoyi.core.domain.BaseEntity;
import org.ruoyi.domain.KnowledgeInfo;

import java.util.List;

/**
 * 知识库业务对象 knowledge_info
 *
 * @author ageerle
 * @date 2025-04-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = KnowledgeInfo.class, reverseConvertGenerate = false)
public class KnowledgeInfoBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 知识库ID
     */
    @NotBlank(message = "知识库ID不能为空", groups = {EditGroup.class})
    private String kid;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空", groups = {EditGroup.class})
    private Long uid;

    /**
     * 知识库名称
     */
    @NotBlank(message = "知识库名称不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 1, max = 50, message = "名称长度应在 1-50 个字符之间", groups = {AddGroup.class, EditGroup.class})
    private String kname;

    /**
     * 是否公开知识库（0 否 1是）
     */
    @NotNull(message = "是否公开知识库（0 否 1是）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer share;

    /**
     * 描述
     */
    @Size(max = 1000, message = "描述长度不能超过 1000 个字符", groups = {AddGroup.class, EditGroup.class})
    private String description;

    /**
     * 知识库分类（用途类型，如：漏洞修复、编码规范、安全标准等）
     */
    private String category;

    /**
     * 占用字节数（所有知识条目内容的总大小）
     */
    private Long dataSize;

    /**
     * 知识条目数量
     */
    private Integer itemCount;

    /**
     * 知识片段数量
     */
    private Integer fragmentCount;

    /**
     * 知识分隔符
     */
    @Size(max = 255, message = "分隔符长度不能超过 255 个字符", groups = {AddGroup.class, EditGroup.class})
    private String knowledgeSeparator;

    /**
     * 提问分隔符
     */
    @Size(max = 255, message = "分隔符长度不能超过 255 个字符", groups = {AddGroup.class, EditGroup.class})
    private String questionSeparator;

    /**
     * 重叠字符数
     */
    @Min(value = 0, message = "重叠字符数不能小于 0", groups = {AddGroup.class, EditGroup.class})
    @Max(value = 200, message = "重叠字符数不能超过 200", groups = {AddGroup.class, EditGroup.class})
    private Long overlapChar;

    /**
     * 知识库中检索的条数
     */
    @NotNull(message = "知识库中检索的条数不能为空", groups = {AddGroup.class, EditGroup.class})
    @Min(value = 1, message = "检索条数不能小于 1", groups = {AddGroup.class, EditGroup.class})
    @Max(value = 10, message = "检索条数不能超过 10", groups = {AddGroup.class, EditGroup.class})
    private Long retrieveLimit;

    /**
     * 文本块大小
     */
    @NotNull(message = "文本块大小不能为空", groups = {AddGroup.class, EditGroup.class})
    @Min(value = 100, message = "文本块大小不能小于 100", groups = {AddGroup.class, EditGroup.class})
    @Max(value = 2000, message = "文本块大小不能超过 2000", groups = {AddGroup.class, EditGroup.class})
    private Long textBlockSize;

    /**
     * 向量库模型名称
     */
    @NotBlank(message = "向量库不能为空", groups = {AddGroup.class, EditGroup.class})
    private String vectorModelName;

    /**
     * 向量化模型名称
     */
    private Long embeddingModelId;

    /**
     * 向量化模型名称
     */
    private String embeddingModelName;


    /**
     * 系统提示词
     */
    @Size(max = 255, message = "系统提示词长度不能超过 255 个字符", groups = {AddGroup.class, EditGroup.class})
    private String systemPrompt;

    /**
     * 备注
     */
    private String remark;

    // ========== 列表查询筛选参数（不参与验证） ==========

    /**
     * 分类筛选（多选，数组形式）
     */
    private List<String> categories;

    /**
     * 创建人筛选（多选，数组形式）
     */
    private List<String> createBys;

    /**
     * 创建部门筛选（多选，数组形式）
     */
    private List<Long> createDepts;

    /**
     * 条目数最小值
     */
    private Integer itemCountMin;

    /**
     * 条目数最大值
     */
    private Integer itemCountMax;

    /**
     * 片段数最小值
     */
    private Integer fragmentCountMin;

    /**
     * 片段数最大值
     */
    private Integer fragmentCountMax;

    /**
     * 存储大小最小值（单位：字节）
     */
    private Long dataSizeMin;

    /**
     * 存储大小最大值（单位：字节）
     */
    private Long dataSizeMax;

    /**
     * 创建时间起始（ISO 8601格式字符串）
     */
    private String createTimeStart;

    /**
     * 创建时间结束（ISO 8601格式字符串）
     */
    private String createTimeEnd;

    /**
     * 更新时间起始（ISO 8601格式字符串）
     */
    private String updateTimeStart;

    /**
     * 更新时间结束（ISO 8601格式字符串）
     */
    private String updateTimeEnd;

    /**
     * 排序字段（create_time/update_time/item_count/fragment_count/data_size/kname/category）
     */
    private String orderBy;

    /**
     * 排序方向（asc/desc）
     */
    private String order;

    /**
     * 所有权类型筛选（mine-我创建的, assigned-分配给我的, all-全部）
     * 用于区分知识库的来源：用户自己创建的 vs 通过角色分配获得的
     */
    private String ownershipType;

    /**
     * 搜索关键词（用于多字段搜索：kname + description）
     */
    private String searchKeyword;

}
