package com.rainbow.bridge.estarget;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 源 datasource缓存
 * @author gujiachun
 */
public class SourceDsCache {

    private Map<String, DataSource> dataSourceMap;

    /**
     * 增加
     *@author gujiachun
     *@date 2021/12/8 4:49 下午
     *@param taskId
     *@param ds
     *@return void
    */
    public void add(String taskId,DataSource ds){
        if (dataSourceMap == null){
            dataSourceMap = new ConcurrentHashMap<>(10);
        }

        if (dataSourceMap.containsKey(taskId)){
            return;
        }

        dataSourceMap.put(taskId, ds);
    }

    /**
     * 获得datasource
     *@author gujiachun
     *@date 2021/12/8 4:51 下午
     *@param taskId
     *@return javax.sql.DataSource
    */
    public DataSource get(String taskId){
        if (dataSourceMap == null){
           return null;
        }

        return dataSourceMap.get(taskId);
    }

}
