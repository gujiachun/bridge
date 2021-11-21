package com.rainbow.bridge.targetcore.factory;

/**
 * 工厂接口
 * @author gujiachun
 */
public interface BridgeFactory {

    /**
     * 此目标源类型 是否支持
     *@author gujiachun
     *@date 2021/11/19 9:44 上午
     *@param targetType
     *@return boolean
     */
    boolean support(String targetType);
}
