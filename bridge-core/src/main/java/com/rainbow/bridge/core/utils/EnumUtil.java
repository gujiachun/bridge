package com.rainbow.bridge.core.utils;

import cn.hutool.core.util.StrUtil;
import com.rainbow.bridge.core.ResultEnum;

/**
 * 通过code找到msg, 通过msg找到code
 *
 * @author gujiachun
 * */
public class EnumUtil {

    public static <T extends ResultEnum> String getMsgByCode(Integer code, Class<T> t){
        for(T item: t.getEnumConstants()){
            if(item.getCode().equals(code)){
                return item.getMessage();
            }
        }
        return "";
    }

    public static <T extends ResultEnum> Integer getCodeByMsg(String message, Class<T> t){
        for(T item: t.getEnumConstants()){
            if(StrUtil.equals(item.getMessage(),message)){
                return item.getCode();
            }
        }
        return 500;
    }

}
