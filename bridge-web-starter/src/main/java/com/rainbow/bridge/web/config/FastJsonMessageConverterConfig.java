package com.rainbow.bridge.web.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.rainbow.bridge.web.properties.JsonProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * fastJson转换 对象为json
 * 加入到HttpMessageConverters，而且优先级别最高
 * @author gujiachun
 */
@Configuration
@EnableConfigurationProperties({JsonProperties.class})
public class FastJsonMessageConverterConfig {

    private static final Logger logger = LoggerFactory.getLogger(FastJsonMessageConverterConfig.class);

    @Bean
    @ConditionalOnProperty(prefix = JsonProperties.PREFIX,value = {"enable"}, havingValue = "true",matchIfMissing = true)
    public HttpMessageConverters fastJsonHttpMessageConverters(JsonProperties jsonProperties){
        logger.info("======fastJsonHttpMessageConverters bean初始化中...");
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(
                // 保留map空的字段
                //SerializerFeature.WriteMapNullValue,
                // 将String类型的null转成""
                //SerializerFeature.WriteNullStringAsEmpty,
                // 将Number类型的null转成0
                //SerializerFeature.WriteNullNumberAsZero,
                // 将List类型的null转成[]
                //SerializerFeature.WriteNullListAsEmpty,
                // 将Boolean类型的null转成false
                //SerializerFeature.WriteNullBooleanAsFalse,
                // 避免循环引用
                SerializerFeature.DisableCircularReferenceDetect);

        //时间格式
        if (StringUtils.isNotBlank(jsonProperties.getDateFormat())){
            config.setDateFormat(jsonProperties.getDateFormat());
        }else{
            config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        }

        //设置时区
        if (StringUtils.isNotBlank(jsonProperties.getTimeZone())){
            JSON.defaultTimeZone = TimeZone.getTimeZone(jsonProperties.getTimeZone());
        }else{
            JSON.defaultTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        }

        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(Charset.forName("UTF-8"));
        List<MediaType> mediaTypeList = new ArrayList<>();
        // 解决中文乱码问题，相当于在Controller上的@RequestMapping中加了个属性produces = "application/json"
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(mediaTypeList);

        // 修改StringHttpMessageConverter默认配置
        StringHttpMessageConverter httpMessageConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);

        return new HttpMessageConverters(converter, httpMessageConverter);
    }

}
