package com.rainbow.bridge.targetcore.model;

/**
 * 目标源的 链接字符串 领域模型
 * @author gujiachun
 */
public class TargetConn {

    private Integer targetId;

    private String props;

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public String getProps() {
        return props;
    }

    public void setProps(String props) {
        this.props = props;
    }
}
