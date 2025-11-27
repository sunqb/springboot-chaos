package com.chaos.service.entity.bo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 菜单请求参数
 */
@Data
@Schema(description = "菜单请求参数")
public class MenuBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "父菜单ID")
    private Long parentId;

    @NotBlank(message = "菜单名称不能为空")
    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "菜单类型：0-目录，1-菜单，2-按钮")
    private Integer menuType;

    @Schema(description = "权限标识")
    private String permission;

    @Schema(description = "路由地址")
    private String path;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "是否可见：0-隐藏，1-显示")
    private Integer visible;

    @Schema(description = "状态：0-禁用，1-正常")
    private Integer status;
}
