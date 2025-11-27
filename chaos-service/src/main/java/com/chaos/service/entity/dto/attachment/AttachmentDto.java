package com.chaos.service.entity.dto.attachment;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 附件表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_attachment")
public class AttachmentDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文件唯一标识
     */
    @TableField("file_key")
    private String fileKey;

    /**
     * 原文件名
     */
    @TableField("original_name")
    private String originalName;

    /**
     * 存储文件名
     */
    @TableField("store_name")
    private String storeName;

    /**
     * 文件后缀
     */
    @TableField("suffix")
    private String suffix;

    /**
     * 文件大小（字节）
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 存储路径
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 访问URL
     */
    @TableField("file_url")
    private String fileUrl;

    /**
     * 文件类型：1-图片，2-文档，3-视频，4-音频，5-其他
     */
    @TableField("file_type")
    private Integer fileType;

    /**
     * 存储类型：1-本地，2-OSS，3-MinIO
     */
    @TableField("storage_type")
    private Integer storageType;

    /**
     * 业务类型
     */
    @TableField("biz_type")
    private String bizType;

    /**
     * 业务ID
     */
    @TableField("biz_id")
    private Long bizId;

    /**
     * 状态：0-禁用，1-正常
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 删除标志：0-正常，1-删除
     */
    @TableField("del_flag")
    @TableLogic
    private Integer delFlag;
}
