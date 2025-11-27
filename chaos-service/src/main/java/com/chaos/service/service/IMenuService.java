package com.chaos.service.service;

import com.chaos.common.vo.AjaxResult;
import com.github.pagehelper.PageInfo;
import com.chaos.service.entity.bo.menu.MenuBo;
import com.chaos.service.entity.bo.menu.MenuConditionBo;
import com.chaos.service.entity.vo.menu.MenuVo;

import java.util.List;

/**
 * 菜单服务接口
 *
 * @author chaos
 */
public interface IMenuService {

    // ==================== 标准CRUD方法 ====================

    /**
     * 列表查询
     */
    List<MenuVo> list(MenuConditionBo condition);

    /**
     * 分页查询
     */
    PageInfo<MenuVo> page(MenuConditionBo condition);

    /**
     * 根据ID查询
     */
    MenuVo getById(Long id);

    /**
     * 新增
     */
    AjaxResult add(MenuBo bo);

    /**
     * 更新
     */
    AjaxResult update(MenuBo bo);

    /**
     * 删除（软删除）
     */
    AjaxResult delete(Long id);

    // ==================== 业务自定义方法 ====================

    /**
     * 查询菜单列表（树形）
     */
    List<MenuVo> getMenuTree();

    /**
     * 根据角色ID列表查询菜单
     */
    List<MenuVo> getMenusByRoleIds(List<Long> roleIds);
}
