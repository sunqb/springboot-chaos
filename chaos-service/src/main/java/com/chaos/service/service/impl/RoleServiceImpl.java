package com.chaos.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.role.RoleBo;
import com.chaos.service.entity.dto.role.RoleDto;
import com.chaos.service.entity.dto.role.RoleMenuDto;
import com.chaos.service.entity.vo.role.RoleVo;
import com.chaos.service.mapper.AdminRoleMapper;
import com.chaos.service.mapper.RoleMapper;
import com.chaos.service.mapper.RoleMenuMapper;
import com.chaos.service.service.IRoleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务实现
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

    @Override
    public List<RoleVo> getRoleList(String roleName, Integer status) {
        LambdaQueryWrapper<RoleDto> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(roleName), RoleDto::getRoleName, roleName)
                .eq(status != null, RoleDto::getStatus, status)
                .orderByAsc(RoleDto::getSort);
        List<RoleDto> list = roleMapper.selectList(wrapper);
        return list.stream().map(this::convertToVo).collect(Collectors.toList());
    }

    @Override
    public List<RoleVo> getAllRoles() {
        LambdaQueryWrapper<RoleDto> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleDto::getStatus, 1).orderByAsc(RoleDto::getSort);
        List<RoleDto> list = roleMapper.selectList(wrapper);
        return list.stream().map(this::convertToVo).collect(Collectors.toList());
    }

    @Override
    public RoleVo getRoleById(Long id) {
        RoleDto roleDto = roleMapper.selectById(id);
        return roleDto == null ? null : convertToVo(roleDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addRole(RoleBo roleBo) {
        // 检查角色编码是否存在
        RoleDto existRole = roleMapper.selectByRoleCode(roleBo.getRoleCode());
        if (existRole != null) {
            return AjaxResult.fail("角色编码已存在");
        }

        RoleDto roleDto = new RoleDto();
        BeanUtil.copyProperties(roleBo, roleDto);
        roleDto.setStatus(1);
        roleDto.setDelFlag(0);
        roleMapper.insert(roleDto);

        // 保存菜单关联
        saveRoleMenus(roleDto.getId(), roleBo.getMenuIds());

        return AjaxResult.success("新增成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateRole(RoleBo roleBo) {
        if (roleBo.getId() == null) {
            return AjaxResult.fail("ID不能为空");
        }

        RoleDto roleDto = roleMapper.selectById(roleBo.getId());
        if (roleDto == null) {
            return AjaxResult.fail("角色不存在");
        }

        // 检查角色编码是否被其他角色使用
        RoleDto existRole = roleMapper.selectByRoleCode(roleBo.getRoleCode());
        if (existRole != null && !existRole.getId().equals(roleBo.getId())) {
            return AjaxResult.fail("角色编码已存在");
        }

        RoleDto updateDto = new RoleDto();
        BeanUtil.copyProperties(roleBo, updateDto);
        roleMapper.updateById(updateDto);

        // 更新菜单关联
        roleMenuMapper.deleteByRoleId(roleBo.getId());
        saveRoleMenus(roleBo.getId(), roleBo.getMenuIds());

        return AjaxResult.success("更新成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult deleteRole(Long id) {
        RoleDto roleDto = roleMapper.selectById(id);
        if (roleDto == null) {
            return AjaxResult.fail("角色不存在");
        }

        // 删除角色（逻辑删除）
        roleMapper.deleteById(id);
        // 删除菜单关联
        roleMenuMapper.deleteByRoleId(id);
        // 删除用户关联
        adminRoleMapper.deleteByRoleId(id);

        return AjaxResult.success("删除成功");
    }

    @Override
    public AjaxResult updateStatus(Long id, Integer status) {
        RoleDto roleDto = roleMapper.selectById(id);
        if (roleDto == null) {
            return AjaxResult.fail("角色不存在");
        }

        RoleDto updateDto = new RoleDto();
        updateDto.setId(id);
        updateDto.setStatus(status);
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

    /**
     * 保存角色菜单关联
     */
    private void saveRoleMenus(Long roleId, List<Long> menuIds) {
        if (CollUtil.isNotEmpty(menuIds)) {
            for (Long menuId : menuIds) {
                RoleMenuDto roleMenuDto = new RoleMenuDto();
                roleMenuDto.setRoleId(roleId);
                roleMenuDto.setMenuId(menuId);
                roleMenuMapper.insert(roleMenuDto);
            }
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
