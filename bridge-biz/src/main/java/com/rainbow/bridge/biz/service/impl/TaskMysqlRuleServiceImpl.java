package com.rainbow.bridge.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainbow.bridge.biz.entity.SyncTaskRuleMysqlEntity;
import com.rainbow.bridge.biz.mapper.SyncTaskRuleMysqlMapper;
import com.rainbow.bridge.biz.service.TaskMysqlRuleService;
import org.springframework.stereotype.Service;

/**
 * @author gujiachun
 */
@Service
public class TaskMysqlRuleServiceImpl extends ServiceImpl<SyncTaskRuleMysqlMapper, SyncTaskRuleMysqlEntity> implements TaskMysqlRuleService {
}
