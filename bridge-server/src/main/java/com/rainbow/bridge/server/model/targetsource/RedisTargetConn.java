package com.rainbow.bridge.server.model.targetsource;

/**
 * redis目标源 链接字符串 领域模型
 * @author gujiachun
 */
public class RedisTargetConn extends TargetConn {

    /**
     * redis部署模式 0：单机，1：哨兵，2：集群
     * */
    private Integer mode;

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }
}
