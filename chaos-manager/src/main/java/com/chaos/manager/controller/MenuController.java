package com.chaos.manager.controller;

import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.menu.MenuBo;
import com.chaos.service.entity.vo.menu.MenuVo;
import com.chaos.service.service.IMenuService;
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
 */
@Slf4j
@RestController
@RequestMapping("/menu")
@Tag(name = "菜单管理", description = "菜单CRUD操作")
public class MenuController extends BaseController {

    @Resource
    private IMenuService menuService;

    @GetMapping("/tree")
    @Operation(summary = "查询菜单树")
    public AjaxResult tree() {
        List<MenuVo> tree = menuService.getMenuTree();
        return AjaxResult.success(tree);
    }

    @GetMapping("/list")
    @Operation(summary = "查询菜单列表")
    public AjaxResult list(
            @Parameter(description = "菜单名称") @RequestParam(required = false) String menuName,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        List<MenuVo> list = menuService.getMenuList(menuName, status);
        return AjaxResult.success(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询菜单详情")
    public AjaxResult getById(@Parameter(description = "菜单ID") @PathVariable Long id) {
        MenuVo menu = menuService.getMenuById(id);
        if (menu == null) {
            return AjaxResult.fail("菜单不存在");
        }
        return AjaxResult.success(menu);
    }

    @PostMapping
    @Operation(summary = "新增菜单")
    public AjaxResult add(@Valid @RequestBody MenuBo menuBo) {
        log.info("新增菜单: {}", menuBo.getMenuName());
        return menuService.addMenu(menuBo);
    }

    @PutMapping
    @Operation(summary = "更新菜单")
    public AjaxResult update(@Valid @RequestBody MenuBo menuBo) {
        log.info("更新菜单: {}", menuBo.getId());
        return menuService.updateMenu(menuBo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除菜单")
    public AjaxResult delete(@Parameter(description = "菜单ID") @PathVariable Long id) {
        log.info("删除菜单: {}", id);
        return menuService.deleteMenu(id);
    }
}
