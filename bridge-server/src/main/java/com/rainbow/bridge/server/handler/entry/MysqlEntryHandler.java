package com.rainbow.bridge.server.handler.entry;

import com.rainbow.bridge.core.entity.*;
import com.rainbow.bridge.core.enums.EventEnum;
import com.rainbow.bridge.core.utils.PropertiesUtil;
import com.rainbow.bridge.model.CanalModel;
import com.rainbow.bridge.server.adapter.BridgeAdapter;
import com.rainbow.bridge.server.factory.targetsource.TargetFactory;
import com.rainbow.bridge.server.factory.taskrule.TaskRuleFactory;
import com.rainbow.bridge.server.model.action.DeleteMysqlParam;
import com.rainbow.bridge.server.model.action.Param;
import com.rainbow.bridge.server.model.action.InsertMysqlParam;
import com.rainbow.bridge.server.model.action.UpdateMysqlParam;
import com.rainbow.bridge.server.model.task.MysqlTaskRule;
import com.rainbow.bridge.server.model.task.TaskRule;
import java.util.*;

/**
 * 目标源类型mysql的 同步处理
 * @author gujiachun
 */
public class MysqlEntryHandler extends AbsEntryHandler {

    public MysqlEntryHandler(String taskId, TaskRuleFactory taskRuleFactory, TargetFactory targetFactory,
                             String targetType, BridgeAdapter bridgeAdapter){
        this.taskRuleFactory = taskRuleFactory;
        this.taskId = taskId;
        this.targetType = targetType;
        this.batchTran = Boolean.TRUE;
        this.targetFactory = targetFactory;
        this.bridgeAdapter = bridgeAdapter;
    }

    @Override
    public Param buildInsertParam(String targetType, Integer targetId, String taskId, TargetFactory targetFactory, TaskRuleFactory taskRuleFactory,
                                     TaskRule taskRule, CanalModel model, Map<String, String> mysqlType, Map<String, Object> values) throws Exception {

        MysqlTaskRule mysqlTaskRule = (MysqlTaskRule)taskRule ;

        InsertMysqlParam param = new InsertMysqlParam(taskId,targetId, mysqlTaskRule.getTargetDb(),mysqlTaskRule.getTargetTable());
        //目标源的主键原则
        Map<String, String> insertTargetPkMap = PropertiesUtil.stringToMap(mysqlTaskRule.getInsertTargetPks());
        if (insertTargetPkMap != null && !insertTargetPkMap.isEmpty()){
            for (String key : insertTargetPkMap.keySet()){
                String pkType = insertTargetPkMap.get(key);
                //现在只支持 uuid
                if ("uuid".equals(pkType)){
                    String pkValue = UUID.randomUUID().toString();
                    Pk pk = new Pk(key,pkValue);
                    param.addPk(pk);
                }
            }
        }

        //列名对
        Map<String, String> sourceTargetCols = PropertiesUtil.stringToMap(mysqlTaskRule.getSyncColumns());
        if (sourceTargetCols == null){
            logger.error("同步规则id：{},syncColumns配置有错误，不能为空",mysqlTaskRule.getId());
        }else {
            for (String sourceCol : sourceTargetCols.keySet()) {
                //新增的值，是不是包含了；有的才更新
                if (values.containsKey(sourceCol)){
                    String targetCol = sourceTargetCols.get(sourceCol);
                    Object sourceColValue = values.get(sourceCol);
                    Column column = new Column(targetCol,sourceColValue);
                    param.addColumn(column);
                }
            }
        }

        //源头的 类型名对
        Map<String, String> originTypeCols = PropertiesUtil.stringToMap(mysqlTaskRule.getInsertTargetOriginCol());
        if (originTypeCols != null && !originTypeCols.isEmpty()){
            for (String origin : originTypeCols.keySet()) {
                String originValue = originTypeCols.get(origin);
                OriginCol originCol = new OriginCol(origin,originValue);
                param.addOrigin(originCol);
            }
        }

        //有主键 和 列名变化 才能更新
        if (param.getColumnList().size() > 0){
            return param;
        }

        return null;
    }

    @Override
    public void insertBatchOpr(Integer targetId, List<Param> params) throws Exception {
        if (params == null){
            return;
        }
        bridgeAdapter.execute(targetFactory.getTarget(targetId), EventEnum.insert,params);
    }

    @Override
    public void insertOpr(Integer targetId, Param param) throws Exception {
        return;
    }

