package com.rainbow.bridge.server.handler;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.googlecode.aviator.AviatorEvaluator;
import com.rainbow.bridge.biz.entity.SyncTaskRuleRedisEntity;
import com.rainbow.bridge.core.constant.RedisCommandCons;
import com.rainbow.bridge.core.entity.*;
import com.rainbow.bridge.core.utils.PropertiesUtil;
import com.rainbow.bridge.core.utils.PublicUtil;
import com.rainbow.bridge.handler.EntryHandler;
import com.rainbow.bridge.model.CanalModel;
import com.rainbow.bridge.server.factory.MysqlDataSourceFactory;
import com.rainbow.bridge.server.factory.RedisFactory;
import com.rainbow.bridge.server.factory.TaskRuleFactory;
import com.rainbow.bridge.server.redis.RedisService;
import com.rainbow.bridge.server.utils.JdbcUtil;
import com.rainbow.bridge.server.utils.TemplateUtil;
import freemarker.template.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author gujiachun
 */
public class RedisEntryHandler implements EntryHandler {

    private static final Logger logger = LoggerFactory.getLogger(RedisEntryHandler.class);

    private String taskId;

    private Configuration freeMarkerConfiguration;

    public RedisEntryHandler(String taskId, TaskRuleFactory taskRuleFactory, RedisFactory redisFactory,
                             Configuration freeMarkerConfiguration){
        this.taskRuleFactory = taskRuleFactory;
        this.taskId = taskId;
        this.redisFactory = redisFactory;
        this.freeMarkerConfiguration = freeMarkerConfiguration;
    }

    private TaskRuleFactory taskRuleFactory;

    private RedisFactory redisFactory;

    @Override
    public void insert(CanalModel model, Map<String,String> mysqlType,Map<String, Object> t) throws Exception {
        List<SyncTaskRuleRedisEntity> insertSyncTaskRuleList = taskRuleFactory.getInsertSyncRedisTaskRuleList(taskId);
        Set<Integer> targetIdList = taskRuleFactory.getRedisTargetIds(insertSyncTaskRuleList);
        for (Integer targetId : targetIdList){
            List<SyncTaskRuleRedisEntity> sameTargetIdList = taskRuleFactory.getSyncRedisTaskRuleListByTaskAndTargetId(insertSyncTaskRuleList, targetId);

            for (SyncTaskRuleRedisEntity entity : sameTargetIdList){

                //配对源头
                if (model.getDatabase().equals(entity.getSourceDb()) &&
                        model.getTable().equals(entity.getSourceTable())){

                    int filter = 0;
                    //针对 新增的值 动态的表达式
                    if (StringUtils.isNotBlank(entity.getInsertSourceConditionFilter())){
                        Object ave = AviatorEvaluator.execute(entity.getInsertSourceConditionFilter(), t);

                        Boolean b = Boolean.valueOf(ave.toString());
                        filter = b ? filter : 1;

                        logger.info(">>>>执行新增 过滤条件:{} ",b);
                    }

                    //表达式 都通过了，可以同步了
                    if (filter == 0){
                        String command = entity.getCommand();
                        String key = TemplateUtil.processTemplate(freeMarkerConfiguration,entity.getKeyFormat(),t);
                        String field = StringUtils.isNotBlank(entity.getFieldFormat()) ?  TemplateUtil.processTemplate(freeMarkerConfiguration,entity.getFieldFormat(),t) : null;
                        String value = StringUtils.isNotBlank(entity.getValueFormat()) ?  TemplateUtil.processTemplate(freeMarkerConfiguration,entity.getValueFormat(),t) : null;
                        Long expireTime = entity.getExpireTime();
                        Time fixedTime = entity.getFixedTime();
                        actionCommand(redisFactory.getRedisService(targetId),command,key,field,value,expireTime,fixedTime);
                    }
                }
            }
            logger.info("相同sameTargetIdList:{}", JSON.toJSONString(sameTargetIdList));
        }

        logger.info("》》》》》》》》》insert,taskId:{} database:{}  table:{}",taskId,model.getDatabase(),model.getTable());
    }

    @Override
    public void update(CanalModel model, Map<String,String> mysqlType,Map<String, Object> before, Map<String, Object> after) throws Exception {

        List<SyncTaskRuleRedisEntity> updateSyncTaskRuleList = taskRuleFactory.getUpdateSyncRedisTaskRuleList(taskId);
        Set<Integer> targetIdList = taskRuleFactory.getRedisTargetIds(updateSyncTaskRuleList);
        for (Integer targetId : targetIdList){
            List<SyncTaskRuleRedisEntity> sameTargetIdList = taskRuleFactory.getSyncRedisTaskRuleListByTaskAndTargetId(updateSyncTaskRuleList, targetId);

            for (SyncTaskRuleRedisEntity entity : sameTargetIdList){

                //配对源头
                if (model.getDatabase().equals(entity.getSourceDb()) &&
                            model.getTable().equals(entity.getSourceTable())){

                    int filter = 0;
                    //针对 修改前的值 动态的表达式
                    if (StringUtils.isNotBlank(entity.getUpdateSourceConditionFilter())){
                        //获取以前的值
                        Map<String, Object> beforeAllMap = PublicUtil.getBefore(before,after);

                        Object ave = AviatorEvaluator.execute(entity.getUpdateSourceConditionFilter(), beforeAllMap);

                        Boolean b = Boolean.valueOf(ave.toString());
                        filter = b ? filter : 1;

                        logger.info(">>>>执行修改过滤条件--修改前的值 过滤条件:{} ",b);
                    }
                    //针对 修改后的值 动态的表达式
                    if (StringUtils.isNotBlank(entity.getUpdateNewConditionFilter())){
                        Object ave = AviatorEvaluator.execute(entity.getUpdateNewConditionFilter(), after);
                        Boolean b = Boolean.valueOf(ave.toString());
                        filter = b ? filter : 1;
                        logger.info(">>>>>执行修改过滤条件--修改后的值 过滤条件:{} ",b);
                    }

                    //表达式 都通过了，可以同步了
                    if (filter == 0){
                        String command = entity.getCommand();
                        String key = TemplateUtil.processTemplate(freeMarkerConfiguration,entity.getKeyFormat(),after);
                        String field = StringUtils.isNotBlank(entity.getFieldFormat()) ?  TemplateUtil.processTemplate(freeMarkerConfiguration,entity.getFieldFormat(),after) : null;
                        String value = StringUtils.isNotBlank(entity.getValueFormat()) ?  TemplateUtil.processTemplate(freeMarkerConfiguration,entity.getValueFormat(),after) : null;
                        Long expireTime = entity.getExpireTime();
                        Time fixedTime = entity.getFixedTime();
                        actionCommand(redisFactory.getRedisService(targetId),command,key,field,value,expireTime,fixedTime);
                    }
                }
            }
            logger.info("相同sameTargetIdList:{}", JSON.toJSONString(sameTargetIdList));
        }
        logger.info("》》》》》》》》》update,taskId:{} database:{}  table:{}",taskId,model.getDatabase(),model.getTable());
    }

