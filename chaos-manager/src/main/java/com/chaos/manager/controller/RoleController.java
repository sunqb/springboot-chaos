package com.chaos.manager.controller;

import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.role.RoleBo;
import com.chaos.service.entity.vo.role.RoleVo;
import com.chaos.service.service.IRoleService;
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
 */
@Slf4j
@RestController
@RequestMapping("/role")
@Tag(name = "角色管理", description = "角色CRUD操作")
public class RoleController extends BaseController {

    @Resource
    private IRoleService roleService;

    @GetMapping("/list")
    @Operation(summary = "查询角色列表")
    public AjaxResult list(
            @Parameter(description = "角色名称") @RequestParam(required = false) String roleName,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        List<RoleVo> list = roleService.getRoleList(roleName, status);
        return AjaxResult.success(list);
    }

    @GetMapping("/all")
    @Operation(summary = "查询所有角色（下拉选项）")
    public AjaxResult getAllRoles() {
        List<RoleVo> list = roleService.getAllRoles();
        return AjaxResult.success(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询角色详情")
    public AjaxResult getById(@Parameter(description = "角色ID") @PathVariable Long id) {
        RoleVo role = roleService.getRoleById(id);
        if (role == null) {
            return AjaxResult.fail("角色不存在");
        }
        return AjaxResult.success(role);
    }

    @PostMapping
    @Operation(summary = "新增角色")
    public AjaxResult add(@Valid @RequestBody RoleBo roleBo) {
        log.info("新增角色: {}", roleBo.getRoleName());
        return roleService.addRole(roleBo);
    }

    @PutMapping
    @Operation(summary = "更新角色")
    public AjaxResult update(@Valid @RequestBody RoleBo roleBo) {
        log.info("更新角色: {}", roleBo.getId());
        return roleService.updateRole(roleBo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色")
    public AjaxResult delete(@Parameter(description = "角色ID") @PathVariable Long id) {
        log.info("删除角色: {}", id);
        return roleService.deleteRole(id);
    }

    @PutMapping("/updateStatus")
    @Operation(summary = "更新状态")
    public AjaxResult updateStatus(
            @Parameter(description = "角色ID") @RequestParam Long id,
            @Parameter(description = "状态") @RequestParam Integer status) {
        log.info("更新状态: {} -> {}", id, status);
        return roleService.updateStatus(id, status);
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
