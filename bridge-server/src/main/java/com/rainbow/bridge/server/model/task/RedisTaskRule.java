package com.rainbow.bridge.server.model.task;

import java.sql.Time;

/**
 * redis任务规则
 * @author gujiachun
 */
public class RedisTaskRule extends TaskRule {

    /** redis执行命令 */
    private String command;

    /** key模板 */
    private String keyFormat;

    /** field模板 针对hash结构 */
    private String fieldFormat;

    /** value模板 */
    private String valueFormat;

    /** 过期时间 毫秒 */
    private Long expireTime;

    /** 在每天的固定时间过期 */
    private Time fixedTime;

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

    public Time getFixedTime() {
        return fixedTime;
    }

    public void setFixedTime(Time fixedTime) {
        this.fixedTime = fixedTime;
    }
}
