package com.rainbow.bridge.biz.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Time;
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
@TableName("sync_task_rule_redis")
@ApiModel(value="SyncTaskRuleRedisEntity对象", description="任务规则之redis")
public class SyncTaskRuleRedisEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty(value = "redis执行命令")
    private String command;

    @ApiModelProperty(value = "key模板")
    @TableField("key_format")
    private String keyFormat;

    @ApiModelProperty(value = "field模板 针对hash结构")
    @TableField(value = "field_format",updateStrategy = FieldStrategy.IGNORED)
    private String fieldFormat;

    @ApiModelProperty(value = "value模板")
    @TableField(value = "value_format",updateStrategy = FieldStrategy.IGNORED)
    private String valueFormat;

    @ApiModelProperty(value = "过期时间 毫秒")
    @TableField(value = "expire_time",updateStrategy = FieldStrategy.IGNORED)
    private Long expireTime;

    @ApiModelProperty(value = "在每天的固定时间过期")
    @TableField(value = "fixed_time",updateStrategy = FieldStrategy.IGNORED)
    private Time fixedTime;

    @ApiModelProperty(value = "是否开启新增同步 0:不开启，1：开启")
    private Integer insertEnable;

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

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getKeyFormat() {
        return keyFormat;
    }

    public void setKeyFormat(String keyFormat) {
        this.keyFormat = keyFormat;
    }

    public String getFieldFormat() {
        return fieldFormat;
    }

    public void setFieldFormat(String fieldFormat) {
        this.fieldFormat = fieldFormat;
    }

    public String getValueFormat() {
        return valueFormat;
    }

    public void setValueFormat(String valueFormat) {
        this.valueFormat = valueFormat;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
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

    public Time getFixedTime() {
        return fixedTime;
    }

    public void setFixedTime(Time fixedTime) {
        this.fixedTime = fixedTime;
    }

    @Override
    public String toString() {
        return "SyncTaskRuleRedisEntity{" +
                "id=" + id +
                ", taskId='" + taskId + '\'' +
                ", sourceDb='" + sourceDb + '\'' +
                ", sourceTable='" + sourceTable + '\'' +
                ", targetId=" + targetId +
                ", command='" + command + '\'' +
                ", keyFormat='" + keyFormat + '\'' +
                ", fieldFormat='" + fieldFormat + '\'' +
                ", valueFormat='" + valueFormat + '\'' +
                ", expireTime=" + expireTime +
                ", fixedTime=" + fixedTime +
                ", insertEnable=" + insertEnable +
                ", insertSourceConditionFilter='" + insertSourceConditionFilter + '\'' +
                ", updateEnable=" + updateEnable +
                ", updateSourceConditionFilter='" + updateSourceConditionFilter + '\'' +
                ", updateNewConditionFilter='" + updateNewConditionFilter + '\'' +
                ", deleteEnable=" + deleteEnable +
                ", deleteSourceConditionFilter='" + deleteSourceConditionFilter + '\'' +
                ", status=" + status +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                '}';
    }
}
