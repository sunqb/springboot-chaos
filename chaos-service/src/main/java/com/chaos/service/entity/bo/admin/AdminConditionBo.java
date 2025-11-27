package com.chaos.service.entity.bo.admin;

import com.chaos.common.vo.BaseCondition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 管理员查询条件
 *
 * @author chaos
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "管理员查询条件")
public class AdminConditionBo extends BaseCondition {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "账户名")
    private String accountName;

    @Schema(description = "管理员姓名")
    private String adminName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "是否锁定：0-否，1-是")
    private Integer isLocked;

    @Schema(description = "所属组织ID")
    private Long organizationId;
}
