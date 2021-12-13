package com.rainbow.bridge.biz.entity.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rainbow.bridge.biz.entity.BridgeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * 任务规则之mysql
 * </p>
 *
 * @author gujiachun
 * @since 2021-09-27
 */
@TableName("sync_task_rule_mysql")
@ApiModel(value="SyncTaskRuleMysqlEntity对象", description="任务规则之mysql")
public class SyncTaskRuleMysqlEntity extends BridgeEntity {

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "任务id")
    private String taskId;

    @ApiModelProperty(value = "源库")
    private String sourceDb;

    @ApiModelProperty(value = "源表")
    private String sourceTable;

    @ApiModelProperty(value = "目标数据源ID")
    private Integer targetId;

    @ApiModelProperty(value = "目标库")
    private String targetDb;

    @ApiModelProperty(value = "目标表")
    private String targetTable;

    @ApiModelProperty(value = "同步时对应的关联PK，格式（源列1=目标列1;源列2=目标列2）;如果列名一样可简写(列名1;列名2)")
    private String syncPks;

    @ApiModelProperty(value = "同步列名，格式（源列1=目标列1;源列2=目标列2）;如果列名一样可简写(列名1;列名2)")
    private String syncColumns;

    @ApiModelProperty(value = "是否开启新增同步 0:不开启，1：开启")
    private Integer insertEnable;

    @ApiModelProperty(value = "新增同步时，目标表的主键Id列名，以及生成规则，暂只支持uuid（列名=uuid），如果此值为空 表示新增时 忽略")
    @TableField("insert_target_pks")
    private String insertTargetPks;

    @ApiModelProperty(value = "新增同步时，目标表区分来源列名，格式( 目标列=指定来源值sync)；目标就是能够区分数据来源；可以为空，这样忽略区分")
    private String insertTargetOriginCol;

    @ApiModelProperty(value = "新增过滤条件，同步的源数据过滤，表达式")
    private String insertSourceConditionFilter;

    @ApiModelProperty(value = "是否开启更新同步 0:不开启，1：开启")
    private Integer updateEnable;

    @ApiModelProperty(value = "更新过滤条件，同步的源数据过滤，表达式")
    private String updateSourceConditionFilter;

    @ApiModelProperty(value = "更新新值的过滤条件，表达式；决定要不要同步")
    private String updateNewConditionFilter;

    @ApiModelProperty(value = "是否开启删除同步 0:不开启，1：开启")
    private Integer deleteEnable;

    @ApiModelProperty(value = "删除策略: 0:也删除对应的行，1：只更新对应的值为空（源和目标值不一致，就忽略更新）2：只更新对应的值为空（源和目标值不一致，也强制更新为NULL）")
    private Integer deleteStrategy;

    @ApiModelProperty(value = "删除过滤条件，同步的源数据过滤，表达式")
    private String deleteSourceConditionFilter;

    @ApiModelProperty(value = "状态 0:未发布 1:发布有效 2：失效")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createdTime;

    @ApiModelProperty(value = "更新时间")
    private Date updatedTime;

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
    public Integer getInsertEnable() {
        return insertEnable;
    }

    public void setInsertEnable(Integer insertEnable) {
        this.insertEnable = insertEnable;
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

    public Integer getUpdateEnable() {
        return updateEnable;
    }

    public void setUpdateEnable(Integer updateEnable) {
        this.updateEnable = updateEnable;
    }

    public Integer getDeleteEnable() {
        return deleteEnable;
    }

    public void setDeleteEnable(Integer deleteEnable) {
        this.deleteEnable = deleteEnable;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getInsertSourceConditionFilter() {
        return insertSourceConditionFilter;
    }

    public void setInsertSourceConditionFilter(String insertSourceConditionFilter) {
        this.insertSourceConditionFilter = insertSourceConditionFilter;
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

    public String getDeleteSourceConditionFilter() {
        return deleteSourceConditionFilter;
    }

    public void setDeleteSourceConditionFilter(String deleteSourceConditionFilter) {
        this.deleteSourceConditionFilter = deleteSourceConditionFilter;
    }

    public Integer getDeleteStrategy() {
        return deleteStrategy;
    }

    public void setDeleteStrategy(Integer deleteStrategy) {
        this.deleteStrategy = deleteStrategy;
    }

    @Override
    public String toString() {
        return "SyncTaskRuleMysqlEntity{" +
                "id=" + id +
                ", taskId='" + taskId + '\'' +
                ", sourceDb='" + sourceDb + '\'' +
                ", sourceTable='" + sourceTable + '\'' +
                ", targetId='" + targetId + '\'' +
                ", targetDb='" + targetDb + '\'' +
                ", targetTable='" + targetTable + '\'' +
                ", syncPks='" + syncPks + '\'' +
                ", syncColumns='" + syncColumns + '\'' +
                ", insertEnable='" + insertEnable + '\'' +
                ", insertTargetPks='" + insertTargetPks + '\'' +
                ", insertTargetOriginCol='" + insertTargetOriginCol + '\'' +
                ", insertSourceConditionFilter='" + insertSourceConditionFilter + '\'' +
                ", updateEnable='" + updateEnable + '\'' +
                ", updateSourceConditionFilter='" + updateSourceConditionFilter + '\'' +
                ", updateNewConditionFilter='" + updateNewConditionFilter + '\'' +
                ", deleteEnable='" + deleteEnable + '\'' +
                ", deleteSourceConditionFilter='" + deleteSourceConditionFilter + '\'' +
                ", status=" + status +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SyncTaskRuleMysqlEntity that = (SyncTaskRuleMysqlEntity) o;
        return id.equals(that.id) && taskId.equals(that.taskId) && sourceDb.equals(that.sourceDb) && sourceTable.equals(that.sourceTable) && targetId.equals(that.targetId) && targetDb.equals(that.targetDb) && targetTable.equals(that.targetTable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskId, sourceDb, sourceTable, targetId, targetDb, targetTable);
    }
}
