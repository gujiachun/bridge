package com.rainbow.bridge.web.config;

import com.rainbow.bridge.web.handler.CustomErrorAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

/**
 * web自动配置
 * @author gujiachun
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE - 1)
@Import(cn.hutool.extra.spring.SpringUtil.class)
public class WebConfig {

    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Bean
    @Primary
    public CustomErrorAttributes customErrorAttributes(){
        logger.info("=====初始化 CustomErrorAttributes。。。。。");
        return new CustomErrorAttributes();
    }
}
