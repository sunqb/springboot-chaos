package com.chaos.common.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 基础分页查询条件
 *
 * @author chaos
 */
@Data
@Accessors(chain = true)
public class BaseCondition implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码（默认1）
     */
    private Integer page = 1;

    /**
     * 每页条数（默认10）
     */
    private Integer limit = 10;

    /**
     * 起始位置（自动计算）
     */
    private Integer start;

    /**
     * 排序字段，格式：字段名 asc/desc
     */
    private String orderBy;

    public void setPage(Integer page) {
        this.page = page;
        if (this.limit != null) {
            this.start = (this.page - 1) * limit;
        }
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
        if (this.page != null) {
            this.start = (this.page - 1) * limit;
        }
    }
}
