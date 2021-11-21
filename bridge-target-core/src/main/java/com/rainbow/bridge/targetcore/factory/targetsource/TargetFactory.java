package com.rainbow.bridge.targetcore.factory.targetsource;


import com.rainbow.bridge.targetcore.factory.BridgeFactory;
import com.rainbow.bridge.targetcore.model.TargetConn;

import java.util.List;

/**
 * 目标源工厂，主要用来构建 目标源链接对象
 * @author gujiachun
 */
public interface TargetFactory<T> extends BridgeFactory {

    /**
     * 构建 链接对象，会遍历TargetConn，调用initTagret方法
     *@author gujiachun
     *@date 2021/11/17 4:44 下午
     *@param targetCons 链接字符串 模型
     *@return void
    */
    void build(List<TargetConn> targetCons);

    /**
     * 根据任务ID ，获取需要的目标源targetid列表；构建目标源对象
     *@author gujiachun
     *@date 2021/11/19 1:16 下午
     *@param taskId
     *@return void
    */
    void build(String taskId);

    /**
     * 这个就是 具体 组装 链接对象
     *@author gujiachun
     *@date 2021/11/17 4:50 下午
     *@param targetConn
     *@return void
    */
    void initTarget(TargetConn targetConn);

    /**
     * 得到目标源 链接对象
     *@author gujiachun
     *@date 2021/11/17 4:55 下午
     *@param targetId
     *@return java.lang.Object
    */
    T getTarget(Integer targetId);

    /**
     * 实例移除任务id时，移除相应的目标源对象
     *@author gujiachun
     *@date 2021/11/17 5:04 下午
     *@param taskId
     *@return void
    */
    void removeTarget(String taskId);

    /**
     * 停止实例时，释放所有目标源 链接对象
     *@author gujiachun
     *@date 2021/11/17 5:07 下午
     *@param
     *@return void
    */
    void release();
}
