package com.rainbow.bridge.core.entity;

/**
 * 主键实体
 * @author gujiachun
 */
public class Pk {

    private String pkCol;

    private Object pkValue;

    public Pk(String pkCol, Object pkValue) {
        this.pkCol = pkCol;
        this.pkValue = pkValue;
    }

    public String getPkCol() {
        return pkCol;
    }

    public void setPkCol(String pkCol) {
        this.pkCol = pkCol;
    }

    public Object getPkValue() {
        return pkValue;
    }

    public void setPkValue(Object pkValue) {
        this.pkValue = pkValue;
    }
}
