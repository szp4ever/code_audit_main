package org.ruoyi.chat.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


import java.io.Serializable;

/**
 * 代码规范检查通过率实体
 *
 * @author ruoyi
 */
@Data
public class CodeStandardPassRate{
    /**
     * 通过的代码规范检查数量
     */
    private Integer passed;

    /**
     * 未通过的代码规范检查数量
     */
    private Integer failed;
}