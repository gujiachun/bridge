package com.rainbow.bridge.canal;

/**
 * @author gujiachun
 */
public interface CanalClient {

    void init();

    void start();

    void stop();

    void process();
}
