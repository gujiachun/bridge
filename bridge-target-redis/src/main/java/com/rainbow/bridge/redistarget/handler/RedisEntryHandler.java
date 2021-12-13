package com.rainbow.bridge.redistarget.handler;

import com.rainbow.bridge.core.enums.EventEnum;
import com.rainbow.bridge.model.CanalModel;
import com.rainbow.bridge.redistarget.RedisTaskRule;
import com.rainbow.bridge.redistarget.param.RedisParam;
import com.rainbow.bridge.targetcore.adapter.BridgeAdapter;
import com.rainbow.bridge.targetcore.factory.targetsource.TargetFactory;
import com.rainbow.bridge.targetcore.factory.taskrule.TaskRuleFactory;
import com.rainbow.bridge.targetcore.handler.AbsEntryHandler;
import com.rainbow.bridge.targetcore.model.Param;
import com.rainbow.bridge.targetcore.model.TaskRule;
import com.rainbow.bridge.targetcore.utils.TemplateUtil;
import freemarker.template.Configuration;
import org.apache.commons.lang3.StringUtils;

import java.sql.Time;
import java.util.*;

/**
 * @author gujiachun
 */
public class RedisEntryHandler extends AbsEntryHandler {

    private Configuration freeMarkerConfiguration;

    public RedisEntryHandler(String taskId, TaskRuleFactory taskRuleFactory, TargetFactory targetFactory,
                             Configuration freeMarkerConfiguration, String targetType, BridgeAdapter bridgeAdapter){
        super(taskId, taskRuleFactory, targetFactory, targetType, bridgeAdapter,Boolean.FALSE);
        this.freeMarkerConfiguration = freeMarkerConfiguration;
    }

    @Override
    public Param buildInsertParam(String targetType, Integer targetId, String taskId, TargetFactory targetFactory,
                                  TaskRuleFactory taskRuleFactory, TaskRule taskRule, CanalModel model,
                                  Map<String, String> mysqlType, Map<String, Object> values) throws Exception {
        return buildParam(taskRule,targetId,values);
    }

    @Override
    public Param buildUpdateParam(String targetType, Integer targetId, String taskId, TargetFactory targetFactory,
                                  TaskRuleFactory taskRuleFactory, TaskRule taskRule, CanalModel model,
                                  Map<String, String> mysqlType, Map<String, Object> before, Map<String, Object> after) throws Exception {
        return buildParam(taskRule,targetId,after);
    }

    @Override
    public Param buildDeleteParam(String targetType, Integer targetId, String taskId, TargetFactory targetFactory,
                                  TaskRuleFactory taskRuleFactory, TaskRule taskRule, CanalModel model,
                                  Map<String, String> mysqlType, Map<String, Object> values) throws Exception {

        return buildParam(taskRule,targetId,values);
    }

    private Param buildParam(TaskRule taskRule, Integer targetId, Map<String, Object> values) throws Exception {
        RedisTaskRule redisTaskRule = (RedisTaskRule) taskRule;

        String command = redisTaskRule.getCommand();
        String key = TemplateUtil.processTemplate(freeMarkerConfiguration,redisTaskRule.getKeyFormat(),values);
        String field = StringUtils.isNotBlank(redisTaskRule.getFieldFormat()) ?  TemplateUtil.processTemplate(freeMarkerConfiguration,redisTaskRule.getFieldFormat(),values) : null;
        String value = StringUtils.isNotBlank(redisTaskRule.getValueFormat()) ?  TemplateUtil.processTemplate(freeMarkerConfiguration,redisTaskRule.getValueFormat(),values) : null;
        Long expireTime = redisTaskRule.getExpireTime();
        Time fixedTime = redisTaskRule.getFixedTime();

        return new RedisParam(taskId,targetId,command,key,field,value,expireTime,fixedTime);
    }
}
