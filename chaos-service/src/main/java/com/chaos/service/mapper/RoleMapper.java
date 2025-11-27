package com.chaos.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.service.entity.dto.role.RoleDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色 Mapper
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleDto> {

    /**
     * 根据角色编码查询
     */
    RoleDto selectByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 查询角色的菜单ID列表
     */
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据管理员ID查询角色列表
     */
    List<RoleDto> selectRolesByAdminId(@Param("adminId") Long adminId);
}
