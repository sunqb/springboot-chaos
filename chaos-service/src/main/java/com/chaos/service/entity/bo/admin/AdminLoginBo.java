package com.chaos.service.entity.bo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 管理员登录请求参数
 *
 * @author chaos
 */
@Data
@Schema(description = "管理员登录请求参数")
public class AdminLoginBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "账户名不能为空")
    @Schema(description = "账户名")
    private String accountName;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String password;

    @Schema(description = "验证码")
    private String captcha;

    @Schema(description = "验证码Key")
    private String captchaKey;
}
