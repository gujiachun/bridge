package com.rainbow.bridge.core.utils;

import cn.hutool.core.map.MapUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author gujiachun
 */
public class PropertiesUtil {

    private final static String splitKey = ";";

    /**
     * map 转换 props
     *@author gujiachun
     *@date 2021/9/25 2:24 下午
     *@param map
     *@return java.util.Properties
    */
    public static Properties mapToProperties(Map<String,String> map){
        Properties props = new Properties();
        if (MapUtil.isNotEmpty(map)){
            for (String key : map.keySet()){
                props.setProperty(key,map.get(key));
            }
        }
        return props;
    }

    /**
     * str 转换 props
     *@author gujiachun
     *@date 2021/9/25 2:49 下午
     *@param str
     *@return java.util.Properties
    */
    public static Properties stringToProperties(String str){
        return mapToProperties(stringToMap(str));
    }

    /**
     * string 转换 map
     *@author gujiachun
     *@date 2021/9/25 2:26 下午
     *@param str （格式: k1=v1;k2=v2）或（k1;k2;k3）
     *@return java.util.Map<java.lang.String,java.lang.String>
    */
    public static Map<String, String> stringToMap(String str){
        if (StringUtils.isNotBlank(str)){
            Map<String, String> map = new HashMap<>(20);
            String[] split = str.split(splitKey);
            for (String s : split){
                if (StringUtils.isNotBlank(s)){
                    int pos = s.indexOf('=');

                    String k;
                    String v;
                    if (pos > 0){
                        k = s.substring(0,pos);
                        v = s.substring(pos + 1);
                    }else{
                        k = s;
                        v = s;
                    }

                    map.put(k.trim(),v.trim());
                }
            }
            return map;
        }
        return null;
    }
}
