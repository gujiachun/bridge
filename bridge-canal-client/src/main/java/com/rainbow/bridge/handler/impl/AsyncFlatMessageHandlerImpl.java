package com.rainbow.bridge.handler.impl;

import com.alibaba.otter.canal.protocol.FlatMessage;
import com.rainbow.bridge.handler.AbstractFlatMessageHandler;
import com.rainbow.bridge.handler.EntryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步消费
 * @author gujiachun
 */
public class AsyncFlatMessageHandlerImpl extends AbstractFlatMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(AsyncFlatMessageHandlerImpl.class);

    private ExecutorService executor;

    public AsyncFlatMessageHandlerImpl(EntryHandler entryHandler,ExecutorService executor){
        this.entryHandler = entryHandler;
        this.executor = executor;
    }

    @Override
    public void handleMessage(FlatMessage flatMessage) {
        ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) executor;
        logger.info("当前队列线程数 {} 堆积数量 {}",poolExecutor.getActiveCount(), poolExecutor.getQueue().size());
        executor.execute(() -> super.handleMessage(flatMessage));
    }

    public ExecutorService getExecutor() {
        return executor;
    }
}
