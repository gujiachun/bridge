package com.rainbow.bridge.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainbow.bridge.biz.entity.BasicMqEntity;
import com.rainbow.bridge.biz.entity.BasicTopicEntity;
import com.rainbow.bridge.biz.entity.SyncTaskEntity;
import com.rainbow.bridge.biz.mapper.BasicTopicMapper;
import com.rainbow.bridge.biz.mapper.SyncTaskMapper;
import com.rainbow.bridge.biz.service.TaskService;
import com.rainbow.bridge.biz.service.TopicService;
import org.springframework.stereotype.Service;

/**
 * @author gujiachun
 */
@Service
public class TopicServiceImpl extends ServiceImpl<BasicTopicMapper, BasicTopicEntity> implements TopicService {
}
