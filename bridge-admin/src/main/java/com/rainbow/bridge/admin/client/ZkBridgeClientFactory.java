package com.rainbow.bridge.admin.client;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gujiachun
 */
@Component
public class ZkBridgeClientFactory {

    private Map<String, ZkBridgeClient> zkBridgeClientMap;

    public ZkBridgeClientFactory() {
        zkBridgeClientMap = new ConcurrentHashMap<>(10);
    }

    public void addZkBridgeClient(String env,ZkBridgeClient client){
        zkBridgeClientMap.put(env, client);
    }

    public ZkBridgeClient getZkBridgeClient(String env){
        return zkBridgeClientMap.get(env);
    }

    public Map<String, ZkBridgeClient> getZkBridgeClientMap() {
        return zkBridgeClientMap;
    }
}
