package com.rainbow.bridge.server.model.action;

import java.sql.Time;

/**
 * redis执行参数
 * @author gujiachun
 */
public class RedisParam extends Param {

    private String command;
    private String key;
    private String field;
    private String value;
    private Long expireTime;
    private Time fixedTime;

    public RedisParam(String taskId,Integer targetId,String command,String key,String field,
                      String value,Long expireTime,Time fixedTime){
        this.targetId = targetId;
        this.taskId = taskId;
        this.command = command;
        this.key = key;
        this.field = field;
        this.value = value;
        this.expireTime = expireTime;
        this.fixedTime = fixedTime;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Time getFixedTime() {
        return fixedTime;
    }

    public void setFixedTime(Time fixedTime) {
        this.fixedTime = fixedTime;
    }
}
