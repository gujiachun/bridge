package com.rainbow.bridge.core.utils;

import cn.hutool.extra.spring.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 国际化 工具类
 *@author gujiachun
*/
public class I18nUtil {

    private static final Logger logger = LoggerFactory.getLogger(I18nUtil.class);

    /**
     * 框架内部使用到的国际化(固定路径);业务系统不要使用
     *@author gujiachun
     *@date 2021/9/13 1:47 下午
     *@param key
     *@return java.lang.String
    */
    public static String getAssignMessage(String key){
        return getAssignMessage(key,null);
    }

    /**
     * 框架内部使用到的国际化(固定路径);业务系统不要使用
     *@author gujiachun
     *@date 2021/9/13 1:38 下午
     *@param key
     *@param defaultMessage
     *@return java.lang.String
    */
    public static String getAssignMessage(String key,String defaultMessage){
        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        if (bundle != null) {
            try {
                String msg = bundle.getString(key.toString());
                if (StringUtils.isNotBlank(msg)) {
                    return msg;
                }
            } catch (MissingResourceException ex) {
                logger.error("国际化key:{}不存在,ex:{}", key, ex);
            }
        }

        return defaultMessage;
    }

    /**
     * 获取国际化信息 如: start.ge.end = 开始日期{0}必须小于结束日期{1}！
     *@author gujiachun
     *@date 2021/9/13 1:32 下午
     *@param code 对应的key值 如start.ge.end
     *@param args 动态数据 如 String [] param = {startDate, endDate}
     *@param defaultMessage 默认值，如果没有找到，就返回默认值
     *@param locale 国际对象
     *@return java.lang.String
    */
    public static String getMessage(String code, Object[] args, String defaultMessage, Locale locale){
        MessageSource messageSource = SpringUtil.getBean(ResourceBundleMessageSource.class);
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }

    public static String getMessage(String code, Object[] args, String defaultMessage){
        Locale locale =LocaleContextHolder.getLocale();
        return getMessage(code, args, defaultMessage, locale);
    }

    public static String getMessage(String code, Object[] args, Locale locale){
        return getMessage(code, args, "", locale);
    }

    public static String getMessage(String code, Object[] args){
        return getMessage(code, args, "");
    }

    public static String getMessage(String code, Locale locale){
        return getMessage(code, null, "",locale);
    }

    public static String getMessage(String code, String defaultMessage,Locale locale){
        return getMessage(code, null, defaultMessage,locale);
    }

    public static String getMessage(String code, String defaultMessage){
        return getMessage(code, null, defaultMessage);
    }

    public static String getMessage(String code){
        return getMessage(code, "");
    }
}
