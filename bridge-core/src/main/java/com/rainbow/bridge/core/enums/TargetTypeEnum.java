package com.rainbow.bridge.core.enums;

/**
 * @author gujiachun
 */
public enum TargetTypeEnum {

    /**
     * mysql类型
     * */
    mysql("mysql"),
    /**
     * es类型
     * */
    es("es"),
    /**
     * redis类型
     * */
    redis("redis"),
    /**
     * rocketmq类型
     * */
    rocketmq("rocketmq"),
    ;

    private String type;

    TargetTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }


}
