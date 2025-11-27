package com.chaos.service.entity.vo.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单响应对象
 *
 * @author chaos
 */
@Data
@Schema(description = "菜单响应对象")
public class MenuVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单id")
    private Long id;

    @Schema(description = "菜单父id")
    private Long parentId;

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

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "子菜单")
    private List<MenuVo> children;
}
