package com.chaos.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaos.common.exception.BusinessException;
import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.admin.AdminBo;
import com.chaos.service.entity.bo.admin.AdminLoginBo;
import com.chaos.service.entity.dto.admin.AdminDto;
import com.chaos.service.entity.dto.admin.AdminRoleDto;
import com.chaos.service.entity.dto.role.RoleDto;
import com.chaos.service.entity.vo.admin.AdminVo;
import com.chaos.service.mapper.AdminMapper;
import com.chaos.service.mapper.AdminRoleMapper;
import com.chaos.service.mapper.RoleMapper;
import com.chaos.service.service.IAdminService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员服务实现
 */
@Slf4j
@Service
public class AdminServiceImpl implements IAdminService {

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private AdminRoleMapper adminRoleMapper;

    @Resource
    private RoleMapper roleMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public AdminVo login(AdminLoginBo loginBo) {
        AdminDto adminDto = adminMapper.selectByUsername(loginBo.getUsername());
        if (adminDto == null) {
            throw new BusinessException("用户名或密码错误");
        }

        if (!passwordEncoder.matches(loginBo.getPassword(), adminDto.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        if (adminDto.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        // 更新登录时间
        AdminDto updateDto = new AdminDto();
        updateDto.setId(adminDto.getId());
        updateDto.setLastLoginTime(LocalDateTime.now());
        adminMapper.updateById(updateDto);

        return convertToVo(adminDto);
    }

    @Override
    public AjaxResult getAdminInfo(Long adminId) {
        AdminDto adminDto = adminMapper.selectById(adminId);
        if (adminDto == null) {
            return AjaxResult.fail("管理员不存在");
        }
        AdminVo adminVo = convertToVo(adminDto);
        // 获取权限列表
        adminVo.setPermissions(adminMapper.selectPermissionsByAdminId(adminId));
        return AjaxResult.success(adminVo);
    }

    @Override
    public List<AdminVo> getAdminList(String username, String realName, Integer status) {
        LambdaQueryWrapper<AdminDto> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(username), AdminDto::getUsername, username)
                .like(StringUtils.isNotBlank(realName), AdminDto::getRealName, realName)
                .eq(status != null, AdminDto::getStatus, status)
                .orderByDesc(AdminDto::getCreateTime);
        List<AdminDto> list = adminMapper.selectList(wrapper);
        return list.stream().map(this::convertToVo).collect(Collectors.toList());
    }

    @Override
    public AdminVo getAdminById(Long id) {
        AdminDto adminDto = adminMapper.selectById(id);
        return adminDto == null ? null : convertToVo(adminDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addAdmin(AdminBo adminBo) {
        // 检查用户名是否存在
        AdminDto existAdmin = adminMapper.selectByUsername(adminBo.getUsername());
        if (existAdmin != null) {
            return AjaxResult.fail("用户名已存在");
        }

        AdminDto adminDto = new AdminDto();
        BeanUtil.copyProperties(adminBo, adminDto);
        // 加密密码
        String password = StringUtils.isBlank(adminBo.getPassword()) ? "123456" : adminBo.getPassword();
        adminDto.setPassword(passwordEncoder.encode(password));
        adminDto.setStatus(1);
        adminDto.setDelFlag(0);
        adminMapper.insert(adminDto);

        // 保存角色关联
        saveAdminRoles(adminDto.getId(), adminBo.getRoleIds());

        return AjaxResult.success("新增成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateAdmin(AdminBo adminBo) {
        if (adminBo.getId() == null) {
            return AjaxResult.fail("ID不能为空");
        }

        AdminDto adminDto = adminMapper.selectById(adminBo.getId());
        if (adminDto == null) {
            return AjaxResult.fail("管理员不存在");
        }

        // 检查用户名是否被其他人使用
        AdminDto existAdmin = adminMapper.selectByUsername(adminBo.getUsername());
        if (existAdmin != null && !existAdmin.getId().equals(adminBo.getId())) {
            return AjaxResult.fail("用户名已存在");
        }

        AdminDto updateDto = new AdminDto();
        BeanUtil.copyProperties(adminBo, updateDto);
        updateDto.setPassword(null); // 不更新密码
        adminMapper.updateById(updateDto);

        // 更新角色关联
        adminRoleMapper.deleteByAdminId(adminBo.getId());
        saveAdminRoles(adminBo.getId(), adminBo.getRoleIds());

        return AjaxResult.success("更新成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult deleteAdmin(Long id) {
        AdminDto adminDto = adminMapper.selectById(id);
        if (adminDto == null) {
            return AjaxResult.fail("管理员不存在");
        }

        // 删除管理员（逻辑删除）
        adminMapper.deleteById(id);
        // 删除角色关联
        adminRoleMapper.deleteByAdminId(id);

        return AjaxResult.success("删除成功");
    }

    @Override
    public AjaxResult resetPassword(Long id, String newPassword) {
        AdminDto adminDto = adminMapper.selectById(id);
        if (adminDto == null) {
            return AjaxResult.fail("管理员不存在");
        }

        String password = StringUtils.isBlank(newPassword) ? "123456" : newPassword;
        AdminDto updateDto = new AdminDto();
        updateDto.setId(id);
        updateDto.setPassword(passwordEncoder.encode(password));
        adminMapper.updateById(updateDto);

        return AjaxResult.success("密码重置成功");
    }

    @Override
    public AjaxResult updateStatus(Long id, Integer status) {
        AdminDto adminDto = adminMapper.selectById(id);
        if (adminDto == null) {
            return AjaxResult.fail("管理员不存在");
        }

        AdminDto updateDto = new AdminDto();
        updateDto.setId(id);
        updateDto.setStatus(status);
        adminMapper.updateById(updateDto);

        return AjaxResult.success("状态更新成功");
    }

    @Override
    public List<String> getPermissionsByAdminId(Long adminId) {
        return adminMapper.selectPermissionsByAdminId(adminId);
    }

    /**
     * 保存管理员角色关联
     */
    private void saveAdminRoles(Long adminId, List<Long> roleIds) {
        if (CollUtil.isNotEmpty(roleIds)) {
            for (Long roleId : roleIds) {
                AdminRoleDto adminRoleDto = new AdminRoleDto();
                adminRoleDto.setAdminId(adminId);
                adminRoleDto.setRoleId(roleId);
                adminRoleMapper.insert(adminRoleDto);
            }
        }
    }

    /**
     * 转换为VO
     */
    private AdminVo convertToVo(AdminDto adminDto) {
        AdminVo adminVo = new AdminVo();
        BeanUtil.copyProperties(adminDto, adminVo);

        // 获取角色信息
        List<Long> roleIds = adminMapper.selectRoleIdsByAdminId(adminDto.getId());
        adminVo.setRoleIds(roleIds);

        if (CollUtil.isNotEmpty(roleIds)) {
            List<RoleDto> roles = roleMapper.selectRolesByAdminId(adminDto.getId());
            adminVo.setRoleNames(roles.stream().map(RoleDto::getRoleName).collect(Collectors.toList()));
        } else {
            adminVo.setRoleNames(new ArrayList<>());
        }

        return adminVo;
    }
}
