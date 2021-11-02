package com.rainbow.bridge.canal.impl;

import com.alibaba.otter.canal.client.rocketmq.RocketMQCanalConnector;
import com.rainbow.bridge.canal.AbstractCanalClient;

/**
 * @author gujiachun
 */
public class RocketMqCanalClient extends AbstractCanalClient {

    private String nameServers;

    @Override
    public void init() {
        connector = new RocketMQCanalConnector(nameServers, topic, groupName, batchSize, true);
    }

    public String getNameServers() {
        return nameServers;
    }

    public void setNameServers(String nameServers) {
        this.nameServers = nameServers;
    }
}
