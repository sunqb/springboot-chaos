package com.chaos.service.entity.dto.org;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 组织机构表
 *
 * @author chaos
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("p_organization")
@Schema(description = "组织机构实体")
public class OrganizationDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "父级ID")
    private Long parentId;

    @Schema(description = "上级组织机构ids（逗号分隔）")
    private String superiorIds;

    @Schema(description = "组织机构名称")
    private String name;

    @Schema(description = "组织机构简称")
    private String shortName;

    @Schema(description = "组织机构编码")
    private String orgCode;

    @Schema(description = "负责人")
    private String leader;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "状态：1-启用，2-禁用")
    private Integer state;

    @Schema(description = "是否统计：1-统计，2-不统计")
    private Integer isStatistics;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "生效开始时间")
    private LocalDateTime authStartTime;

    @Schema(description = "生效截止时间")
    private LocalDateTime authEndTime;

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
