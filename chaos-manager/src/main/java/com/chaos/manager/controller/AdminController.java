package com.chaos.manager.controller;

import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.admin.AdminBo;
import com.chaos.service.entity.bo.admin.AdminConditionBo;
import com.chaos.service.entity.vo.admin.AdminVo;
import com.chaos.service.service.IAdminService;
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
 * 管理员管理控制器
 *
 * @author chaos
 */
@Slf4j
@RestController
@RequestMapping("/admin")
@Tag(name = "管理员管理", description = "管理员CRUD操作")
public class AdminController extends BaseController {

    @Resource
    private IAdminService adminService;

    // ==================== 标准CRUD接口 ====================

    @GetMapping("/list")
    @Operation(summary = "查询管理员列表")
    public AjaxResult list(AdminConditionBo condition) {
        List<AdminVo> list = adminService.list(condition);
        return AjaxResult.success(list);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询管理员列表")
    public AjaxResult page(AdminConditionBo condition) {
        PageInfo<AdminVo> page = adminService.page(condition);
        return AjaxResult.success(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询管理员详情")
    public AjaxResult getById(@Parameter(description = "管理员ID") @PathVariable Long id) {
        AdminVo admin = adminService.getById(id);
        if (admin == null) {
            return AjaxResult.fail("管理员不存在");
        }
        return AjaxResult.success(admin);
    }

    @PostMapping
    @Operation(summary = "新增管理员")
    public AjaxResult add(@Valid @RequestBody AdminBo bo) {
        log.info("新增管理员: {}", bo.getAccountName());
        return adminService.add(bo);
    }

    @PutMapping
    @Operation(summary = "更新管理员")
    public AjaxResult update(@Valid @RequestBody AdminBo bo) {
        log.info("更新管理员: {}", bo.getId());
        return adminService.update(bo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除管理员")
    public AjaxResult delete(@Parameter(description = "管理员ID") @PathVariable Long id) {
        log.info("删除管理员: {}", id);
        return adminService.delete(id);
    }

    // ==================== 业务自定义接口 ====================

    @PutMapping("/resetPassword")
    @Operation(summary = "重置密码")
    public AjaxResult resetPassword(
            @Parameter(description = "管理员ID") @RequestParam Long id,
            @Parameter(description = "新密码") @RequestParam(required = false) String newPassword) {
        log.info("重置密码: {}", id);
        return adminService.resetPassword(id, newPassword);
    }

    @PutMapping("/updateLockStatus")
    @Operation(summary = "更新锁定状态")
    public AjaxResult updateLockStatus(
            @Parameter(description = "管理员ID") @RequestParam Long id,
            @Parameter(description = "锁定状态：0-未锁定，1-锁定") @RequestParam Integer isLocked) {
        log.info("更新锁定状态: {} -> {}", id, isLocked);
        return adminService.updateLockStatus(id, isLocked);
    }
}
