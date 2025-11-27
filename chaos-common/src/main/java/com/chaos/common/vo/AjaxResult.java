package com.chaos.common.vo;

import com.chaos.common.constants.Constants;
import com.chaos.common.enums.ResultCode;
import com.chaos.common.utils.DateTimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 统一响应结果
 */
@Data
@Schema(description = "统一响应结果")
public class AjaxResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "状态码")
    private Integer code = 200;

    @Schema(description = "消息")
    private String msg;

    @Schema(description = "数据")
    private Object data;

    @Schema(description = "是否成功")
    private Boolean success;

    @Schema(description = "时间戳")
    private String time;

    public AjaxResult() {
        this.time = DateTimeUtil.formatDatetime(new Date());
    }

    // ========== 成功响应 ==========

    public static AjaxResult success() {
        AjaxResult result = new AjaxResult();
        result.setSuccess(true);
        result.setCode(Constants.CODE_SUCCESS);
        result.setMsg("操作成功");
        return result;
    }

    public static AjaxResult success(Object data) {
        AjaxResult result = success();
        result.setData(data);
        return result;
    }

    public static AjaxResult success(String msg) {
        AjaxResult result = success();
        result.setMsg(msg);
        return result;
    }

    public static AjaxResult success(Object data, String msg) {
        AjaxResult result = success();
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    // ========== 失败响应 ==========

    public static AjaxResult fail() {
        AjaxResult result = new AjaxResult();
        result.setSuccess(false);
        result.setCode(Constants.CODE_ERROR);
        result.setMsg("操作失败");
        return result;
    }

    public static AjaxResult fail(String msg) {
        AjaxResult result = fail();
        result.setMsg(msg);
        return result;
    }

    public static AjaxResult fail(Integer code, String msg) {
        AjaxResult result = new AjaxResult();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static AjaxResult fail(ResultCode resultCode) {
        return fail(resultCode.getCode(), resultCode.getMessage());
    }

    public static AjaxResult fail(ResultCode resultCode, String msg) {
        return fail(resultCode.getCode(), msg);
    }

    // ========== 警告响应 ==========

    public static AjaxResult warn(String msg) {
        AjaxResult result = new AjaxResult();
        result.setSuccess(false);
        result.setCode(Constants.CODE_WARN);
        result.setMsg(msg);
        return result;
    }

    // ========== 未登录响应 ==========

    public static AjaxResult unauthorized() {
        return fail(Constants.CODE_UNAUTHORIZED, "请先登录");
    }

    public static AjaxResult unauthorized(String msg) {
        return fail(Constants.CODE_UNAUTHORIZED, msg);
    }

    // ========== 权限不足响应 ==========

    public static AjaxResult forbidden() {
        return fail(Constants.CODE_FORBIDDEN, "权限不足");
    }

    public static AjaxResult forbidden(String msg) {
        return fail(Constants.CODE_FORBIDDEN, msg);
    }

    // ========== 辅助方法 ==========

    /**
     * 判断是否失败
     */
    public Boolean isFailed() {
        return !Constants.CODE_SUCCESS.equals(this.code);
    }

    /**
     * 判断是否成功
     */
    public Boolean isSuccess() {
        return Constants.CODE_SUCCESS.equals(this.code);
    }
}
