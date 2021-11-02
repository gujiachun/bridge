package com.rainbow.bridge.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rainbow.bridge.biz.dto.MysqlTargetDto;
import com.rainbow.bridge.biz.dto.TaskBizDto;
import com.rainbow.bridge.biz.dto.query.TargetQueryDto;
import com.rainbow.bridge.biz.dto.query.TaskQueryDto;
import com.rainbow.bridge.biz.entity.BasicMqEntity;
import com.rainbow.bridge.biz.entity.BasicTopicEntity;
import com.rainbow.bridge.biz.entity.SyncTaskEntity;

/**
 * @author gujiachun
 */
public interface TaskService extends IService<SyncTaskEntity> {

    BasicMqEntity getBasicMqEntityByTaskId(String taskId);

    BasicTopicEntity getBasicTopicEntityByTaskId(String taskId);

    IPage<TaskBizDto> query(TaskQueryDto taskQueryDto);
}
