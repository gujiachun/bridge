package com.rainbow.bridge.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rainbow.bridge.admin.model.TopicVo;
import com.rainbow.bridge.admin.model.query.TopicQueryVo;
import com.rainbow.bridge.biz.entity.BasicTopicEntity;
import com.rainbow.bridge.biz.entity.SyncTaskEntity;
import com.rainbow.bridge.biz.service.TaskService;
import com.rainbow.bridge.biz.service.TopicService;
import com.rainbow.bridge.core.PageResult;
import com.rainbow.bridge.core.Result;
import com.rainbow.bridge.core.utils.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author gujiachun
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class TopicController {

    private final TopicService topicService;

    private final TaskService taskService;

    @GetMapping("/topic")
    public Result<PageResult<BasicTopicEntity>> getList(TopicQueryVo vo){

        LambdaQueryWrapper<BasicTopicEntity> queryWrapper = Wrappers.lambdaQuery(BasicTopicEntity.class);
        queryWrapper.eq(StringUtils.isNotEmpty(vo.getEnv()), BasicTopicEntity::getEnv, vo.getEnv());
        queryWrapper.like(StringUtils.isNotEmpty(vo.getTopic()), BasicTopicEntity::getTopic, vo.getTopic());
        IPage<BasicTopicEntity> page = topicService.page(new Page<>(vo.getCurrentPage(), vo.getPageSize()), queryWrapper);

        return Result.success(new PageResult<>(page.getTotal(), BeanUtil.copyList(page.getRecords(), BasicTopicEntity.class)));
    }

    @GetMapping("/topic/env")
    public Result<List<BasicTopicEntity>> getListByEnv(@RequestParam("env") String env){

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("env",env);

        return Result.success(topicService.list(queryWrapper));
    }

    @GetMapping("/topic/task/{taskId}")
    public Result<BasicTopicEntity> getTopicEntity(@PathVariable("taskId") String taskId){

        SyncTaskEntity taskEntity = taskService.getById(taskId);

        return Result.success(topicService.getById(taskEntity.getBasicTopicId()));
    }

    @PostMapping("/topic")
    public Result<Boolean> add(@RequestBody TopicVo vo){
        BasicTopicEntity entity = new BasicTopicEntity();
        entity.setTopic(vo.getTopic());
        entity.setEnv(vo.getEnv());
        entity.setMqId(vo.getMqId());
        entity.setRemark(vo.getRemark());
        entity.setSourceId(vo.getSourceId());
        entity.setSyncDb(vo.getSyncDb());
        entity.setSyncTable(vo.getSyncTable());
//        if (StringUtils.isNotBlank(vo.getSyncTable())){
//            List<String> strings = JSONObject.parseArray(vo.getSyncTable(), String.class);
//            String join = String.join(",", strings);
//            entity.setSyncTable(join);
//        }

        return Result.success(topicService.save(entity));
    }

    @PutMapping("/topic")
    public Result<Boolean> update(@RequestBody TopicVo vo){

        BasicTopicEntity entity = new BasicTopicEntity();
        entity.setTopic(vo.getTopic());
        entity.setEnv(vo.getEnv());
        entity.setMqId(vo.getMqId());
        entity.setSourceId(vo.getSourceId());
        entity.setRemark(vo.getRemark());
        entity.setSyncDb(vo.getSyncDb());
        entity.setSyncTable(vo.getSyncTable());
        entity.setId(vo.getId());

        return Result.success(topicService.updateById(entity));
    }

    @DeleteMapping("/topic/{id}")
    public Result<Boolean> delete(@PathVariable("id") Integer id){
        return Result.success(topicService.removeById(id));
    }

}
