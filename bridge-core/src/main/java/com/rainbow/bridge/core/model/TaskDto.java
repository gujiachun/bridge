package com.rainbow.bridge.core.model;

import java.util.Date;

/**
 * @author gujiachun
 */
public class TaskDto {

    private String taskId;

    /**
     * 目标数据源类型 mysql、es、redis、rocketmq
     * */
    private String targetType;

    /**
     * 需要的实例数
     * */
    private Integer instCount;

    /**
     * 任务规则内容有变化的 更新时间
     * */
    private Date updateTaskRuleTime;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Integer getInstCount() {
        return instCount;
    }

    public void setInstCount(Integer instCount) {
        this.instCount = instCount;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Date getUpdateTaskRuleTime() {
        return updateTaskRuleTime;
    }

    public void setUpdateTaskRuleTime(Date updateTaskRuleTime) {
        this.updateTaskRuleTime = updateTaskRuleTime;
    }

    @Override
    public String toString() {
        return "TaskDto{" +
                "taskId='" + taskId + '\'' +
                ", targetType='" + targetType + '\'' +
                ", instCount=" + instCount +
                ", updateTaskRuleTime=" + updateTaskRuleTime +
                '}';
    }
}
