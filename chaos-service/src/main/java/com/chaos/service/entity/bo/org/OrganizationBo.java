package com.chaos.service.entity.bo.org;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 组织机构请求参数
 *
 * @author chaos
 */
@Data
@Schema(description = "组织机构请求参数")
public class OrganizationBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "父级ID")
    private Long parentId;

    @NotBlank(message = "组织名称不能为空")
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
}
