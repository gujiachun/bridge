package com.rainbow.bridge.biz.service.impl.mysql;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rainbow.bridge.biz.entity.mysql.SyncTaskRuleMysqlEntity;
import com.rainbow.bridge.biz.mapper.mysql.SyncTaskRuleMysqlMapper;
import com.rainbow.bridge.biz.service.mysql.TaskMysqlRuleService;
import org.springframework.stereotype.Service;

/**
 * @author gujiachun
 */
@Service
public class TaskMysqlRuleServiceImpl extends ServiceImpl<SyncTaskRuleMysqlMapper, SyncTaskRuleMysqlEntity> implements TaskMysqlRuleService {
}
