package com.rainbow.bridge.estarget;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rainbow.bridge.biz.entity.es.SyncTaskRuleEsEntity;
import com.rainbow.bridge.biz.entity.redis.SyncTaskRuleRedisEntity;
import com.rainbow.bridge.core.enums.StatusEnum;
import com.rainbow.bridge.core.utils.BeanUtil;
import com.rainbow.bridge.targetcore.factory.taskrule.AbsTaskRuleFactory;
import com.rainbow.bridge.targetcore.model.TaskRule;

import java.util.List;

/**
 * es任务规则
 * @author gujiachun
 */
public class EsTaskRuleFactory extends AbsTaskRuleFactory<EsTaskRule> {

    private IService taskEsRuleService;

    public EsTaskRuleFactory(String targetType, IService taskEsRuleService){
        super(targetType);
        this.taskEsRuleService = taskEsRuleService;
    }

    @Override
    public List<EsTaskRule> getTaskRulesByTaskId(String taskId) {

        QueryWrapper<SyncTaskRuleEsEntity> wrapper = new QueryWrapper();
        wrapper.eq("task_id",taskId);
        wrapper.eq("status", StatusEnum.valid.getStatus());

        List<SyncTaskRuleEsEntity> syncTaskRuleEsEntityList = taskEsRuleService.list(wrapper);

        if (syncTaskRuleEsEntityList == null || syncTaskRuleEsEntityList.isEmpty()){
            return null;
        }

        List<EsTaskRule> mysqlTaskRules = BeanUtil.copyList(syncTaskRuleEsEntityList, EsTaskRule.class);

        return mysqlTaskRules;
    }
}
