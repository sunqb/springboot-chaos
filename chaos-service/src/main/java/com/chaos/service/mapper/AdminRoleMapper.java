package com.chaos.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.service.entity.dto.admin.AdminRoleDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 管理员角色关联 Mapper
 */
@Mapper
public interface AdminRoleMapper extends BaseMapper<AdminRoleDto> {

    /**
     * 根据管理员ID删除关联
     */
    int deleteByAdminId(@Param("adminId") Long adminId);

    /**
     * 根据角色ID删除关联
     */
    int deleteByRoleId(@Param("roleId") Long roleId);
}
