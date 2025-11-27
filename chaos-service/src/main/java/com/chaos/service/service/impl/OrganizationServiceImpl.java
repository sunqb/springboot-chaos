package com.chaos.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.org.OrganizationBo;
import com.chaos.service.entity.dto.org.OrganizationDto;
import com.chaos.service.entity.vo.org.OrganizationVo;
import com.chaos.service.mapper.OrganizationMapper;
import com.chaos.service.service.IOrganizationService;
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
 */
@Slf4j
@Service
public class OrganizationServiceImpl implements IOrganizationService {

    @Resource
    private OrganizationMapper organizationMapper;

    @Override
    public List<OrganizationVo> getOrgTree() {
        List<OrganizationDto> allOrgs = organizationMapper.selectAllOrdered();
        List<OrganizationVo> orgVoList = allOrgs.stream().map(this::convertToVo).collect(Collectors.toList());
        return buildTree(orgVoList, 0L);
    }

    @Override
    public List<OrganizationVo> getOrgList(String orgName, Integer status) {
        LambdaQueryWrapper<OrganizationDto> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(orgName), OrganizationDto::getOrgName, orgName)
                .eq(status != null, OrganizationDto::getStatus, status)
                .orderByAsc(OrganizationDto::getSort);
        List<OrganizationDto> list = organizationMapper.selectList(wrapper);
        return list.stream().map(this::convertToVo).collect(Collectors.toList());
    }

    @Override
    public OrganizationVo getOrgById(Long id) {
        OrganizationDto orgDto = organizationMapper.selectById(id);
        return orgDto == null ? null : convertToVo(orgDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addOrg(OrganizationBo orgBo) {
        // 检查组织编码是否存在
        if (StringUtils.isNotBlank(orgBo.getOrgCode())) {
            OrganizationDto existOrg = organizationMapper.selectByOrgCode(orgBo.getOrgCode());
            if (existOrg != null) {
                return AjaxResult.fail("组织编码已存在");
            }
        }

        OrganizationDto orgDto = new OrganizationDto();
        BeanUtil.copyProperties(orgBo, orgDto);
        if (orgDto.getParentId() == null) {
            orgDto.setParentId(0L);
        }

        // 设置祖级列表
        if (orgDto.getParentId() == 0L) {
            orgDto.setAncestors("0");
        } else {
            OrganizationDto parentOrg = organizationMapper.selectById(orgDto.getParentId());
            if (parentOrg != null) {
                orgDto.setAncestors(parentOrg.getAncestors() + "," + orgDto.getParentId());
            }
        }

        orgDto.setStatus(1);
        orgDto.setDelFlag(0);
        organizationMapper.insert(orgDto);
        return AjaxResult.success("新增成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateOrg(OrganizationBo orgBo) {
        if (orgBo.getId() == null) {
            return AjaxResult.fail("ID不能为空");
        }

        OrganizationDto orgDto = organizationMapper.selectById(orgBo.getId());
        if (orgDto == null) {
            return AjaxResult.fail("组织机构不存在");
        }

        // 检查组织编码是否被其他组织使用
        if (StringUtils.isNotBlank(orgBo.getOrgCode())) {
            OrganizationDto existOrg = organizationMapper.selectByOrgCode(orgBo.getOrgCode());
            if (existOrg != null && !existOrg.getId().equals(orgBo.getId())) {
                return AjaxResult.fail("组织编码已存在");
            }
        }

        OrganizationDto updateDto = new OrganizationDto();
        BeanUtil.copyProperties(orgBo, updateDto);
        organizationMapper.updateById(updateDto);

        return AjaxResult.success("更新成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult deleteOrg(Long id) {
        OrganizationDto orgDto = organizationMapper.selectById(id);
        if (orgDto == null) {
            return AjaxResult.fail("组织机构不存在");
        }

        // 检查是否有子组织
        List<OrganizationDto> children = organizationMapper.selectByParentId(id);
        if (CollUtil.isNotEmpty(children)) {
            return AjaxResult.fail("存在子组织，无法删除");
        }

        // 删除组织
        organizationMapper.deleteById(id);

        return AjaxResult.success("删除成功");
    }

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
