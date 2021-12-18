package com.rainbow.bridge.admin.model;

import lombok.Data;

import java.util.Date;

/**
 * @author gujiachun
 */
@Data
public class TaskEsRuleVo {

    private Integer id;

    private String taskId;

    private String sourceDb;

    private String sourceTable;

    private Integer targetId;

    private String indexFormat;

    private String idFormat;

    private Integer indexType;

    private String partFormat;

    private String sqlFormat;

    private String sqlFieldFormat;

    private String sqlNullField;

    private String fieldType;

    private String skipsField;

    private Integer relationType;

    private String relationFieldName;

    private String relationJoinName;

    private String relationChildRouteFormat;

    private String relationChildParentFormat;

    private Integer insertEnable;

    private String insertSourceConditionFilter;

    private Integer updateEnable;

    private String updateSourceConditionFilter;

    private String updateNewConditionFilter;

    private Integer deleteEnable;

    private Integer deleteStrategy;

    private String deleteSourceConditionFilter;

    private Integer status;

    private Date createdTime;

    private Date updatedTime;

}
