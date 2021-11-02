package com.rainbow.bridge.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author gujiachun
 */
@SpringBootApplication(scanBasePackages = {"com.rainbow.bridge.biz","com.rainbow.bridge.server"})
@MapperScan(basePackages = "com.rainbow.bridge.biz.mapper")
public class BridgeServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BridgeServerApplication.class, args);
    }
}
