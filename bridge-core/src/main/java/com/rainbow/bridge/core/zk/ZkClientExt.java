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
        this.lockNode = rootPath + "/" +clusterName + "-locker";
        this.rootPath = rootPath;
        this.clusterName = clusterName;
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

    @Override
    public void watchForData(final String path) {
//        retryUntilConnected(() -> {
//            logger.info("进入重写的watchForData方法了！要被监听的节点path是:{}",path);
//            Stat stat = new Stat();
//            _connection.readData(path, stat, true);
//            // 监听节点时，若节点不存在 则抛出异常
//            if(!exists(path)){
//                logger.info("节点path:{} 不存在",path);
//                throw new ZkNoNodeException("节点path:" + path + " 不存在");
//            }
//            return null;
//        });

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

//
//    /**
//     * 分布式锁使用
//     * @return
//     */
//    public String getLock(String key) {
//        try {
//            // 判断是否存在父节点 --创建永久节点才能创建子节点
//            if(!this.exists(lockNode)){
//                this.create(lockNode, "分布式锁节点".getBytes(), CreateMode.PERSISTENT);
//            }
//            // 1。在Zookeeper指定节点下创建临时顺序节点 --当失去连接或者会话过期就会自动删除
//            String lockName = this.createEphemeralSequential(lockNode + "/" + key, "children".getBytes());
//            logger.info(">>>>>临时节点锁:{}",lockName);
//            // 尝试获取锁
//            acquireLock(key,lockName);
//
//            logger.info(">>>>>>>一直等待 锁名称:{}",lockName);
//
//            return lockName;
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public boolean releaseLock(String lockName){
//        logger.info(">>>>>>>释放锁:{}",lockName);
//        this.writeData(lockName,null);
//        return this.delete(lockName);
//    }
//
//    /**
//     * 获取锁
//     * @throws InterruptedException
//     */
//    protected Boolean acquireLock(String key,String lockName) throws InterruptedException {
//        // 2.获取lock节点下的所有子节点
//        List<String> childrenList = this.getChildren(lockNode);
//        // 3.对子节点进行排序，获取最小值
//        Collections.sort(childrenList, new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                String o11 = o1.replace(key,"");
//                String o22 = o2.replace(key,"");
//                return Integer.parseInt(o11) - Integer.parseInt(o22);
//            }
//        });
//        // 4.判断当前创建的节点是否在第一位
//        int lockPostion = childrenList.indexOf(lockName.split("/")[lockName.split("/").length - 1]);
//        if(lockPostion < 0) {
//            // 不存在该节点
//            throw new ZkNodeExistsException("不存在的节点：" + lockName);
//        } else if (lockPostion == 0) {
//            // 获取到锁
//            logger.info("获取到锁：" + lockName);
//            return true;
//        } else if (lockPostion > 0) {
//            // 未获取到锁，阻塞
//            logger.info("...... 未获取到锁，阻塞等待 。。。。。。");
//            // 5.如果未获取得到锁，监听当前创建的节点前一位的节点
//            final CountDownLatch latch = new CountDownLatch(1);
//            IZkDataListener listener = new IZkDataListener() {
//
//                @Override
//                public void handleDataDeleted(String dataPath) throws Exception {
//                    // 6.前一个节点被删除,当不保证轮到自己
//                    logger.info("。。。。。。前一个节点被删除  。。。。。。");
//                    acquireLock(key,lockName);
//                    latch.countDown();
//                }
//
//                @Override
//                public void handleDataChange(String dataPath, Object data) throws Exception {
//                    // 节点内容被修改
//                }
//            };
//            try {
//                logger.info("监听锁节点变化:{}",lockNode + "/" + childrenList.get(lockPostion - 1));
//                this.subscribeDataChanges(lockNode + "/" + childrenList.get(lockPostion - 1), listener);
//                latch.await();
//            } finally {
//                this.unsubscribeDataChanges(lockNode + "/" + childrenList.get(lockPostion - 1), listener);
//            }
//        }
//        return false;
//    }
}
