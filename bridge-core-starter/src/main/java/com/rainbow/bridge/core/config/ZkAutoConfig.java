//package com.rainbow.bridge.core.config;
//
//import com.rainbow.bridge.core.properties.ZkProperties;
//import com.rainbow.bridge.core.zk.ZkClient1;
//import org.apache.zookeeper.Watcher;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * zk自动装配
// * @author gujiachun
// */
//@Configuration
//@EnableConfigurationProperties({ZkProperties.class})
//public class ZkAutoConfig {
//
//    private static final Logger logger = LoggerFactory.getLogger(ZkAutoConfig.class);
//
//    private Watcher watcher;
//
//    @Autowired(required = false)
//    public void setWatcher(Watcher watcher) {
//        this.watcher = watcher;
//    }
//
//    @Bean
//    public ZkClient1 zkClient(ZkProperties props){
//        ZkClient1 zkClient1 = new ZkClient1(props.getConnStr());
//        if (props.getWatched()){
//            try {
//                zkClient1.addWatch(props.getRootPath(),watcher);
//            }catch (Exception e){
//                logger.error("增加zookeeper监听失败,e:{}",e.getMessage());
//            }
//        }
//        return zkClient1;
//    }
//
//}
