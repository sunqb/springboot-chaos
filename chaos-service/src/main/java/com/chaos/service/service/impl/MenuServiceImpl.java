package com.chaos.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.menu.MenuBo;
import com.chaos.service.entity.bo.menu.MenuConditionBo;
import com.chaos.service.entity.dto.menu.MenuDto;
import com.chaos.service.entity.vo.menu.MenuVo;
import com.chaos.service.mapper.MenuMapper;
import com.chaos.service.mapper.RoleMenuMapper;
import com.chaos.service.service.IMenuService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单服务实现
 *
 * @author chaos
 */
@Slf4j
@Service
public class MenuServiceImpl implements IMenuService {

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    // ==================== 标准CRUD方法 ====================

    @Override
    public List<MenuVo> list(MenuConditionBo condition) {
        return menuMapper.selectList(condition);
    }

    @Override
    public PageInfo<MenuVo> page(MenuConditionBo condition) {
        PageHelper.startPage(condition.getPage(), condition.getLimit(), condition.getOrderBy());
        List<MenuVo> list = menuMapper.selectList(condition);
        return new PageInfo<>(list);
    }

    @Override
    public MenuVo getById(Long id) {
        return menuMapper.selectDetail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult add(MenuBo bo) {
        MenuDto menuDto = new MenuDto();
        BeanUtil.copyProperties(bo, menuDto);
        if (menuDto.getParentId() == null) {
            menuDto.setParentId(0L);
        }
        menuDto.setState(1);
        menuDto.setIsDelete(0);
        menuMapper.insert(menuDto);
        return AjaxResult.success("新增成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult update(MenuBo bo) {
        if (bo.getId() == null) {
            return AjaxResult.fail("ID不能为空");
        }

        MenuDto menuDto = menuMapper.selectById(bo.getId());
        if (menuDto == null) {
            return AjaxResult.fail("菜单不存在");
        }

        MenuDto updateDto = new MenuDto();
        BeanUtil.copyProperties(bo, updateDto);
        menuMapper.updateById(updateDto);

        return AjaxResult.success("更新成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult delete(Long id) {
        MenuDto menuDto = menuMapper.selectById(id);
        if (menuDto == null) {
            return AjaxResult.fail("菜单不存在");
        }

        // 检查是否有子菜单
        List<MenuDto> children = menuMapper.selectByParentId(id);
        if (CollUtil.isNotEmpty(children)) {
            return AjaxResult.fail("存在子菜单，无法删除");
        }

        // 软删除：更新is_delete字段
        LambdaUpdateWrapper<MenuDto> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MenuDto::getId, id)
                .set(MenuDto::getIsDelete, 1);
        menuMapper.update(null, updateWrapper);

        // 删除角色菜单关联
        roleMenuMapper.deleteByMenuId(id);

        return AjaxResult.success("删除成功");
    }

    // ==================== 业务自定义方法 ====================

    @Override
    public List<MenuVo> getMenuTree() {
        List<MenuDto> allMenus = menuMapper.selectAllOrdered();
        List<MenuVo> menuVoList = allMenus.stream().map(this::convertToVo).collect(Collectors.toList());
        return buildTree(menuVoList, 0L);
    }

    @Override
    public List<MenuVo> getMenusByRoleIds(List<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return new ArrayList<>();
        }
        List<MenuDto> menus = menuMapper.selectMenusByRoleIds(roleIds);
        List<MenuVo> menuVoList = menus.stream().map(this::convertToVo).collect(Collectors.toList());
        return buildTree(menuVoList, 0L);
    }

    // ==================== 私有方法 ====================

    /**
     * 构建树形结构
     */
    private List<MenuVo> buildTree(List<MenuVo> menuList, Long parentId) {
        List<MenuVo> tree = new ArrayList<>();
        for (MenuVo menu : menuList) {
            if (parentId.equals(menu.getParentId())) {
                menu.setChildren(buildTree(menuList, menu.getId()));
                tree.add(menu);
            }
        }
        return tree;
    }

    /**
     * 转换为VO
     */
    private MenuVo convertToVo(MenuDto menuDto) {
        MenuVo menuVo = new MenuVo();
        BeanUtil.copyProperties(menuDto, menuVo);
        return menuVo;
    }
}
