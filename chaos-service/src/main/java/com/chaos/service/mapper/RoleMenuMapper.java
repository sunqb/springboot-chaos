package com.chaos.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.service.entity.dto.role.RoleMenuDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 角色菜单关联 Mapper
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenuDto> {

    /**
     * 根据角色ID删除关联
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据菜单ID删除关联
     */
    int deleteByMenuId(@Param("menuId") Long menuId);
}
