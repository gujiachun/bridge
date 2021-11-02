package com.rainbow.bridge.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainbow.bridge.biz.entity.SyncTaskRuleRedisEntity;
import com.rainbow.bridge.biz.mapper.SyncTaskRuleRedisMapper;
import com.rainbow.bridge.biz.service.TaskRedisRuleService;
import org.springframework.stereotype.Service;

/**
 * @author gujiachun
 */
@Service
public class TaskRedisRuleServiceImpl extends ServiceImpl<SyncTaskRuleRedisMapper, SyncTaskRuleRedisEntity> implements TaskRedisRuleService {
}
