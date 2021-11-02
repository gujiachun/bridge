package com.rainbow.bridge.server.handler;

import com.alibaba.fastjson.JSON;
import com.googlecode.aviator.AviatorEvaluator;
import com.rainbow.bridge.biz.entity.SyncTaskRuleMysqlEntity;
import com.rainbow.bridge.core.entity.*;
import com.rainbow.bridge.core.utils.PropertiesUtil;
import com.rainbow.bridge.core.utils.PublicUtil;
import com.rainbow.bridge.handler.EntryHandler;
import com.rainbow.bridge.model.CanalModel;
import com.rainbow.bridge.server.factory.MysqlDataSourceFactory;
import com.rainbow.bridge.server.factory.TaskRuleFactory;
import com.rainbow.bridge.server.utils.JdbcUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

/**
 * @author gujiachun
 */
public class MysqlEntryHandler implements EntryHandler {

    private static final Logger logger = LoggerFactory.getLogger(MysqlEntryHandler.class);

    private String taskId;

    public MysqlEntryHandler(String taskId,TaskRuleFactory taskRuleFactory,MysqlDataSourceFactory mysqlDataSourceFactory){
        this.taskRuleFactory = taskRuleFactory;
        this.taskId = taskId;
        this.mysqlDataSourceFactory = mysqlDataSourceFactory;
    }

    private TaskRuleFactory taskRuleFactory;

    private MysqlDataSourceFactory mysqlDataSourceFactory;

    @Override
    public void insert(CanalModel model, Map<String,String> mysqlType,Map<String, Object> t) throws Exception {
        List<SyncTaskRuleMysqlEntity> insertSyncTaskRuleList = taskRuleFactory.getInsertSyncMysqlTaskRuleList(taskId);
        Set<Integer> targetIdList = taskRuleFactory.getMysqlTargetIds(insertSyncTaskRuleList);
        for (Integer targetId : targetIdList){
            List<SyncTaskRuleMysqlEntity> sameTargetIdList = taskRuleFactory.getSyncMysqlTaskRuleListByTaskAndTargetId(insertSyncTaskRuleList, targetId);

            //更新的规则
            List<InsertEntity> insertEntityList = new ArrayList<>();

            for (SyncTaskRuleMysqlEntity syncTaskRuleMysqlEntity : sameTargetIdList){

                //配对源头
                if (model.getDatabase().equals(syncTaskRuleMysqlEntity.getSourceDb()) &&
                        model.getTable().equals(syncTaskRuleMysqlEntity.getSourceTable())){

                    int filter = 0;
                    //针对 新增的值 动态的表达式
                    if (StringUtils.isNotBlank(syncTaskRuleMysqlEntity.getInsertSourceConditionFilter())){
                        Object ave = AviatorEvaluator.execute(syncTaskRuleMysqlEntity.getInsertSourceConditionFilter(), t);

                        Boolean b = Boolean.valueOf(ave.toString());
                        filter = b ? filter : 1;

                        logger.info(">>>>执行新增 过滤条件:{} ",b);
                    }

                    //表达式 都通过了，可以同步了
                    if (filter == 0){
                        //new update entity
                        InsertEntity insertEntity = new InsertEntity(syncTaskRuleMysqlEntity.getTargetId(),
                                syncTaskRuleMysqlEntity.getTargetDb(),syncTaskRuleMysqlEntity.getTargetTable());
                        //目标源的主键原则
                        Map<String, String> insertTargetPkMap = PropertiesUtil.stringToMap(syncTaskRuleMysqlEntity.getInsertTargetPks());
                        if (insertTargetPkMap != null && !insertTargetPkMap.isEmpty()){
                            for (String key : insertTargetPkMap.keySet()){
                                String pkType = insertTargetPkMap.get(key);
                                //现在只支持 uuid
                                if ("uuid".equals(pkType)){
                                    String pkValue = UUID.randomUUID().toString();
                                    Pk pk = new Pk(key,pkValue);
                                    insertEntity.addPk(pk);
                                }
                            }
                        }

                        //列名对
                        Map<String, String> sourceTargetCols = PropertiesUtil.stringToMap(syncTaskRuleMysqlEntity.getSyncColumns());
                        if (sourceTargetCols == null){
                            logger.error("同步规则id：{},syncColumns配置有错误，不能为空",syncTaskRuleMysqlEntity.getId());
                        }else {
                            for (String sourceCol : sourceTargetCols.keySet()) {
                                //新增的值，是不是包含了；有的才更新
                                if (t.containsKey(sourceCol)){
                                    String targetCol = sourceTargetCols.get(sourceCol);
                                    Object sourceColValue = t.get(sourceCol);
                                    Column column = new Column(targetCol,sourceColValue);
                                    insertEntity.addColumn(column);
                                }
                            }
                        }

                        //源头的 类型名对
                        Map<String, String> originTypeCols = PropertiesUtil.stringToMap(syncTaskRuleMysqlEntity.getInsertTargetOriginCol());
                        if (originTypeCols != null && !originTypeCols.isEmpty()){
                            for (String origin : originTypeCols.keySet()) {
                                String originValue = originTypeCols.get(origin);
                                OriginCol originCol = new OriginCol(origin,originValue);
                                insertEntity.addOrigin(originCol);
                            }
                        }

                        //有主键 和 列名变化 才能更新
                        if (insertEntity.getColumnList().size() > 0){
                            insertEntityList.add(insertEntity);
                        }
                    }
                }
            }
            logger.info("相同sameTargetIdList:{}", JSON.toJSONString(sameTargetIdList));

            JdbcUtil._insert(mysqlDataSourceFactory.getDataSource(targetId),insertEntityList);
        }

        logger.info("》》》》》》》》》insert,taskId:{} database:{}  table:{}",taskId,model.getDatabase(),model.getTable());
    }

