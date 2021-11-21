package com.rainbow.bridge.mysqltarget.param;

import com.rainbow.bridge.targetcore.model.Param;

/**
 * @author gujiachun
 */
public class MysqlParam extends Param {

    protected String database;

    protected String tableName;

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
