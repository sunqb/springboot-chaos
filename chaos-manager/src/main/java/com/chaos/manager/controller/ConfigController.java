package com.chaos.manager.controller;

import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.dto.config.ConfigDto;
import com.chaos.service.service.IConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统配置管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/config")
@Tag(name = "系统配置管理", description = "系统配置CRUD操作")
public class ConfigController extends BaseController {

    @Resource
    private IConfigService configService;

    @GetMapping("/list")
    @Operation(summary = "查询配置列表")
    public AjaxResult list(
            @Parameter(description = "配置名称") @RequestParam(required = false) String configName,
            @Parameter(description = "配置键") @RequestParam(required = false) String configKey) {
        List<ConfigDto> list = configService.getConfigList(configName, configKey);
        return AjaxResult.success(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询配置详情")
    public AjaxResult getById(@Parameter(description = "配置ID") @PathVariable Long id) {
        ConfigDto config = configService.getConfigById(id);
        if (config == null) {
            return AjaxResult.fail("配置不存在");
        }
        return AjaxResult.success(config);
    }

    @GetMapping("/key/{configKey}")
    @Operation(summary = "根据Key查询配置值")
    public AjaxResult getByKey(@Parameter(description = "配置Key") @PathVariable String configKey) {
        String value = configService.getConfigValueByKey(configKey);
        return AjaxResult.success(value);
    }

    @PostMapping
    @Operation(summary = "新增配置")
    public AjaxResult add(@RequestBody ConfigDto configDto) {
        log.info("新增配置: {}", configDto.getConfigKey());
        return configService.addConfig(configDto);
    }

    @PutMapping
    @Operation(summary = "更新配置")
    public AjaxResult update(@RequestBody ConfigDto configDto) {
        log.info("更新配置: {}", configDto.getId());
        return configService.updateConfig(configDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除配置")
    public AjaxResult delete(@Parameter(description = "配置ID") @PathVariable Long id) {
        log.info("删除配置: {}", id);
        return configService.deleteConfig(id);
    }
}
