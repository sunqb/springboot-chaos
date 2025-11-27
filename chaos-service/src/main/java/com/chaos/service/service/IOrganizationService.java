package com.chaos.service.service;

import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.org.OrganizationBo;
import com.chaos.service.entity.vo.org.OrganizationVo;

import java.util.List;

/**
 * 组织机构服务接口
 */
public interface IOrganizationService {

    /**
     * 查询组织机构列表（树形）
     */
    List<OrganizationVo> getOrgTree();

    /**
     * 查询组织机构列表
     */
    List<OrganizationVo> getOrgList(String orgName, Integer status);

    /**
     * 根据ID查询组织机构
     */
    OrganizationVo getOrgById(Long id);

    /**
     * 新增组织机构
     */
    AjaxResult addOrg(OrganizationBo orgBo);

    /**
     * 更新组织机构
     */
    AjaxResult updateOrg(OrganizationBo orgBo);

    /**
     * 删除组织机构
     */
    AjaxResult deleteOrg(Long id);
}
