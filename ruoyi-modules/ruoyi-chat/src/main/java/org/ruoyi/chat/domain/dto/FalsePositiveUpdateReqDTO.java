package org.ruoyi.chat.domain.dto;

import lombok.Data;
import java.util.List;

@Data
public class FalsePositiveUpdateReqDTO {
    private List<Long> markIds;
    private List<Long> restoreIds;
    private Long taskId;
}
