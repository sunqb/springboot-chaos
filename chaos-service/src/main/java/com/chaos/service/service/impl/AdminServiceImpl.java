package com.chaos.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.chaos.common.exception.BusinessException;
import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.admin.AdminBo;
import com.chaos.service.entity.bo.admin.AdminConditionBo;
import com.chaos.service.entity.bo.admin.AdminLoginBo;
import com.chaos.service.entity.dto.admin.AdminDto;
import com.chaos.service.entity.dto.admin.AdminRoleDto;
import com.chaos.service.entity.dto.role.RoleDto;
import com.chaos.service.entity.vo.admin.AdminVo;
import com.chaos.service.mapper.AdminMapper;
import com.chaos.service.mapper.AdminRoleMapper;
import com.chaos.service.mapper.RoleMapper;
import com.chaos.service.service.IAdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
 *
 * @author chaos
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

    // ==================== 标准CRUD方法 ====================

    @Override
    public List<AdminVo> list(AdminConditionBo condition) {
        return adminMapper.selectList(condition);
    }

    @Override
    public PageInfo<AdminVo> page(AdminConditionBo condition) {
        PageHelper.startPage(condition.getPage(), condition.getLimit(), condition.getOrderBy());
        List<AdminVo> list = adminMapper.selectList(condition);
        return new PageInfo<>(list);
    }

    @Override
    public AdminVo getById(Long id) {
        return adminMapper.selectDetail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult add(AdminBo bo) {
        // 检查账号是否存在
        AdminDto existAdmin = adminMapper.selectByAccountName(bo.getAccountName());
        if (existAdmin != null) {
            return AjaxResult.fail("账号已存在");
        }

        AdminDto adminDto = new AdminDto();
        BeanUtil.copyProperties(bo, adminDto);
        // 生成oid
        adminDto.setOid(IdUtil.fastSimpleUUID());
        // 加密密码
        String password = StringUtils.isBlank(bo.getPassword()) ? "123456" : bo.getPassword();
        adminDto.setPassword(passwordEncoder.encode(password));
        adminDto.setIsLocked(0);
        adminDto.setIsDelete(0);
        adminMapper.insert(adminDto);

        // 保存角色关联
        saveAdminRoles(adminDto.getOid(), bo.getRoleIds(), bo.getOrganizationId());

        return AjaxResult.success("新增成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult update(AdminBo bo) {
        if (bo.getId() == null) {
            return AjaxResult.fail("ID不能为空");
        }

        AdminDto adminDto = adminMapper.selectById(bo.getId());
        if (adminDto == null) {
            return AjaxResult.fail("管理员不存在");
        }

        // 检查账号是否被其他人使用
        AdminDto existAdmin = adminMapper.selectByAccountName(bo.getAccountName());
        if (existAdmin != null && !existAdmin.getId().equals(bo.getId())) {
            return AjaxResult.fail("账号已存在");
        }

        AdminDto updateDto = new AdminDto();
        BeanUtil.copyProperties(bo, updateDto);
        updateDto.setPassword(null); // 不更新密码
        adminMapper.updateById(updateDto);

        // 更新角色关联
        adminRoleMapper.deleteByAdminOid(adminDto.getOid());
        saveAdminRoles(adminDto.getOid(), bo.getRoleIds(), bo.getOrganizationId());

        return AjaxResult.success("更新成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult delete(Long id) {
        AdminDto adminDto = adminMapper.selectById(id);
        if (adminDto == null) {
            return AjaxResult.fail("管理员不存在");
        }

        // 软删除：更新is_delete字段
        LambdaUpdateWrapper<AdminDto> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AdminDto::getId, id)
                .set(AdminDto::getIsDelete, 1);
        adminMapper.update(null, updateWrapper);

        // 删除角色关联
        adminRoleMapper.deleteByAdminOid(adminDto.getOid());

        return AjaxResult.success("删除成功");
    }

    // ==================== 业务自定义方法 ====================

    @Override
    public AdminVo login(AdminLoginBo loginBo) {
        AdminDto adminDto = adminMapper.selectByAccountName(loginBo.getAccountName());
        if (adminDto == null) {
            throw new BusinessException("账号或密码错误");
        }

        if (!passwordEncoder.matches(loginBo.getPassword(), adminDto.getPassword())) {
            throw new BusinessException("账号或密码错误");
        }

        if (adminDto.getIsLocked() != null && adminDto.getIsLocked() == 1) {
            throw new BusinessException("账号已被锁定");
        }

        // 更新登录时间
        AdminDto updateDto = new AdminDto();
        updateDto.setId(adminDto.getId());
        updateDto.setLastLoginTime(LocalDateTime.now());
        adminMapper.updateById(updateDto);

        return convertToVo(adminDto);
    }

    @Override
    public AjaxResult getAdminInfo(String adminOid) {
        AdminDto adminDto = adminMapper.selectByOid(adminOid);
        if (adminDto == null) {
            return AjaxResult.fail("管理员不存在");
        }
        AdminVo adminVo = convertToVo(adminDto);
        // 获取权限列表
        adminVo.setPermissions(adminMapper.selectPermissionsByAdminOid(adminOid));
        return AjaxResult.success(adminVo);
    }

    @Override
    public AdminVo getByOid(String oid) {
        AdminDto adminDto = adminMapper.selectByOid(oid);
        return adminDto == null ? null : convertToVo(adminDto);
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
    public AjaxResult updateLockStatus(Long id, Integer isLocked) {
        AdminDto adminDto = adminMapper.selectById(id);
        if (adminDto == null) {
            return AjaxResult.fail("管理员不存在");
        }

        AdminDto updateDto = new AdminDto();
        updateDto.setId(id);
        updateDto.setIsLocked(isLocked);
        adminMapper.updateById(updateDto);

        return AjaxResult.success("状态更新成功");
    }

    @Override
    public List<String> getPermissionsByAdminOid(String adminOid) {
        return adminMapper.selectPermissionsByAdminOid(adminOid);
    }

    // ==================== 私有方法 ====================

    /**
     * 保存管理员角色关联
     */
    private void saveAdminRoles(String adminOid, List<Long> roleIds, Long organizationId) {
        if (CollUtil.isNotEmpty(roleIds)) {
            List<AdminRoleDto> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                AdminRoleDto adminRoleDto = new AdminRoleDto();
                adminRoleDto.setAdminOid(adminOid);
                adminRoleDto.setRoleId(roleId);
                adminRoleDto.setOrganizationId(organizationId);
                list.add(adminRoleDto);
            }
            adminRoleMapper.batchInsert(list);
        }
    }

    /**
     * 转换为VO
     */
    private AdminVo convertToVo(AdminDto adminDto) {
        AdminVo adminVo = new AdminVo();
        BeanUtil.copyProperties(adminDto, adminVo);

        // 获取角色信息
        List<Long> roleIds = adminMapper.selectRoleIdsByAdminOid(adminDto.getOid());
        adminVo.setRoleIds(roleIds);

        if (CollUtil.isNotEmpty(roleIds)) {
            List<RoleDto> roles = roleMapper.selectRolesByAdminOid(adminDto.getOid());
            adminVo.setRoleNames(roles.stream().map(RoleDto::getRoleName).collect(Collectors.toList()));
        } else {
            adminVo.setRoleNames(new ArrayList<>());
        }

        return adminVo;
    }
}
