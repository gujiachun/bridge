package com.rainbow.bridge.targetcore.factory.taskrule;

import com.rainbow.bridge.core.constant.CommonCons;
import com.rainbow.bridge.targetcore.model.TaskRule;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 任务规则 工厂
 * @author gujiachun
 */
public abstract class AbsTaskRuleFactory<T extends TaskRule> implements TaskRuleFactory<T> {

    private Map<String, List<T>> taskRuleMap;

    protected String targetType;

    public AbsTaskRuleFactory(String targetType){
        this.targetType = targetType;
    }

    @Override
    public void addTaskRule(String taskId, List<T> taskRules) {
        if (taskRuleMap == null){
            taskRuleMap = new ConcurrentHashMap<>(10);
        }
        List<T> ts = taskRuleMap.get(taskId);
        if (ts == null){
            taskRuleMap.put(taskId, taskRules);
            return;
        }
        ts.clear();
        ts.addAll(taskRules);
    }

    @Override
    public void reloadTaskRules(String taskId) {
        if (taskRuleMap == null){
            taskRuleMap = new ConcurrentHashMap<>(10);
        }
        taskRuleMap.remove(taskId);

        List<T> taskRules = getTaskRulesByTaskId(taskId);
        addTaskRule(taskId,taskRules);
    }

    /**
     * 根据任务id 获取 任务规则
     *@author gujiachun
     *@date 2021/11/18 4:42 下午
     *@param taskId
     *@return java.util.List<T>
    */
    public abstract List<T> getTaskRulesByTaskId(String taskId);

    @Override
    public List<T> getTaskRulesByTaskIdAndTargetId(String taskId,String targetId) {
        if (taskRuleMap == null || taskRuleMap.isEmpty()){
            return null;
        }

        List<T> ts = taskRuleMap.get(taskId);
        if (ts == null){
            return null;
        }

        return ts.stream().filter(t -> targetId.equals(t.getTargetId())).collect(Collectors.toList());
    }

    @Override
    public List<T> getInsertEventTaskRules(String taskId) {
        if (taskRuleMap == null || taskRuleMap.isEmpty()){
            return null;
        }
        List<T> ts = taskRuleMap.get(taskId);
        if (ts == null){
            return null;
        }
        return ts.stream().filter(t -> t.getInsertEnable().equals(CommonCons.enable)).collect(Collectors.toList());
    }

    @Override
    public List<T> getUpdateEventTaskRules(String taskId) {
        if (taskRuleMap == null || taskRuleMap.isEmpty()){
            return null;
        }
        List<T> ts = taskRuleMap.get(taskId);
        if (ts == null){
            return null;
        }
        return ts.stream().filter(t -> t.getUpdateEnable().equals(CommonCons.enable)).collect(Collectors.toList());
    }

    @Override
    public List<T> getDeleteEventTaskRules(String taskId) {
        if (taskRuleMap == null || taskRuleMap.isEmpty()){
            return null;
        }
        List<T> ts = taskRuleMap.get(taskId);
        if (ts == null){
            return null;
        }
        return ts.stream().filter(t -> t.getDeleteEnable().equals(CommonCons.enable)).collect(Collectors.toList());
    }

    @Override
    public Set<Integer> getTargetIdsByTaskId(String taskId) {
        if (taskRuleMap == null || taskRuleMap.isEmpty()){
            return null;
        }

        List<T> ts = taskRuleMap.get(taskId);
        if (ts == null){
            return null;
        }

        Set<Integer> sets = new HashSet<>();
        ts.forEach(t -> sets.add(t.getTargetId()));
        return sets;
    }

    @Override
    public Set<Integer> getTargetIdsByTaskRules(List<T> taskRules) {
        if (taskRules == null){
            return null;
        }

        Set<Integer> sets = new HashSet<>();
        taskRules.forEach(t -> sets.add(t.getTargetId()));
        return sets;
    }

    @Override
    public List<T> filterTaskRulesByTargetId(List<T> taskRules, Integer targetId) {
        if (taskRules == null){
            return null;
        }
        return taskRules.stream().filter(t -> targetId.equals(t.getTargetId())).collect(Collectors.toList());
    }

    @Override
    public boolean support(String targetType) {
        if (this.targetType.equals(targetType)){
            return true;
        }
        return false;
    }
}
