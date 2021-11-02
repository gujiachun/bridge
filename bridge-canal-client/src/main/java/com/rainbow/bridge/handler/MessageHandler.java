package com.rainbow.bridge.handler;

/**
 * @author gujiachun
 */
public interface MessageHandler<T> {

    /**
     * 处理消息
     *@author gujiachun
     *@date 2021/9/26 9:08 上午
     *@param t
     *@return void
    */
    void handleMessage(T t);
}
