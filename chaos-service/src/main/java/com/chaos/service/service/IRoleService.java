package com.chaos.service.service;

import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.role.RoleBo;
import com.chaos.service.entity.vo.role.RoleVo;

import java.util.List;

/**
 * 角色服务接口
 */
public interface IRoleService {

    /**
     * 查询角色列表
     */
    List<RoleVo> getRoleList(String roleName, Integer status);

    /**
     * 查询所有角色（下拉选项）
     */
    List<RoleVo> getAllRoles();

    /**
     * 根据ID查询角色
     */
    RoleVo getRoleById(Long id);

    /**
     * 新增角色
     */
    AjaxResult addRole(RoleBo roleBo);

    /**
     * 更新角色
     */
    AjaxResult updateRole(RoleBo roleBo);

    /**
     * 删除角色
     */
    AjaxResult deleteRole(Long id);

    /**
     * 更新状态
     */
    AjaxResult updateStatus(Long id, Integer status);

    /**
     * 分配菜单权限
     */
    AjaxResult assignMenus(Long roleId, List<Long> menuIds);
}
