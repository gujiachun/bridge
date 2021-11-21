package com.rainbow.bridge.targetcore.handler;

import com.googlecode.aviator.AviatorEvaluator;
import com.rainbow.bridge.core.utils.PublicUtil;
import com.rainbow.bridge.handler.EntryHandler;
import com.rainbow.bridge.model.CanalModel;
import com.rainbow.bridge.targetcore.adapter.BridgeAdapter;
import com.rainbow.bridge.targetcore.factory.targetsource.TargetFactory;
import com.rainbow.bridge.targetcore.factory.taskrule.TaskRuleFactory;
import com.rainbow.bridge.targetcore.model.Param;
import com.rainbow.bridge.targetcore.model.TaskRule;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

/**
 * 处理新增、修改、删除事件的 同步逻辑
 * @author gujiachun
 */
public abstract class AbsEntryHandler implements EntryHandler {

    protected static final Logger logger = LoggerFactory.getLogger(AbsEntryHandler.class);

    /** 任务规则工厂 */
    protected TaskRuleFactory taskRuleFactory;

    /** 目标源工厂 */
    protected TargetFactory targetFactory;

    /** 任务id */
    protected String taskId;

    /** 目标源类型 */
    protected String targetType;

    /** 是否 同一个 targetid 合并，作批量事务处理 */
    protected Boolean batchTran;

    /** 与目标源类型的处理对象的适配器，如：mysql数据库操作对象，redis操作对象 */
    protected BridgeAdapter bridgeAdapter;

    public AbsEntryHandler(String taskId, TaskRuleFactory taskRuleFactory, TargetFactory targetFactory,
                             String targetType, BridgeAdapter bridgeAdapter,Boolean batchTran){
        this.taskRuleFactory = taskRuleFactory;
        this.taskId = taskId;
        this.targetType = targetType;
        this.batchTran = batchTran;
        this.targetFactory = targetFactory;
        this.bridgeAdapter = bridgeAdapter;
    }


    @Override
    public void insert(CanalModel model, Map<String, String> mysqlType, Map<String, Object> t) throws Exception {
        if (!check()){
            return;
        }
        List<TaskRule> taskRules = taskRuleFactory.getInsertEventTaskRules(taskId);
        Set<Integer> targetIds = taskRuleFactory.getTargetIdsByTaskRules(taskRules);

        for (Integer targetId : targetIds){
            List<TaskRule> targetTaskRules = taskRuleFactory.filterTaskRulesByTargetId(taskRules, targetId);

            //批量对象
            List<Param> batchParams = null;
            if (batchTran){
                batchParams = new ArrayList<>();
            }

            for (TaskRule taskRule : targetTaskRules){
                //配对源头
                if (model.getDatabase().equals(taskRule.getSourceDb()) &&
                        model.getTable().equals(taskRule.getSourceTable())){
                    int filter = 0;
                    //针对 新增的值 动态的表达式
                    if (StringUtils.isNotBlank(taskRule.getInsertSourceConditionFilter())){
                        Object ave = AviatorEvaluator.execute(taskRule.getInsertSourceConditionFilter(), t);

                        Boolean b = Boolean.valueOf(ave.toString());
                        filter = b ? filter : 1;

                        logger.info(">>>>执行新增 过滤条件:{} ",b);
                    }

                    //表达式 没有通过了，此数据不需要同步
                    if (filter != 0){
                        continue;
                    }
                    Param param = buildInsertParam(targetType,targetId,taskId,targetFactory,taskRuleFactory,taskRule,model,mysqlType,t);
                    //是否需要批量处理 同一个targetId的任务
                    if (batchTran){
                        if (param != null){
                            batchParams.add(param);
                        }
                    }else{
                        //不需要批量 ，就直接执行
                        if (param != null) {
                            insertOpr(targetId, param);
                        }
                    }
                }
            }
            if (batchTran){
                if (batchParams != null && !batchParams.isEmpty()){
                    insertBatchOpr(targetId,batchParams);
                }
            }
        }

        logger.info("》》》》》》》》》insert,taskId:{} database:{}  table:{}",taskId,model.getDatabase(),model.getTable());
    }

     /**
      * batchTran为True时，执行批量操作
     * 新增事件同步操作,多个规则时，同一个targetId，一起批量操作
     * 如果目标源支持事务，可在同一个事务中 批量操作
     *@author gujiachun
     *@date 2021/11/19 4:43 下午
     *@param targetId 目标源id
     *@param params 批量执行新增的所需参数
     *@return void
     */
    public abstract void insertBatchOpr(Integer targetId,List<Param> params) throws Exception;

