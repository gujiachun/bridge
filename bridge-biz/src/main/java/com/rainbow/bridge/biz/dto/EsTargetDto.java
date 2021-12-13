package com.rainbow.bridge.biz.dto;

/**
 * @author gujiachun
 */
public class EsTargetDto extends TargetDto {

    private Integer id;

    private String hosts;

    private String version;

    private String props;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getProps() {
        return props;
    }

    public void setProps(String props) {
        this.props = props;
    }
}
