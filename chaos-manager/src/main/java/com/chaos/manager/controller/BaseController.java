package com.chaos.manager.controller;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 控制器基类
 */
@Slf4j
@Component
public class BaseController {

    @Resource
    protected HttpServletRequest request;

    /**
     * 获取当前登录管理员ID
     */
    protected Long getCurrentAdminId() {
        return StpUtil.getLoginIdAsLong();
    }

    /**
     * 获取当前登录管理员ID（字符串）
     */
    protected String getCurrentAdminIdStr() {
        return StpUtil.getLoginIdAsString();
    }

    /**
     * 判断是否登录
     */
    protected boolean isLogin() {
        return StpUtil.isLogin();
    }

    /**
     * 获取请求IP
     */
    protected String getClientIp() {
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多个代理时，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
