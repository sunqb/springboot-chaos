package com.chaos.service.service;

import com.chaos.common.vo.AjaxResult;
import com.github.pagehelper.PageInfo;
import com.chaos.service.entity.bo.admin.AdminBo;
import com.chaos.service.entity.bo.admin.AdminConditionBo;
import com.chaos.service.entity.bo.admin.AdminLoginBo;
import com.chaos.service.entity.vo.admin.AdminVo;

import java.util.List;

/**
 * 管理员服务接口
 *
 * @author chaos
 */
public interface IAdminService {

    // ==================== 标准CRUD方法 ====================

    /**
     * 列表查询
     */
    List<AdminVo> list(AdminConditionBo condition);

    /**
     * 分页查询
     */
    PageInfo<AdminVo> page(AdminConditionBo condition);

    /**
     * 根据ID查询
     */
    AdminVo getById(Long id);

    /**
     * 新增
     */
    AjaxResult add(AdminBo bo);

    /**
     * 更新
     */
    AjaxResult update(AdminBo bo);

    /**
     * 删除（软删除）
     */
    AjaxResult delete(Long id);

    // ==================== 业务自定义方法 ====================

    /**
     * 管理员登录
     */
    AdminVo login(AdminLoginBo loginBo);

    /**
     * 获取管理员信息
     */
    AjaxResult getAdminInfo(String adminOid);

    /**
     * 根据oid查询管理员
     */
    AdminVo getByOid(String oid);

    /**
     * 重置密码
     */
    AjaxResult resetPassword(Long id, String newPassword);

    /**
     * 更新锁定状态
     */
    AjaxResult updateLockStatus(Long id, Integer isLocked);

    /**
     * 获取管理员权限列表
     */
    List<String> getPermissionsByAdminOid(String adminOid);
}