    /**
     * batchTran为false时，执行单次操作
     * 新增事件同步操作
     *@author gujiachun
     *@date 2021/11/19 4:43 下午
     *@param targetId 目标源id
     *@param param 执行新增的所需参数
     *@return void
     */
    public abstract void insertOpr(Integer targetId,Param param) throws Exception;

    /**
     * 构建 新增事件同步 参数
     *@author gujiachun
     *@date 2021/11/19 4:46 下午
     *@param targetType 目标源类型
     *@param targetId 目标源id
     *@param taskId 任务id
     *@param targetFactory 目标工厂对象
     *@param taskRuleFactory 任务规则对象
     *@param taskRule taskRule有对应的规则值
     *@param model canal model
     *@param mysqlType 表列 对应的 类型
     *@param values 新增的值
     *@return com.rainbow.bridge.targetcore.adapter.model.Param
    */
    public abstract Param buildInsertParam(String targetType, Integer targetId, String taskId, TargetFactory targetFactory, TaskRuleFactory taskRuleFactory,
                                              TaskRule taskRule, CanalModel model, Map<String,String> mysqlType, Map<String, Object> values) throws Exception;


    @Override
    public void update(CanalModel model, Map<String, String> mysqlType, Map<String, Object> before, Map<String, Object> after) throws Exception {
        if (!check()){
            return;
        }
        List<TaskRule> taskRules = taskRuleFactory.getUpdateEventTaskRules(taskId);
        Set<Integer> targetIds = taskRuleFactory.getTargetIdsByTaskRules(taskRules);

        for (Integer targetId : targetIds){
            List<TaskRule> targetTaskRules = taskRuleFactory.filterTaskRulesByTargetId(taskRules, targetId);

            //批量对象
            List<Param> batchParams = null;
            if (batchTran){
                batchParams = new ArrayList<>();
            }

            for (TaskRule taskRule : targetTaskRules){

                //配对源头
                if (model.getDatabase().equals(taskRule.getSourceDb()) &&
                        model.getTable().equals(taskRule.getSourceTable())){

                    int filter = 0;
                    //针对 修改前的值 动态的表达式
                    if (StringUtils.isNotBlank(taskRule.getUpdateSourceConditionFilter())){
                        //获取以前的值
                        Map<String, Object> beforeAllMap = PublicUtil.getBefore(before,after);

                        Object ave = AviatorEvaluator.execute(taskRule.getUpdateSourceConditionFilter(), beforeAllMap);

                        Boolean b = Boolean.valueOf(ave.toString());
                        filter = b ? filter : 1;

                        logger.info(">>>>执行修改过滤条件--修改前的值 过滤条件:{} ",b);
                    }
                    //针对 修改后的值 动态的表达式
                    if (StringUtils.isNotBlank(taskRule.getUpdateNewConditionFilter())){
                        Object ave = AviatorEvaluator.execute(taskRule.getUpdateNewConditionFilter(), after);
                        Boolean b = Boolean.valueOf(ave.toString());
                        filter = b ? filter : 1;
                        logger.info(">>>>>执行修改过滤条件--修改后的值 过滤条件:{} ",b);
                    }

                    //表达式不通过，不同步此数据
                    if (filter != 0){
                        continue;
                    }

                    Param param = buildUpdateParam(targetType,targetId,taskId,targetFactory,taskRuleFactory,taskRule,model,mysqlType,before,after);

                    //是否需要批量处理 同一个targetId的任务
                    if (batchTran){
                        if (param != null){
                            batchParams.add(param);
                        }
                    }else{
                        //不需要批量 ，就直接执行
                        if (param != null) {
                            updateOpr(targetId, param);
                        }
                    }
                }
            }
            if (batchTran){
                if (batchParams != null && !batchParams.isEmpty()) {
                    updateBatchOpr(targetId, batchParams);
                }
            }
        }
        logger.info("》》》》》》》》》update,taskId:{} database:{}  table:{}",taskId,model.getDatabase(),model.getTable());
    }

    /**
     * batchTran为True时，执行批量操作
     * 修改事件 同步操作,多个规则时，同一个targetId，一起批量操作
     * 如果目标源支持事务，可在同一个事务中 批量操作
     *@author gujiachun
     *@date 2021/11/19 4:43 下午
     *@param targetId 目标源id
     *@param params 批量执行参数
     *@return void
     */
    public abstract void updateBatchOpr(Integer targetId,List<Param> params) throws Exception;

    /**
     * batchTran为false时，执行单次操作
     * 修改事件 同步操作
     *@author gujiachun
     *@date 2021/11/20 12:40 上午
     *@param targetId 目标源id
     *@param param 执行参数
     *@return void
    */
    public abstract void updateOpr(Integer targetId,Param param) throws Exception;

