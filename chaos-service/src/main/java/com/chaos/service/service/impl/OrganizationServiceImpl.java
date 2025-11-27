package com.chaos.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.org.OrganizationBo;
import com.chaos.service.entity.bo.org.OrganizationConditionBo;
import com.chaos.service.entity.dto.org.OrganizationDto;
import com.chaos.service.entity.vo.org.OrganizationVo;
import com.chaos.service.mapper.OrganizationMapper;
import com.chaos.service.service.IOrganizationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织机构服务实现
 *
 * @author chaos
 */
@Slf4j
@Service
public class OrganizationServiceImpl implements IOrganizationService {

    @Resource
    private OrganizationMapper organizationMapper;

    // ==================== 标准CRUD方法 ====================

    @Override
    public List<OrganizationVo> list(OrganizationConditionBo condition) {
        return organizationMapper.selectList(condition);
    }

    @Override
    public PageInfo<OrganizationVo> page(OrganizationConditionBo condition) {
        PageHelper.startPage(condition.getPage(), condition.getLimit(), condition.getOrderBy());
        List<OrganizationVo> list = organizationMapper.selectList(condition);
        return new PageInfo<>(list);
    }

    @Override
    public OrganizationVo getById(Long id) {
        return organizationMapper.selectDetail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult add(OrganizationBo bo) {
        // 检查组织编码是否存在
        if (StringUtils.isNotBlank(bo.getOrgCode())) {
            OrganizationDto existOrg = organizationMapper.selectByOrgCode(bo.getOrgCode());
            if (existOrg != null) {
                return AjaxResult.fail("组织编码已存在");
            }
        }

        OrganizationDto orgDto = new OrganizationDto();
        BeanUtil.copyProperties(bo, orgDto);
        if (orgDto.getParentId() == null) {
            orgDto.setParentId(0L);
        }

        // 设置上级组织ID列表
        if (orgDto.getParentId() == 0L) {
            orgDto.setSuperiorIds("0");
        } else {
            OrganizationDto parentOrg = organizationMapper.selectById(orgDto.getParentId());
            if (parentOrg != null) {
                orgDto.setSuperiorIds(parentOrg.getSuperiorIds() + "," + orgDto.getParentId());
            }
        }

        orgDto.setState(1);
        orgDto.setIsDelete(0);
        organizationMapper.insert(orgDto);
        return AjaxResult.success("新增成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult update(OrganizationBo bo) {
        if (bo.getId() == null) {
            return AjaxResult.fail("ID不能为空");
        }

        OrganizationDto orgDto = organizationMapper.selectById(bo.getId());
        if (orgDto == null) {
            return AjaxResult.fail("组织机构不存在");
        }

        // 检查组织编码是否被其他组织使用
        if (StringUtils.isNotBlank(bo.getOrgCode())) {
            OrganizationDto existOrg = organizationMapper.selectByOrgCode(bo.getOrgCode());
            if (existOrg != null && !existOrg.getId().equals(bo.getId())) {
                return AjaxResult.fail("组织编码已存在");
            }
        }

        OrganizationDto updateDto = new OrganizationDto();
        BeanUtil.copyProperties(bo, updateDto);
        organizationMapper.updateById(updateDto);

        return AjaxResult.success("更新成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult delete(Long id) {
        OrganizationDto orgDto = organizationMapper.selectById(id);
        if (orgDto == null) {
            return AjaxResult.fail("组织机构不存在");
        }

        // 检查是否有子组织
        List<OrganizationDto> children = organizationMapper.selectByParentId(id);
        if (CollUtil.isNotEmpty(children)) {
            return AjaxResult.fail("存在子组织，无法删除");
        }

        // 软删除：更新is_delete字段
        LambdaUpdateWrapper<OrganizationDto> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(OrganizationDto::getId, id)
                .set(OrganizationDto::getIsDelete, 1);
        organizationMapper.update(null, updateWrapper);

        return AjaxResult.success("删除成功");
    }

    // ==================== 业务自定义方法 ====================

    @Override
    public List<OrganizationVo> getOrgTree() {
        List<OrganizationDto> allOrgs = organizationMapper.selectAllOrdered();
        List<OrganizationVo> orgVoList = allOrgs.stream().map(this::convertToVo).collect(Collectors.toList());
        return buildTree(orgVoList, 0L);
    }

    // ==================== 私有方法 ====================

    /**
     * 构建树形结构
     */
    private List<OrganizationVo> buildTree(List<OrganizationVo> orgList, Long parentId) {
        List<OrganizationVo> tree = new ArrayList<>();
        for (OrganizationVo org : orgList) {
            if (parentId.equals(org.getParentId())) {
                org.setChildren(buildTree(orgList, org.getId()));
                tree.add(org);
            }
        }
        return tree;
    }

    /**
     * 转换为VO
     */
    private OrganizationVo convertToVo(OrganizationDto orgDto) {
        OrganizationVo orgVo = new OrganizationVo();
        BeanUtil.copyProperties(orgDto, orgVo);
        return orgVo;
    }
}
