package com.rainbow.bridge.mysqltarget.handler;

import com.rainbow.bridge.handler.EntryHandler;
import com.rainbow.bridge.mysqltarget.MysqlBridgeAdapter;
import com.rainbow.bridge.targetcore.factory.targetsource.TargetFactory;
import com.rainbow.bridge.targetcore.factory.taskrule.TaskRuleFactory;
import com.rainbow.bridge.targetcore.handler.AbsEntryHandlerFactory;
import freemarker.template.Configuration;

/**
 * 目标源类型mysql的同步对象
 * @author gujiachun
 */
public class MysqlEntryHandlerFactory extends AbsEntryHandlerFactory {

    public MysqlEntryHandlerFactory(String targetType){
        super(targetType);
    }

    @Override
    public EntryHandler buildEntryHandler(String targetType, String taskId, TaskRuleFactory taskRuleFactory,
                                          TargetFactory targetFactory, Configuration freeMarkerConfiguration) {

        EntryHandler entryHandler = new MysqlEntryHandler(taskId,taskRuleFactory,targetFactory,
                targetType,new MysqlBridgeAdapter());

        return entryHandler;
    }
}
