package com.rainbow.bridge.server.listener;

import com.rainbow.bridge.server.handler.TaskHandler;
import com.rainbow.bridge.server.utils.SpringUtil;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gujiachun
 */
public class ServerZkDataListener implements NodeCacheListener {

    private static final Logger logger = LoggerFactory.getLogger(ServerZkDataListener.class);

    private String path;

    public ServerZkDataListener(String path){
        this.path = path;
    }

    @Override
    public void nodeChanged() throws Exception {
        logger.info("节点数据变化 dataPath:{}",path);
        TaskHandler taskHandler = SpringUtil.getBean(TaskHandler.class);
        if (taskHandler == null){
            logger.warn("taskHandler is null");
            return;
        }
        taskHandler.refresh();
    }
}
