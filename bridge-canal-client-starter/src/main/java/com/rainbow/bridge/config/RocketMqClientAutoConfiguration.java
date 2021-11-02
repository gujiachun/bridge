package com.rainbow.bridge.config;


import com.rainbow.bridge.canal.impl.RocketMqCanalClient;
import com.rainbow.bridge.handler.EntryHandler;
import com.rainbow.bridge.handler.MessageHandler;
import com.rainbow.bridge.handler.impl.AsyncFlatMessageHandlerImpl;
import com.rainbow.bridge.handler.impl.SyncFlatMessageHandlerImpl;
import com.rainbow.bridge.properties.CanalProperties;
import com.rainbow.bridge.properties.CanalRocketMqProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import java.util.concurrent.ExecutorService;

@Configuration
@EnableConfigurationProperties(CanalRocketMqProperties.class)
@ConditionalOnBean(value = {EntryHandler.class})
@ConditionalOnProperty(value = CanalProperties.CANAL_MODE, havingValue = "rocketmq")
@Import(ThreadPoolAutoConfiguration.class)
public class RocketMqClientAutoConfiguration {

    private CanalRocketMqProperties canalRocketMqProperties;

    public RocketMqClientAutoConfiguration(CanalRocketMqProperties canalRocketMqProperties) {
        this.canalRocketMqProperties = canalRocketMqProperties;
    }

//    @Bean
//    @ConditionalOnProperty(value = CanalProperties.CANAL_ASYNC, havingValue = "true", matchIfMissing = true)
//    public MessageHandler asyncMessageHandler(EntryHandler entryHandler,ExecutorService executorService) {
//        return new AsyncFlatMessageHandlerImpl(entryHandler, executorService);
//    }
//
//
//    @Bean
//    @ConditionalOnProperty(value = CanalProperties.CANAL_ASYNC, havingValue = "false")
//    public MessageHandler syncMessageHandler(EntryHandler entryHandler) {
//        return new SyncFlatMessageHandlerImpl(entryHandler);
//    }

//    @Bean(initMethod = "start", destroyMethod = "stop")
//    public RocketMqCanalClient rocketMqCanalClient(MessageHandler messageHandler) {
//        RocketMqCanalClient client = new RocketMqCanalClient();
//        client.setNameServers(canalRocketMqProperties.getServer());
//        client.setBatchSize(canalRocketMqProperties.getBatchSize());
//        client.setGroupName(canalRocketMqProperties.getGroupId());
//        client.setMessageHandler(messageHandler);
//        client.setTimeout(canalRocketMqProperties.getTimeout());
//        client.setTopic(canalRocketMqProperties.getDestination());
//        client.setUnit(canalRocketMqProperties.getUnit());
//
//        return client;
//    }

}
