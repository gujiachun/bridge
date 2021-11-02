package com.rainbow.bridge.web.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(JsonProperties.PREFIX)
public class JsonProperties {

    public static final String PREFIX = "json.fastjson";

    /**
     * 是否使用fastjson做序列化
     */
    private Boolean enable;

    /**
     * 使用fastjson设置 时区，默认 Asia/Shanghai
     */
    private String timeZone;

    /**
     * 使用fastjson 全局设置 时间格式，默认 yyyy-MM-dd HH:mm:ss
     * 可以使用@JSONField(format = "yyyy-MM-dd")自定义
     */
    private String dateFormat;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
}
