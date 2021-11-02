package com.rainbow.bridge.admin.listener;

import org.I0Itec.zkclient.IZkChildListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author gujiachun
 */
public class ServerZkChildListener implements IZkChildListener {

    private static final Logger logger = LoggerFactory.getLogger(ServerZkChildListener.class);

    @Override
    public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
        logger.info("变化节点:{},当前子节点数量{}",parentPath,currentChilds);
    }
}
