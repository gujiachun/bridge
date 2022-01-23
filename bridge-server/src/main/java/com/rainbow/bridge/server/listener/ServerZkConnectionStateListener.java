package com.rainbow.bridge.server.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gujiachun
 */
public class ServerZkConnectionStateListener implements ConnectionStateListener {

    private static final Logger logger = LoggerFactory.getLogger(ServerZkConnectionStateListener.class);

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        if (newState == ConnectionState.RECONNECTED){
            logger.info(">>>>>>> zk 重连上了");
        }else if (newState == ConnectionState.LOST){
            logger.warn(">>>>>>> zk 中断了");
        }
    }
}
