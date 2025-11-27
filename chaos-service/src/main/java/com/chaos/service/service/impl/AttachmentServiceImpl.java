package com.chaos.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.chaos.common.vo.AjaxResult;
import com.chaos.service.entity.bo.attachment.AttachmentBo;
import com.chaos.service.entity.bo.attachment.AttachmentConditionBo;
import com.chaos.service.entity.dto.attachment.AttachmentDto;
import com.chaos.service.mapper.AttachmentMapper;
import com.chaos.service.service.IAttachmentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 附件服务实现
 *
 * @author chaos
 */
@Slf4j
@Service
public class AttachmentServiceImpl implements IAttachmentService {

    @Resource
    private AttachmentMapper attachmentMapper;

    // ==================== 标准CRUD方法 ====================

    @Override
    public List<AttachmentDto> list(AttachmentConditionBo condition) {
        return attachmentMapper.selectList(condition);
    }

    @Override
    public PageInfo<AttachmentDto> page(AttachmentConditionBo condition) {
        PageHelper.startPage(condition.getPage(), condition.getLimit(), condition.getOrderBy());
        List<AttachmentDto> list = attachmentMapper.selectList(condition);
        return new PageInfo<>(list);
    }

    @Override
    public AttachmentDto getById(Long id) {
        return attachmentMapper.selectDetail(id);
    }

    @Override
    public AjaxResult add(AttachmentBo bo) {
        AttachmentDto attachmentDto = new AttachmentDto();
        BeanUtil.copyProperties(bo, attachmentDto);
        // 生成oid
        if (StrUtil.isBlank(attachmentDto.getOid())) {
            attachmentDto.setOid(IdUtil.fastSimpleUUID());
        }
        attachmentDto.setState(1);
        attachmentDto.setIsDelete(0);
        attachmentMapper.insert(attachmentDto);
        return AjaxResult.success("新增成功");
    }

    @Override
    public AjaxResult update(AttachmentBo bo) {
        if (bo.getId() == null) {
            return AjaxResult.fail("ID不能为空");
        }

        AttachmentDto existAttachment = attachmentMapper.selectById(bo.getId());
        if (existAttachment == null) {
            return AjaxResult.fail("附件不存在");
        }

        AttachmentDto updateDto = new AttachmentDto();
        BeanUtil.copyProperties(bo, updateDto);
        attachmentMapper.updateById(updateDto);
        return AjaxResult.success("更新成功");
    }

    @Override
    public AjaxResult delete(Long id) {
        AttachmentDto attachmentDto = attachmentMapper.selectById(id);
        if (attachmentDto == null) {
            return AjaxResult.fail("附件不存在");
        }

        // 软删除：更新is_delete字段
        LambdaUpdateWrapper<AttachmentDto> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AttachmentDto::getId, id)
                .set(AttachmentDto::getIsDelete, 1);
        attachmentMapper.update(null, updateWrapper);

        return AjaxResult.success("删除成功");
    }

    // ==================== 业务自定义方法 ====================

    @Override
    public AttachmentDto getByOid(String oid) {
        return attachmentMapper.selectByOid(oid);
    }

}
