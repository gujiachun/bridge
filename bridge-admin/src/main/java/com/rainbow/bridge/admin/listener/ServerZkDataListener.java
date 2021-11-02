package com.rainbow.bridge.admin.listener;

import org.I0Itec.zkclient.IZkDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gujiachun
 */
public class ServerZkDataListener implements IZkDataListener {

    private static final Logger logger = LoggerFactory.getLogger(ServerZkDataListener.class);

    @Override
    public void handleDataChange(String dataPath, Object data) throws Exception {
        logger.info("节点数据变化 dataPath:{},data:{}",dataPath,data);
    }

    @Override
    public void handleDataDeleted(String dataPath) throws Exception {
        logger.info("节点数据删除 dataPath:{}",dataPath);
    }
}
