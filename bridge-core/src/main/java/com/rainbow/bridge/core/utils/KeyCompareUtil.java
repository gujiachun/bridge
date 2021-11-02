package com.rainbow.bridge.core.utils;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 *  Map排序
 */
public class KeyCompareUtil implements Comparator<String> {

    /**
     *  从小到大排序
     * @see Comparator#compare(Object, Object)
     */
    @Override
    public int compare(String s1, String s2) {
        return s1.compareTo(s2); //按升序排列 ,改为按降序排列return s2.compareTo(s1);
    }

    /**
     *
     * <b>Description:</b><br>对map利用key进行排序
     * @param map
     * @return
     * @Note
     * <b>Author:</b> 简陌刀丶阿吉
     * <br><b>Date:</b> 2019年8月27日 上午11:06:32
     * <br><b>Version:</b> 1.0
     */
    public static Map<String, Object> sortMapObjectByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> sortMap = new TreeMap<String, Object>(new KeyCompareUtil());
        sortMap.putAll(map);
        return sortMap;
    }

    public static Map<String, String> sortMapStringByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>(new KeyCompareUtil());
        sortMap.putAll(map);
        return sortMap;
    }

    public static Map<String, String[]> sortMapStringArrayByKey(Map<String, String[]> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String[]> sortMap = new TreeMap<String, String[]>(new KeyCompareUtil());
        sortMap.putAll(map);
        return sortMap;
    }
}
