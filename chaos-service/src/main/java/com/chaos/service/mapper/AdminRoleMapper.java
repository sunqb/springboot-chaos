package com.chaos.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.service.entity.dto.admin.AdminRoleDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 管理员角色关联 Mapper
 *
 * @author chaos
 */
@Mapper
public interface AdminRoleMapper extends BaseMapper<AdminRoleDto> {

    /**
     * 根据管理员oid删除关联
     */
    int deleteByAdminOid(@Param("adminOid") String adminOid);

    /**
     * 根据角色ID删除关联
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 批量插入管理员角色关联
     */
    int batchInsert(@Param("list") List<AdminRoleDto> list);
}
