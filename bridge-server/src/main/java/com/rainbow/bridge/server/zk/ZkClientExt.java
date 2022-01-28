package com.rainbow.bridge.server.zk;

import com.rainbow.bridge.core.zk.ZkClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gujiachun
 */
public class ZkClientExt extends ZkClientImpl {

    private static final Logger logger = LoggerFactory.getLogger(ZkClientExt.class);

    public ZkClientExt(String zkServers,
                       String rootPath,String clusterName) {
        super(zkServers);
        setRootPathCluster(rootPath,clusterName);
    }

    public ZkClientExt(String zkServers) {
        super(zkServers);
    }

    private String rootPath;

    private String clusterName;

    private String lockNode;

    public String getClusterPath(){
        return rootPath + "/" +clusterName;
    }

    public String getRootPath() {
        return rootPath;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setRootPathCluster(String rootPath,String clusterName){
        this.lockNode = rootPath + "/" +clusterName + "-locker";
        this.rootPath = rootPath;
        this.clusterName = clusterName;
    }
}
