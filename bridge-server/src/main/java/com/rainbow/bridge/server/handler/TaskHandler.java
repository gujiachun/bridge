package com.rainbow.bridge.server.handler;

import com.alibaba.fastjson.JSON;
import com.rainbow.bridge.biz.entity.*;
import com.rainbow.bridge.biz.service.*;
import com.rainbow.bridge.canal.CanalClient;
import com.rainbow.bridge.core.constant.CommonCons;
import com.rainbow.bridge.core.model.TaskDto;
import com.rainbow.bridge.core.utils.IpLocalUtil;
import com.rainbow.bridge.core.zk.SimpleDistributedLockImpl;
import com.rainbow.bridge.core.zk.ZkClientExt;
import com.rainbow.bridge.server.factory.CanalClientFactory;
import com.rainbow.bridge.server.factory.target.TargetFactory;
import com.rainbow.bridge.server.factory.taskrule.TaskRuleFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 核心的refresh，启动任务
 * @author gujiachun
 */
@Component
public class TaskHandler {

    private static final Logger logger = LoggerFactory.getLogger(TaskHandler.class);

    /**
     * 一个实例最多 执行3个任务
     * */
    @Value("#{T(java.lang.Integer).parseInt('${bridge.maxTaskCount:1}')}")
    public Integer maxTaskCount;

    @Autowired
    private ZkClientExt zkClientExt;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private MqService mqService;

    @Autowired
    private CanalClientFactory canalClientFactory;

    @Autowired
    private Map<String, TargetFactory> targetFactoryMap;

    @Autowired
    private Map<String, TaskRuleFactory> taskRuleFactoryMap;

    @Value("${server.port}")
    private Integer port;

    /**
     * 任务对应的临时节点
     * */
    private Map<String, String> zkEsTaskMap = new ConcurrentHashMap<>(20);

    @PostConstruct
    public void load() throws Exception {
        logger.info("-----------load 前--------");
        refresh();
        logger.info("-----------load 后--------");
    }

    /**
     * 此集群还有 需要增加的实例任务，还是减少的实例任务
     *@author gujiachun
     *@date 2021/10/7 8:20 下午
     *@param addTaskList
     *@param delTaskList
     *@return void
    */
    private void checkTodoTasks(List<TaskDto> addTaskList,List<TaskDto> delTaskList,List<TaskDto> updateTaskList){
        String tasks = zkClientExt.readData(zkClientExt.getClusterPath(), true);

        if (StringUtils.isBlank(tasks)){
            return;
        }

        List<TaskDto> taskList = JSON.parseArray(tasks, TaskDto.class);

        //获取集群节点 下 所有的任务实例
        List<String> taskPathChildren = zkClientExt.getChildren(zkClientExt.getClusterPath());

        if (addTaskList == null){
            addTaskList = new ArrayList<>();
        }

        if (delTaskList == null){
            delTaskList = new ArrayList<>();
        }

        if (updateTaskList == null){
            updateTaskList = new ArrayList<>();
        }

        for (TaskDto taskDto : taskList){
            //当前task任务实例数
            int taskPathCount = getTaskPathCount(taskPathChildren, taskDto.getTaskId());

            //如果没有达到数量，那需要增加实例消费
            if (taskDto.getInstCount() > taskPathCount){
                addTaskList.add(taskDto);
            }
            //已经有多余的实例消费，那就需要剔除实例
            else if (taskDto.getInstCount() < taskPathCount){
                delTaskList.add(taskDto);
            }

            //获取当前的任务是不是 需要更新 同步规则
            boolean taskUpdated = isTaskRuleUpdated(taskDto);
            if (taskUpdated){
                updateTaskList.add(taskDto);
            }
        }
    }

    /**
     * 释放达到了 处理任务极限
     *@author gujiachun
     *@date 2021/9/30 10:14 上午
     *@param
     *@return boolean
    */
    private boolean isMaxTaskCount(){
        //获取正在执行的任务
        Map<String, CanalClient> taskCanalClientMap = canalClientFactory.getTaskCanalClientMap();
        //已经达到了极限
        if (taskCanalClientMap.size() >= maxTaskCount){
            logger.info("集群任务发生变化了，但任务已满，不去认领任务。");
            return true;
        }
        return false;
    }

