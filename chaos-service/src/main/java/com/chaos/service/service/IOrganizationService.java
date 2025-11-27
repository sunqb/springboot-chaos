package com.chaos.service.service;

import com.chaos.common.vo.AjaxResult;
import com.github.pagehelper.PageInfo;
import com.chaos.service.entity.bo.org.OrganizationBo;
import com.chaos.service.entity.bo.org.OrganizationConditionBo;
import com.chaos.service.entity.vo.org.OrganizationVo;

import java.util.List;

/**
 * 组织机构服务接口
 *
 * @author chaos
 */
public interface IOrganizationService {

    // ==================== 标准CRUD方法 ====================

    /**
     * 列表查询
     */
    List<OrganizationVo> list(OrganizationConditionBo condition);

    /**
     * 分页查询
     */
    PageInfo<OrganizationVo> page(OrganizationConditionBo condition);

    /**
     * 根据ID查询
     */
    OrganizationVo getById(Long id);

    /**
     * 新增
     */
    AjaxResult add(OrganizationBo bo);

    /**
     * 更新
     */
    AjaxResult update(OrganizationBo bo);

    /**
     * 删除（软删除）
     */
    AjaxResult delete(Long id);

    // ==================== 业务自定义方法 ====================

    /**
     * 查询组织机构列表（树形）
     */
    List<OrganizationVo> getOrgTree();
}
