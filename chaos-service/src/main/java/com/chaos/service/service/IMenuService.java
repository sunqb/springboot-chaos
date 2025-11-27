package com.chaos.service.service;

import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.menu.MenuBo;
import com.chaos.service.entity.vo.menu.MenuVo;

import java.util.List;

/**
 * 菜单服务接口
 */
public interface IMenuService {

    /**
     * 查询菜单列表（树形）
     */
    List<MenuVo> getMenuTree();

    /**
     * 查询菜单列表
     */
    List<MenuVo> getMenuList(String menuName, Integer status);

    /**
     * 根据角色ID列表查询菜单
     */
    List<MenuVo> getMenusByRoleIds(List<Long> roleIds);

    /**
     * 根据ID查询菜单
     */
    MenuVo getMenuById(Long id);

    /**
     * 新增菜单
     */
    AjaxResult addMenu(MenuBo menuBo);

    /**
     * 更新菜单
     */
    AjaxResult updateMenu(MenuBo menuBo);

    /**
     * 删除菜单
     */
    AjaxResult deleteMenu(Long id);
}
