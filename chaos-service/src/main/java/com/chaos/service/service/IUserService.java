package com.chaos.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.user.LoginBo;
import com.chaos.service.entity.bo.user.UserBo;
import com.chaos.service.entity.dto.user.UserDto;
import com.chaos.service.entity.vo.user.UserVo;

import java.util.List;

/**
 * 用户服务接口
 */
public interface IUserService extends IService<UserDto> {

    /**
     * 用户登录
     *
     * @param loginBo 登录参数
     * @return 用户信息
     */
    UserVo login(LoginBo loginBo);

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    AjaxResult getUserInfo(Long userId);

    /**
     * 查询用户列表
     *
     * @param username 用户名（模糊查询）
     * @param status   状态
     * @return 用户列表
     */
    List<UserVo> getUserList(String username, Integer status);

    /**
     * 根据ID查询用户详情
     *
     * @param id 用户ID
     * @return 用户详情
     */
    UserVo getUserById(Long id);

    /**
     * 新增用户
     *
     * @param userBo 用户信息
     * @return 结果
     */
    AjaxResult addUser(UserBo userBo);

    /**
     * 更新用户
     *
     * @param userBo 用户信息
     * @return 结果
     */
    AjaxResult updateUser(UserBo userBo);

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 结果
     */
    AjaxResult deleteUser(Long id);

    /**
     * 重置密码
     *
     * @param id          用户ID
     * @param newPassword 新密码
     * @return 结果
     */
    AjaxResult resetPassword(Long id, String newPassword);
}
