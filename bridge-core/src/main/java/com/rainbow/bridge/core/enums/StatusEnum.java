package com.rainbow.bridge.core.enums;

/**
 * @author gujiachun
 */
public enum StatusEnum {

    /**
     * 审核 未发布
     * */
    audit(0),
    /**
     * 已发布 有效
     * */
    valid(1),
    /**
     * 已发布 无效
     * */
    invalid(2),
    ;

    private Integer status;

    StatusEnum(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
