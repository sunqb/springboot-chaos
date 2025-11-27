package com.chaos.service.entity.dto.attachment;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 附件表
 *
 * @author chaos
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("p_attachment")
@Schema(description = "附件实体")
public class AttachmentDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "id")
    private Long id;

    @Schema(description = "文件oid")
    private String oid;

    @Schema(description = "原文件名")
    private String originalName;

    @Schema(description = "新文件名")
    private String newName;

    @Schema(description = "后缀名")
    private String suffix;

    @Schema(description = "大小（字节）")
    private Long size;

    @Schema(description = "封面")
    private String cover;

    @Schema(description = "原始文件路径")
    private String originPath;

    @Schema(description = "预览路径")
    private String viewPath;

    @Schema(description = "时长（毫秒）")
    private Long duration;

    @Schema(description = "上传状态：1-上传中，2-上传成功，3-上传失败")
    private Integer uploadState;

    @Schema(description = "状态：1-待处理，2-处理中，3-处理成功，4-处理失败")
    private Integer state;

    @Schema(description = "备注")
    private String note;

    @Schema(description = "业务类型")
    private String bizType;

    @Schema(description = "业务ID")
    private Long bizId;

    @Schema(description = "是否删除：0-正常，1-删除")
    @TableLogic
    private Integer isDelete;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建人")
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新人")
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
