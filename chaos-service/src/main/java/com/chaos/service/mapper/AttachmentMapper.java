package com.chaos.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.service.entity.dto.attachment.AttachmentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 附件 Mapper
 */
@Mapper
public interface AttachmentMapper extends BaseMapper<AttachmentDto> {

    /**
     * 根据文件Key查询
     */
    AttachmentDto selectByFileKey(@Param("fileKey") String fileKey);
}
