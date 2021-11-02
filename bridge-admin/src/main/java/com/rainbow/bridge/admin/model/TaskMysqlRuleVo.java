package com.rainbow.bridge.admin.model;

import lombok.Data;

import java.util.Date;

/**
 * @author gujiachun
 */
@Data
public class TaskMysqlRuleVo {

    private Integer id;

    private String taskId;

    private String sourceDb;

    private String sourceTable;

    private Integer targetId;

    private String targetDb;

    private String targetTable;

    private String syncPks;

    private String syncColumns;

    private Integer insertEnable;

    private String insertTargetPks;

    private String insertTargetOriginCol;

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
