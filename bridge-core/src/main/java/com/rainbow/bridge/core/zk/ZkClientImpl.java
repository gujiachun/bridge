package com.rainbow.bridge.core.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author gujiachun
 */
public class ZkClientImpl implements ZkClient {

    private static final Logger logger = LoggerFactory.getLogger(ZkClientImpl.class);

    private CuratorFramework client;

    /** zkclient 重试间隔时间 */
    private int baseSleepTimeMs = 5000;

    /** zkclient 重试次数 */
    private int retryCount = 5;

    public ZkClientImpl(String zkServers, int sessionTimeout){
        client = CuratorFrameworkFactory
                .builder()
                .connectString(zkServers)
                .sessionTimeoutMs(sessionTimeout)
                .retryPolicy(new ExponentialBackoffRetry(baseSleepTimeMs, retryCount))
                .build();
        client.start();
    }

    public CuratorFramework getClient(){
        return client;
    }

    @Override
    public void close() {
        if (null != client) {
            client.close();
        }
    }

    @Override
    public Boolean exists(String path) {
        try {
            Stat stat = client.checkExists().forPath(path);
            if (stat != null){
                return Boolean.TRUE;
            }
        }catch (Exception e){
            logger.error("验证节点存在zk path:{},异常:{}",path,e.getMessage());
        }

        return Boolean.FALSE;
    }

    @Override
    public void createPersistent(String path) {
        try {
            client.create().creatingParentsIfNeeded() // 若创建节点的父节点不存在则先创建父节点再创建子节点
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(path);
        }catch (Exception e){
            logger.error("创建zk path:{},异常:{}",path,e.getMessage());
        }
    }

    @Override
    public void deletePath(String path) {
        try {
            client.delete().deletingChildrenIfNeeded()
                    .forPath(path);
        }catch (Exception e){
            logger.error("删除zk path:{},异常:{}",path,e.getMessage());
        }
    }

    @Override
    public void writeData(String path, String data) {
        try {
            Stat stat = client.checkExists().forPath(path);
            if(null == stat){
                logger.info(String.format("{} Znode is not exists",path));
            }else {
                client.setData().withVersion(stat.getVersion()).forPath(path, data.getBytes());
            }
        }catch (Exception e){
            logger.error("写数据 zk path:{},异常:{}",path,e.getMessage());
        }
    }

    @Override
    public String readData(String path) {
        try {
            Stat stat = new Stat();
            return new String(client.getData().storingStatIn(stat).forPath(path));
        }catch (Exception e){
            logger.error("读数据 zk path:{},异常:{}",path,e.getMessage());
        }
        return null;
    }

    @Override
    public List<String> getChildren(String path) {
        try {
            return client.getChildren().forPath(path);
        }catch (Exception e){
            logger.error("读数据 zk path:{},异常:{}",path,e.getMessage());
        }
        return null;
    }

    @Override
    public String createEphemeralSequential(String path, String data) {
        try {
            return client.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(path,data.getBytes());
        }catch (Exception e){
            logger.error("读数据 zk path:{},异常:{}",path,e.getMessage());
        }
        return null;
    }
}
