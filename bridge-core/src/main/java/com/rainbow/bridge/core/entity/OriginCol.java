package com.rainbow.bridge.core.entity;

/**
 * 新增时 新增来源
 * @author gujiachun
 */
public class OriginCol {

    private String origin;
    private String value;

    public OriginCol(String origin, String value) {
        this.origin = origin;
        this.value = value;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
