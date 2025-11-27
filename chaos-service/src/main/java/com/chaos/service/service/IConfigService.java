package com.chaos.service.service;

import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.dto.config.ConfigDto;

import java.util.List;

/**
 * 系统配置服务接口
 */
public interface IConfigService {

    /**
     * 查询配置列表
     */
    List<ConfigDto> getConfigList(String configName, String configKey);

    /**
     * 根据Key查询配置值
     */
    String getConfigValueByKey(String configKey);

    /**
     * 根据ID查询配置
     */
    ConfigDto getConfigById(Long id);

    /**
     * 新增配置
     */
    AjaxResult addConfig(ConfigDto configDto);

    /**
     * 更新配置
     */
    AjaxResult updateConfig(ConfigDto configDto);

    /**
     * 删除配置
     */
    AjaxResult deleteConfig(Long id);
}
