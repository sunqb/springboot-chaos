package com.chaos.service.entity.bo.attachment;

import com.chaos.common.vo.BaseCondition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 附件查询条件
 *
 * @author chaos
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "附件查询条件")
public class AttachmentConditionBo extends BaseCondition {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "原始文件名")
    private String originName;

    @Schema(description = "文件类型")
    private String suffix;

    @Schema(description = "状态：0-禁用，1-启用")
    private Integer state;
}
