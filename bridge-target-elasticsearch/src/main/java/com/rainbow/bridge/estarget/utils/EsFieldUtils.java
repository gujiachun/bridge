package com.rainbow.bridge.estarget.utils;

import com.alibaba.fastjson.JSONObject;
import com.rainbow.bridge.core.constant.CommonCons;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author gujiachun
 */
public class EsFieldUtils {

    public static String[] PARSE_PATTERNS = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 自定义字段类型数据个还是转换
     *
     * @param fieldType field type
     * @param columnValue 数据库里面的值
     * @return
     */
    public static Object convertType(String fieldType, Object columnValue) {
        if (columnValue == null) {
            return null;
        }

        String colValue = columnValue.toString();
        if (StringUtils.isBlank(colValue)){
            return columnValue;
        }

        if (fieldType.equals(CommonCons.EsFieldType.INT)) {
            return Integer.parseInt(colValue);
        }

        if (fieldType.equals(CommonCons.EsFieldType.DATE)) {
            SimpleDateFormat df = new SimpleDateFormat(CommonCons.DATA_FORMAT);
            return df.format(parseDate(colValue));
        }

        if (fieldType.equals(CommonCons.EsFieldType.DECIMAL)) {
            return new BigDecimal(colValue);
        }

        if (fieldType.equals(CommonCons.EsFieldType.JSON)) {
            return JSONObject.parseObject(colValue);
        }

        if (fieldType.startsWith(CommonCons.EsFieldType.ARRAY)) {
            String arraySplit = fieldType.replace(CommonCons.EsFieldType.ARRAY,"");
            //数组格式 array+值分隔符(1个字符)+值类型（int，decimal，string；默认不写为string）
            if (StringUtils.isNotBlank(arraySplit)){
                if (arraySplit.length() == 1){
                    return colValue.split(arraySplit);
                }
                String split = arraySplit.substring(0,1);
                String[] arrValue = colValue.split(split);

                String type = arraySplit.substring(1);

                return convertArray(arrValue,type);
            }
        }

        return columnValue;
    }

    private static Object[] convertArray(String[] arrayValue , String type){

        if (arrayValue == null || arrayValue.length == 0){
            return null;
        }

        Object[] objects = new Object[arrayValue.length];

        for (int i=0; i< arrayValue.length; i++){
            switch (type){
                case CommonCons.EsFieldType.INT:
                    objects[i] = Integer.parseInt(arrayValue[i]);
                    break;
                case CommonCons.EsFieldType.DECIMAL:
                    objects[i] = new BigDecimal(arrayValue[i]);
                    break;
                default:
                    objects[i] = arrayValue[i];
                    break;
            }
        }

        return objects;
    }


    public static Date parseDate(String str) {
        if (str == null) {
            return null;
        }
        try {
            return org.apache.commons.lang.time.DateUtils.parseDate(str, PARSE_PATTERNS);
        } catch (ParseException e) {
            return null;
        }
    }

}