    @Override
    public void update(CanalModel model, Map<String,String> mysqlType,Map<String, Object> before, Map<String, Object> after) throws Exception {

        List<SyncTaskRuleMysqlEntity> updateSyncTaskRuleList = taskRuleFactory.getUpdateSyncMysqlTaskRuleList(taskId);
        Set<Integer> targetIdList = taskRuleFactory.getMysqlTargetIds(updateSyncTaskRuleList);
        for (Integer targetId : targetIdList){
            List<SyncTaskRuleMysqlEntity> sameTargetIdList = taskRuleFactory.getSyncMysqlTaskRuleListByTaskAndTargetId(updateSyncTaskRuleList, targetId);

            //更新的规则
            List<UpdateEntity> updateEntityList = new ArrayList<>();

            for (SyncTaskRuleMysqlEntity syncTaskRuleMysqlEntity : sameTargetIdList){

                //配对源头
                if (model.getDatabase().equals(syncTaskRuleMysqlEntity.getSourceDb()) &&
                            model.getTable().equals(syncTaskRuleMysqlEntity.getSourceTable())){

                    int filter = 0;
                    //针对 修改前的值 动态的表达式
                    if (StringUtils.isNotBlank(syncTaskRuleMysqlEntity.getUpdateSourceConditionFilter())){
                        //获取以前的值
                        Map<String, Object> beforeAllMap = PublicUtil.getBefore(before,after);

                        Object ave = AviatorEvaluator.execute(syncTaskRuleMysqlEntity.getUpdateSourceConditionFilter(), beforeAllMap);

                        Boolean b = Boolean.valueOf(ave.toString());
                        filter = b ? filter : 1;

                        logger.info(">>>>执行修改过滤条件--修改前的值 过滤条件:{} ",b);
                    }
                    //针对 修改后的值 动态的表达式
                    if (StringUtils.isNotBlank(syncTaskRuleMysqlEntity.getUpdateNewConditionFilter())){
                        Object ave = AviatorEvaluator.execute(syncTaskRuleMysqlEntity.getUpdateNewConditionFilter(), after);
                        Boolean b = Boolean.valueOf(ave.toString());
                        filter = b ? filter : 1;
                        logger.info(">>>>>执行修改过滤条件--修改后的值 过滤条件:{} ",b);
                    }

                    //表达式 都通过了，可以同步了
                    if (filter == 0){
                        //new update entity
                        UpdateEntity updateEntity = new UpdateEntity(syncTaskRuleMysqlEntity.getTargetId(),
                                syncTaskRuleMysqlEntity.getTargetDb(),syncTaskRuleMysqlEntity.getTargetTable());
                        //主键对
                        Map<String, String> sourceTargetPkCols = PropertiesUtil.stringToMap(syncTaskRuleMysqlEntity.getSyncPks());
                        if (sourceTargetPkCols == null){
                            logger.error("同步规则id：{},syncPks配置有错误，不能为空",syncTaskRuleMysqlEntity.getId());
                        }else {
                            for (String sourcePkCol : sourceTargetPkCols.keySet()){
                                String targetPkCol = sourceTargetPkCols.get(sourcePkCol);
                                Object sourcePkValue = after.get(sourcePkCol);
                                Pk pk = new Pk(targetPkCol,sourcePkValue);
                                updateEntity.addPk(pk);
                            }
                        }

                        //列名对
                        Map<String, String> sourceTargetCols = PropertiesUtil.stringToMap(syncTaskRuleMysqlEntity.getSyncColumns());
                        if (sourceTargetCols == null){
                            logger.error("同步规则id：{},syncColumns配置有错误，不能为空",syncTaskRuleMysqlEntity.getId());
                        }else {
                            for (String sourceCol : sourceTargetCols.keySet()) {
                                //更新的值，是不是包含了；有变化的才更新
                                if (before.containsKey(sourceCol)){
                                    String targetCol = sourceTargetCols.get(sourceCol);
                                    Object sourceColValue = after.get(sourceCol);
                                    Column column = new Column(targetCol,sourceColValue);
                                    updateEntity.addColumn(column);
                                }
                            }
                        }

                        //有主键 和 列名变化 才能更新
                        if (updateEntity.getPkList().size() > 0 &&
                             updateEntity.getColumnList().size() > 0){
                            updateEntityList.add(updateEntity);
                        }
                    }
                }
            }
            logger.info("相同sameTargetIdList:{}", JSON.toJSONString(sameTargetIdList));

            JdbcUtil._update(mysqlDataSourceFactory.getDataSource(targetId),updateEntityList);
        }
        logger.info("》》》》》》》》》update,taskId:{} database:{}  table:{}",taskId,model.getDatabase(),model.getTable());
    }

