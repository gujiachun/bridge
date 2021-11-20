package com.rainbow.bridge.server.model.task;

import java.util.Date;

/**
 * 任务规则
 * @author gujiachun
 */
public class TaskRule {

    /** 规则id */
    private Integer id;

    /** 任务id */
    protected String taskId;

    /** 源库 */
    protected String sourceDb;

    /** 源表 */
    protected String sourceTable;

    /** 目标数据源ID */
    protected Integer targetId;

    /** 是否开启新增同步 0:不开启，1：开启 */
    protected Integer insertEnable;

    /** 新增过滤条件，同步的源数据过滤，表达式 */
    protected String insertSourceConditionFilter;

    /** 是否开启更新同步 0:不开启，1：开启 */
    protected Integer updateEnable;

    /** 更新过滤条件，同步的源数据过滤，表达式 */
    protected String updateSourceConditionFilter;

    /** 更新新值的过滤条件，表达式；决定要不要同步 */
    protected String updateNewConditionFilter;

    /** 是否开启删除同步 0:不开启，1：开启 */
    protected Integer deleteEnable;

    /** 删除过滤条件，同步的源数据过滤，表达式 */
    protected String deleteSourceConditionFilter;

    /** 状态 0:未发布 1:发布有效 2：失效 */
    protected Integer status;

    /** 创建时间 */
    protected Date createdTime;

    /** 更新时间 */
    protected Date updatedTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getSourceDb() {
        return sourceDb;
    }

    public void setSourceDb(String sourceDb) {
        this.sourceDb = sourceDb;
    }

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Integer getInsertEnable() {
        return insertEnable;
    }

    public void setInsertEnable(Integer insertEnable) {
        this.insertEnable = insertEnable;
    }

    public String getInsertSourceConditionFilter() {
        return insertSourceConditionFilter;
    }

    public void setInsertSourceConditionFilter(String insertSourceConditionFilter) {
        this.insertSourceConditionFilter = insertSourceConditionFilter;
    }

    public Integer getUpdateEnable() {
        return updateEnable;
    }

    public void setUpdateEnable(Integer updateEnable) {
        this.updateEnable = updateEnable;
    }

    public String getUpdateSourceConditionFilter() {
        return updateSourceConditionFilter;
    }

    public void setUpdateSourceConditionFilter(String updateSourceConditionFilter) {
        this.updateSourceConditionFilter = updateSourceConditionFilter;
    }

    public String getUpdateNewConditionFilter() {
        return updateNewConditionFilter;
    }

    public void setUpdateNewConditionFilter(String updateNewConditionFilter) {
        this.updateNewConditionFilter = updateNewConditionFilter;
    }

    public Integer getDeleteEnable() {
        return deleteEnable;
    }

    public void setDeleteEnable(Integer deleteEnable) {
        this.deleteEnable = deleteEnable;
    }

    public String getDeleteSourceConditionFilter() {
        return deleteSourceConditionFilter;
    }

    public void setDeleteSourceConditionFilter(String deleteSourceConditionFilter) {
        this.deleteSourceConditionFilter = deleteSourceConditionFilter;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
