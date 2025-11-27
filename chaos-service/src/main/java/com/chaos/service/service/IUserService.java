package com.chaos.service.service;

import com.chaos.common.vo.AjaxResult;
import com.github.pagehelper.PageInfo;
import com.chaos.service.entity.bo.user.LoginBo;
import com.chaos.service.entity.bo.user.UserBo;
import com.chaos.service.entity.bo.user.UserConditionBo;
import com.chaos.service.entity.vo.user.UserVo;

import java.util.List;

/**
 * 用户服务接口
 *
 * @author chaos
 */
public interface IUserService {

    // ==================== 标准CRUD方法 ====================

    /**
     * 列表查询
     */
    List<UserVo> list(UserConditionBo condition);

    /**
     * 分页查询
     */
    PageInfo<UserVo> page(UserConditionBo condition);

    /**
     * 根据ID查询
     */
    UserVo getById(Long id);

    /**
     * 新增
     */
    AjaxResult add(UserBo bo);

    /**
     * 更新
     */
    AjaxResult update(UserBo bo);

    /**
     * 删除（软删除）
     */
    AjaxResult delete(Long id);

    // ==================== 业务自定义方法 ====================

    /**
     * 用户登录
     */
    UserVo login(LoginBo loginBo);

    /**
     * 根据oid查询用户
     */
    UserVo getByOid(String oid);

    /**
     * 获取用户信息
     */
    AjaxResult getUserInfo(Long userId);

    /**
     * 重置密码
     */
    AjaxResult resetPassword(Long id, String newPassword);

    /**
     * 更新锁定状态
     */
    AjaxResult updateLockStatus(Long id, Integer isLocked);
}
