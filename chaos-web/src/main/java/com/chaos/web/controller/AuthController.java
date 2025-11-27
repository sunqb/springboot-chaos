package com.chaos.web.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.chaos.common.constants.Constants;
import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.user.LoginBo;
import com.chaos.service.entity.vo.user.UserVo;
import com.chaos.service.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 认证控制器
 *
 * @author chaos
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "认证管理", description = "登录、登出、验证码等")
public class AuthController extends BaseController {

    @Resource
    private IUserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public AjaxResult login(@Valid @RequestBody LoginBo loginBo) {
        log.info("用户登录: {}", loginBo.getAccount());

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
        UserVo userVo = userService.login(loginBo);

        // Sa-Token 登录（使用oid作为登录ID）
        StpUtil.login(userVo.getOid());

        // 构建返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("token", StpUtil.getTokenValue());
        result.put("tokenName", StpUtil.getTokenName());
        result.put("user", userVo);

        log.info("用户登录成功: {} - {}", loginBo.getAccount(), getClientIp());
        return AjaxResult.success(result, "登录成功");
    }

    @GetMapping("/logout")
    @Operation(summary = "用户登出")
    public AjaxResult logout() {
        if (StpUtil.isLogin()) {
            log.info("用户登出: {}", StpUtil.getLoginIdAsString());
            StpUtil.logout();
        }
        return AjaxResult.success("退出成功");
    }

    @GetMapping("/info")
    @Operation(summary = "获取当前用户信息")
    public AjaxResult getUserInfo() {
        Long userId = getCurrentUserId();
        return userService.getUserInfo(userId);
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
