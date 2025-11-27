package com.chaos.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.service.entity.bo.org.OrganizationConditionBo;
import com.chaos.service.entity.dto.org.OrganizationDto;
import com.chaos.service.entity.vo.org.OrganizationVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组织机构 Mapper
 *
 * @author chaos
 */
@Mapper
public interface OrganizationMapper extends BaseMapper<OrganizationDto> {

    // ==================== 标准查询方法 ====================

    /**
     * 条件查询组织列表
     */
    List<OrganizationVo> selectList(OrganizationConditionBo condition);

    /**
     * 查询组织详情
     */
    OrganizationVo selectDetail(@Param("id") Long id);

    // ==================== 业务自定义方法 ====================

    /**
     * 查询所有组织（按排序）
     */
    List<OrganizationDto> selectAllOrdered();

    /**
     * 根据父ID查询子组织
     */
    List<OrganizationDto> selectByParentId(@Param("parentId") Long parentId);

    /**
     * 根据组织编码查询
     */
    OrganizationDto selectByOrgCode(@Param("orgCode") String orgCode);

    /**
     * 查询启用的组织
     */
    List<OrganizationDto> selectEnabled();
}
