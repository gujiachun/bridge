package com.rainbow.bridge.server.handler.entry;

import com.rainbow.bridge.core.enums.EventEnum;
import com.rainbow.bridge.model.CanalModel;
import com.rainbow.bridge.server.adapter.BridgeAdapter;
import com.rainbow.bridge.server.factory.targetsource.TargetFactory;
import com.rainbow.bridge.server.factory.taskrule.TaskRuleFactory;
import com.rainbow.bridge.server.model.action.Param;
import com.rainbow.bridge.server.model.action.RedisParam;
import com.rainbow.bridge.server.model.task.RedisTaskRule;
import com.rainbow.bridge.server.model.task.TaskRule;
import com.rainbow.bridge.server.utils.TemplateUtil;
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
        this.taskRuleFactory = taskRuleFactory;
        this.taskId = taskId;
        this.targetFactory = targetFactory;
        this.freeMarkerConfiguration = freeMarkerConfiguration;
        this.targetType = targetType;
        this.batchTran = Boolean.FALSE;
        this.bridgeAdapter = bridgeAdapter;
    }

    @Override
    public Param buildInsertParam(String targetType, Integer targetId, String taskId, TargetFactory targetFactory,
                                  TaskRuleFactory taskRuleFactory, TaskRule taskRule, CanalModel model,
                                  Map<String, String> mysqlType, Map<String, Object> values) throws Exception {
        return buildParam(taskRule,targetId,values);
    }

    @Override
    public void insertBatchOpr(Integer targetId, List<Param> params) throws Exception {
        return;
    }

    @Override
    public void insertOpr(Integer targetId, Param param) throws Exception {
        bridgeAdapter.execute(targetFactory.getTarget(targetId), EventEnum.insert,param);
    }

    @Override
    public Param buildUpdateParam(String targetType, Integer targetId, String taskId, TargetFactory targetFactory,
                                  TaskRuleFactory taskRuleFactory, TaskRule taskRule, CanalModel model,
                                  Map<String, String> mysqlType, Map<String, Object> before, Map<String, Object> after) throws Exception {
        return buildParam(taskRule,targetId,after);
    }

    @Override
    public void updateOpr(Integer targetId, Param param) throws Exception {
        bridgeAdapter.execute(targetFactory.getTarget(targetId), EventEnum.update,param);
    }

    @Override
    public void updateBatchOpr(Integer targetId, List<Param> params) throws Exception {
        return;
    }

    @Override
    public Param buildDeleteParam(String targetType, Integer targetId, String taskId, TargetFactory targetFactory,
                                  TaskRuleFactory taskRuleFactory, TaskRule taskRule, CanalModel model,
                                  Map<String, String> mysqlType, Map<String, Object> values) throws Exception {

        return buildParam(taskRule,targetId,values);
    }

    @Override
    public void deleteOpr(Integer targetId, Param param) throws Exception {
        bridgeAdapter.execute(targetFactory.getTarget(targetId), EventEnum.delete, param);
    }

    @Override
    public void deleteBatchOpr(Integer targetId, List<Param> params) throws Exception {
        return;
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
