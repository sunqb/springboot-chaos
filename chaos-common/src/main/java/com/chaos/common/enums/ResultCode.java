package com.chaos.common.enums;

import lombok.Getter;

/**
 * 响应码枚举
 */
@Getter
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    WARN(300, "警告"),

    // 认证相关 4xx
    UNAUTHORIZED(401, "未登录或登录已过期"),
    TOKEN_INVALID(402, "Token无效"),
    FORBIDDEN(403, "权限不足"),
    NOT_FOUND(404, "资源不存在"),

    // 业务错误 5xx
    ERROR(500, "系统内部错误"),
    PARAM_ERROR(501, "参数错误"),
    DATA_NOT_FOUND(502, "数据不存在"),
    DATA_EXIST(503, "数据已存在"),
    OPERATION_FAILED(504, "操作失败"),

    // 用户相关
    USER_NOT_EXIST(1001, "用户不存在"),
    USER_PASSWORD_ERROR(1002, "密码错误"),
    USER_DISABLED(1003, "用户已被禁用"),
    USER_EXIST(1004, "用户名已存在"),
    CAPTCHA_ERROR(1005, "验证码错误"),
    CAPTCHA_EXPIRED(1006, "验证码已过期");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
