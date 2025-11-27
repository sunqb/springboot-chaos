package com.chaos.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.service.entity.dto.admin.AdminDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 管理员 Mapper
 */
@Mapper
public interface AdminMapper extends BaseMapper<AdminDto> {

    /**
     * 根据用户名查询管理员
     */
    AdminDto selectByUsername(@Param("username") String username);

    /**
     * 查询管理员的角色ID列表
     */
    List<Long> selectRoleIdsByAdminId(@Param("adminId") Long adminId);

    /**
     * 查询管理员的权限列表
     */
    List<String> selectPermissionsByAdminId(@Param("adminId") Long adminId);
}
