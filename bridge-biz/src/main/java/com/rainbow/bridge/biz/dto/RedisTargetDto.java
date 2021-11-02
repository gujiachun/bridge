package com.rainbow.bridge.biz.dto;

/**
 * @author gujiachun
 */
public class RedisTargetDto extends TargetDto {

    private Integer id;

    private Integer mode;

    private String props;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public String getProps() {
        return props;
    }

    public void setProps(String props) {
        this.props = props;
    }
}
