package com.chaos.service.service;

import com.chaos.common.vo.AjaxResult;
import com.github.pagehelper.PageInfo;
import com.chaos.service.entity.bo.attachment.AttachmentBo;
import com.chaos.service.entity.bo.attachment.AttachmentConditionBo;
import com.chaos.service.entity.dto.attachment.AttachmentDto;

import java.util.List;

/**
 * 附件服务接口
 *
 * @author chaos
 */
public interface IAttachmentService {

    // ==================== 标准CRUD方法 ====================

    /**
     * 列表查询
     */
    List<AttachmentDto> list(AttachmentConditionBo condition);

    /**
     * 分页查询
     */
    PageInfo<AttachmentDto> page(AttachmentConditionBo condition);

    /**
     * 根据ID查询
     */
    AttachmentDto getById(Long id);

    /**
     * 新增
     */
    AjaxResult add(AttachmentBo bo);

    /**
     * 更新
     */
    AjaxResult update(AttachmentBo bo);

    /**
     * 删除（软删除）
     */
    AjaxResult delete(Long id);

    // ==================== 业务自定义方法 ====================

    /**
     * 根据oid查询附件
     */
    AttachmentDto getByOid(String oid);
}
