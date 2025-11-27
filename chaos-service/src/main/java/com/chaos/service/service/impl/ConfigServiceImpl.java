package com.chaos.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.dto.config.ConfigDto;
import com.chaos.service.mapper.ConfigMapper;
import com.chaos.service.service.IConfigService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统配置服务实现
 */
@Slf4j
@Service
public class ConfigServiceImpl implements IConfigService {

    @Resource
    private ConfigMapper configMapper;

    @Override
    public List<ConfigDto> getConfigList(String configName, String configKey) {
        LambdaQueryWrapper<ConfigDto> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(configName), ConfigDto::getConfigName, configName)
                .like(StringUtils.isNotBlank(configKey), ConfigDto::getConfigKey, configKey)
                .orderByDesc(ConfigDto::getCreateTime);
        return configMapper.selectList(wrapper);
    }

    @Override
    public String getConfigValueByKey(String configKey) {
        ConfigDto configDto = configMapper.selectByConfigKey(configKey);
        return configDto == null ? null : configDto.getConfigValue();
    }

    @Override
    public ConfigDto getConfigById(Long id) {
        return configMapper.selectById(id);
    }

    @Override
    public AjaxResult addConfig(ConfigDto configDto) {
        // 检查配置键是否存在
        ConfigDto existConfig = configMapper.selectByConfigKey(configDto.getConfigKey());
        if (existConfig != null) {
            return AjaxResult.fail("配置键已存在");
        }

        configMapper.insert(configDto);
        return AjaxResult.success("新增成功");
    }

    @Override
    public AjaxResult updateConfig(ConfigDto configDto) {
        if (configDto.getId() == null) {
            return AjaxResult.fail("ID不能为空");
        }

        ConfigDto existConfig = configMapper.selectById(configDto.getId());
        if (existConfig == null) {
            return AjaxResult.fail("配置不存在");
        }

        // 检查配置键是否被其他配置使用
        ConfigDto keyExist = configMapper.selectByConfigKey(configDto.getConfigKey());
        if (keyExist != null && !keyExist.getId().equals(configDto.getId())) {
            return AjaxResult.fail("配置键已存在");
        }

        configMapper.updateById(configDto);
        return AjaxResult.success("更新成功");
    }

    @Override
    public AjaxResult deleteConfig(Long id) {
        ConfigDto configDto = configMapper.selectById(id);
        if (configDto == null) {
            return AjaxResult.fail("配置不存在");
        }

        // 系统内置配置不允许删除
        if (configDto.getConfigType() != null && configDto.getConfigType() == 0) {
            return AjaxResult.fail("系统内置配置不允许删除");
        }

        configMapper.deleteById(id);
        return AjaxResult.success("删除成功");
    }
}
