package com.chaos.manager.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.chaos.common.constants.Constants;
import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.admin.AdminLoginBo;
import com.chaos.service.entity.vo.admin.AdminVo;
import com.chaos.service.service.IAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 管理员认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "认证管理", description = "管理员登录、登出、验证码等")
public class AuthController extends BaseController {

    @Resource
    private IAdminService adminService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/login")
    @Operation(summary = "管理员登录")
    public AjaxResult login(@Valid @RequestBody AdminLoginBo loginBo) {
        log.info("管理员登录: {}", loginBo.getUsername());

        // 验证码校验（如果启用）
        if (loginBo.getCaptchaKey() != null && !loginBo.getCaptchaKey().isEmpty()) {
            String captchaKey = Constants.REDIS_CAPTCHA_PREFIX + loginBo.getCaptchaKey();
            String savedCaptcha = stringRedisTemplate.opsForValue().get(captchaKey);
            if (savedCaptcha == null) {
                return AjaxResult.fail("验证码已过期");
            }
            if (!savedCaptcha.equalsIgnoreCase(loginBo.getCaptcha())) {
                return AjaxResult.fail("验证码错误");
            }
            // 验证通过后删除验证码
            stringRedisTemplate.delete(captchaKey);
        }

        // 执行登录
        AdminVo adminVo = adminService.login(loginBo);

        // Sa-Token 登录
        StpUtil.login(adminVo.getId());

        // 获取权限列表
        List<String> permissions = adminService.getPermissionsByAdminId(adminVo.getId());
        adminVo.setPermissions(permissions);

        // 构建返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("token", StpUtil.getTokenValue());
        result.put("tokenName", StpUtil.getTokenName());
        result.put("admin", adminVo);

        log.info("管理员登录成功: {} - {}", loginBo.getUsername(), getClientIp());
        return AjaxResult.success(result, "登录成功");
    }

    @GetMapping("/logout")
    @Operation(summary = "管理员登出")
    public AjaxResult logout() {
        if (StpUtil.isLogin()) {
            log.info("管理员登出: {}", StpUtil.getLoginIdAsString());
            StpUtil.logout();
        }
        return AjaxResult.success("退出成功");
    }

    @GetMapping("/info")
    @Operation(summary = "获取当前管理员信息")
    public AjaxResult getAdminInfo() {
        Long adminId = getCurrentAdminId();
        return adminService.getAdminInfo(adminId);
    }

    @GetMapping("/token")
    @Operation(summary = "获取Token信息")
    public AjaxResult getTokenInfo() {
        return AjaxResult.success(StpUtil.getTokenInfo());
    }

    @GetMapping("/captcha")
    @Operation(summary = "获取图形验证码")
    public AjaxResult getCaptcha() {
        // 生成验证码
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(120, 40, 4, 50);
        String code = captcha.getCode();
        String imageBase64 = captcha.getImageBase64Data();

        // 生成唯一Key
        String captchaKey = UUID.randomUUID().toString().replace("-", "");

        // 存入Redis（5分钟过期）
        stringRedisTemplate.opsForValue().set(
                Constants.REDIS_CAPTCHA_PREFIX + captchaKey,
                code,
                Constants.CAPTCHA_EXPIRE_SECONDS,
                TimeUnit.SECONDS
        );

        // 返回验证码
        Map<String, String> result = new HashMap<>();
        result.put("captchaKey", captchaKey);
        result.put("captchaImage", imageBase64);

        return AjaxResult.success(result);
    }
}
