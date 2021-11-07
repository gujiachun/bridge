package com.rainbow.bridge.core.zk;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @author gujiachun
 */
public class ZkClientExt extends ZkClient {

    private static final Logger logger = LoggerFactory.getLogger(ZkClientExt.class);

    public ZkClientExt(String zkServers, int sessionTimeout, int connectionTimeout, ZkSerializer zkSerializer,
                       String rootPath,String clusterName) {
        super(zkServers, sessionTimeout, connectionTimeout, zkSerializer);
        setRootPathCluster(rootPath,clusterName);
    }

    public ZkClientExt(String zkServers, int sessionTimeout, int connectionTimeout, ZkSerializer zkSerializer){
        super(zkServers, sessionTimeout, connectionTimeout, zkSerializer);
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

    @Override
    public void watchForData(final String path) {
        retryUntilConnected(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                logger.info("进入重写的watchForData方法了！要被监听的节点path是:{}",path);
                //_connection.exists(path, true);
                Stat stat = new Stat();
                _connection.readData(path, stat, true);
                logger.info("判断锁节点是不是存在:{}",path);
                if(!exists(path)){
                //if(!_connection.exists(path,true)){
                    logger.info("锁节点path:{} 不存在",path);
                    throw new ZkNoNodeException("锁节点path:" + path + " 不存在");
                }
                logger.info("锁节点还是存在:{}",path);
                return null;
            }
        });
    }
}
