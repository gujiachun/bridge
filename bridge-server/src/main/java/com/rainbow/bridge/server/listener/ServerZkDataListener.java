package com.rainbow.bridge.server.listener;

import com.rainbow.bridge.server.handler.TaskHandler;
import org.I0Itec.zkclient.IZkDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author gujiachun
 */
public class ServerZkDataListener implements IZkDataListener {

    private static final Logger logger = LoggerFactory.getLogger(ServerZkDataListener.class);

    @Autowired
    private TaskHandler taskHandler;

    @Override
    public void handleDataChange(String dataPath, Object data) throws Exception {
        logger.info("节点数据变化 dataPath:{},data:{}",dataPath,data);
        taskHandler.refresh();
    }

    @Override
    public void handleDataDeleted(String dataPath) throws Exception {
        logger.info("节点数据删除 dataPath:{}",dataPath);
        taskHandler.refresh();
    }
}