    /**
     * 实时监听集群任务的变化，再结合自身实例 有没有能力再处理其他任务
     * maxTaskCount就是自身能够处理的任务
     *@author gujiachun
     *@date 2021/9/29 2:08 下午
     *@param
     *@return void
    */
    public synchronized void refresh() throws Exception {

        logger.info("自动refresh了哦........");

        String lockName = zkClientExt.getRootPath() + "/" + zkClientExt.getClusterName() + "-task-locker";
        SimpleDistributedLockImpl lock = new SimpleDistributedLockImpl(zkClientExt, lockName);

        try{
            logger.info(">>>>>>>>去申请加锁:{}",lockName);
            //获取锁
            lock.acquire();

            logger.info(">>>>>>>>成功获取锁:{}",lockName);

            List<TaskDto> addTaskList = new ArrayList<>();
            List<TaskDto> delTaskList = new ArrayList<>();
            List<TaskDto> updateTaskList = new ArrayList<>();
            //检查需要做的任务
            checkTodoTasks(addTaskList,delTaskList,updateTaskList);

            buildDeleteTasks(delTaskList);

            buildAddTasks(addTaskList);
            //更新任务规则
            buildUpdateTasks(updateTaskList);

        }catch (Exception e){
            logger.error("领取任务 异常:{}",e.getMessage());
        }finally {
            logger.info("释放锁>>>>>>>>:{}",lock.getLockerNodePath());
            lock.release();
            canalClientFactory.start();
        }
    }

    /**
     * 更新任务规则
     *@author gujiachun
     *@date 2021/10/7 10:33 下午
     *@param updateTaskList
     *@return void
    */
    private void buildUpdateTasks(List<TaskDto> updateTaskList){
        if (updateTaskList != null && !updateTaskList.isEmpty()){
            logger.info("集群任务发生变化了，更新任务规则开始.....");
            for (TaskDto taskDto : updateTaskList){
                logger.info("集群任务发生变化了，更新任务规则:{}",taskDto.getTaskId());

                TaskRuleFactory taskRuleFactory = getTaskRuleFactory(taskDto.getTargetType());
                if (taskRuleFactory != null){
                    taskRuleFactory.reloadTaskRules(taskDto.getTaskId());
                }
            }
        }
    }

    /**
     * 剔除任务
     *@author gujiachun
     *@date 2021/10/7 10:34 下午
     *@param delTaskList
     *@return void
    */
    private void buildDeleteTasks(List<TaskDto> delTaskList){
        //先剔除 多余实例
        if (delTaskList != null && !delTaskList.isEmpty()){
            logger.info("集群任务发生变化了，剔除实例开始.....");
            //获取正在执行的任务
            Map<String, CanalClient> taskCanalClientMap = canalClientFactory.getTaskCanalClientMap();

            for (TaskDto taskDto : delTaskList){
                logger.info("集群任务发生变化了，剔除实例:{}",taskDto.getTaskId());
                //移除订阅消息
                CanalClient canalClient = taskCanalClientMap.get(taskDto.getTaskId());
                if (canalClient != null){
                    logger.info("剔除任务 stop");
                    canalClient.stop();
                    canalClient = null;
                }
                taskCanalClientMap.remove(taskDto.getTaskId());
                //移除临时节点
                logger.info(">>>>移除临时节点:{}",zkEsTaskMap.get(taskDto.getTaskId()));
                zkClientExt.delete(zkEsTaskMap.get(taskDto.getTaskId()));
                //移除任务和节点关联
                zkEsTaskMap.remove(taskDto.getTaskId());

                TargetFactory targetFactory = getTargetFactory(taskDto.getTargetType());
                if (targetFactory != null){
                    targetFactory.removeTarget(taskDto.getTaskId());
                }
            }
        }
    }

    private TargetFactory getTargetFactory(String targetType){

        if (targetFactoryMap == null || targetFactoryMap.isEmpty()){
            return null;
        }

        return targetFactoryMap.get(CommonCons.TARGET_PREFIX + targetType);
    }

    private TaskRuleFactory getTaskRuleFactory(String targetType){
        if (taskRuleFactoryMap == null || taskRuleFactoryMap.isEmpty()){
            return null;
        }

        return taskRuleFactoryMap.get(CommonCons.TASK_RULE_PREFIX + targetType);
    }

    /**
     * 添加任务
     *@author gujiachun
     *@date 2021/10/7 10:34 下午
     *@param addTaskList
     *@return void
    */
    private void buildAddTasks(List<TaskDto> addTaskList){
        //剔除后，看看是不是达到最大任务数
        if (isMaxTaskCount()){
            return;
        }

        if (addTaskList == null || addTaskList.isEmpty()){
            return;
        }
        logger.info("集群任务发生变化了，去认领任务了哦");

        //获取正在执行的任务
        Map<String, CanalClient> taskCanalClientMap = canalClientFactory.getTaskCanalClientMap();

        if (taskCanalClientMap != null && taskCanalClientMap.size() > 0) {
            logger.info("集群任务发生变化了，去认领任务了哦-----过滤重复任务");
            //过滤掉 已经处理的任务taskId
            Iterator<TaskDto> iterator = addTaskList.iterator();
            while (iterator.hasNext()) {
                TaskDto taskDto = iterator.next();
                if (taskCanalClientMap.containsKey(taskDto.getTaskId())) {
                    iterator.remove();
                }
            }
        }

        if (addTaskList.size() == 0){
            return;
        }

        int actionCount = maxTaskCount - taskCanalClientMap.size();
        logger.info("集群任务发生变化了，去认领任务了哦-----过滤后---新增任务");
        //有很多任务没有处理，就随机取
        if (addTaskList.size() > actionCount){
            //随机置换
            Collections.shuffle(addTaskList);
            buildExecutor(addTaskList.subList(0, actionCount));
        }else {
            buildExecutor(addTaskList);
        }
    }

