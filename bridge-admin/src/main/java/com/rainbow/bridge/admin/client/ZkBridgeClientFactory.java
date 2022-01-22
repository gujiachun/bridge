package com.rainbow.bridge.admin.client;

import com.rainbow.bridge.core.zk.ZkClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gujiachun
 */
@Component
public class ZkBridgeClientFactory {

    private static final Logger logger = LoggerFactory.getLogger(ZkBridgeClientFactory.class);

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

    @PreDestroy
    public void closeAll(){
        logger.info(">>>>>>>>>>>>zk client close");
        if (zkBridgeClientMap.isEmpty()){
            return;
        }

        for (String env : zkBridgeClientMap.keySet()){
            ZkBridgeClient zkBridgeClient = zkBridgeClientMap.get(env);
            zkBridgeClient.close();
        }
    }
}
