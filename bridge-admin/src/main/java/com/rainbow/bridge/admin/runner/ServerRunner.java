package com.rainbow.bridge.admin.runner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rainbow.bridge.admin.client.ZkBridgeClient;
import com.rainbow.bridge.admin.client.ZkBridgeClientFactory;
import com.rainbow.bridge.biz.entity.BasicClusterEntity;
import com.rainbow.bridge.biz.entity.BasicZkEntity;
import com.rainbow.bridge.biz.entity.SyncTaskEntity;
import com.rainbow.bridge.biz.service.ClusterService;
import com.rainbow.bridge.biz.service.TaskService;
import com.rainbow.bridge.biz.service.ZkService;
import com.rainbow.bridge.core.enums.StatusEnum;
import com.rainbow.bridge.core.model.TaskDto;
import com.rainbow.bridge.core.zk.BridgeZkSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gujiachun
 */
@Component
public class ServerRunner implements ApplicationRunner {

    @Autowired
    private ZkBridgeClientFactory zkBridgeClientFactory;

    @Autowired
    private ZkService zkService;

    @Autowired
    private ClusterService clusterService;

    @Autowired
    private TaskService taskService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initZkClient();
        initZkPath();
        initClusters();
        initZkClusterPathData();
    }

    private void initZkClient(){
        List<BasicZkEntity> list = zkService.list();
        for (BasicZkEntity entity : list){
            ZkBridgeClient client = new ZkBridgeClient(entity.getServers(), 5000,3000,
                    new BridgeZkSerializer(),entity.getRootPath(),entity.getEnv());
            zkBridgeClientFactory.addZkBridgeClient(entity.getEnv(),client);
        }
    }

    private void initZkPath(){
        Map<String, ZkBridgeClient> zkBridgeClientMap = zkBridgeClientFactory.getZkBridgeClientMap();
        for (String key : zkBridgeClientMap.keySet()){
            ZkBridgeClient client = zkBridgeClientMap.get(key);
            if (!client.exists(client.getRootPath())) {
                client.createPersistent(client.getRootPath(), true);
            }
        }
    }

    private void initClusters(){
        List<BasicClusterEntity> list = clusterService.list();
        for (BasicClusterEntity entity : list){
            ZkBridgeClient zkBridgeClient = zkBridgeClientFactory.getZkBridgeClient(entity.getEnv());
            if (!zkBridgeClient.exists(zkBridgeClient.getRootPath() + "/" + entity.getCode())){
                zkBridgeClient.createPersistent(zkBridgeClient.getRootPath() + "/" + entity.getCode());
            }

//            zkBridgeClient.subscribeChildChanges(zkBridgeClient.getRootPath() + "/" + entity.getCode(),
//                    new ServerZkChildListener());
        }
    }

    private void initZkClusterPathData(){
        QueryWrapper<SyncTaskEntity> queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", StatusEnum.valid.getStatus());
        List<SyncTaskEntity> list = taskService.list(queryWrapper);
        Map<String,List<TaskDto>> clusterTaskMap = new HashMap<>(10);
        Map<String,String> clusterEnvMap = new HashMap<>(10);
        for (SyncTaskEntity entity : list){

            if (!clusterTaskMap.containsKey(entity.getPublishCluster())){
                List<TaskDto> taskDtoArrayList = new ArrayList<>();
                clusterTaskMap.put(entity.getPublishCluster(), taskDtoArrayList);
            }
            List<TaskDto> taskDtos = clusterTaskMap.get(entity.getPublishCluster());
            TaskDto taskDto = new TaskDto();
            taskDto.setTaskId(entity.getId());
            taskDto.setInstCount(entity.getInstanceCount());
            taskDto.setTargetType(entity.getTargetType());
            taskDto.setUpdateTaskRuleTime(entity.getUpdatedTime());
            taskDtos.add(taskDto);

            if (!clusterEnvMap.containsKey(entity.getPublishCluster())){
                clusterEnvMap.put(entity.getPublishCluster(), entity.getEnv());
            }
        }

        for (String clusterCode : clusterEnvMap.keySet()){
            ZkBridgeClient zkBridgeClient = zkBridgeClientFactory.getZkBridgeClient(clusterEnvMap.get(clusterCode));
            List<TaskDto> taskDtos = clusterTaskMap.get(clusterCode);
            zkBridgeClient.writeData(zkBridgeClient.getRootPath() + "/" + clusterCode,
                    JSON.toJSONStringWithDateFormat(taskDtos,"yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat));
        }
    }
}
