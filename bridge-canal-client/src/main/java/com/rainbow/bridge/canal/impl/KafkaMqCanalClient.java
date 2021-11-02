package com.rainbow.bridge.canal.impl;

import com.alibaba.otter.canal.client.kafka.KafkaCanalConnector;
import com.rainbow.bridge.canal.AbstractCanalClient;

/**
 * @author gujiachun
 */
public class KafkaMqCanalClient extends AbstractCanalClient {

    private String servers;

    private Integer partition;

    @Override
    public void init() {
        connector = new KafkaCanalConnector(servers, topic, partition, groupName, batchSize, true);
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public Integer getPartition() {
        return partition;
    }

    public void setPartition(Integer partition) {
        this.partition = partition;
    }
}
