package com.rainbow.bridge.biz.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainbow.bridge.biz.dto.TaskBizDto;
import com.rainbow.bridge.biz.dto.query.TaskQueryDto;
import com.rainbow.bridge.biz.entity.BasicMqEntity;
import com.rainbow.bridge.biz.entity.BasicTopicEntity;
import com.rainbow.bridge.biz.entity.SyncTaskEntity;
import com.rainbow.bridge.biz.mapper.SyncTaskMapper;
import com.rainbow.bridge.biz.service.MqService;
import com.rainbow.bridge.biz.service.TaskService;
import com.rainbow.bridge.biz.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gujiachun
 */
@Service
public class TaskServiceImpl extends ServiceImpl<SyncTaskMapper, SyncTaskEntity> implements TaskService {

    @Autowired
    private TopicService topicService;

    @Autowired
    private MqService mqService;

    @Override
    public BasicMqEntity getBasicMqEntityByTaskId(String taskId) {
        SyncTaskEntity syncTaskEntity = this.getById(taskId);

        BasicTopicEntity basicTopicEntity = topicService.getById(syncTaskEntity.getBasicTopicId());

        return mqService.getById(basicTopicEntity.getMqId());
    }

    @Override
    public BasicTopicEntity getBasicTopicEntityByTaskId(String taskId) {
        SyncTaskEntity syncTaskEntity = this.getById(taskId);

        return topicService.getById(syncTaskEntity.getBasicTopicId());
    }

    @Override
    public IPage<TaskBizDto> query(TaskQueryDto taskQueryDto) {
        IPage<TaskBizDto> page = new Page<>(taskQueryDto.getCurrentPage(), taskQueryDto.getPageSize());
        return baseMapper.queryTask(page,taskQueryDto);
    }


}
