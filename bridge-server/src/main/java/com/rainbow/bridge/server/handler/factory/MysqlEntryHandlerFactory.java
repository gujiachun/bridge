package com.rainbow.bridge.server.handler.factory;

import com.rainbow.bridge.handler.EntryHandler;
import com.rainbow.bridge.server.adapter.MysqlBridgeAdapter;
import com.rainbow.bridge.server.factory.targetsource.TargetFactory;
import com.rainbow.bridge.server.factory.taskrule.TaskRuleFactory;
import com.rainbow.bridge.server.handler.entry.MysqlEntryHandler;
import freemarker.template.Configuration;

/**
 * 目标源类型mysql的同步对象
 * @author gujiachun
 */
public class MysqlEntryHandlerFactory implements EntryHandlerFactory {

    @Override
    public EntryHandler buildEntryHandler(String targetType, String taskId, TaskRuleFactory taskRuleFactory,
                                          TargetFactory targetFactory, Configuration freeMarkerConfiguration) {

        EntryHandler entryHandler = new MysqlEntryHandler(taskId,taskRuleFactory,targetFactory,
                targetType,new MysqlBridgeAdapter());

        return entryHandler;
    }
}
