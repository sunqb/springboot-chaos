package com.chaos.manager.controller;

import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.org.OrganizationBo;
import com.chaos.service.entity.vo.org.OrganizationVo;
import com.chaos.service.service.IOrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 组织机构管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/organization")
@Tag(name = "组织机构管理", description = "组织机构CRUD操作")
public class OrganizationController extends BaseController {

    @Resource
    private IOrganizationService organizationService;

    @GetMapping("/tree")
    @Operation(summary = "查询组织机构树")
    public AjaxResult tree() {
        List<OrganizationVo> tree = organizationService.getOrgTree();
        return AjaxResult.success(tree);
    }

    @GetMapping("/list")
    @Operation(summary = "查询组织机构列表")
    public AjaxResult list(
            @Parameter(description = "组织名称") @RequestParam(required = false) String orgName,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        List<OrganizationVo> list = organizationService.getOrgList(orgName, status);
        return AjaxResult.success(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询组织机构详情")
    public AjaxResult getById(@Parameter(description = "组织ID") @PathVariable Long id) {
        OrganizationVo org = organizationService.getOrgById(id);
        if (org == null) {
            return AjaxResult.fail("组织机构不存在");
        }
        return AjaxResult.success(org);
    }

    @PostMapping
    @Operation(summary = "新增组织机构")
    public AjaxResult add(@Valid @RequestBody OrganizationBo orgBo) {
        log.info("新增组织机构: {}", orgBo.getOrgName());
        return organizationService.addOrg(orgBo);
    }

    @PutMapping
    @Operation(summary = "更新组织机构")
    public AjaxResult update(@Valid @RequestBody OrganizationBo orgBo) {
        log.info("更新组织机构: {}", orgBo.getId());
        return organizationService.updateOrg(orgBo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除组织机构")
    public AjaxResult delete(@Parameter(description = "组织ID") @PathVariable Long id) {
        log.info("删除组织机构: {}", id);
        return organizationService.deleteOrg(id);
    }
}
