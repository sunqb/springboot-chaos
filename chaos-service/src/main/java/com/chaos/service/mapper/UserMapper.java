package com.chaos.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.service.entity.bo.user.UserConditionBo;
import com.chaos.service.entity.dto.user.UserDto;
import com.chaos.service.entity.vo.user.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 Mapper 接口
 *
 * @author chaos
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDto> {

    // ==================== 标准查询方法 ====================

    /**
     * 条件查询用户列表
     */
    List<UserVo> selectList(UserConditionBo condition);

    /**
     * 查询用户详情
     */
    UserVo selectDetail(@Param("id") Long id);

    // ==================== 业务自定义方法 ====================

    /**
     * 根据账号查询用户
     */
    UserDto selectByAccount(@Param("account") String account);

    /**
     * 根据oid查询用户
     */
    UserDto selectByOid(@Param("oid") String oid);

    /**
     * 根据手机号查询用户
     */
    UserDto selectByPhone(@Param("phone") String phone);
}
