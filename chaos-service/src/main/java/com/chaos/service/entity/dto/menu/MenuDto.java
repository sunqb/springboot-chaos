package com.chaos.service.entity.dto.menu;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜单表
 *
 * @author chaos
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("p_menu")
@Schema(description = "菜单实体")
public class MenuDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "菜单id")
    private Long id;

    @Schema(description = "菜单父id (一级菜单为0)")
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
