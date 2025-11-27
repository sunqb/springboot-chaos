package com.chaos.manager.controller;

import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.role.RoleBo;
import com.chaos.service.entity.bo.role.RoleConditionBo;
import com.chaos.service.entity.vo.role.RoleVo;
import com.chaos.service.service.IRoleService;
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
 * 角色管理控制器
 *
 * @author chaos
 */
@Slf4j
@RestController
@RequestMapping("/role")
@Tag(name = "角色管理", description = "角色CRUD操作")
public class RoleController extends BaseController {

    @Resource
    private IRoleService roleService;

    // ==================== 标准CRUD接口 ====================

    @GetMapping("/list")
    @Operation(summary = "查询角色列表")
    public AjaxResult list(RoleConditionBo condition) {
        List<RoleVo> list = roleService.list(condition);
        return AjaxResult.success(list);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询角色列表")
    public AjaxResult page(RoleConditionBo condition) {
        PageInfo<RoleVo> page = roleService.page(condition);
        return AjaxResult.success(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询角色详情")
    public AjaxResult getById(@Parameter(description = "角色ID") @PathVariable Long id) {
        RoleVo role = roleService.getById(id);
        if (role == null) {
            return AjaxResult.fail("角色不存在");
        }
        return AjaxResult.success(role);
    }

    @PostMapping
    @Operation(summary = "新增角色")
    public AjaxResult add(@Valid @RequestBody RoleBo bo) {
        log.info("新增角色: {}", bo.getRoleName());
        return roleService.add(bo);
    }

    @PutMapping
    @Operation(summary = "更新角色")
    public AjaxResult update(@Valid @RequestBody RoleBo bo) {
        log.info("更新角色: {}", bo.getId());
        return roleService.update(bo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色")
    public AjaxResult delete(@Parameter(description = "角色ID") @PathVariable Long id) {
        log.info("删除角色: {}", id);
        return roleService.delete(id);
    }

    // ==================== 业务自定义接口 ====================

    @GetMapping("/all")
    @Operation(summary = "查询所有角色（下拉选项）")
    public AjaxResult getAllRoles() {
        List<RoleVo> list = roleService.getAllRoles();
        return AjaxResult.success(list);
    }

    @PutMapping("/updateLockStatus")
    @Operation(summary = "更新锁定状态")
    public AjaxResult updateLockStatus(
            @Parameter(description = "角色ID") @RequestParam Long id,
            @Parameter(description = "锁定状态") @RequestParam Integer isLocked) {
        log.info("更新锁定状态: {} -> {}", id, isLocked);
        return roleService.updateLockStatus(id, isLocked);
    }

    @PostMapping("/assignMenus")
    @Operation(summary = "分配菜单权限")
    public AjaxResult assignMenus(
            @Parameter(description = "角色ID") @RequestParam Long roleId,
            @Parameter(description = "菜单ID列表") @RequestBody List<Long> menuIds) {
        log.info("分配菜单: {} -> {}", roleId, menuIds);
        return roleService.assignMenus(roleId, menuIds);
    }
}
