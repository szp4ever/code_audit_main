package org.ruoyi.domain.bo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ageer
 */
@Data
public class KnowledgeInfoUploadBo {

    private String kid;

    private MultipartFile file;

    /**
     * 是否自动创建知识条目（默认 true）
     */
    private Boolean autoCreateItems;

    /**
     * 是否启用相似度匹配（默认 false）
     */
    private Boolean autoClassify;

}
