package com.rainbow.bridge.admin.client;

import com.rainbow.bridge.core.zk.ZkClientExt;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gujiachun
 */
public class ZkBridgeClient extends ZkClient {

    private static final Logger logger = LoggerFactory.getLogger(ZkBridgeClient.class);

    public ZkBridgeClient(String zkServers, int sessionTimeout, int connectionTimeout, ZkSerializer zkSerializer,
                       String rootPath,String env) {
        super(zkServers, sessionTimeout, connectionTimeout, zkSerializer);
        this.rootPath = rootPath;
        this.env = env;
    }

    public ZkBridgeClient(String zkServers, int sessionTimeout, int connectionTimeout, ZkSerializer zkSerializer){
        super(zkServers, sessionTimeout, connectionTimeout, zkSerializer);
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
