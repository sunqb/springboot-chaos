package com.chaos.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.config.ConfigBo;
import com.chaos.service.entity.bo.config.ConfigConditionBo;
import com.chaos.service.entity.dto.config.ConfigDto;
import com.chaos.service.mapper.ConfigMapper;
import com.chaos.service.service.IConfigService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统配置服务实现
 *
 * @author chaos
 */
@Slf4j
@Service
public class ConfigServiceImpl implements IConfigService {

    @Resource
    private ConfigMapper configMapper;

    // ==================== 标准CRUD方法 ====================

    @Override
    public List<ConfigDto> list(ConfigConditionBo condition) {
        return configMapper.selectList(condition);
    }

    @Override
    public PageInfo<ConfigDto> page(ConfigConditionBo condition) {
        PageHelper.startPage(condition.getPage(), condition.getLimit(), condition.getOrderBy());
        List<ConfigDto> list = configMapper.selectList(condition);
        return new PageInfo<>(list);
    }

    @Override
    public ConfigDto getById(Long id) {
        return configMapper.selectDetail(id);
    }

    @Override
    public AjaxResult add(ConfigBo bo) {
        // 检查配置键是否存在
        ConfigDto existConfig = configMapper.selectByConfigKey(bo.getConfigKey());
        if (existConfig != null) {
            return AjaxResult.fail("配置键已存在");
        }

        ConfigDto configDto = new ConfigDto();
        BeanUtil.copyProperties(bo, configDto);
        configDto.setIsDelete(0);
        configMapper.insert(configDto);
        return AjaxResult.success("新增成功");
    }

    @Override
    public AjaxResult update(ConfigBo bo) {
        if (bo.getId() == null) {
            return AjaxResult.fail("ID不能为空");
        }

        ConfigDto existConfig = configMapper.selectById(bo.getId());
        if (existConfig == null) {
            return AjaxResult.fail("配置不存在");
        }

        // 检查配置键是否被其他配置使用
        ConfigDto keyExist = configMapper.selectByConfigKey(bo.getConfigKey());
        if (keyExist != null && !keyExist.getId().equals(bo.getId())) {
            return AjaxResult.fail("配置键已存在");
        }

        ConfigDto updateDto = new ConfigDto();
        BeanUtil.copyProperties(bo, updateDto);
        configMapper.updateById(updateDto);
        return AjaxResult.success("更新成功");
    }

    @Override
    public AjaxResult delete(Long id) {
        ConfigDto configDto = configMapper.selectById(id);
        if (configDto == null) {
            return AjaxResult.fail("配置不存在");
        }

        // 软删除：更新is_delete字段
        LambdaUpdateWrapper<ConfigDto> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ConfigDto::getId, id)
                .set(ConfigDto::getIsDelete, 1);
        configMapper.update(null, updateWrapper);

        return AjaxResult.success("删除成功");
    }

    // ==================== 业务自定义方法 ====================

    @Override
    public String getConfigValueByKey(String configKey) {
        ConfigDto configDto = configMapper.selectByConfigKey(configKey);
        return configDto == null ? null : configDto.getConfigValue();
    }

}
