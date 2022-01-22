package com.rainbow.bridge.core.zk;

import java.util.List;

/**
 * @author gujiachun
 */
public interface ZkClient {

    void close();

    Boolean exists(String path);

    void createPersistent(String path);

    String createEphemeralSequential(String path,String data);

    void deletePath(String path);

    void writeData(String path,String data);

    String readData(String path);

    List<String> getChildren(String path);
}
