package com.chaos.service.entity.bo.attachment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 附件请求参数
 *
 * @author chaos
 */
@Data
@Schema(description = "附件请求参数")
public class AttachmentBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "oid")
    private String oid;

    @Schema(description = "原始文件名")
    private String originName;

    @Schema(description = "新文件名")
    private String newName;

    @Schema(description = "文件后缀")
    private String suffix;

    @Schema(description = "文件大小")
    private Long size;

    @Schema(description = "原始路径")
    private String originPath;

    @Schema(description = "访问路径")
    private String viewPath;

    @Schema(description = "状态：0-禁用，1-启用")
    private Integer state;

    @Schema(description = "备注")
    private String remark;
}
