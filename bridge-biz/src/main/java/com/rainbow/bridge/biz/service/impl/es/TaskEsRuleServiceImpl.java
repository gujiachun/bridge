package com.rainbow.bridge.biz.service.impl.es;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainbow.bridge.biz.entity.es.SyncTaskRuleEsEntity;
import com.rainbow.bridge.biz.mapper.es.SyncTaskRuleEsMapper;
import com.rainbow.bridge.biz.service.es.TaskEsRuleService;
import org.springframework.stereotype.Service;

/**
 * @author gujiachun
 */
@Service
public class TaskEsRuleServiceImpl extends ServiceImpl<SyncTaskRuleEsMapper, SyncTaskRuleEsEntity> implements TaskEsRuleService {
}
