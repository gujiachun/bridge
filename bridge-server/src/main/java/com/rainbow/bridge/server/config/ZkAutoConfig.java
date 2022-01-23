package com.rainbow.bridge.server.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rainbow.bridge.biz.entity.BasicClusterEntity;
import com.rainbow.bridge.biz.entity.BasicZkEntity;
import com.rainbow.bridge.biz.service.ClusterService;
import com.rainbow.bridge.biz.service.ZkService;
import com.rainbow.bridge.core.ResultEnum;
import com.rainbow.bridge.core.exception.BusinessException;
import com.rainbow.bridge.server.listener.ServerZkChildListener;
import com.rainbow.bridge.server.listener.ServerZkConnectionStateListener;
import com.rainbow.bridge.server.listener.ServerZkDataListener;
import com.rainbow.bridge.server.zk.ZkClientExt;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.recipes.cache.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * zookeeper协调业务 配置
 * @author gujiachun
 */
@Configuration
public class ZkAutoConfig {

    private static final Logger logger = LoggerFactory.getLogger(ZkAutoConfig.class);

    @Value("${bridge.env}")
    public String env;

    @Value("${bridge.clusterCode:}")
    public String clusterCode;

    @Autowired
    public ClusterService clusterService;

    @Bean
    public ZkClientExt zkClientExt(ZkService zkService) throws Exception {

        QueryWrapper<BasicZkEntity> wrapper = new QueryWrapper();
        wrapper.eq("env",env);

        BasicZkEntity one = zkService.getOne(wrapper);

        if (one == null){
            throw new BusinessException(ResultEnum.SYSTEM_INNER_ERROR,"zookeeper没有配置");
        }

        ZkClientExt zkc;
        if (StringUtils.isNotBlank(clusterCode)){
            zkc = new ZkClientExt(one.getServers(), 5000,one.getRootPath(),clusterCode);
        }else{
            zkc = new ZkClientExt(one.getServers(), 5000);
            List<BasicClusterEntity> listByEnv = clusterService.getListByEnv(env);
            if (listByEnv == null || listByEnv.size() == 0){
                logger.error("环境:{}没有对应的集群配置,请到控制台中的集群管理菜单去配置",env);
                throw new BusinessException(ResultEnum.SYSTEM_INNER_ERROR);
            }

            int selectChildrenCount = Integer.MAX_VALUE;
            String selectClusterCode = null;
            for (BasicClusterEntity entity : listByEnv){
                //集群代码
                String code = entity.getCode();
                //获取此集群代码 中 在线的任务数
                List<String> children = zkc.getChildren(one.getRootPath() + "/" + code);
                //在线实例数
                int count = 0;
                if (children != null){
                    Set<String> ipPortList = new HashSet<>();
                    for (String taskPath : children){
                        //任务节点的值 格式[ip:port,datetime] 如172.16.112.1:8064,2021-10-22 21:50:57
                        //ip+port代表着实例
                        Object o = zkc.readData(one.getRootPath() + "/" + code + "/" + taskPath);
                        if (o != null){
                            String v = o.toString();
                            String[] split = v.split(",");
                            ipPortList.add(split[0]);
                        }
                    }
                    //集群中的在线实例数
                    count = ipPortList.size();
                    logger.info("集群:{}中的任务数{},server实例数:{}",code,children.size(),ipPortList.size());
                }

                if (count < selectChildrenCount){
                    selectChildrenCount = count;
                    selectClusterCode = code;
                }
            }
            if (StringUtils.isBlank(selectClusterCode)){
                logger.error("环境:{} 竟然没找到合适的集群",env);
                throw new BusinessException(ResultEnum.SYSTEM_INNER_ERROR);
            }
            this.clusterCode = selectClusterCode;
            zkc.setRootPathCluster(one.getRootPath(),clusterCode);
        }

        String path = one.getRootPath() + "/" + clusterCode;
        PathChildrenCache childrenCache = new PathChildrenCache(zkc.getClient(), path, true);
        PathChildrenCacheListener childrenCacheListener = new ServerZkChildListener(path);
        childrenCache.getListenable().addListener(childrenCacheListener);
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        logger.info("监听节点:{}下的子节点成功",one.getRootPath() + "/" + clusterCode);

        final NodeCache nodeCache = new NodeCache(zkc.getClient(), path, false);
        nodeCache.getListenable().addListener(new ServerZkDataListener(path));

        nodeCache.start(true);

        zkc.getClient().getConnectionStateListenable().addListener(new ServerZkConnectionStateListener());

        return zkc;
    }
}
