package com.rainbow.bridge.targetcore.adapter;

import com.rainbow.bridge.core.enums.EventEnum;
import com.rainbow.bridge.targetcore.model.Param;

import java.util.List;

/**
 * 目标源client对象 适配器
 * 某个操作 和 批量操作 必须至少实现一个
 * @author gujiachun
 */
public interface BridgeAdapter<T> {

    /**
     * 执行某个操作,如果不需要单个操作，可不实现
     *@author gujiachun
     *@date 2021/11/20 3:13 下午
     *@param client 目标源对应的client对象 如：datasource、redisservice
     *@param event 对应canal源数据 发生了事件
     *@param param 对应参数
     *@return void
    */
    default void execute(T client, EventEnum event, Param param) throws Exception {

    }

    /**
     * 执行批量操作，如果不需要批量，可不实现批量操作
     *@author gujiachun
     *@date 2021/11/20 3:14 下午
     *@param client 目标源对应的client对象 如：datasource、redisservice
     *@param event 对应canal源数据 发生了事件
     *@param params 对应参数
     *@return void
    */
    default void execute(T client, EventEnum event, List<Param> params) throws Exception {

    }
}
