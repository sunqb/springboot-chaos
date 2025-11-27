package com.chaos.service.entity.bo.menu;

import com.chaos.common.vo.BaseCondition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 菜单查询条件
 *
 * @author chaos
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "菜单查询条件")
public class MenuConditionBo extends BaseCondition {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "菜单类型：M-目录，C-菜单，F-按钮")
    private String type;

    @Schema(description = "状态：0-禁用，1-启用")
    private Integer state;

    @Schema(description = "父菜单ID")
    private Long parentId;
}
