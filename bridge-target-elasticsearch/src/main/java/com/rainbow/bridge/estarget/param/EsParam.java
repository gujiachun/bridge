package com.rainbow.bridge.estarget.param;

import com.rainbow.bridge.targetcore.model.Param;

import java.util.HashMap;
import java.util.Map;

/**
 * es执行参数
 * @author gujiachun
 */
public class EsParam extends Param {

    private String indexFormat;
    private String idFormat;
    private String sqlFormat;
    private String sqlFieldFormat;
    private String fieldType;
    /** 跳过忽略es属性，不需要把此es属性更新进去（F1,F2以逗号隔开） */
    private String skipsField;
    private Integer relationType;
    private String relationFieldName;
    private String relationJoinName;
    private String relationChildRouteFormat;
    private String relationChildParentFormat;
    private Integer deleteStrategy;
    /** 如果sql执行结果为空是，需要清理的esFiled（逗号隔开） */
    private String sqlNullField;
    /** 索引更新类型 0：索引更新sql模式，1：索引更新canal模式 */
    private Integer indexType;

    /** canal模式 更新 */
    private String partFormat;

    /** canal模式 key为es属性，value对应 db属性的值 */
    private Map<String,Object> partMap;


    public EsParam(String taskId, Integer targetId, String indexFormat, String idFormat, String sqlFormat, String sqlFieldFormat, String fieldType,
                   String sqlNullField,String skipsField,Integer relationType, String relationFieldName, String relationJoinName,
                   String relationChildRouteFormat, String relationChildParentFormat,Integer deleteStrategy,Integer indexType,String partFormat) {
        super(taskId, targetId);
        this.indexFormat = indexFormat;
        this.idFormat = idFormat;
        this.sqlFormat = sqlFormat;
        this.sqlFieldFormat = sqlFieldFormat;
        this.fieldType = fieldType;
        this.sqlNullField = sqlNullField;
        this.skipsField = skipsField;
        this.relationType = relationType;
        this.relationFieldName = relationFieldName;
        this.relationJoinName = relationJoinName;
        this.relationChildRouteFormat = relationChildRouteFormat;
        this.relationChildParentFormat = relationChildParentFormat;
        this.deleteStrategy = deleteStrategy;
        this.indexType = indexType;
        this.partFormat = partFormat;
        partMap = new HashMap<>(10);
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

    public Map<String, Object> getPartMap() {
        return partMap;
    }

    public void setPartMap(Map<String, Object> partMap) {
        this.partMap = partMap;
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
