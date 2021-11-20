package com.rainbow.bridge.server.model.action;

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
