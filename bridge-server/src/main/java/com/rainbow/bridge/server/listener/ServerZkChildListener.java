package com.rainbow.bridge.server.listener;

import com.rainbow.bridge.server.handler.TaskHandler;
import com.rainbow.bridge.server.utils.SpringUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gujiachun
 */
public class ServerZkChildListener implements PathChildrenCacheListener {

    private static final Logger logger = LoggerFactory.getLogger(ServerZkChildListener.class);

    private String path;

    public ServerZkChildListener(String path){
        this.path = path;
    }

    @Override
    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
        logger.info("节点{}的子节点发生变化了",path);
        TaskHandler taskHandler = SpringUtil.getBean(TaskHandler.class);
        if (taskHandler == null){
            logger.warn("taskHandler is null");
            return;
        }
        switch (event.getType()) {
            case CHILD_ADDED:
                logger.info("增加了子节点");
                taskHandler.refresh();
                break;
            case CHILD_REMOVED:
                logger.info("删除了子节点");
                taskHandler.refresh();
                break;
            default:
                break;
        }
    }
}
