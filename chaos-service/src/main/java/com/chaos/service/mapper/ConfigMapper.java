package com.chaos.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.service.entity.dto.config.ConfigDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统配置 Mapper
 */
@Mapper
public interface ConfigMapper extends BaseMapper<ConfigDto> {

    /**
     * 根据配置键查询
     */
    ConfigDto selectByConfigKey(@Param("configKey") String configKey);
}