    @Override
    public void delete(CanalModel model, Map<String,String> mysqlType,Map<String, Object> t) throws Exception {
        List<SyncTaskRuleMysqlEntity> deleteSyncTaskRuleList = taskRuleFactory.getDeleteSyncMysqlTaskRuleList(taskId);
        Set<Integer> targetIdList = taskRuleFactory.getMysqlTargetIds(deleteSyncTaskRuleList);
        for (Integer targetId : targetIdList){
            List<SyncTaskRuleMysqlEntity> sameTargetIdList = taskRuleFactory.getSyncMysqlTaskRuleListByTaskAndTargetId(deleteSyncTaskRuleList, targetId);

            //更新的规则
            List<DeleteEntity> deleteEntityList = new ArrayList<>();

            for (SyncTaskRuleMysqlEntity syncTaskRuleMysqlEntity : sameTargetIdList){

                //配对源头
                if (model.getDatabase().equals(syncTaskRuleMysqlEntity.getSourceDb()) &&
                        model.getTable().equals(syncTaskRuleMysqlEntity.getSourceTable())){

                    int filter = 0;
                    //针对 修改前的值 动态的表达式
                    if (StringUtils.isNotBlank(syncTaskRuleMysqlEntity.getDeleteSourceConditionFilter())){
                        Object ave = AviatorEvaluator.execute(syncTaskRuleMysqlEntity.getUpdateSourceConditionFilter(), t);

                        Boolean b = Boolean.valueOf(ave.toString());
                        filter = b ? filter : 1;

                        logger.info(">>>>执行删除 过滤条件:{} ",b);
                    }

                    //表达式 都通过了，可以同步了
                    if (filter == 0){
                        //new update entity
                        DeleteEntity deleteEntity = new DeleteEntity(syncTaskRuleMysqlEntity.getTargetId(),
                                syncTaskRuleMysqlEntity.getTargetDb(),syncTaskRuleMysqlEntity.getTargetTable());
                        //主键对
                        Map<String, String> sourceTargetPkCols = PropertiesUtil.stringToMap(syncTaskRuleMysqlEntity.getSyncPks());
                        if (sourceTargetPkCols == null){
                            logger.error("同步规则id：{},syncPks配置有错误，不能为空",syncTaskRuleMysqlEntity.getId());
                        }else {
                            for (String sourcePkCol : sourceTargetPkCols.keySet()){
                                String targetPkCol = sourceTargetPkCols.get(sourcePkCol);
                                Object sourcePkValue = t.get(sourcePkCol);
                                Pk pk = new Pk(targetPkCol,sourcePkValue);
                                deleteEntity.addPk(pk);
                            }
                        }

                        //列名对
                        Map<String, String> sourceTargetCols = PropertiesUtil.stringToMap(syncTaskRuleMysqlEntity.getSyncColumns());
                        if (sourceTargetCols != null && !sourceTargetCols.isEmpty()){
                            for (String sourceCol : sourceTargetCols.keySet()) {

                                Integer deleteStrategy = syncTaskRuleMysqlEntity.getDeleteStrategy();
                                //删除
                                if (deleteStrategy == 0){
                                    continue;
                                }
                                //不一致 ，就忽略
                                else if (deleteStrategy == 1){
                                    String targetCol = sourceTargetCols.get(sourceCol);
                                    Column column = new Column(targetCol,t.get(sourceCol));
                                    deleteEntity.addColumn(column);
                                }
                                //不一致 ，强制为null
                                else if (deleteStrategy == 2){
                                    String targetCol = sourceTargetCols.get(sourceCol);
                                    Column column = new Column(targetCol,null);
                                    deleteEntity.addColumn(column);
                                }
                            }
                        }
                        //删除策略
                        deleteEntity.setDeleteStrategy(syncTaskRuleMysqlEntity.getDeleteStrategy());

                        //有主键 和 列名变化 才能更新
                        if (deleteEntity.getPkList().size() > 0){
                            deleteEntityList.add(deleteEntity);
                        }
                    }
                }
            }
            logger.info("相同sameTargetIdList:{}", JSON.toJSONString(sameTargetIdList));

            JdbcUtil._delete(mysqlDataSourceFactory.getDataSource(targetId),deleteEntityList);
        }

        logger.info("》》》》》》》》》delete,taskId:{} database:{}  table:{}",taskId,model.getDatabase(),model.getTable());
    }


}
