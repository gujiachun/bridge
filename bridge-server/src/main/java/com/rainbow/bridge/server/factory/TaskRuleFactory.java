package com.rainbow.bridge.server.factory;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rainbow.bridge.biz.entity.SyncMysqlTargetEntity;
import com.rainbow.bridge.biz.entity.SyncTaskRuleMysqlEntity;
import com.rainbow.bridge.biz.entity.SyncTaskRuleRedisEntity;
import com.rainbow.bridge.biz.service.TaskMysqlRuleService;
import com.rainbow.bridge.biz.service.TaskRedisRuleService;
import com.rainbow.bridge.core.enums.StatusEnum;
import com.rainbow.bridge.core.model.TaskDto;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 规则工厂
 * @author gujiachun
 */
@Component
public class TaskRuleFactory {

    private Map<String, List<SyncTaskRuleMysqlEntity>> mysqlTaskRuleMap;

    private Map<String, List<SyncTaskRuleRedisEntity>> redisTaskRuleMap;

    @Autowired
    private TaskMysqlRuleService taskMysqlRuleService;

    @Autowired
    private TaskRedisRuleService taskRedisRuleService;

    public TaskRuleFactory(){
        mysqlTaskRuleMap = new ConcurrentHashMap<>(10);
        redisTaskRuleMap = new ConcurrentHashMap<>(10);
    }

    public void addMysqlTaskRules(String taskId, List<SyncTaskRuleMysqlEntity> list){
        List<SyncTaskRuleMysqlEntity> syncTaskRuleMysqlEntities = mysqlTaskRuleMap.get(taskId);
        if (syncTaskRuleMysqlEntities == null){
            mysqlTaskRuleMap.put(taskId, list);
            return;
        }
        syncTaskRuleMysqlEntities.clear();
        syncTaskRuleMysqlEntities.addAll(list);
    }

    public void addRedisTaskRules(String taskId, List<SyncTaskRuleRedisEntity> list){
        List<SyncTaskRuleRedisEntity> syncTaskRuleRedisEntities = redisTaskRuleMap.get(taskId);
        if (syncTaskRuleRedisEntities == null){
            redisTaskRuleMap.put(taskId, list);
            return;
        }
        syncTaskRuleRedisEntities.clear();
        syncTaskRuleRedisEntities.addAll(list);
    }

//    private void filterDuplicate(List<SyncTaskRuleMysqlEntity> list){
//        if (list!= null && list.size() > 0) {
//            TreeSet<SyncTaskRuleMysqlEntity> set = new TreeSet<>(list);
//            list.clear();
//            list.addAll(set);
//        }
//    }

    public Map<String, List<SyncTaskRuleMysqlEntity>> getMysqlTaskRuleMap() {
        return mysqlTaskRuleMap;
    }

    /**
     * 获取此任务的 规则
     *@author gujiachun
     *@date 2021/9/30 11:19 上午
     *@param taskId
     *@return java.util.List<com.rainbow.bridge.biz.entity.SyncTaskRuleMysqlEntity>
    */
    public List<SyncTaskRuleMysqlEntity> getSyncMysqlTaskRuleList(String taskId){
        return mysqlTaskRuleMap.get(taskId);
    }

    public List<SyncTaskRuleMysqlEntity> getSyncMysqlTaskRuleListByTaskAndTargetId(String taskId,Integer targetId){
        List<SyncTaskRuleMysqlEntity> syncTaskRuleMysqlEntities = mysqlTaskRuleMap.get(taskId);

        return syncTaskRuleMysqlEntities.stream().filter(t -> targetId.equals(t.getTargetId())).collect(Collectors.toList());
    }

    public List<SyncTaskRuleMysqlEntity> getSyncMysqlTaskRuleListByTaskAndTargetId(List<SyncTaskRuleMysqlEntity> list,Integer targetId){
        return list.stream().filter(t -> targetId.equals(t.getTargetId())).collect(Collectors.toList());
    }

    public List<SyncTaskRuleRedisEntity> getSyncRedisTaskRuleListByTaskAndTargetId(List<SyncTaskRuleRedisEntity> list,Integer targetId){
        return list.stream().filter(t -> targetId.equals(t.getTargetId())).collect(Collectors.toList());
    }

