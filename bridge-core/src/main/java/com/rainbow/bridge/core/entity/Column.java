package com.rainbow.bridge.core.entity;

/**
 * 列实体
 * @author gujiachun
 */
public class Column {

    private String colName;

    private Object colValue;

    public Column(String colName, Object colValue) {
        this.colName = colName;
        this.colValue = colValue;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public Object getColValue() {
        return colValue;
    }

    public void setColValue(Object colValue) {
        this.colValue = colValue;
    }
}