    @Override
    public Param buildUpdateParam(String targetType, Integer targetId, String taskId, TargetFactory targetFactory,
                                  TaskRuleFactory taskRuleFactory, TaskRule taskRule, CanalModel model,
                                  Map<String, String> mysqlType, Map<String, Object> before, Map<String, Object> after) throws Exception {

        MysqlTaskRule mysqlTaskRule = (MysqlTaskRule)taskRule ;

        UpdateMysqlParam param = new UpdateMysqlParam(taskId,targetId,
                mysqlTaskRule.getTargetDb(),mysqlTaskRule.getTargetTable());
        //主键对
        Map<String, String> sourceTargetPkCols = PropertiesUtil.stringToMap(mysqlTaskRule.getSyncPks());
        if (sourceTargetPkCols == null){
            logger.error("同步规则id：{},syncPks配置有错误，不能为空",mysqlTaskRule.getId());
        }else {
            for (String sourcePkCol : sourceTargetPkCols.keySet()){
                String targetPkCol = sourceTargetPkCols.get(sourcePkCol);
                Object sourcePkValue = after.get(sourcePkCol);
                Pk pk = new Pk(targetPkCol,sourcePkValue);
                param.addPk(pk);
            }
        }

        //列名对
        Map<String, String> sourceTargetCols = PropertiesUtil.stringToMap(mysqlTaskRule.getSyncColumns());
        if (sourceTargetCols == null){
            logger.error("同步规则id：{},syncColumns配置有错误，不能为空",mysqlTaskRule.getId());
        }else {
            for (String sourceCol : sourceTargetCols.keySet()) {
                //更新的值，是不是包含了；有变化的才更新
                if (before.containsKey(sourceCol)){
                    String targetCol = sourceTargetCols.get(sourceCol);
                    Object sourceColValue = after.get(sourceCol);
                    Column column = new Column(targetCol,sourceColValue);
                    param.addColumn(column);
                }
            }
        }

        //有主键 和 列名变化 才能更新
        if (param.getPkList().size() > 0 &&
                param.getColumnList().size() > 0){
            return param;
        }
        return null;
    }

    @Override
    public void updateBatchOpr(Integer targetId, List<Param> params) throws Exception {
        if (params == null){
            return;
        }
        bridgeAdapter.execute(targetFactory.getTarget(targetId), EventEnum.update,params);
    }

    @Override
    public void updateOpr(Integer targetId, Param param) throws Exception {
        return;
    }

    @Override
    public Param buildDeleteParam(String targetType, Integer targetId, String taskId, TargetFactory targetFactory,
                                  TaskRuleFactory taskRuleFactory, TaskRule taskRule, CanalModel model,
                                  Map<String, String> mysqlType, Map<String, Object> values) throws Exception {

        MysqlTaskRule mysqlTaskRule = (MysqlTaskRule)taskRule ;

        DeleteMysqlParam param = new DeleteMysqlParam(taskId,targetId,
                mysqlTaskRule.getTargetDb(),mysqlTaskRule.getTargetTable());
        //主键对
        Map<String, String> sourceTargetPkCols = PropertiesUtil.stringToMap(mysqlTaskRule.getSyncPks());
        if (sourceTargetPkCols == null){
            logger.error("同步规则id：{},syncPks配置有错误，不能为空",mysqlTaskRule.getId());
        }else {
            for (String sourcePkCol : sourceTargetPkCols.keySet()){
                String targetPkCol = sourceTargetPkCols.get(sourcePkCol);
                Object sourcePkValue = values.get(sourcePkCol);
                Pk pk = new Pk(targetPkCol,sourcePkValue);
                param.addPk(pk);
            }
        }

        //列名对
        Map<String, String> sourceTargetCols = PropertiesUtil.stringToMap(mysqlTaskRule.getSyncColumns());
        if (sourceTargetCols != null && !sourceTargetCols.isEmpty()){
            for (String sourceCol : sourceTargetCols.keySet()) {

                Integer deleteStrategy = mysqlTaskRule.getDeleteStrategy();
                //删除
                if (deleteStrategy == 0){
                    continue;
                }
                //不一致 ，就忽略
                else if (deleteStrategy == 1){
                    String targetCol = sourceTargetCols.get(sourceCol);
                    Column column = new Column(targetCol,values.get(sourceCol));
                    param.addColumn(column);
                }
                //不一致 ，强制为null
                else if (deleteStrategy == 2){
                    String targetCol = sourceTargetCols.get(sourceCol);
                    Column column = new Column(targetCol,null);
                    param.addColumn(column);
                }
            }
        }
        //删除策略
        param.setDeleteStrategy(mysqlTaskRule.getDeleteStrategy());

        //有主键 和 列名变化 才能更新
        if (param.getPkList().size() > 0){
            return param;
        }

        return null;
    }

    @Override
    public void deleteBatchOpr(Integer targetId, List<Param> params) throws Exception {
        if (params == null){
            return;
        }
        bridgeAdapter.execute(targetFactory.getTarget(targetId), EventEnum.delete,params);
    }

    @Override
    public void deleteOpr(Integer targetId, Param param) throws Exception {
        return;
    }
}