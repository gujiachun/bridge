package com.rainbow.bridge.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置
 * @author gujiachun
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                // 允许跨域的域名
                // 允许所有域
                .allowedOrigins("*")
                // 允许任何方法（post、get等）
                .allowedMethods("*")
                // 允许任何请求头
                .allowedHeaders("*")
                // 允许证书、cookie
                .allowCredentials(true)
                .exposedHeaders(HttpHeaders.SET_COOKIE)
                // maxAge(3600)表明在3600秒内，不需要再发送预检验请求，可以缓存该结果
                .maxAge(3600L);
    }


    //    @Order(Ordered.HIGHEST_PRECEDENCE)
//    @Bean
//    public CorsWebFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        // cookie跨域
//        config.setAllowCredentials(Boolean.TRUE);
//        config.addAllowedMethod(ALL);
//        config.addAllowedOrigin(ALL);
//        config.addAllowedHeader(ALL);
//        // 配置前端js允许访问的自定义响应头
//        config.addExposedHeader("setToken");
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
//        source.registerCorsConfiguration("/**", config);
//
//        return new CorsWebFilter(source);
//    }
}
