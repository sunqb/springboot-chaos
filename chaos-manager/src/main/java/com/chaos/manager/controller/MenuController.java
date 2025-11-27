package com.chaos.manager.controller;

import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.menu.MenuBo;
import com.chaos.service.entity.bo.menu.MenuConditionBo;
import com.chaos.service.entity.vo.menu.MenuVo;
import com.chaos.service.service.IMenuService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理控制器
 *
 * @author chaos
 */
@Slf4j
@RestController
@RequestMapping("/menu")
@Tag(name = "菜单管理", description = "菜单CRUD操作")
public class MenuController extends BaseController {

    @Resource
    private IMenuService menuService;

    // ==================== 标准CRUD接口 ====================

    @GetMapping("/list")
    @Operation(summary = "查询菜单列表")
    public AjaxResult list(MenuConditionBo condition) {
        List<MenuVo> list = menuService.list(condition);
        return AjaxResult.success(list);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询菜单列表")
    public AjaxResult page(MenuConditionBo condition) {
        PageInfo<MenuVo> page = menuService.page(condition);
        return AjaxResult.success(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询菜单详情")
    public AjaxResult getById(@Parameter(description = "菜单ID") @PathVariable Long id) {
        MenuVo menu = menuService.getById(id);
        if (menu == null) {
            return AjaxResult.fail("菜单不存在");
        }
        return AjaxResult.success(menu);
    }

    @PostMapping
    @Operation(summary = "新增菜单")
    public AjaxResult add(@Valid @RequestBody MenuBo bo) {
        log.info("新增菜单: {}", bo.getMenuName());
        return menuService.add(bo);
    }

    @PutMapping
    @Operation(summary = "更新菜单")
    public AjaxResult update(@Valid @RequestBody MenuBo bo) {
        log.info("更新菜单: {}", bo.getId());
        return menuService.update(bo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除菜单")
    public AjaxResult delete(@Parameter(description = "菜单ID") @PathVariable Long id) {
        log.info("删除菜单: {}", id);
        return menuService.delete(id);
    }

    // ==================== 业务自定义接口 ====================

    @GetMapping("/tree")
    @Operation(summary = "查询菜单树")
    public AjaxResult tree() {
        List<MenuVo> tree = menuService.getMenuTree();
        return AjaxResult.success(tree);
    }
}
