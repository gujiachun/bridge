package com.rainbow.bridge.core.utils;

import com.rainbow.bridge.core.entity.Pk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gujiachun
 */
public class PublicUtil {

    private static final Logger logger = LoggerFactory.getLogger(PublicUtil.class);

    public static Map<String, Object> parse(Map<String, String> map){
        Map<String, Object> objectMap = new HashMap<>(10);
        if (map != null && map.size() > 0){
            for (String key : map.keySet()){
                objectMap.put(key,map.get(key));
            }
        }
        return objectMap;
    }

    /**
     * 获取之前的值，全部字段
     *@author gujiachun
     *@date 2021/10/1 10:04 下午
     *@param partBeforeMap flatmessage old对象的值，只有改变的字段，不是全部字段
     *@param afterMap flatmessage data值，更新后的值，全部字段
     *@return java.util.Map<java.lang.String,java.lang.String>
    */
    public static Map<String, Object> getBefore(Map<String,Object> partBeforeMap,Map<String,Object> afterMap){

        if (partBeforeMap == null || partBeforeMap.size() == 0){
            return  afterMap;
        }

        if (afterMap == null || afterMap.size() == 0){
            logger.error(" afterMap 为 空，数据有问题");
            return null;
        }

        Map<String, Object> beforeMap = new HashMap<>(50);

        for (String col : afterMap.keySet()){
            boolean b = partBeforeMap.containsKey(col);
            beforeMap.put(col, b ? partBeforeMap.get(col) : afterMap.get(col));
        }

        return beforeMap;
    }
}