    /**
     * 构建 修改事件同步 参数
     *@author gujiachun
     *@date 2021/11/19 4:46 下午
     *@param targetType 目标源类型
     *@param targetId 目标源id
     *@param taskId 任务id
     *@param targetFactory 目标工厂对象
     *@param taskRuleFactory 任务规则对象
     *@param taskRule taskRule有对应的规则值
     *@param model canal model
     *@param mysqlType 表列 对应的 类型
     *@param before 修改之前的值
     *@param after 修改之后的值
     *@return com.rainbow.bridge.targetcore.adapter.model.Param
     */
    public abstract Param buildUpdateParam(String targetType, Integer targetId, String taskId, TargetFactory targetFactory, TaskRuleFactory taskRuleFactory,
                                              TaskRule taskRule, CanalModel model, Map<String,String> mysqlType, Map<String, Object> before, Map<String, Object> after) throws Exception;


    @Override
    public void delete(CanalModel model, Map<String, String> mysqlType, Map<String, Object> t) throws Exception {
        if (!check()){
            return;
        }
        List<TaskRule> taskRules = taskRuleFactory.getDeleteEventTaskRules(taskId);
        Set<Integer> targetIds = taskRuleFactory.getTargetIdsByTaskRules(taskRules);

        for (Integer targetId : targetIds){
            List<TaskRule> targetTaskRules = taskRuleFactory.filterTaskRulesByTargetId(taskRules, targetId);

            //批量对象
            List<Param> batchParams = null;
            if (batchTran){
                batchParams = new ArrayList<>();
            }

            for (TaskRule taskRule : targetTaskRules){

                //配对源头
                if (model.getDatabase().equals(taskRule.getSourceDb()) &&
                        model.getTable().equals(taskRule.getSourceTable())){

                    int filter = 0;
                    //针对 修改前的值 动态的表达式
                    if (StringUtils.isNotBlank(taskRule.getDeleteSourceConditionFilter())){
                        Object ave = AviatorEvaluator.execute(taskRule.getDeleteSourceConditionFilter(), t);

                        Boolean b = Boolean.valueOf(ave.toString());
                        filter = b ? filter : 1;

                        logger.info(">>>>执行删除 过滤条件:{} ",b);
                    }

                    //表达式 不通过了，此数据不同步了
                    if (filter != 0){
                        continue;
                    }

                    Param param = buildDeleteParam(targetType,targetId,taskId,targetFactory,taskRuleFactory,taskRule,model,mysqlType,t);
                    //是否需要批量处理 同一个targetId的任务
                    if (batchTran){
                        if (param != null){
                            batchParams.add(param);
                        }
                    }else{
                        //不需要批量 ，就直接执行
                        if (param != null) {
                            deleteOpr(targetId, param);
                        }
                    }
                }
            }

            if (batchTran){
                if (batchParams != null && !batchParams.isEmpty()) {
                    deleteBatchOpr(targetId, batchParams);
                }
            }
        }

        logger.info("》》》》》》》》》delete,taskId:{} database:{}  table:{}",taskId,model.getDatabase(),model.getTable());
    }

    /**
     * batchTran为True时，执行批量操作
     * 删除事件同步操作,多个规则时，同一个targetId，一起批量操作
     * 如果目标源支持事务，可在同一个事务中 批量操作
     *@author gujiachun
     *@date 2021/11/19 4:43 下午
     *@param targetId 目标源id
     *@param params 执行参数
     *@return void
     */
    public abstract void deleteBatchOpr(Integer targetId,List<Param> params) throws Exception;

    /**
     * batchTran为false时，执行单次操作
     * 删除事件同步操作
     *@author gujiachun
     *@date 2021/11/20 1:00 上午
     *@param targetId 目标源id
     *@param param 执行参数
     *@return void
    */
    public abstract void deleteOpr(Integer targetId,Param param) throws Exception;

    /**
     * 构建 删除事件同步参数
     *@author gujiachun
     *@date 2021/11/19 4:46 下午
     *@param targetType 目标源类型
     *@param targetId 目标源id
     *@param taskId 任务id
     *@param targetFactory 目标工厂对象
     *@param taskRuleFactory 任务规则对象
     *@param taskRule taskRule有对应的规则值
     *@param model canal model
     *@param mysqlType 表列 对应的 类型
     *@param values 新增的值
     *@return com.rainbow.bridge.targetcore.adapter.model.Param
     */
    public abstract Param buildDeleteParam(String targetType, Integer targetId, String taskId, TargetFactory targetFactory, TaskRuleFactory taskRuleFactory,
                                              TaskRule taskRule, CanalModel model, Map<String,String> mysqlType, Map<String, Object> values) throws Exception;


    private boolean check(){
        if (targetFactory == null){
            logger.error("类型{}目标源未初始化",this.targetType);
            return false;
        }
        return true;
    }
}
