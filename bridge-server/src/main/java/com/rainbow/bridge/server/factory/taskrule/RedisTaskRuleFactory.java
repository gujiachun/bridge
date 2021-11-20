package com.rainbow.bridge.server.factory.taskrule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rainbow.bridge.biz.entity.SyncTaskRuleMysqlEntity;
import com.rainbow.bridge.biz.entity.SyncTaskRuleRedisEntity;
import com.rainbow.bridge.biz.service.TaskMysqlRuleService;
import com.rainbow.bridge.biz.service.TaskRedisRuleService;
import com.rainbow.bridge.core.enums.StatusEnum;
import com.rainbow.bridge.core.utils.BeanUtil;
import com.rainbow.bridge.server.model.task.MysqlTaskRule;
import com.rainbow.bridge.server.model.task.RedisTaskRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * redis任务规则
 * @author gujiachun
 */
public class RedisTaskRuleFactory extends AbsTaskRuleFactory<RedisTaskRule> {

    private IService taskRedisRuleService;

    public RedisTaskRuleFactory(IService taskRedisRuleService){
        this.taskRedisRuleService = taskRedisRuleService;
    }

    @Override
    public List<RedisTaskRule> getTaskRulesByTaskId(String taskId) {

        QueryWrapper<SyncTaskRuleRedisEntity> wrapper = new QueryWrapper();
        wrapper.eq("task_id",taskId);
        wrapper.eq("status", StatusEnum.valid.getStatus());

        List<SyncTaskRuleRedisEntity> syncTaskRuleRedisEntityList = taskRedisRuleService.list(wrapper);

        if (syncTaskRuleRedisEntityList == null || syncTaskRuleRedisEntityList.isEmpty()){
            return null;
        }

        List<RedisTaskRule> mysqlTaskRules = BeanUtil.copyList(syncTaskRuleRedisEntityList, RedisTaskRule.class);

        return mysqlTaskRules;
    }
}
