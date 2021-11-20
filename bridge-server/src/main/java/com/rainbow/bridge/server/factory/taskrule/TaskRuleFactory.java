package com.rainbow.bridge.server.factory.taskrule;

import com.rainbow.bridge.server.factory.BridgeFactory;
import com.rainbow.bridge.server.model.task.TaskRule;

import java.util.List;
import java.util.Set;

/**
 * 任务规则工厂接口
 * @author gujiachun
 */
public interface TaskRuleFactory<T extends TaskRule> extends BridgeFactory {

    /**
     * 添加任务规则,全量新增
     *@author gujiachun
     *@date 2021/11/18 8:52 上午
     *@param taskId
     *@param taskRules
     *@return void
    */
    void addTaskRule(String taskId, List<T> taskRules);

    /**
     * 重新加载任务规则
     *@author gujiachun
     *@date 2021/11/18 8:59 上午
     *@param taskId
     *@return void
    */
    void reloadTaskRules(String taskId);

    /**
     * 根据任务id 和 目标源targetId 获取任务规则
     *@author gujiachun
     *@date 2021/11/18 5:03 下午
     *@param taskId
     *@param targetId
     *@return java.util.List<T>
    */
    List<T> getTaskRulesByTaskIdAndTargetId(String taskId,String targetId);

    /**
     * 获取 启动了新增事件 的任务规则
     *@author gujiachun
     *@date 2021/11/18 9:09 上午
     *@param taskId
     *@return java.util.List<com.rainbow.bridge.server.model.task.TaskRule>
    */
    List<T> getInsertEventTaskRules(String taskId);

    /**
     * 获取 启动了修改事件 的任务规则
     *@author gujiachun
     *@date 2021/11/18 9:09 上午
     *@param taskId
     *@return java.util.List<com.rainbow.bridge.server.model.task.TaskRule>
     */
    List<T> getUpdateEventTaskRules(String taskId);

    /**
     * 获取 启动了删除事件 的任务规则
     *@author gujiachun
     *@date 2021/11/18 9:09 上午
     *@param taskId
     *@return java.util.List<com.rainbow.bridge.server.model.task.TaskRule>
     */
    List<T> getDeleteEventTaskRules(String taskId);

    /**
     * 获取任务id 有哪些目标源id
     *@author gujiachun
     *@date 2021/11/18 6:19 下午
     *@param taskId
     *@return java.util.Set<java.lang.Integer>
    */
    Set<Integer> getTargetIdsByTaskId(String taskId);

    /**
     * 根据任务规则，获取目标源id
     *@author gujiachun
     *@date 2021/11/18 6:27 下午
     *@param taskRules
     *@return java.util.Set<java.lang.Integer>
    */
    Set<Integer> getTargetIdsByTaskRules(List<T> taskRules);

    /**
     * 根据目标源targetId 过滤 任务规则
     *@author gujiachun
     *@date 2021/11/18 6:36 下午
     *@param taskRules
     *@param targetId
     *@return java.util.List<T>
    */
    List<T> filterTaskRulesByTargetId(List<T> taskRules,Integer targetId);

}
