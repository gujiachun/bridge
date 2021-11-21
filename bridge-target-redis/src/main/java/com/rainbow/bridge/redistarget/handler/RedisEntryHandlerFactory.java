package com.rainbow.bridge.redistarget.handler;

import com.rainbow.bridge.handler.EntryHandler;
import com.rainbow.bridge.redistarget.RedisBridgeAdapter;
import com.rainbow.bridge.targetcore.factory.targetsource.TargetFactory;
import com.rainbow.bridge.targetcore.factory.taskrule.TaskRuleFactory;
import com.rainbow.bridge.targetcore.handler.AbsEntryHandlerFactory;
import freemarker.template.Configuration;

/**
 * 目标源类型redis的同步对象
 * @author gujiachun
 */
public class RedisEntryHandlerFactory extends AbsEntryHandlerFactory {

    public RedisEntryHandlerFactory(String targetType){
        super(targetType);
    }

    @Override
    public EntryHandler buildEntryHandler(String targetType, String taskId, TaskRuleFactory taskRuleFactory,
                                          TargetFactory targetFactory, Configuration freeMarkerConfiguration) {
        EntryHandler entryHandler = new RedisEntryHandler(taskId,taskRuleFactory,targetFactory,
                freeMarkerConfiguration,targetType,new RedisBridgeAdapter());

        return entryHandler;
    }
}
