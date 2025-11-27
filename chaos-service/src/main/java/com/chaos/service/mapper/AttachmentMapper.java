package com.chaos.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.service.entity.bo.attachment.AttachmentConditionBo;
import com.chaos.service.entity.dto.attachment.AttachmentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 附件 Mapper
 *
 * @author chaos
 */
@Mapper
public interface AttachmentMapper extends BaseMapper<AttachmentDto> {

    // ==================== 标准查询方法 ====================

    /**
     * 条件查询附件列表
     */
    List<AttachmentDto> selectList(AttachmentConditionBo condition);

    /**
     * 查询附件详情
     */
    AttachmentDto selectDetail(@Param("id") Long id);

    // ==================== 业务自定义方法 ====================

    /**
     * 根据oid查询附件
     */
    AttachmentDto selectByOid(@Param("oid") String oid);

    /**
     * 根据业务类型和业务ID查询附件
     */
    List<AttachmentDto> selectByBiz(@Param("bizType") String bizType, @Param("bizId") Long bizId);

    /**
     * 查询所有附件
     */
    List<AttachmentDto> selectAll();
}
