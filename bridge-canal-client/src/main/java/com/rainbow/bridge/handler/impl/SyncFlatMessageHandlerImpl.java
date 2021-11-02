package com.rainbow.bridge.handler.impl;

import com.alibaba.otter.canal.protocol.FlatMessage;
import com.rainbow.bridge.handler.AbstractFlatMessageHandler;
import com.rainbow.bridge.handler.EntryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 同步消费
 * @author gujiachun
 */
public class SyncFlatMessageHandlerImpl extends AbstractFlatMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(SyncFlatMessageHandlerImpl.class);

    public SyncFlatMessageHandlerImpl(EntryHandler entryHandler){
        this.entryHandler = entryHandler;
    }

    @Override
    public void handleMessage(FlatMessage flatMessage) {
        super.handleMessage(flatMessage);
    }
}
