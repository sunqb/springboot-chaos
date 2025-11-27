package com.chaos.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.service.entity.dto.org.OrganizationDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组织机构 Mapper
 */
@Mapper
public interface OrganizationMapper extends BaseMapper<OrganizationDto> {

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
}
