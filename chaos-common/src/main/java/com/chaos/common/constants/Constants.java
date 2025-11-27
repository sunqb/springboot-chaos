package com.chaos.common.constants;

/**
 * 全局常量定义
 */
public class Constants {

    // ========== 系统状态码 ==========
    public static final Integer CODE_SUCCESS = 200;
    public static final Integer CODE_WARN = 300;
    public static final Integer CODE_UNAUTHORIZED = 401;
    public static final Integer CODE_FORBIDDEN = 403;
    public static final Integer CODE_NOT_FOUND = 404;
    public static final Integer CODE_ERROR = 500;
    public static final Integer CODE_PARAM_ERROR = 501;

    // ========== HTTP Headers ==========
    public static final String AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json;charset=UTF-8";

    // ========== 通用状态 ==========
    public static final Integer STATUS_ENABLE = 1;
    public static final Integer STATUS_DISABLE = 0;

    // ========== 删除标记 ==========
    public static final Integer NOT_DELETED = 0;
    public static final Integer DELETED = 1;

    // ========== Redis Key 前缀 ==========
    public static final String REDIS_KEY_PREFIX = "chaos:";
    public static final String REDIS_CAPTCHA_PREFIX = REDIS_KEY_PREFIX + "captcha:";
    public static final String REDIS_USER_PREFIX = REDIS_KEY_PREFIX + "user:";

    // ========== 验证码配置 ==========
    public static final Integer CAPTCHA_EXPIRE_SECONDS = 300;
    public static final Integer CAPTCHA_LENGTH = 4;

    // ========== 分页默认值 ==========
    public static final Integer DEFAULT_PAGE_NO = 1;
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    public static final Integer MAX_PAGE_SIZE = 100;

    private Constants() {
        // 私有构造函数，防止实例化
    }
}
