package com.rainbow.bridge.mysqltarget;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rainbow.bridge.biz.entity.mysql.SyncTaskRuleMysqlEntity;
import com.rainbow.bridge.core.enums.StatusEnum;
import com.rainbow.bridge.core.utils.BeanUtil;
import com.rainbow.bridge.targetcore.factory.taskrule.AbsTaskRuleFactory;

import java.util.List;

/**
 * mysql任务规则
 * @author gujiachun
 */
public class MysqlTaskRuleFactory extends AbsTaskRuleFactory<MysqlTaskRule> {

    private IService taskMysqlRuleService;

    public MysqlTaskRuleFactory(String targetType,IService taskMysqlRuleService){
        super(targetType);
        this.taskMysqlRuleService = taskMysqlRuleService;
    }

    @Override
    public List<MysqlTaskRule> getTaskRulesByTaskId(String taskId) {

        QueryWrapper<SyncTaskRuleMysqlEntity> wrapper = new QueryWrapper();
        wrapper.eq("task_id",taskId);
        wrapper.eq("status", StatusEnum.valid.getStatus());

        List<SyncTaskRuleMysqlEntity> syncTaskRuleMysqlEntityList = taskMysqlRuleService.list(wrapper);

        if (syncTaskRuleMysqlEntityList == null || syncTaskRuleMysqlEntityList.isEmpty()){
            return null;
        }

        List<MysqlTaskRule> mysqlTaskRules = BeanUtil.copyList(syncTaskRuleMysqlEntityList, MysqlTaskRule.class);

        return mysqlTaskRules;
    }
}
