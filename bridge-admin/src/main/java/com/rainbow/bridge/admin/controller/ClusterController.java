package com.rainbow.bridge.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rainbow.bridge.admin.client.ZkBridgeClient;
import com.rainbow.bridge.admin.client.ZkBridgeClientFactory;
import com.rainbow.bridge.admin.model.ClusterVo;
import com.rainbow.bridge.biz.dto.BasicClusterDto;
import com.rainbow.bridge.biz.entity.BasicClusterEntity;
import com.rainbow.bridge.biz.service.ClusterService;
import com.rainbow.bridge.core.Result;
import com.rainbow.bridge.core.ResultEnum;
import com.rainbow.bridge.core.utils.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author gujiachun
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ClusterController {

    private static final Logger logger = LoggerFactory.getLogger(ClusterController.class);

    private final ClusterService clusterService;

    private final ZkBridgeClientFactory zkBridgeClientFactory;

    @GetMapping("/cluster")
    public Result<List<BasicClusterDto>> getList(){
        List<BasicClusterEntity> list = clusterService.list();

        if (list == null || list.size() == 0){
            return Result.success();
        }

        List<BasicClusterDto> basicClusterDtos = BeanUtil.copyList(list, BasicClusterDto.class);

        for (BasicClusterDto basicClusterDto : basicClusterDtos) {
            ZkBridgeClient zkBridgeClient = zkBridgeClientFactory.getZkBridgeClient(basicClusterDto.getEnv());
            List<String> children = zkBridgeClient.getChildren(zkBridgeClient.getRootPath() + "/" + basicClusterDto.getCode());

            if (children != null){
                List<String> taskList = new ArrayList<>();
                Set<String> ipPortList = new HashSet<>();
                for (String taskPath : children){
                    //任务节点的值 格式[ip:port,datetime] 如172.16.112.1:8064,2021-10-22 21:50:57
                    //ip+port代表着实例
                    Object o = zkBridgeClient.readData(zkBridgeClient.getRootPath() + "/" + basicClusterDto.getCode() + "/" + taskPath);
                    if (o != null){
                        String v = o.toString();
                        String[] split = v.split(",");
                        ipPortList.add(split[0]);
                    }
                    taskList.add(taskPath);
                }
                basicClusterDto.setActiveTaskCount(children.size());
                basicClusterDto.setActiveServerCount(ipPortList.size());
                basicClusterDto.setTasks(taskList);
                basicClusterDto.setServers(ipPortList);

                logger.info("集群:{}中的任务数{},server实例数:{}",basicClusterDto.getCode(),children.size(),ipPortList.size());
            }
        }
        return Result.success(basicClusterDtos);
    }

    @GetMapping("/cluster/env")
    public Result<List<BasicClusterEntity>> getListByEnv(@RequestParam("env") String env){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("env",env);

        return Result.success(clusterService.list(queryWrapper));
    }

    @PostMapping("/cluster")
    public Result<Boolean> add(@RequestBody ClusterVo vo){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("code",vo.getCode());
        BasicClusterEntity one = clusterService.getOne(queryWrapper);
        if (one != null){
            return Result.fail(ResultEnum.SYSTEM_OP_ERROR,"集群代码必须全局唯一");
        }

        BasicClusterEntity entity = new BasicClusterEntity();
        entity.setName(vo.getName());
        entity.setEnv(vo.getEnv());
        entity.setCode(vo.getCode());
        entity.setRemark(vo.getRemark());
        Date date = new Date();
        entity.setCreatedTime(date);
        entity.setUpdatedTime(date);
        return Result.success(clusterService.save(entity));
    }

    @PutMapping("/cluster")
    public Result<Boolean> update(@RequestBody ClusterVo vo){

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("code",vo.getCode());
        BasicClusterEntity one = clusterService.getOne(queryWrapper);
        if (one != null && !one.getId().equals(vo.getId())){
            return Result.fail(ResultEnum.SYSTEM_OP_ERROR,"集群代码必须全局唯一");
        }

        BasicClusterEntity entity = new BasicClusterEntity();
        entity.setName(vo.getName());
        entity.setEnv(vo.getEnv());
        entity.setCode(vo.getCode());
        entity.setRemark(vo.getRemark());
        entity.setId(vo.getId());
        entity.setUpdatedTime(new Date());

        return Result.success(clusterService.updateById(entity));
    }

    @DeleteMapping("/cluster/{id}")
    public Result<Boolean> delete(@PathVariable("id") Integer id){
        return Result.success(clusterService.removeById(id));
    }

}
