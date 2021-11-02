package com.rainbow.bridge.core.zk;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁 --- 接口
 * @author gujiachun
 */
public interface DistributedLock {

    /**
     * 获取锁，如果没有得到就等待
     */
    void acquire() throws Exception;

    /**
     * 获取锁，直到超时
     */
    boolean acquire(long time, TimeUnit unit) throws Exception;

    /**
     * 释放锁
     */
    void release() throws Exception;

}
