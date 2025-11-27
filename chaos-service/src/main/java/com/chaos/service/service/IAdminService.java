package com.chaos.service.service;

import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.admin.AdminBo;
import com.chaos.service.entity.bo.admin.AdminLoginBo;
import com.chaos.service.entity.vo.admin.AdminVo;

import java.util.List;

/**
 * 管理员服务接口
 */
public interface IAdminService {

    /**
     * 管理员登录
     */
    AdminVo login(AdminLoginBo loginBo);

    /**
     * 获取管理员信息
     */
    AjaxResult getAdminInfo(Long adminId);

    /**
     * 查询管理员列表
     */
    List<AdminVo> getAdminList(String username, String realName, Integer status);

    /**
     * 根据ID查询管理员
     */
    AdminVo getAdminById(Long id);

    /**
     * 新增管理员
     */
    AjaxResult addAdmin(AdminBo adminBo);

    /**
     * 更新管理员
     */
    AjaxResult updateAdmin(AdminBo adminBo);

    /**
     * 删除管理员
     */
    AjaxResult deleteAdmin(Long id);

    /**
     * 重置密码
     */
    AjaxResult resetPassword(Long id, String newPassword);

    /**
     * 更新状态
     */
    AjaxResult updateStatus(Long id, Integer status);

    /**
     * 获取管理员权限列表
     */
    List<String> getPermissionsByAdminId(Long adminId);
}
