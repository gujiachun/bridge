package com.rainbow.bridge.server.listener;

import com.rainbow.bridge.server.handler.TaskHandler;
import org.I0Itec.zkclient.IZkChildListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author gujiachun
 */
public class ServerZkChildListener implements IZkChildListener {

    private static final Logger logger = LoggerFactory.getLogger(ServerZkChildListener.class);

    @Autowired
    private TaskHandler taskHandler;

    @Override
    public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
        logger.info("变化节点:{},当前子节点数量{}",parentPath,currentChilds);
        taskHandler.refresh();
    }
}
