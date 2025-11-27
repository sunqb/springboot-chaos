package com.chaos.manager.controller;

import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.admin.AdminBo;
import com.chaos.service.entity.vo.admin.AdminVo;
import com.chaos.service.service.IAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/admin")
@Tag(name = "管理员管理", description = "管理员CRUD操作")
public class AdminController extends BaseController {

    @Resource
    private IAdminService adminService;

    @GetMapping("/list")
    @Operation(summary = "查询管理员列表")
    public AjaxResult list(
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "真实姓名") @RequestParam(required = false) String realName,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        List<AdminVo> list = adminService.getAdminList(username, realName, status);
        return AjaxResult.success(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询管理员详情")
    public AjaxResult getById(@Parameter(description = "管理员ID") @PathVariable Long id) {
        AdminVo admin = adminService.getAdminById(id);
        if (admin == null) {
            return AjaxResult.fail("管理员不存在");
        }
        return AjaxResult.success(admin);
    }

    @PostMapping
    @Operation(summary = "新增管理员")
    public AjaxResult add(@Valid @RequestBody AdminBo adminBo) {
        log.info("新增管理员: {}", adminBo.getUsername());
        return adminService.addAdmin(adminBo);
    }

    @PutMapping
    @Operation(summary = "更新管理员")
    public AjaxResult update(@Valid @RequestBody AdminBo adminBo) {
        log.info("更新管理员: {}", adminBo.getId());
        return adminService.updateAdmin(adminBo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除管理员")
    public AjaxResult delete(@Parameter(description = "管理员ID") @PathVariable Long id) {
        log.info("删除管理员: {}", id);
        return adminService.deleteAdmin(id);
    }

    @PutMapping("/resetPassword")
    @Operation(summary = "重置密码")
    public AjaxResult resetPassword(
            @Parameter(description = "管理员ID") @RequestParam Long id,
            @Parameter(description = "新密码") @RequestParam(required = false) String newPassword) {
        log.info("重置密码: {}", id);
        return adminService.resetPassword(id, newPassword);
    }

    @PutMapping("/updateStatus")
    @Operation(summary = "更新状态")
    public AjaxResult updateStatus(
            @Parameter(description = "管理员ID") @RequestParam Long id,
            @Parameter(description = "状态") @RequestParam Integer status) {
        log.info("更新状态: {} -> {}", id, status);
        return adminService.updateStatus(id, status);
    }
}
