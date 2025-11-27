package com.chaos.service.service;

import com.chaos.common.vo.AjaxResult;
import com.github.pagehelper.PageInfo;
import com.chaos.service.entity.bo.role.RoleBo;
import com.chaos.service.entity.bo.role.RoleConditionBo;
import com.chaos.service.entity.vo.role.RoleVo;

import java.util.List;

/**
 * 角色服务接口
 *
 * @author chaos
 */
public interface IRoleService {

    // ==================== 标准CRUD方法 ====================

    /**
     * 列表查询
     */
    List<RoleVo> list(RoleConditionBo condition);

    /**
     * 分页查询
     */
    PageInfo<RoleVo> page(RoleConditionBo condition);

    /**
     * 根据ID查询
     */
    RoleVo getById(Long id);

    /**
     * 新增
     */
    AjaxResult add(RoleBo bo);

    /**
     * 更新
     */
    AjaxResult update(RoleBo bo);

    /**
     * 删除（软删除）
     */
    AjaxResult delete(Long id);

    // ==================== 业务自定义方法 ====================

    /**
     * 查询所有角色（下拉选项）
     */
    List<RoleVo> getAllRoles();

    /**
     * 更新锁定状态
     */
    AjaxResult updateLockStatus(Long id, Integer isLocked);

    /**
     * 分配菜单权限
     */
    AjaxResult assignMenus(Long roleId, List<Long> menuIds);
}
