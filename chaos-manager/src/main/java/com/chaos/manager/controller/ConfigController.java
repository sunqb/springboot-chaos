package com.chaos.manager.controller;

import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.config.ConfigBo;
import com.chaos.service.entity.bo.config.ConfigConditionBo;
import com.chaos.service.entity.dto.config.ConfigDto;
import com.chaos.service.service.IConfigService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统配置管理控制器
 *
 * @author chaos
 */
@Slf4j
@RestController
@RequestMapping("/config")
@Tag(name = "系统配置管理", description = "系统配置CRUD操作")
public class ConfigController extends BaseController {

    @Resource
    private IConfigService configService;

    // ==================== 标准CRUD接口 ====================

    @GetMapping("/list")
    @Operation(summary = "查询配置列表")
    public AjaxResult list(ConfigConditionBo condition) {
        List<ConfigDto> list = configService.list(condition);
        return AjaxResult.success(list);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询配置列表")
    public AjaxResult page(ConfigConditionBo condition) {
        PageInfo<ConfigDto> page = configService.page(condition);
        return AjaxResult.success(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询配置详情")
    public AjaxResult getById(@Parameter(description = "配置ID") @PathVariable Long id) {
        ConfigDto config = configService.getById(id);
        if (config == null) {
            return AjaxResult.fail("配置不存在");
        }
        return AjaxResult.success(config);
    }

    @PostMapping
    @Operation(summary = "新增配置")
    public AjaxResult add(@Valid @RequestBody ConfigBo bo) {
        log.info("新增配置: {}", bo.getConfigKey());
        return configService.add(bo);
    }

    @PutMapping
    @Operation(summary = "更新配置")
    public AjaxResult update(@Valid @RequestBody ConfigBo bo) {
        log.info("更新配置: {}", bo.getId());
        return configService.update(bo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除配置")
    public AjaxResult delete(@Parameter(description = "配置ID") @PathVariable Long id) {
        log.info("删除配置: {}", id);
        return configService.delete(id);
    }

    // ==================== 业务自定义接口 ====================

    @GetMapping("/key/{configKey}")
    @Operation(summary = "根据Key查询配置值")
    public AjaxResult getByKey(@Parameter(description = "配置Key") @PathVariable String configKey) {
        String value = configService.getConfigValueByKey(configKey);
        return AjaxResult.success(value);
    }
}
