package com.chaos.web.controller;

import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.user.UserBo;
import com.chaos.service.entity.vo.user.UserVo;
import com.chaos.service.service.IUserService;
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
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户CRUD操作")
public class UserController extends BaseController {

    @Resource
    private IUserService userService;

    @GetMapping("/list")
    @Operation(summary = "查询用户列表")
    public AjaxResult list(
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        List<UserVo> list = userService.getUserList(username, status);
        return AjaxResult.success(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询用户详情")
    public AjaxResult getById(@Parameter(description = "用户ID") @PathVariable Long id) {
        UserVo user = userService.getUserById(id);
        if (user == null) {
            return AjaxResult.fail("用户不存在");
        }
        return AjaxResult.success(user);
    }

    @PostMapping
    @Operation(summary = "新增用户")
    public AjaxResult add(@Valid @RequestBody UserBo userBo) {
        log.info("新增用户: {}", userBo.getUsername());
        return userService.addUser(userBo);
    }

    @PutMapping
    @Operation(summary = "更新用户")
    public AjaxResult update(@Valid @RequestBody UserBo userBo) {
        log.info("更新用户: {}", userBo.getId());
        return userService.updateUser(userBo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    public AjaxResult delete(@Parameter(description = "用户ID") @PathVariable Long id) {
        log.info("删除用户: {}", id);
        return userService.deleteUser(id);
    }

    @PutMapping("/resetPassword")
    @Operation(summary = "重置密码")
    public AjaxResult resetPassword(
            @Parameter(description = "用户ID") @RequestParam Long id,
            @Parameter(description = "新密码") @RequestParam String newPassword) {
        log.info("重置密码: {}", id);
        return userService.resetPassword(id, newPassword);
    }
}
