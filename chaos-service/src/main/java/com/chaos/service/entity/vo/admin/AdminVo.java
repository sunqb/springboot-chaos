package com.chaos.service.entity.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理员响应对象
 *
 * @author chaos
 */
@Data
@Schema(description = "管理员响应对象")
public class AdminVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "oid")
    private String oid;

    @Schema(description = "账户名")
    private String accountName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "管理员姓名")
    private String adminName;

    @Schema(description = "联系电话")
    private String contactNumber;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "性别：1-男，2-女")
    private Integer sex;

    @Schema(description = "生日")
    private LocalDate birthday;

    @Schema(description = "头像")
    private String picture;

    @Schema(description = "介绍")
    private String introduction;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "所属组织ID")
    private Long organizationId;

    @Schema(description = "所属组织名称")
    private String organizationName;

    @Schema(description = "是否锁定：0-否，1-是")
    private Integer isLocked;

    @Schema(description = "是否激活：0-否，1-是")
    private Integer isActivation;

    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "角色ID列表")
    private List<Long> roleIds;

    @Schema(description = "角色名称列表")
    private List<String> roleNames;

    @Schema(description = "权限列表")
    private List<String> permissions;
}