    public List<SyncTaskRuleMysqlEntity> getInsertSyncMysqlTaskRuleList(String taskId){
        List<SyncTaskRuleMysqlEntity> syncTaskRuleMysqlEntities = mysqlTaskRuleMap.get(taskId);

        return syncTaskRuleMysqlEntities.stream().filter(t -> t.getInsertEnable()==1).collect(Collectors.toList());
    }

    public List<SyncTaskRuleRedisEntity> getInsertSyncRedisTaskRuleList(String taskId){
        List<SyncTaskRuleRedisEntity> entities = redisTaskRuleMap.get(taskId);

        return entities.stream().filter(t -> t.getInsertEnable()==1).collect(Collectors.toList());
    }

    public List<SyncTaskRuleMysqlEntity> getUpdateSyncMysqlTaskRuleList(String taskId){
        List<SyncTaskRuleMysqlEntity> syncTaskRuleMysqlEntities = mysqlTaskRuleMap.get(taskId);

        return syncTaskRuleMysqlEntities.stream().filter(t -> t.getUpdateEnable()==1).collect(Collectors.toList());
    }

    public List<SyncTaskRuleRedisEntity> getUpdateSyncRedisTaskRuleList(String taskId){
        List<SyncTaskRuleRedisEntity> entities = redisTaskRuleMap.get(taskId);

        return entities.stream().filter(t -> t.getUpdateEnable()==1).collect(Collectors.toList());
    }

    public List<SyncTaskRuleMysqlEntity> getDeleteSyncMysqlTaskRuleList(String taskId){
        List<SyncTaskRuleMysqlEntity> syncTaskRuleMysqlEntities = mysqlTaskRuleMap.get(taskId);

        return syncTaskRuleMysqlEntities.stream().filter(t -> t.getDeleteEnable()==1).collect(Collectors.toList());
    }

    public List<SyncTaskRuleRedisEntity> getDeleteSyncRedisTaskRuleList(String taskId){
        List<SyncTaskRuleRedisEntity> entities = redisTaskRuleMap.get(taskId);

        return entities.stream().filter(t -> t.getDeleteEnable()==1).collect(Collectors.toList());
    }

    public Set<Integer> getMysqlTargetIds(String taskId){
        List<SyncTaskRuleMysqlEntity> syncTaskRuleMysqlEntities = mysqlTaskRuleMap.get(taskId);
        Set<Integer> sets = new HashSet<>();

        syncTaskRuleMysqlEntities.forEach(t -> sets.add(t.getTargetId()));

        return sets;
    }

    public Set<Integer> getMysqlTargetIds(List<SyncTaskRuleMysqlEntity> list){
        Set<Integer> sets = new HashSet<>();

        list.forEach(t -> sets.add(t.getTargetId()));

        return sets;
    }

    public Set<Integer> getRedisTargetIds(List<SyncTaskRuleRedisEntity> list){
        Set<Integer> sets = new HashSet<>();

        list.forEach(t -> sets.add(t.getTargetId()));

        return sets;
    }

    /**
     * 刷新最新的规则
     *@author gujiachun
     *@date 2021/10/7 9:36 下午
     *@param taskId
     *@return void
    */
    public void refreshMysqlTask(String taskId){
        mysqlTaskRuleMap.remove(taskId);

        QueryWrapper<SyncTaskRuleMysqlEntity> wrapper = new QueryWrapper();
        wrapper.eq("task_id",taskId);
        wrapper.eq("status", StatusEnum.valid.getStatus());

        List<SyncTaskRuleMysqlEntity> syncTaskRuleMysqlEntityList = taskMysqlRuleService.list(wrapper);

        addMysqlTaskRules(taskId,syncTaskRuleMysqlEntityList);
    }

    public void refreshRedisTask(String taskId){
        redisTaskRuleMap.remove(taskId);

        QueryWrapper<SyncTaskRuleRedisEntity> wrapper = new QueryWrapper();
        wrapper.eq("task_id",taskId);
        wrapper.eq("status", StatusEnum.valid.getStatus());

        List<SyncTaskRuleRedisEntity> syncTaskRuleRedisEntityList = taskRedisRuleService.list(wrapper);

        addRedisTaskRules(taskId,syncTaskRuleRedisEntityList);
    }

    /**
     * 移除任务规则
     *@author gujiachun
     *@date 2021/10/7 9:45 下午
     *@param taskId
     *@return void
    */
    public void removeMysqlTask(String taskId){
        mysqlTaskRuleMap.remove(taskId);
    }

    public void removeRedisTask(String taskId){
        redisTaskRuleMap.remove(taskId);
    }
}
