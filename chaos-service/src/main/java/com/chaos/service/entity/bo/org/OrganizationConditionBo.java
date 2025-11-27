package com.chaos.service.entity.bo.org;

import com.chaos.common.vo.BaseCondition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 组织机构查询条件
 *
 * @author chaos
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "组织机构查询条件")
public class OrganizationConditionBo extends BaseCondition {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "组织名称")
    private String name;

    @Schema(description = "组织编码")
    private String orgCode;

    @Schema(description = "状态：0-禁用，1-启用")
    private Integer state;

    @Schema(description = "父组织ID")
    private Long parentId;
}
