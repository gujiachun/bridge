package com.rainbow.bridge.estarget.handler;

import com.alibaba.fastjson.JSONObject;
import com.rainbow.bridge.core.constant.CommonCons;
import com.rainbow.bridge.estarget.EsTaskRule;
import com.rainbow.bridge.estarget.param.EsParam;
import com.rainbow.bridge.model.CanalModel;
import com.rainbow.bridge.targetcore.adapter.BridgeAdapter;
import com.rainbow.bridge.targetcore.factory.targetsource.TargetFactory;
import com.rainbow.bridge.targetcore.factory.taskrule.TaskRuleFactory;
import com.rainbow.bridge.targetcore.handler.AbsEntryHandler;
import com.rainbow.bridge.targetcore.model.Param;
import com.rainbow.bridge.targetcore.model.TaskRule;
import com.rainbow.bridge.targetcore.utils.TemplateUtil;
import freemarker.template.Configuration;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author gujiachun
 */
public class EsEntryHandler extends AbsEntryHandler {

    private Configuration freeMarkerConfiguration;

    public EsEntryHandler(String taskId, TaskRuleFactory taskRuleFactory, TargetFactory targetFactory,
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
        EsTaskRule esTaskRule = (EsTaskRule) taskRule;

        String indexFormat = TemplateUtil.processTemplate(freeMarkerConfiguration,esTaskRule.getIndexFormat(),values);
        String idFormat = TemplateUtil.processTemplate(freeMarkerConfiguration,esTaskRule.getIdFormat(),values);
        String sqlFormat = esTaskRule.getSqlFormat();
        String sqlFieldFormat = TemplateUtil.processTemplate(freeMarkerConfiguration,esTaskRule.getSqlFieldFormat(),values);
        String fieldType = esTaskRule.getFieldType();
        Integer relationType = esTaskRule.getRelationType();
        String relationFieldName = esTaskRule.getRelationFieldName();
        String relationJoinName = esTaskRule.getRelationJoinName();
        String relationChildRouteFormat = StringUtils.isNotBlank(esTaskRule.getRelationChildRouteFormat()) ?  TemplateUtil.processTemplate(freeMarkerConfiguration,esTaskRule.getRelationChildRouteFormat(),values) : null;
        String relationChildParentFormat = StringUtils.isNotBlank(esTaskRule.getRelationChildParentFormat()) ?  TemplateUtil.processTemplate(freeMarkerConfiguration,esTaskRule.getRelationChildParentFormat(),values) : null;
        Integer deleteStrategy = esTaskRule.getDeleteStrategy();

        EsParam esParam = new EsParam(taskId,targetId,indexFormat,idFormat,sqlFormat,sqlFieldFormat,fieldType,
                esTaskRule.getSqlNullField(),esTaskRule.getSkipsField(),relationType,
                relationFieldName,relationJoinName,relationChildRouteFormat,relationChildParentFormat,deleteStrategy,
                esTaskRule.getIndexType(),esTaskRule.getPartFormat());

        //更新索引部分属性
        if (CommonCons.EsIndexType.part_type.equals(esTaskRule.getIndexType())){
            Map<String, Object> partMap = esParam.getPartMap();
            if (StringUtils.isNotBlank(esTaskRule.getPartFormat())){
                JSONObject data = JSONObject.parseObject(esTaskRule.getPartFormat());
                if (data != null){
                    //遍历
                    for (String esFiled : data.keySet()){
                        //对应db的列名
                        String dbColName = data.getString(esFiled);
                        partMap.put(esFiled,values.get(dbColName));
                    }
                }
            }
        }

        return esParam;
    }
}
