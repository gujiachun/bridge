package com.rainbow.bridge.core.enums;

/**
 * canal server的数据库 事件
 * insert update delete三种事件
 * @author gujiachun
 */
public enum EventEnum {

    /**
     * 源数据 发送的新增
     * */
    insert(0),
    /**
     * 源数据 发送的修改
     * */
    update(1),
    /**
     * 源数据 发送的删除
     * */
    delete(2),
    ;

    private Integer status;

    EventEnum(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
