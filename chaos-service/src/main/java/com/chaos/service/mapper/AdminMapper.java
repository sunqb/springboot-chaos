package com.chaos.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaos.service.entity.bo.admin.AdminConditionBo;
import com.chaos.service.entity.dto.admin.AdminDto;
import com.chaos.service.entity.vo.admin.AdminVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 管理员 Mapper
 *
 * @author chaos
 */
@Mapper
public interface AdminMapper extends BaseMapper<AdminDto> {

    // ==================== 标准查询方法 ====================

    /**
     * 条件查询管理员列表
     */
    List<AdminVo> selectList(AdminConditionBo condition);

    /**
     * 查询管理员详情
     */
    AdminVo selectDetail(@Param("id") Long id);

    // ==================== 业务自定义方法 ====================

    /**
     * 根据账号名查询管理员
     */
    AdminDto selectByAccountName(@Param("accountName") String accountName);

    /**
     * 根据oid查询管理员
     */
    AdminDto selectByOid(@Param("oid") String oid);

    /**
     * 查询管理员的角色ID列表
     */
    List<Long> selectRoleIdsByAdminOid(@Param("adminOid") String adminOid);

    /**
     * 查询管理员的权限列表
     */
    List<String> selectPermissionsByAdminOid(@Param("adminOid") String adminOid);
}
