package com.rainbow.bridge.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.rainbow.bridge.admin.client.ZkBridgeClient;
import com.rainbow.bridge.admin.client.ZkBridgeClientFactory;
import com.rainbow.bridge.biz.dto.TaskBizDto;
import com.rainbow.bridge.biz.dto.query.TaskQueryDto;
import com.rainbow.bridge.biz.entity.BasicClusterEntity;
import com.rainbow.bridge.biz.entity.SyncTaskEntity;
import com.rainbow.bridge.biz.service.ClusterService;
import com.rainbow.bridge.biz.service.TaskService;
import com.rainbow.bridge.core.PageResult;
import com.rainbow.bridge.core.Result;
import com.rainbow.bridge.core.ResultEnum;
import com.rainbow.bridge.core.enums.StatusEnum;
import com.rainbow.bridge.core.model.TaskDto;
import com.rainbow.bridge.core.utils.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author gujiachun
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class TaskController {

    private final TaskService taskService;

    private final ClusterService clusterService;

    private final ZkBridgeClientFactory zkBridgeClientFactory;

    @GetMapping("/task")
    public Result<PageResult<TaskBizDto>> getList(TaskQueryDto dto){
        IPage<TaskBizDto> page = taskService.query(dto);

        //查询在线实例数
        Map<String, List<String>> envTaskActives = new HashMap<>(10);
        for (TaskBizDto taskBizDto : page.getRecords()){

            if (!envTaskActives.containsKey(taskBizDto.getEnv())){
                ZkBridgeClient zkBridgeClient = zkBridgeClientFactory.getZkBridgeClient(taskBizDto.getEnv());
                List<String> children = zkBridgeClient.getChildren(zkBridgeClient.getRootPath() + "/" + taskBizDto.getPublishCluster());
                envTaskActives.put(taskBizDto.getEnv(),children);
            }
            List<String> taskActives = envTaskActives.get(taskBizDto.getEnv());
            Integer count = 0;
            for (String subPath : taskActives){
                if (subPath.startsWith(taskBizDto.getId())){
                    count++;
                }
            }

            taskBizDto.setActiveCount(count);
        }

        return Result.success(new PageResult<>(page.getTotal(), BeanUtil.copyList(page.getRecords(), TaskBizDto.class)));
    }

    @GetMapping("/task/{id}")
    public Result<SyncTaskEntity> getList(@PathVariable("id") String id){
        return Result.success(taskService.getById(id));
    }

    @PostMapping("/task")
    public Result<Boolean> add(@RequestBody TaskBizDto dto){
        SyncTaskEntity entity = new SyncTaskEntity();
        entity.setAsync(dto.getAsync());
        entity.setEnv(dto.getEnv());
        entity.setBasicTopicId(dto.getBasicTopicId());
        entity.setInstanceCount(dto.getInstanceCount());
        Date date = new Date();
        entity.setCreatedTime(date);
        entity.setName(dto.getName());
        entity.setPublishCluster(dto.getPublishCluster());
        entity.setStatus(dto.getStatus());
        entity.setTargetType(dto.getTargetType());
        entity.setUpdatedTime(date);

        return Result.success(taskService.save(entity));
    }

    @PutMapping("/task")
    public Result<Boolean> update(@RequestBody TaskBizDto dto){

        SyncTaskEntity entity = new SyncTaskEntity();
        entity.setId(dto.getId());
        entity.setAsync(dto.getAsync());
        entity.setEnv(dto.getEnv());
        entity.setBasicTopicId(dto.getBasicTopicId());
        entity.setInstanceCount(dto.getInstanceCount());
        Date date = new Date();
        entity.setName(dto.getName());
        entity.setPublishCluster(dto.getPublishCluster());
        entity.setStatus(dto.getStatus());
        entity.setTargetType(dto.getTargetType());
        entity.setUpdatedTime(date);

        return Result.success(taskService.updateById(entity));
    }

    @DeleteMapping("/task/{id}")
    public Result<Boolean> delete(@PathVariable("id") String id){
        SyncTaskEntity entity = taskService.getById(id);
        if (entity == null){
            return Result.success(Boolean.TRUE);
        }
        if (entity.getStatus().equals(StatusEnum.valid.getStatus())){
            return Result.fail(ResultEnum.SYSTEM_OP_ERROR,"已发布的任务无法删除");
        }

        return Result.success(taskService.removeById(id));
    }

    @PostMapping("/task/{id}")
    public Result<Boolean> updateStatus(@PathVariable("id") String id,@RequestParam("status") Integer status){
        SyncTaskEntity entity = new SyncTaskEntity();
        entity.setId(id);
        entity.setStatus(status);
        Date date = new Date();
        entity.setUpdatedTime(date);
        taskService.updateById(entity);

        return Result.success(Boolean.TRUE);
    }

    @GetMapping("/taskActiveCount")
    public Result<List<String>> getTaskActiveCount(@RequestParam("env") String env,
                                              @RequestParam("clusterCode") String clusterCode){

        ZkBridgeClient zkBridgeClient = zkBridgeClientFactory.getZkBridgeClient(env);
        List<String> children = zkBridgeClient.getChildren(zkBridgeClient.getRootPath() + "/" + clusterCode);

        return Result.success(children);
    }

    @GetMapping("/task/refresh/{clusterId}")
    public Result<Void> getTaskActiveCount(@PathVariable("clusterId") Integer clusterId){

        BasicClusterEntity entity = clusterService.getById(clusterId);

        updateZkClusterPathData(entity.getCode(), entity.getEnv());

        return Result.success();
    }

    private void updateZkClusterPathData(String clusterCode,String env){
        QueryWrapper<SyncTaskEntity> queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", StatusEnum.valid.getStatus());
        queryWrapper.eq("publish_cluster", clusterCode);
        List<SyncTaskEntity> list = taskService.list(queryWrapper);

        List<TaskDto> taskDtos = new ArrayList<>();
        for (SyncTaskEntity entity : list){
            TaskDto taskDto = new TaskDto();
            taskDto.setTaskId(entity.getId());
            taskDto.setInstCount(entity.getInstanceCount());
            taskDto.setTargetType(entity.getTargetType());
            taskDto.setUpdateTaskRuleTime(entity.getUpdatedTime());
            taskDtos.add(taskDto);
        }

        ZkBridgeClient zkBridgeClient = zkBridgeClientFactory.getZkBridgeClient(env);
        zkBridgeClient.writeData(zkBridgeClient.getRootPath() + "/" + clusterCode,
                JSON.toJSONStringWithDateFormat(taskDtos,"yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteDateUseDateFormat));
    }

}
