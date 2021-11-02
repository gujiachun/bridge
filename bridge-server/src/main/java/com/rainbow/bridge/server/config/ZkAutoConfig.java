package com.rainbow.bridge.server.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rainbow.bridge.biz.entity.BasicZkEntity;
import com.rainbow.bridge.biz.service.ZkService;
import com.rainbow.bridge.core.ResultEnum;
import com.rainbow.bridge.core.exception.BusinessException;
import com.rainbow.bridge.core.zk.BridgeZkSerializer;
import com.rainbow.bridge.core.zk.ZkClientExt;
import com.rainbow.bridge.server.listener.ServerZkChildListener;
import com.rainbow.bridge.server.listener.ServerZkDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gujiachun
 */
@Configuration
public class ZkAutoConfig {

    private static final Logger logger = LoggerFactory.getLogger(ZkAutoConfig.class);

    @Value("${bridge.env}")
    public String env;

    @Value("${bridge.clusterCode}")
    public String clusterCode;

    @Bean
    public ServerZkChildListener serverZkChildListener(){
        return new ServerZkChildListener();
    }

    @Bean
    public ServerZkDataListener serverZkDataListener(){
        return new ServerZkDataListener();
    }

    @Bean
    public ZkClientExt zkClientExt(ZkService zkService,ServerZkDataListener serverZkDataListener,ServerZkChildListener serverZkChildListener){

        QueryWrapper<BasicZkEntity> wrapper = new QueryWrapper();
        wrapper.eq("env",env);

        BasicZkEntity one = zkService.getOne(wrapper);

        if (one == null){
            throw new BusinessException(ResultEnum.SYSTEM_INNER_ERROR,"zookeeper没有配置");
        }

        ZkClientExt zkc = new ZkClientExt(one.getServers(), 5000,3000,
                new BridgeZkSerializer(),one.getRootPath(),clusterCode);

        zkc.subscribeChildChanges(one.getRootPath() + "/" + clusterCode,serverZkChildListener);
        zkc.subscribeDataChanges(one.getRootPath()+ "/" + clusterCode,serverZkDataListener);

        return zkc;
    }



}
