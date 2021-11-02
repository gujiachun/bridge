package com.rainbow.bridge.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author gujiachun
 */
@SpringBootApplication(scanBasePackages = {"com.rainbow.bridge.biz","com.rainbow.bridge.admin"})
@MapperScan(basePackages = "com.rainbow.bridge.biz.mapper")
public class BridgeAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(BridgeAdminApplication.class, args);
    }
}
