package com.rainbow.bridge.canal;

import com.alibaba.otter.canal.client.CanalMQConnector;
import com.alibaba.otter.canal.protocol.FlatMessage;
import com.alibaba.otter.canal.protocol.exception.CanalClientException;
import com.rainbow.bridge.handler.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author gujiachun
 */
public abstract class AbstractCanalClient implements CanalClient{

    private static final Logger logger = LoggerFactory.getLogger(AbstractCanalClient.class);

    private Thread workThread = null;

    protected CanalMQConnector connector;

    protected MessageHandler messageHandler;

    protected volatile boolean running = false;

    protected Long timeout = 1L;

    protected TimeUnit unit = TimeUnit.SECONDS;

    /**
     * 任务id
     * */
    protected String taskId;

    /**
     * topic主题
     * */
    protected String topic;

    /**
     * group组
     * */
    protected String groupName;

    /**
     * 批量大小
     * */
    protected Integer batchSize;

    @Override
    public void start() {
        logger.info("start canal client work thread");

        if (null == connector){
            logger.warn("connector is null");
            return;
        }

        if (null == messageHandler){
            logger.warn("messageHandler is null");
            return;
        }

        workThread = new Thread(this::process);
        workThread.setName("work-thread-task-" + taskId);
        running = true;
        workThread.start();
    }

    @Override
    public void stop() {
        logger.info("stop canal work thread task:{}",taskId);
        if (!running) {
            return;
        }
        running = false;
        if (null != workThread) {
            workThread.interrupt();
            workThread = null;
        }
    }

    @Override
    public void process() {
        while (running) {
            try {
                connector.connect();
                connector.subscribe();
                while (running) {
                    try {
                        List<FlatMessage> messages = connector.getFlatListWithoutAck(timeout, unit);
                        if (messages != null && messages.size() > 0) {
                            logger.info("这个一批获取消息数量 {}", messages.size());
                            for (FlatMessage flatMessage : messages) {
                                messageHandler.handleMessage(flatMessage);
                            }
                            logger.info(">>>>>>>>>提交消息 ack");
                        }
                        connector.ack();
                    }catch (RejectedExecutionException ex){
                        logger.error("达到异步消费线程池 最大处理能力了");
                    }catch (CanalClientException ex1){
                        logger.warn("canal中断了，", ex1);
                    }catch (Exception e) {
                        logger.error("canal 消费异常", e);
                    }
                }
            } catch (Exception e) {
                logger.error("canal client 异常", e);
            } finally {
                logger.info(">>>>>>>取消订阅，断开链接");
                connector.unsubscribe();
                connector.disconnect();
            }
        }
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }

    public boolean isRunning() {
        return running;
    }
}
