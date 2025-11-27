package com.chaos.service.entity.bo.user;

import com.chaos.common.vo.BaseCondition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 用户查询条件
 *
 * @author chaos
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户查询条件")
public class UserConditionBo extends BaseCondition {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "是否锁定：0-否，1-是")
    private Integer isLocked;

    @Schema(description = "组织ID")
    private Long organizationId;
}
