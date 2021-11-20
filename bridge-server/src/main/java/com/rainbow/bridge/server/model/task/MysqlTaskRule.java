package com.rainbow.bridge.server.model.task;

/**
 * mysql的任务规则
 * @author gujiachun
 */
public class MysqlTaskRule extends TaskRule {

    /** 目标库 */
    private String targetDb;

    /** 目标表 */
    private String targetTable;

    /** 同步时对应的关联PK，格式（源列1=目标列1;源列2=目标列2）;如果列名一样可简写(列名1;列名2) */
    private String syncPks;

    /** 同步列名，格式（源列1=目标列1;源列2=目标列2）;如果列名一样可简写(列名1;列名2) */
    private String syncColumns;

    /** 新增同步时，目标表的主键Id列名，以及生成规则，暂只支持uuid（列名=uuid），如果此值为空 表示新增时 忽略 */
    private String insertTargetPks;

    /** 新增同步时，目标表区分来源列名，格式( 目标列=指定来源值sync)；目标就是能够区分数据来源；可以为空，这样忽略区分 */
    private String insertTargetOriginCol;

    /** 删除策略: 0:也删除对应的行，1：只更新对应的值为空（源和目标值不一致，就忽略更新）2：只更新对应的值为空（源和目标值不一致，也强制更新为NULL） */
    private Integer deleteStrategy;

    public String getTargetDb() {
        return targetDb;
    }

    public void setTargetDb(String targetDb) {
        this.targetDb = targetDb;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public String getSyncPks() {
        return syncPks;
    }

    public void setSyncPks(String syncPks) {
        this.syncPks = syncPks;
    }

    public String getSyncColumns() {
        return syncColumns;
    }

    public void setSyncColumns(String syncColumns) {
        this.syncColumns = syncColumns;
    }

    public String getInsertTargetPks() {
        return insertTargetPks;
    }

    public void setInsertTargetPks(String insertTargetPks) {
        this.insertTargetPks = insertTargetPks;
    }

    public String getInsertTargetOriginCol() {
        return insertTargetOriginCol;
    }

    public void setInsertTargetOriginCol(String insertTargetOriginCol) {
        this.insertTargetOriginCol = insertTargetOriginCol;
    }

    public Integer getDeleteStrategy() {
        return deleteStrategy;
    }

    public void setDeleteStrategy(Integer deleteStrategy) {
        this.deleteStrategy = deleteStrategy;
    }
}
