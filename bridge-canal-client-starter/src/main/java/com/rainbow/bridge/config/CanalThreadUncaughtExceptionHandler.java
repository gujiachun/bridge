package com.rainbow.bridge.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yang peng
 * @date 2019/4/117:29
 */
public class CanalThreadUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {


    private static final Logger logger = LoggerFactory.getLogger(CanalThreadUncaughtExceptionHandler.class);

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        logger.error("thread "+ t.getName()+" have a exception",e);
    }
}
