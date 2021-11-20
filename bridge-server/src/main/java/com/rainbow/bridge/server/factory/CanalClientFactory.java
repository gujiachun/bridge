package com.rainbow.bridge.server.factory;

import com.rainbow.bridge.biz.entity.BasicMqEntity;
import com.rainbow.bridge.biz.entity.BasicTopicEntity;
import com.rainbow.bridge.biz.entity.SyncTaskEntity;
import com.rainbow.bridge.canal.AbstractCanalClient;
import com.rainbow.bridge.canal.CanalClient;
import com.rainbow.bridge.canal.impl.KafkaMqCanalClient;
import com.rainbow.bridge.canal.impl.RocketMqCanalClient;
import com.rainbow.bridge.core.constant.CommonCons;
import com.rainbow.bridge.handler.CanalThreadUncaughtExceptionHandler;
import com.rainbow.bridge.handler.EntryHandler;
import com.rainbow.bridge.handler.MessageHandler;
import com.rainbow.bridge.handler.impl.AsyncFlatMessageHandlerImpl;
import com.rainbow.bridge.handler.impl.SyncFlatMessageHandlerImpl;
import com.rainbow.bridge.server.factory.target.TargetFactory;
import com.rainbow.bridge.server.factory.taskrule.TaskRuleFactory;
import com.rainbow.bridge.server.handler.factory.EntryHandlerFactory;
import freemarker.template.Configuration;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author gujiachun
 */
@Component
public class CanalClientFactory {

    private static final Logger logger = LoggerFactory.getLogger(CanalClientFactory.class);

    private Map<String, CanalClient> taskCanalClientMap;

    /**
     * 一个实例最多 执行1个任务
     * */
    @Value("#{T(java.lang.Integer).parseInt('${bridge.maxTaskCount:1}')}")
    public Integer maxTaskCount;

    @Value("#{T(java.lang.Integer).parseInt('${bridge.threadCorePoolSize:10}')}")
    public Integer threadCorePoolSize;

    @Value("#{T(java.lang.Integer).parseInt('${bridge.threadMaxPoolSize:20}')}")
    public Integer threadMaxPoolSize;

    @Value("#{T(java.lang.Integer).parseInt('${bridge.threadQueueSize:100}')}")
    public Integer threadQueueSize;

    public CanalClientFactory(){
        taskCanalClientMap = new ConcurrentHashMap<>(10);
    }

    @Autowired
    private Map<String, TaskRuleFactory> taskRuleFactoryMap;

    @Autowired
    private Map<String, TargetFactory> targetFactoryMap;

    @Autowired
    private Map<String, EntryHandlerFactory> entryHandlerFactoryMap;

    @Autowired
    private Configuration freeMarkerConfiguration;

    @PreDestroy
    public void stop(){
        for (String key : taskCanalClientMap.keySet()){
            CanalClient canalClient = taskCanalClientMap.get(key);
            canalClient.stop();

            AbstractCanalClient abstractCanalClient = (AbstractCanalClient)canalClient;
            MessageHandler messageHandler = abstractCanalClient.getMessageHandler();

            if (messageHandler instanceof AsyncFlatMessageHandlerImpl){
                AsyncFlatMessageHandlerImpl asyncFlatMessageHandlerImpl = (AsyncFlatMessageHandlerImpl)messageHandler;
                ExecutorService executor = asyncFlatMessageHandlerImpl.getExecutor();
                executor.shutdown();
            }
        }
    }

    public void start(){
        if (taskCanalClientMap == null || taskCanalClientMap.keySet() == null){
            return;
        }
        for (String key : taskCanalClientMap.keySet()){
            AbstractCanalClient canalClient = (AbstractCanalClient)taskCanalClientMap.get(key);
            if (!canalClient.isRunning()){
                canalClient.start();
            }
        }
    }

    public void buildCanalClient(SyncTaskEntity syncTaskEntity, BasicTopicEntity basicTopicEntity, BasicMqEntity basicMqEntity){
        if (taskCanalClientMap.containsKey(syncTaskEntity.getId())){
            return;
        }

        if (maxTaskCount <= taskCanalClientMap.size()){
            logger.warn(">>>>>>任务已满");
        }

        AbstractCanalClient client = null;
        //订阅rocketmq或kafka
        if (CommonCons.mq_kafka_type.equals(basicMqEntity.getMqType())){
            client = new KafkaMqCanalClient();
            ((KafkaMqCanalClient)client).setServers(basicMqEntity.getServers());
        }else if (CommonCons.mq_rocketmq_type.equals(basicMqEntity.getMqType())){
            client = new RocketMqCanalClient();
            ((RocketMqCanalClient)client).setNameServers(basicMqEntity.getServers());
        }

        client.setBatchSize(5);
        client.setGroupName(syncTaskEntity.getId());
        client.setTimeout(20L);
        client.setTopic(basicTopicEntity.getTopic());
        client.setUnit(TimeUnit.SECONDS);
        client.setTaskId(syncTaskEntity.getId());


        String targetType = syncTaskEntity.getTargetType();
        String taskId = syncTaskEntity.getId();

        EntryHandlerFactory entryHandlerFactory = getEntryHandlerFactory(targetType);

        EntryHandler entryHandler = entryHandlerFactory.buildEntryHandler(targetType,taskId,
                getTaskRuleFactory(targetType),getTargetFactory(targetType),freeMarkerConfiguration);

        //同步
        if (syncTaskEntity.getAsync() == 0){
            SyncFlatMessageHandlerImpl syncFlatMessageHandler = new SyncFlatMessageHandlerImpl(entryHandler);
            client.setMessageHandler(syncFlatMessageHandler);
        }else{
            //异步创建线程池
            String namePattern = "task-thread-" + syncTaskEntity.getId()+ "-%d";
            AsyncFlatMessageHandlerImpl asyncFlatMessageHandler = new AsyncFlatMessageHandlerImpl(entryHandler, executorService(namePattern));
            client.setMessageHandler(asyncFlatMessageHandler);
        }
        taskCanalClientMap.put(syncTaskEntity.getId(),client);
        client.init();
    }

    public ExecutorService executorService(String namePattern) {
        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern(namePattern)
                .uncaughtExceptionHandler(new CanalThreadUncaughtExceptionHandler()).build();

        return new ThreadPoolExecutor(threadCorePoolSize, threadMaxPoolSize, 10000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(threadQueueSize),factory);
    }

    public Map<String, CanalClient> getTaskCanalClientMap() {
        return taskCanalClientMap;
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

    private EntryHandlerFactory getEntryHandlerFactory(String targetType){
        if (entryHandlerFactoryMap == null || entryHandlerFactoryMap.isEmpty()){
            return null;
        }

        return entryHandlerFactoryMap.get(CommonCons.ENTRY_HANDLER_FACTORY_PREFIX + targetType);
    }
}
