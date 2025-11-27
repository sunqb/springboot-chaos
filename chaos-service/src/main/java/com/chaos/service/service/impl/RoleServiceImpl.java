package com.chaos.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.role.RoleBo;
import com.chaos.service.entity.bo.role.RoleConditionBo;
import com.chaos.service.entity.dto.role.RoleDto;
import com.chaos.service.entity.dto.role.RoleMenuDto;
import com.chaos.service.entity.vo.role.RoleVo;
import com.chaos.service.mapper.AdminRoleMapper;
import com.chaos.service.mapper.RoleMapper;
import com.chaos.service.mapper.RoleMenuMapper;
import com.chaos.service.service.IRoleService;
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
 * 角色服务实现
 *
 * @author chaos
 */
@Slf4j
@Service
public class RoleServiceImpl implements IRoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private AdminRoleMapper adminRoleMapper;

    // ==================== 标准CRUD方法 ====================

    @Override
    public List<RoleVo> list(RoleConditionBo condition) {
        return roleMapper.selectList(condition);
    }

    @Override
    public PageInfo<RoleVo> page(RoleConditionBo condition) {
        PageHelper.startPage(condition.getPage(), condition.getLimit(), condition.getOrderBy());
        List<RoleVo> list = roleMapper.selectList(condition);
        return new PageInfo<>(list);
    }

    @Override
    public RoleVo getById(Long id) {
        return roleMapper.selectDetail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult add(RoleBo bo) {
        // 检查角色编码是否存在
        RoleDto existRole = roleMapper.selectByRoleCode(bo.getRoleCode());
        if (existRole != null) {
            return AjaxResult.fail("角色编码已存在");
        }

        RoleDto roleDto = new RoleDto();
        BeanUtil.copyProperties(bo, roleDto);
        roleDto.setIsLocked(0);
        roleDto.setIsDelete(0);
        roleMapper.insert(roleDto);

        // 保存菜单关联
        saveRoleMenus(roleDto.getId(), bo.getMenuIds());

        return AjaxResult.success("新增成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult update(RoleBo bo) {
        if (bo.getId() == null) {
            return AjaxResult.fail("ID不能为空");
        }

        RoleDto roleDto = roleMapper.selectById(bo.getId());
        if (roleDto == null) {
            return AjaxResult.fail("角色不存在");
        }

        // 检查角色编码是否被其他角色使用
        RoleDto existRole = roleMapper.selectByRoleCode(bo.getRoleCode());
        if (existRole != null && !existRole.getId().equals(bo.getId())) {
            return AjaxResult.fail("角色编码已存在");
        }

        RoleDto updateDto = new RoleDto();
        BeanUtil.copyProperties(bo, updateDto);
        roleMapper.updateById(updateDto);

        // 更新菜单关联
        roleMenuMapper.deleteByRoleId(bo.getId());
        saveRoleMenus(bo.getId(), bo.getMenuIds());

        return AjaxResult.success("更新成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult delete(Long id) {
        RoleDto roleDto = roleMapper.selectById(id);
        if (roleDto == null) {
            return AjaxResult.fail("角色不存在");
        }

        // 软删除：更新is_delete字段
        LambdaUpdateWrapper<RoleDto> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RoleDto::getId, id)
                .set(RoleDto::getIsDelete, 1);
        roleMapper.update(null, updateWrapper);

        // 删除菜单关联
        roleMenuMapper.deleteByRoleId(id);
        // 删除用户关联
        adminRoleMapper.deleteByRoleId(id);

        return AjaxResult.success("删除成功");
    }

    // ==================== 业务自定义方法 ====================

    @Override
    public List<RoleVo> getAllRoles() {
        LambdaQueryWrapper<RoleDto> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleDto::getIsLocked, 0)
                .eq(RoleDto::getIsDelete, 0)
                .orderByAsc(RoleDto::getSort);
        List<RoleDto> list = roleMapper.selectList(wrapper);
        return list.stream().map(this::convertToVo).collect(Collectors.toList());
    }

    @Override
    public AjaxResult updateLockStatus(Long id, Integer isLocked) {
        RoleDto roleDto = roleMapper.selectById(id);
        if (roleDto == null) {
            return AjaxResult.fail("角色不存在");
        }

        RoleDto updateDto = new RoleDto();
        updateDto.setId(id);
        updateDto.setIsLocked(isLocked);
        roleMapper.updateById(updateDto);

        return AjaxResult.success("状态更新成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult assignMenus(Long roleId, List<Long> menuIds) {
        RoleDto roleDto = roleMapper.selectById(roleId);
        if (roleDto == null) {
            return AjaxResult.fail("角色不存在");
        }

        // 删除旧的菜单关联
        roleMenuMapper.deleteByRoleId(roleId);
        // 保存新的菜单关联
        saveRoleMenus(roleId, menuIds);

        return AjaxResult.success("分配成功");
    }

    // ==================== 私有方法 ====================

    /**
     * 保存角色菜单关联
     */
    private void saveRoleMenus(Long roleId, List<Long> menuIds) {
        if (CollUtil.isNotEmpty(menuIds)) {
            List<RoleMenuDto> list = new ArrayList<>();
            for (Long menuId : menuIds) {
                RoleMenuDto roleMenuDto = new RoleMenuDto();
                roleMenuDto.setRoleId(roleId);
                roleMenuDto.setMenuId(menuId);
                list.add(roleMenuDto);
            }
            roleMenuMapper.batchInsert(list);
        }
    }

    /**
     * 转换为VO
     */
    private RoleVo convertToVo(RoleDto roleDto) {
        RoleVo roleVo = new RoleVo();
        BeanUtil.copyProperties(roleDto, roleVo);
        // 获取菜单ID列表
        roleVo.setMenuIds(roleMapper.selectMenuIdsByRoleId(roleDto.getId()));
        return roleVo;
    }
}
