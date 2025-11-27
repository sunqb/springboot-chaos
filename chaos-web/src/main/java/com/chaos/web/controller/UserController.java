package com.chaos.web.controller;

import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.user.UserBo;
import com.chaos.service.entity.bo.user.UserConditionBo;
import com.chaos.service.entity.vo.user.UserVo;
import com.chaos.service.service.IUserService;
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
 * 用户管理控制器
 *
 * @author chaos
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户CRUD操作")
public class UserController extends BaseController {

    @Resource
    private IUserService userService;

    // ==================== 标准CRUD接口 ====================

    @GetMapping("/list")
    @Operation(summary = "查询用户列表")
    public AjaxResult list(UserConditionBo condition) {
        List<UserVo> list = userService.list(condition);
        return AjaxResult.success(list);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询用户列表")
    public AjaxResult page(UserConditionBo condition) {
        PageInfo<UserVo> page = userService.page(condition);
        return AjaxResult.success(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询用户详情")
    public AjaxResult getById(@Parameter(description = "用户ID") @PathVariable Long id) {
        UserVo user = userService.getById(id);
        if (user == null) {
            return AjaxResult.fail("用户不存在");
        }
        return AjaxResult.success(user);
    }

    @PostMapping
    @Operation(summary = "新增用户")
    public AjaxResult add(@Valid @RequestBody UserBo bo) {
        log.info("新增用户: {}", bo.getAccount());
        return userService.add(bo);
    }

    @PutMapping
    @Operation(summary = "更新用户")
    public AjaxResult update(@Valid @RequestBody UserBo bo) {
        log.info("更新用户: {}", bo.getId());
        return userService.update(bo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    public AjaxResult delete(@Parameter(description = "用户ID") @PathVariable Long id) {
        log.info("删除用户: {}", id);
        return userService.delete(id);
    }

    // ==================== 业务自定义接口 ====================

    @PutMapping("/resetPassword")
    @Operation(summary = "重置密码")
    public AjaxResult resetPassword(
            @Parameter(description = "用户ID") @RequestParam Long id,
            @Parameter(description = "新密码") @RequestParam String newPassword) {
        log.info("重置密码: {}", id);
        return userService.resetPassword(id, newPassword);
    }

    @PutMapping("/updateLockStatus")
    @Operation(summary = "更新锁定状态")
    public AjaxResult updateLockStatus(
            @Parameter(description = "用户ID") @RequestParam Long id,
            @Parameter(description = "锁定状态：0-未锁定，1-锁定") @RequestParam Integer isLocked) {
        log.info("更新锁定状态: {} -> {}", id, isLocked);
        return userService.updateLockStatus(id, isLocked);
    }
}
