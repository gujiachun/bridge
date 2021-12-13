package com.rainbow.bridge.biz.entity.es;

import com.baomidou.mybatisplus.annotation.*;
import com.rainbow.bridge.biz.entity.BridgeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Time;
import java.util.Date;

/**
 * <p>
 * 任务规则之es
 * </p>
 *
 * @author gujiachun
 * @since 2021-09-27
 */
@TableName("sync_task_rule_es")
@ApiModel(value="SyncTaskRuleEsEntity对象", description="任务规则之es")
public class SyncTaskRuleEsEntity extends BridgeEntity {

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

    @ApiModelProperty(value = "索引格式")
    private String indexFormat;

    @ApiModelProperty(value = "索引id格式")
    private String idFormat;

    @ApiModelProperty(value = "索引更新类型 0：索引更新sql模式，1：索引更新canal模式")
    private Integer indexType;

    /**
     canal模式更新索引，格式
     {
     "k1":"v1",
     "k2":"v2"
     }
     k1=v1(es属性=db列)
     * */
    @ApiModelProperty(value = "canal模式更新索引")
    private String partFormat;

    @ApiModelProperty(value = "sql模式 sql模板")
    private String sqlFormat;

    @ApiModelProperty(value = "sql模式 sql模板中?对应的值")
    private String sqlFieldFormat;

    @ApiModelProperty(value = "sql模式 如果sql执行结果为空是，需要清理的esFiled（逗号隔开）")
    @TableField(value = "sql_null_field",updateStrategy = FieldStrategy.IGNORED)
    private String sqlNullField;

    @ApiModelProperty(value = "字段的类型（以#$#隔开）\\nF1=array:;   数组, array:; 代表以;字段里面是以;分隔的\\nF1=object    json对象")
    @TableField(value = "field_type",updateStrategy = FieldStrategy.IGNORED)
    private String fieldType;

    @ApiModelProperty(value = "跳过忽略es属性，不需要把此es属性更新进去（F1,F2以逗号隔开）")
    @TableField(value = "skips_field",updateStrategy = FieldStrategy.IGNORED)
    private String skipsField;

    @ApiModelProperty(value = "0:普通文档，1：父文档，2：子文档")
    private Integer relationType = 0;

    @ApiModelProperty(value = "父子关联健名，如：join_field")
    @TableField(value = "relation_field_name",updateStrategy = FieldStrategy.IGNORED)
    private String relationFieldName;

    @ApiModelProperty(value = "关联名，如：父question，子：answer")
    @TableField(value = "relation_join_name",updateStrategy = FieldStrategy.IGNORED)
    private String relationJoinName;

    @ApiModelProperty(value = "子文档的route格式")
    @TableField(value = "relation_child_route_format",updateStrategy = FieldStrategy.IGNORED)
    private String relationChildRouteFormat;

    @ApiModelProperty(value = "子文档的parent格式")
    @TableField(value = "relation_child_parent_format",updateStrategy = FieldStrategy.IGNORED)
    private String relationChildParentFormat;

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

    @ApiModelProperty(value = "删除策略: 0:根据index和id模板删除索引，1：执行sql模板，更新索引")
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

    public String getIndexFormat() {
        return indexFormat;
    }

    public void setIndexFormat(String indexFormat) {
        this.indexFormat = indexFormat;
    }

    public String getIdFormat() {
        return idFormat;
    }

    public void setIdFormat(String idFormat) {
        this.idFormat = idFormat;
    }

    public String getSqlFormat() {
        return sqlFormat;
    }

    public void setSqlFormat(String sqlFormat) {
        this.sqlFormat = sqlFormat;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public Integer getRelationType() {
        return relationType;
    }

    public void setRelationType(Integer relationType) {
        this.relationType = relationType;
    }

    public String getRelationFieldName() {
        return relationFieldName;
    }

    public void setRelationFieldName(String relationFieldName) {
        this.relationFieldName = relationFieldName;
    }

    public String getRelationJoinName() {
        return relationJoinName;
    }

    public void setRelationJoinName(String relationJoinName) {
        this.relationJoinName = relationJoinName;
    }

    public String getRelationChildRouteFormat() {
        return relationChildRouteFormat;
    }

    public void setRelationChildRouteFormat(String relationChildRouteFormat) {
        this.relationChildRouteFormat = relationChildRouteFormat;
    }

    public String getRelationChildParentFormat() {
        return relationChildParentFormat;
    }

    public void setRelationChildParentFormat(String relationChildParentFormat) {
        this.relationChildParentFormat = relationChildParentFormat;
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

    public String getSqlFieldFormat() {
        return sqlFieldFormat;
    }

    public void setSqlFieldFormat(String sqlFieldFormat) {
        this.sqlFieldFormat = sqlFieldFormat;
    }

    public Integer getDeleteStrategy() {
        return deleteStrategy;
    }

    public void setDeleteStrategy(Integer deleteStrategy) {
        this.deleteStrategy = deleteStrategy;
    }

    public Integer getIndexType() {
        return indexType;
    }

    public void setIndexType(Integer indexType) {
        this.indexType = indexType;
    }

    public String getPartFormat() {
        return partFormat;
    }

    public void setPartFormat(String partFormat) {
        this.partFormat = partFormat;
    }

    public String getSkipsField() {
        return skipsField;
    }

    public void setSkipsField(String skipsField) {
        this.skipsField = skipsField;
    }

    public String getSqlNullField() {
        return sqlNullField;
    }

    public void setSqlNullField(String sqlNullField) {
        this.sqlNullField = sqlNullField;
    }
}
