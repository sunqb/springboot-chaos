package com.chaos.service.entity.bo.org;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 组织机构请求参数
 */
@Data
@Schema(description = "组织机构请求参数")
public class OrganizationBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "父级ID")
    private Long parentId;

    @NotBlank(message = "组织名称不能为空")
    @Schema(description = "组织名称")
    private String orgName;

    @Schema(description = "组织简称")
    private String shortName;

    @Schema(description = "组织编码")
    private String orgCode;

    @Schema(description = "负责人")
    private String leader;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态：0-禁用，1-正常")
    private Integer status;
}