    /**
     * 构建执行任务
     * 1、把rockemq或kafka订阅起来
     * 2、构建目标源工厂
     * 3、构建任务规则工厂
     *@author gujiachun
     *@date 2021/9/28 6:31 下午
     *@param
     *@return void
    */
    private void buildExecutor(List<TaskDto> activeTasks){
        if (activeTasks == null || activeTasks.size() == 0){
            logger.info("没有任务 需要执行");
            return;
        }

        for (TaskDto taskDto: activeTasks) {
            logger.info("认领了任务：{}",taskDto.getTaskId());

            //订阅rocketmq或kafka;构建CanalClient
            buildCanalClient(taskDto);

            //构建 目标源工厂
            TargetFactory targetFactory = getTargetFactory(taskDto.getTargetType());
            if (targetFactory != null){
                targetFactory.build(taskDto.getTaskId());
            }

            //构建 任务规则工厂
            TaskRuleFactory taskRuleFactory = getTaskRuleFactory(taskDto.getTargetType());
            if (taskRuleFactory != null){
                taskRuleFactory.reloadTaskRules(taskDto.getTaskId());
            }

            //处理成功后 增加临时有序节点
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatDate = simpleDateFormat.format(taskDto.getUpdateTaskRuleTime());
            //任务临时节点 和 任务id的关联关系
            String esTaskPath = zkClientExt.createEphemeralSequential(zkClientExt.getClusterPath() + "/" + taskDto.getTaskId(),
                    IpLocalUtil.getLocalIpByNetcard() + ":" + port + "," + formatDate);
            logger.info(">>>>>taskId:{}产生了临时节点:{}",taskDto.getTaskId(),esTaskPath);
            zkEsTaskMap.put(taskDto.getTaskId(),esTaskPath);
        }

    }

    /**
     * 构建canal client
     *@author gujiachun
     *@date 2021/9/28 7:28 下午
     *@param taskDto
     *@return void
    */
    private void buildCanalClient(TaskDto taskDto){
        SyncTaskEntity syncTaskEntity = taskService.getById(taskDto.getTaskId());

        BasicTopicEntity basicTopicEntity = topicService.getById(syncTaskEntity.getBasicTopicId());

        BasicMqEntity basicMqEntity = mqService.getById(basicTopicEntity.getMqId());

        canalClientFactory.buildCanalClient(syncTaskEntity,basicTopicEntity,basicMqEntity);
    }

    /**
     * 获得任务taskid 已经有多少个实例消费
     *@author gujiachun
     *@date 2021/9/28 6:28 下午
     *@param taskPathChildren
     *@param taskId
     *@return int
    */
    private int getTaskPathCount(List<String> taskPathChildren,String taskId){
        if (taskPathChildren == null && taskPathChildren.size() <= 0){
            return 0;
        }

        int count = 0;

        for (String taskPath : taskPathChildren){
            if (taskPath.indexOf(taskId) >= 0){
                count ++;
            }
        }

        return count;
    }

    /**
     * 是否任务有更新规则
     *@author gujiachun
     *@date 2021/10/8 1:21 下午
     *@param taskDto
     *@return boolean
    */
    private boolean isTaskRuleUpdated(TaskDto taskDto){

        if (zkEsTaskMap == null || zkEsTaskMap.isEmpty()){
            return false;
        }

        //本实例做的任务 关联的 zk临时节点
        for (String taskId : zkEsTaskMap.keySet()){
            //此任务判断
            if (taskId.equals(taskDto.getTaskId())){
                logger.info(">>>>>>taskId:{},taskPath:{}",taskId,zkEsTaskMap.get(taskId));
                String value = zkClientExt.readData(zkEsTaskMap.get(taskId),true);
                if (value != null){
                    String[] split = value.split(",");
                    try{
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = df.parse(split[1]);
                        //如果zk节点的时间 小于 变化的时间；表示任务规则有更新
                        if (date.before(taskDto.getUpdateTaskRuleTime())){
                            return true;
                        }
                    }catch (Exception e){
                        logger.error("任务中的时间有误,{}",e.getMessage());
                    }
                }
            }
        }
        return false;
    }

}
