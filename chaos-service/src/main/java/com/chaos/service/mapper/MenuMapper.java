package com.chaos.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.service.entity.dto.menu.MenuDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单 Mapper
 */
@Mapper
public interface MenuMapper extends BaseMapper<MenuDto> {

    /**
     * 查询所有菜单（按排序）
     */
    List<MenuDto> selectAllOrdered();

    /**
     * 根据角色ID列表查询菜单
     */
    List<MenuDto> selectMenusByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 根据父ID查询子菜单
     */
    List<MenuDto> selectByParentId(@Param("parentId") Long parentId);
}
