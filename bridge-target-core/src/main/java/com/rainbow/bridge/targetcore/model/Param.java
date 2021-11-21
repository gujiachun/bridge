package com.rainbow.bridge.targetcore.model;

/**
 * 执行目标源对象的相关参数
 * @author gujiachun
 */
public class Param {

    protected String taskId;

    protected Integer targetId;

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
