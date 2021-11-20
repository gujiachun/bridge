package com.rainbow.bridge.server.adapter;

import com.rainbow.bridge.core.enums.EventEnum;
import com.rainbow.bridge.server.model.action.Param;
import com.rainbow.bridge.server.utils.JdbcUtil;

import javax.sql.DataSource;
import java.util.List;

/**
 * 目标源类型mysql的 具体执行适配器
 * @author gujiachun
 */
public class MysqlBridgeAdapter implements BridgeAdapter<DataSource> {

    @Override
    public void execute(DataSource client, EventEnum event, Param param) throws Exception {
        switch (event){
            case insert:
                JdbcUtil.insert(client,param);
                break;
            case update:
                JdbcUtil.update(client,param);
                break;
            case delete:
                JdbcUtil.delete(client,param);
                break;
        }
    }

    @Override
    public void execute(DataSource client, EventEnum event, List<Param> params) throws Exception {
        switch (event){
            case insert:
                JdbcUtil._insert(client,params);
                break;
            case update:
                JdbcUtil._update(client,params);
                break;
            case delete:
                JdbcUtil._delete(client,params);
                break;
        }
    }
}
