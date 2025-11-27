package com.chaos.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.service.entity.bo.role.RoleConditionBo;
import com.chaos.service.entity.dto.role.RoleDto;
import com.chaos.service.entity.vo.role.RoleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色 Mapper
 *
 * @author chaos
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleDto> {

    // ==================== 标准查询方法 ====================

    /**
     * 条件查询角色列表
     */
    List<RoleVo> selectList(RoleConditionBo condition);

    /**
     * 查询角色详情
     */
    RoleVo selectDetail(@Param("id") Long id);

    // ==================== 业务自定义方法 ====================

    /**
     * 根据角色编码查询
     */
    RoleDto selectByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 查询角色的菜单ID列表
     */
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据管理员oid查询角色列表
     */
    List<RoleDto> selectRolesByAdminOid(@Param("adminOid") String adminOid);

    /**
     * 查询所有启用的角色
     */
    List<RoleDto> selectAllEnabled();
}
