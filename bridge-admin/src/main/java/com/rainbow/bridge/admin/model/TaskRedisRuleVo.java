package com.rainbow.bridge.admin.model;

import lombok.Data;

import java.sql.Time;
import java.util.Date;

/**
 * @author gujiachun
 */
@Data
public class TaskRedisRuleVo {

    private Integer id;

    private String taskId;

    private String sourceDb;

    private String sourceTable;

    private Integer targetId;

    private String command;

    private String keyFormat;

    private String fieldFormat;

    private String valueFormat;

    private Long expireTime;

    private Time fixedTime;

    private Integer insertEnable;

    private String insertSourceConditionFilter;

    private Integer updateEnable;

    private String updateSourceConditionFilter;

    private String updateNewConditionFilter;

    private Integer deleteEnable;

    private String deleteSourceConditionFilter;

    private Integer status;

    private Date createdTime;

    private Date updatedTime;

}
