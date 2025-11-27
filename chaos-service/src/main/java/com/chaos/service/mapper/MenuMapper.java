package com.chaos.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.service.entity.bo.menu.MenuConditionBo;
import com.chaos.service.entity.dto.menu.MenuDto;
import com.chaos.service.entity.vo.menu.MenuVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单 Mapper
 *
 * @author chaos
 */
@Mapper
public interface MenuMapper extends BaseMapper<MenuDto> {

    // ==================== 标准查询方法 ====================

    /**
     * 条件查询菜单列表
     */
    List<MenuVo> selectList(MenuConditionBo condition);

    /**
     * 查询菜单详情
     */
    MenuVo selectDetail(@Param("id") Long id);

    // ==================== 业务自定义方法 ====================

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

    /**
     * 查询公开菜单
     */
    List<MenuDto> selectPublicMenus();
}
