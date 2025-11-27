package com.chaos.service.entity.bo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 菜单请求参数
 *
 * @author chaos
 */
@Data
@Schema(description = "菜单请求参数")
public class MenuBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单id")
    private Long id;

    @Schema(description = "菜单父id")
    private Long parentId;

    @NotBlank(message = "菜单名称不能为空")
    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "权限标识")
    private String permission;

    @Schema(description = "菜单状态：1-公开，2-不公开")
    private Integer state;

    @Schema(description = "菜单地址/路由")
    private String url;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "类型：0-目录，1-菜单，2-按钮")
    private Integer type;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "图标")
    private String icon;
}
