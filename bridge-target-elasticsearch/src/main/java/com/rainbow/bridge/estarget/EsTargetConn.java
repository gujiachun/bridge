package com.rainbow.bridge.estarget;

import com.rainbow.bridge.targetcore.model.TargetConn;

/**
 * elastic search 链接字符串
 * @author gujiachun
 */
public class EsTargetConn extends TargetConn {

    /** 链接地址，集群地址；分号隔开 */
    private String hosts;

    /** es版本 */
    private String version;

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
}
