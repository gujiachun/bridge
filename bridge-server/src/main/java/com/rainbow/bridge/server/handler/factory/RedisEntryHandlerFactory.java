package com.rainbow.bridge.server.handler.factory;

import com.rainbow.bridge.handler.EntryHandler;
import com.rainbow.bridge.server.adapter.RedisBridgeAdapter;
import com.rainbow.bridge.server.factory.targetsource.TargetFactory;
import com.rainbow.bridge.server.factory.taskrule.TaskRuleFactory;
import com.rainbow.bridge.server.handler.entry.RedisEntryHandler;
import freemarker.template.Configuration;

/**
 * 目标源类型redis的同步对象
 * @author gujiachun
 */
public class RedisEntryHandlerFactory implements EntryHandlerFactory {

    @Override
    public EntryHandler buildEntryHandler(String targetType, String taskId, TaskRuleFactory taskRuleFactory,
                                          TargetFactory targetFactory, Configuration freeMarkerConfiguration) {
        EntryHandler entryHandler = new RedisEntryHandler(taskId,taskRuleFactory,targetFactory,
                freeMarkerConfiguration,targetType,new RedisBridgeAdapter());

        return entryHandler;
    }
}
