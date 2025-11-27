package com.chaos.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.chaos.common.exception.BusinessException;
import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.user.LoginBo;
import com.chaos.service.entity.bo.user.UserBo;
import com.chaos.service.entity.bo.user.UserConditionBo;
import com.chaos.service.entity.dto.user.UserDto;
import com.chaos.service.entity.vo.user.UserVo;
import com.chaos.service.mapper.UserMapper;
import com.chaos.service.service.IUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户服务实现
 *
 * @author chaos
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ==================== 标准CRUD方法 ====================

    @Override
    public List<UserVo> list(UserConditionBo condition) {
        return userMapper.selectList(condition);
    }

    @Override
    public PageInfo<UserVo> page(UserConditionBo condition) {
        PageHelper.startPage(condition.getPage(), condition.getLimit(), condition.getOrderBy());
        List<UserVo> list = userMapper.selectList(condition);
        return new PageInfo<>(list);
    }

    @Override
    public UserVo getById(Long id) {
        return userMapper.selectDetail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult add(UserBo bo) {
        // 检查账号是否存在
        UserDto existUser = userMapper.selectByAccount(bo.getAccount());
        if (existUser != null) {
            return AjaxResult.fail("账号已存在");
        }

        // 检查手机号是否存在
        if (StringUtils.isNotBlank(bo.getPhone())) {
            UserDto phoneUser = userMapper.selectByPhone(bo.getPhone());
            if (phoneUser != null) {
                return AjaxResult.fail("手机号已存在");
            }
        }

        UserDto userDto = new UserDto();
        BeanUtil.copyProperties(bo, userDto);
        // 生成oid
        userDto.setOid(IdUtil.fastSimpleUUID());
        // 加密密码
        String password = StringUtils.isBlank(bo.getPassword()) ? "123456" : bo.getPassword();
        userDto.setPassword(passwordEncoder.encode(password));
        userDto.setIsLocked(0);
        userDto.setIsDelete(0);
        userMapper.insert(userDto);

        return AjaxResult.success("新增成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult update(UserBo bo) {
        if (bo.getId() == null) {
            return AjaxResult.fail("ID不能为空");
        }

        UserDto userDto = userMapper.selectById(bo.getId());
        if (userDto == null) {
            return AjaxResult.fail("用户不存在");
        }

        // 检查账号是否被其他人使用
        UserDto existUser = userMapper.selectByAccount(bo.getAccount());
        if (existUser != null && !existUser.getId().equals(bo.getId())) {
            return AjaxResult.fail("账号已存在");
        }

        // 检查手机号是否被其他人使用
        if (StringUtils.isNotBlank(bo.getPhone())) {
            UserDto phoneUser = userMapper.selectByPhone(bo.getPhone());
            if (phoneUser != null && !phoneUser.getId().equals(bo.getId())) {
                return AjaxResult.fail("手机号已存在");
            }
        }

        UserDto updateDto = new UserDto();
        BeanUtil.copyProperties(bo, updateDto);
        updateDto.setPassword(null); // 不更新密码
        userMapper.updateById(updateDto);

        return AjaxResult.success("更新成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult delete(Long id) {
        UserDto userDto = userMapper.selectById(id);
        if (userDto == null) {
            return AjaxResult.fail("用户不存在");
        }

        // 软删除：更新is_delete字段
        LambdaUpdateWrapper<UserDto> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserDto::getId, id)
                .set(UserDto::getIsDelete, 1);
        userMapper.update(null, updateWrapper);

        return AjaxResult.success("删除成功");
    }

    // ==================== 业务自定义方法 ====================

    @Override
    public UserVo login(LoginBo loginBo) {
        UserDto userDto = userMapper.selectByAccount(loginBo.getAccount());
        if (userDto == null) {
            throw new BusinessException("账号或密码错误");
        }

        if (!passwordEncoder.matches(loginBo.getPassword(), userDto.getPassword())) {
            throw new BusinessException("账号或密码错误");
        }

        if (userDto.getIsLocked() != null && userDto.getIsLocked() == 1) {
            throw new BusinessException("账号已被锁定");
        }

        // 更新登录时间
        UserDto updateDto = new UserDto();
        updateDto.setId(userDto.getId());
        updateDto.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(updateDto);

        return convertToVo(userDto);
    }

    @Override
    public UserVo getByOid(String oid) {
        UserDto userDto = userMapper.selectByOid(oid);
        return userDto == null ? null : convertToVo(userDto);
    }

    @Override
    public AjaxResult getUserInfo(Long userId) {
        UserDto userDto = userMapper.selectById(userId);
        if (userDto == null) {
            return AjaxResult.fail("用户不存在");
        }
        return AjaxResult.success(convertToVo(userDto));
    }

    @Override
    public AjaxResult resetPassword(Long id, String newPassword) {
        UserDto userDto = userMapper.selectById(id);
        if (userDto == null) {
            return AjaxResult.fail("用户不存在");
        }

        String password = StringUtils.isBlank(newPassword) ? "123456" : newPassword;
        UserDto updateDto = new UserDto();
        updateDto.setId(id);
        updateDto.setPassword(passwordEncoder.encode(password));
        userMapper.updateById(updateDto);

        return AjaxResult.success("密码重置成功");
    }

    @Override
    public AjaxResult updateLockStatus(Long id, Integer isLocked) {
        UserDto userDto = userMapper.selectById(id);
        if (userDto == null) {
            return AjaxResult.fail("用户不存在");
        }

        UserDto updateDto = new UserDto();
        updateDto.setId(id);
        updateDto.setIsLocked(isLocked);
        userMapper.updateById(updateDto);

        return AjaxResult.success("状态更新成功");
    }

    // ==================== 私有方法 ====================

    /**
     * 转换为VO
     */
    private UserVo convertToVo(UserDto userDto) {
        UserVo userVo = new UserVo();
        BeanUtil.copyProperties(userDto, userVo);
        return userVo;
    }
}
