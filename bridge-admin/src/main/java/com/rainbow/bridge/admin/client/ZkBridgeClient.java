package com.rainbow.bridge.admin.client;

import com.rainbow.bridge.core.zk.ZkClientImpl;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author gujiachun
 */
public class ZkBridgeClient extends ZkClientImpl {

    public ZkBridgeClient(String zkServers, String rootPath,String env) {
        super(zkServers);
        this.rootPath = rootPath;
        this.env = env;
    }

    private String rootPath;

    private String env;

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }
}
