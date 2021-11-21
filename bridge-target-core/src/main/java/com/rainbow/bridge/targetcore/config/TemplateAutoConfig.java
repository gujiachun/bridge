package com.rainbow.bridge.targetcore.config;

import freemarker.cache.StringTemplateLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * freemark引擎 配置
 *@author gujiachun
 *@date 2021/11/19 9:25 上午
*/
@Configuration
public class TemplateAutoConfig {

    @Bean
    public freemarker.template.Configuration configuration() {
        freemarker.template.Configuration configuration = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_30);
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        configuration.setTemplateLoader(templateLoader);
        configuration.setDefaultEncoding("UTF-8");
        return configuration;
    }
}
