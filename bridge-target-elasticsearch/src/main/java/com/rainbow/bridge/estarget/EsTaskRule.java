package com.rainbow.bridge.estarget;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rainbow.bridge.targetcore.model.TaskRule;
import io.swagger.annotations.ApiModelProperty;

/**
 * es任务规则
 * @author gujiachun
 */
public class EsTaskRule extends TaskRule {

    /** 索引格式 */
    private String indexFormat;

    /** 索引id格式 */
    private String idFormat;

    /** 索引更新类型 0：索引更新sql模式，1：索引更新canal模式 */
    private Integer indexType;

    /** canal模式更新*/
    private String partFormat;

    /** sql格式 */
    private String sqlFormat;

    /** sql格式?对应的值 */
    private String sqlFieldFormat;

    /** 字段的类型（以#$#隔开）\nF1=array:;   数组, array:; 代表以;字段里面是以;分隔的\nF1=object    json对象 */
    private String fieldType;

    /** 跳过忽略es属性，不需要把此es属性更新进去（F1,F2以逗号隔开） */
    private String skipsField;

    /** 0:普通文档，1：父文档，2：子文档 */
    private Integer relationType = 0;

    /** 父子关联健名，如：join_field */
    private String relationFieldName;

    /** 关联名，如：父question，子：answer */
    private String relationJoinName;

    /** 子文档的route格式 */
    private String relationChildRouteFormat;

    /** 子文档的parent格式 */
    private String relationChildParentFormat;

    /** 删除策略: 0:根据index和id模板删除索引，1：执行sql模板，更新索引 */
    private Integer deleteStrategy;

    /** 如果sql执行结果为空是，需要清理的esFiled（逗号隔开） */
    private String sqlNullField;

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
