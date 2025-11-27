package com.chaos.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.service.entity.dto.user.UserDto;
import com.chaos.service.entity.vo.user.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 Mapper 接口
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDto> {

    /**
     * 根据用户名查询用户
     */
    UserDto selectByUsername(@Param("username") String username);

    /**
     * 查询用户列表
     */
    List<UserVo> selectUserList(@Param("username") String username,
                                @Param("status") Integer status);

    /**
     * 查询用户详情
     */
    UserVo selectUserById(@Param("id") Long id);
}
