package com.rainbow.bridge.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author gujiachun
 */
@ConfigurationProperties(prefix = CanalProperties.CANAL_PREFIX)
public class CanalKafkaProperties extends CanalProperties {

    private Integer partition;

    private String groupId;

    public Integer getPartition() {
        return partition;
    }

    public void setPartition(Integer partition) {
        this.partition = partition;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
