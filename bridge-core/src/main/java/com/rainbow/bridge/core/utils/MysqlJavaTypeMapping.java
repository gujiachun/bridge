package com.rainbow.bridge.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gujiachun
 */
public class MysqlJavaTypeMapping {

    private static final Logger logger = LoggerFactory.getLogger(MysqlJavaTypeMapping.class);

    public static Map<String,Class<?>> mapping = new HashMap<>(10);

    static {
        mapping.put("varchar",String.class);
        mapping.put("datetime", Date.class);
        mapping.put("tinyint", Integer.class);
        mapping.put("decimal", BigDecimal.class);
        mapping.put("smallint ", Integer.class);
        mapping.put("int", Integer.class);
        mapping.put("bit(1)", Boolean.class);
        mapping.put("float", Float.class);
        mapping.put("double", Double.class);
        mapping.put("date", Date.class);
        mapping.put("char", String.class);
        mapping.put("text", String.class);
        mapping.put("binary", Byte.class);
        mapping.put("bit", Byte.class);
        mapping.put("bigint", Long.class);
        mapping.put("numeric", BigDecimal.class);
    }

    public static Class<?> getType(String sqlType){
        return mapping.get(sqlType);
    }

    public static Map<String, Object> getObjectMap(Map<String, String> mysqlType,Map<String, String> map){
        if (map == null || map.isEmpty()){
            return null;
        }

        Map<String, Object> objectMap = new HashMap<>(50);

        for (String column : map.keySet()){
            String type = mysqlType.get(column);
            Class<?> clz = null;
            //获取类型
            if (StringUtils.isNotBlank(type)){
                if ("bit(1)".equals(type)){
                    clz = mapping.get(type);
                }else{
                    int pos = type.indexOf('(');
                    if (pos > 0){
                        String type1 = type.substring(0,pos);
                        clz = mapping.get(type1);
                    }else{
                        clz = mapping.get(type);
                    }
                }
            }
            if (clz != null){
                if (clz.getName().equals(Integer.class.getName())){
                    objectMap.put(column,map.get(column) == null ? null : Integer.valueOf(map.get(column)));
                }else if(clz.getName().equals(BigDecimal.class.getName())){
                    objectMap.put(column,map.get(column) == null ? null : new BigDecimal(map.get(column)));
                }else if(clz.getName().equals(Boolean.class.getName())){
                    if ("1".equals(map.get(column))){
                        objectMap.put(column,true);
                    }else if("0".equals(map.get(column))){
                        objectMap.put(column,false);
                    }else {
                        objectMap.put(column,map.get(column) == null ? null : Boolean.valueOf(map.get(column)));
                    }
                }else if(clz.getName().equals(Date.class.getName())){
                    objectMap.put(column,map.get(column) == null ? null : stringToDate(map.get(column)));
                }else if(clz.getName().equals(Float.class.getName())){
                    objectMap.put(column,map.get(column) == null ? null : Float.valueOf(map.get(column)));
                }else if(clz.getName().equals(Double.class.getName())){
                    objectMap.put(column,map.get(column) == null ? null : Double.valueOf(map.get(column)));
                }else if(clz.getName().equals(Long.class.getName())){
                    objectMap.put(column,map.get(column) == null ? null : Long.valueOf(map.get(column)));
                }else if(clz.getName().equals(Byte.class.getName())){
                    objectMap.put(column,map.get(column) == null ? null : map.get(column).getBytes());
                }else {
                    objectMap.put(column,map.get(column));
                }
            }
        }

        return objectMap;
    }

    public static Date stringToDate(String str) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            logger.error("转换时间异常:{}",e.getMessage());
        }
        return date;
    }


}
