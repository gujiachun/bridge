package com.rainbow.bridge.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = CanalProperties.CANAL_PREFIX)
public class CanalRocketMqProperties extends CanalProperties {

    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
