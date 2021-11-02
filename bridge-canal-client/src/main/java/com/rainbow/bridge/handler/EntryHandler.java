package com.rainbow.bridge.handler;

import com.rainbow.bridge.model.CanalModel;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author gujiachun
 */
public interface EntryHandler {

    /**
     * 处理新增业务
    * */
    default void insert(CanalModel model, Map<String,String> mysqlType,Map<String, Object> t) throws Exception {

    }

    /**
     * 处理更新业务
     * */
    default void update(CanalModel model,Map<String,String> mysqlType,Map<String, Object> before, Map<String, Object> after) throws Exception {

    }

    /**
     * 处理删除业务
     * */
    default void delete(CanalModel model,Map<String,String> mysqlType,Map<String, Object> t) throws Exception {

    }
}
