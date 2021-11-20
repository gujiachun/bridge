package com.rainbow.bridge.server.model.action;

import com.rainbow.bridge.core.entity.Column;
import com.rainbow.bridge.core.entity.Pk;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gujiachun
 */
public class DeleteMysqlParam extends MysqlParam {

    private List<Pk> pkList;

    private List<Column> columnList;

    private Integer deleteStrategy;

    public DeleteMysqlParam(String taskId, Integer targetId, String db, String tableName){
        this.tableName = tableName;
        this.targetId = targetId;
        this.taskId = taskId;
        this.database = db;
        this.pkList = new ArrayList<>();
        this.columnList = new ArrayList<>();
    }

    public List<Pk> getPkList() {
        return pkList;
    }

    public void setPkList(List<Pk> pkList) {
        this.pkList = pkList;
    }

    public void addPk(Pk pk){
        this.pkList.add(pk);
    }

    public List<Column> getColumnList() {
        return columnList;
    }

    public void addColumn(Column column){
        columnList.add(column);
    }

    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }

    public Integer getDeleteStrategy() {
        return deleteStrategy;
    }

    public void setDeleteStrategy(Integer deleteStrategy) {
        this.deleteStrategy = deleteStrategy;
    }

}