    @Override
    public void delete(CanalModel model, Map<String,String> mysqlType,Map<String, Object> t) throws Exception {
        List<SyncTaskRuleRedisEntity> deleteSyncTaskRuleList = taskRuleFactory.getDeleteSyncRedisTaskRuleList(taskId);
        Set<Integer> targetIdList = taskRuleFactory.getRedisTargetIds(deleteSyncTaskRuleList);
        for (Integer targetId : targetIdList){
            List<SyncTaskRuleRedisEntity> sameTargetIdList = taskRuleFactory.getSyncRedisTaskRuleListByTaskAndTargetId(deleteSyncTaskRuleList, targetId);

            for (SyncTaskRuleRedisEntity entity : sameTargetIdList){

                //配对源头
                if (model.getDatabase().equals(entity.getSourceDb()) &&
                        model.getTable().equals(entity.getSourceTable())){

                    int filter = 0;
                    //针对 修改前的值 动态的表达式
                    if (StringUtils.isNotBlank(entity.getDeleteSourceConditionFilter())){
                        Object ave = AviatorEvaluator.execute(entity.getUpdateSourceConditionFilter(), t);

                        Boolean b = Boolean.valueOf(ave.toString());
                        filter = b ? filter : 1;

                        logger.info(">>>>执行删除 过滤条件:{} ",b);
                    }

                    //表达式 都通过了，可以同步了
                    if (filter == 0){
                        String command = entity.getCommand();
                        String key = TemplateUtil.processTemplate(freeMarkerConfiguration,entity.getKeyFormat(),t);
                        String field = StringUtils.isNotBlank(entity.getFieldFormat()) ?  TemplateUtil.processTemplate(freeMarkerConfiguration,entity.getFieldFormat(),t) : null;
                        String value = StringUtils.isNotBlank(entity.getValueFormat()) ?  TemplateUtil.processTemplate(freeMarkerConfiguration,entity.getValueFormat(),t) : null;
                        Long expireTime = entity.getExpireTime();
                        Time fixedTime = entity.getFixedTime();
                        actionCommand(redisFactory.getRedisService(targetId),command,key,field,value,expireTime,fixedTime);
                    }
                }
            }
            logger.info("相同sameTargetIdList:{}", JSON.toJSONString(sameTargetIdList));

        }

        logger.info("》》》》》》》》》delete,taskId:{} database:{}  table:{}",taskId,model.getDatabase(),model.getTable());
    }


    private void actionCommand(RedisService redisService,String command,
                               String key ,String field,String value,Long expireTime,Time fixedTime) {


        Long et = null;
        if (expireTime != null){
            et = expireTime;
        }else if (fixedTime != null){
            SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
            Date beginDate = new Date();
            Calendar date = Calendar.getInstance();
            date.setTime(beginDate);
            date.set(Calendar.DATE, date.get(Calendar.DATE) + 1);
            try{
                Date endDate = dft.parse(dft.format(date.getTime()) + " " +fixedTime.toString());
                et = DateUtil.between(endDate,beginDate,DateUnit.SECOND,true);
            }catch (ParseException e){
                logger.error("固定过期时间 处理异常:{}",e.getMessage());
            }
        }

        switch (command){
            case RedisCommandCons
                    .set:
                if (et == null){
                    redisService.set(key, value);
                }else {
                    redisService.set(key, value, et.intValue());
                }
                break;
            case RedisCommandCons
                    .delete:
                redisService.delete(key);
                break;
            case RedisCommandCons
                    .hset:
                redisService.hset(key,field,value);
                if (et != null) {
                    redisService.expire(key, et.intValue());
                }
                break;
            case RedisCommandCons
                    .hmset:
                redisService.hmset(key,JSON.parseObject(value,HashMap.class));
                if (et != null) {
                    redisService.expire(key, et.intValue());
                }
                break;
            case RedisCommandCons
                    .incr:
                redisService.inCr(key);
                if (et != null) {
                    redisService.expire(key, et.intValue());
                }
                break;
            case RedisCommandCons
                    .delhKeys:
                redisService.delhKeys(key,field.split(","));
                if (et != null) {
                    redisService.expire(key, et.intValue());
                }
                break;
        }
    }
}
