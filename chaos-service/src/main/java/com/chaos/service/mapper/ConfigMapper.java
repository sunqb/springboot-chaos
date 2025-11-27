package com.chaos.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.service.entity.bo.config.ConfigConditionBo;
import com.chaos.service.entity.dto.config.ConfigDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统配置 Mapper
 *
 * @author chaos
 */
@Mapper
public interface ConfigMapper extends BaseMapper<ConfigDto> {

    // ==================== 标准查询方法 ====================

    /**
     * 条件查询配置列表
     */
    List<ConfigDto> selectList(ConfigConditionBo condition);

    /**
     * 查询配置详情
     */
    ConfigDto selectDetail(@Param("id") Long id);

    // ==================== 业务自定义方法 ====================

    /**
     * 根据配置键查询
     */
    ConfigDto selectByConfigKey(@Param("configKey") String configKey);

    /**
     * 查询所有配置
     */
    List<ConfigDto> selectAll();
}
