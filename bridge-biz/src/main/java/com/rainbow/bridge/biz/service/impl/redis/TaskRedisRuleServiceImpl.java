package com.rainbow.bridge.biz.service.impl.redis;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainbow.bridge.biz.entity.redis.SyncTaskRuleRedisEntity;
import com.rainbow.bridge.biz.mapper.redis.SyncTaskRuleRedisMapper;
import com.rainbow.bridge.biz.service.redis.TaskRedisRuleService;
import org.springframework.stereotype.Service;

/**
 * @author gujiachun
 */
@Service
public class TaskRedisRuleServiceImpl extends ServiceImpl<SyncTaskRuleRedisMapper, SyncTaskRuleRedisEntity> implements TaskRedisRuleService {
}
