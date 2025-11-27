package com.chaos.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaos.common.constants.Constants;
import com.chaos.common.enums.ResultCode;
import com.chaos.common.exception.BusinessException;
import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.user.LoginBo;
import com.chaos.service.entity.bo.user.UserBo;
import com.chaos.service.entity.dto.user.UserDto;
import com.chaos.service.entity.vo.user.UserVo;
import com.chaos.service.mapper.UserMapper;
import com.chaos.service.service.IUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDto> implements IUserService {

    @Resource
    private UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserVo login(LoginBo loginBo) {
        // 查询用户
        UserDto user = userMapper.selectByUsername(loginBo.getUsername());
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        // 校验密码
        if (!passwordEncoder.matches(loginBo.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.USER_PASSWORD_ERROR);
        }

        // 校验状态
        if (!Constants.STATUS_ENABLE.equals(user.getStatus())) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        // 构建返回对象
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

    @Override
    public AjaxResult getUserInfo(Long userId) {
        UserVo userVo = userMapper.selectUserById(userId);
        if (userVo == null) {
            return AjaxResult.fail(ResultCode.USER_NOT_EXIST);
        }
        return AjaxResult.success(userVo);
    }

    @Override
    public List<UserVo> getUserList(String username, Integer status) {
        return userMapper.selectUserList(username, status);
    }

    @Override
    public UserVo getUserById(Long id) {
        return userMapper.selectUserById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addUser(UserBo userBo) {
        // 检查用户名是否存在
        UserDto existUser = userMapper.selectByUsername(userBo.getUsername());
        if (existUser != null) {
            return AjaxResult.fail(ResultCode.USER_EXIST);
        }

        // 构建用户实体
        UserDto user = new UserDto();
        BeanUtils.copyProperties(userBo, user);

        // 加密密码（默认密码123456）
        String password = userBo.getPassword();
        if (password == null || password.isEmpty()) {
            password = "123456";
        }
        user.setPassword(passwordEncoder.encode(password));

        // 设置默认状态
        if (user.getStatus() == null) {
            user.setStatus(Constants.STATUS_ENABLE);
        }

        // 保存用户
        boolean result = this.save(user);
        return result ? AjaxResult.success("新增成功") : AjaxResult.fail("新增失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateUser(UserBo userBo) {
        if (userBo.getId() == null) {
            return AjaxResult.fail(ResultCode.PARAM_ERROR, "用户ID不能为空");
        }

        // 查询原用户
        UserDto existUser = this.getById(userBo.getId());
        if (existUser == null) {
            return AjaxResult.fail(ResultCode.USER_NOT_EXIST);
        }

        // 如果修改了用户名，检查是否重复
        if (userBo.getUsername() != null && !userBo.getUsername().equals(existUser.getUsername())) {
            UserDto userByName = userMapper.selectByUsername(userBo.getUsername());
            if (userByName != null) {
                return AjaxResult.fail(ResultCode.USER_EXIST);
            }
        }

        // 更新用户信息
        UserDto updateUser = new UserDto();
        BeanUtils.copyProperties(userBo, updateUser);

        // 不更新密码
        updateUser.setPassword(null);

        boolean result = this.updateById(updateUser);
        return result ? AjaxResult.success("更新成功") : AjaxResult.fail("更新失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult deleteUser(Long id) {
        UserDto user = this.getById(id);
        if (user == null) {
            return AjaxResult.fail(ResultCode.USER_NOT_EXIST);
        }

        boolean result = this.removeById(id);
        return result ? AjaxResult.success("删除成功") : AjaxResult.fail("删除失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult resetPassword(Long id, String newPassword) {
        UserDto user = this.getById(id);
        if (user == null) {
            return AjaxResult.fail(ResultCode.USER_NOT_EXIST);
        }

        UserDto updateUser = new UserDto();
        updateUser.setId(id);
        updateUser.setPassword(passwordEncoder.encode(newPassword));

        boolean result = this.updateById(updateUser);
        return result ? AjaxResult.success("密码重置成功") : AjaxResult.fail("密码重置失败");
    }
}
