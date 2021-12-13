package com.rainbow.bridge.estarget.handler;

import com.rainbow.bridge.estarget.EsBridgeAdapter;
import com.rainbow.bridge.handler.EntryHandler;
import com.rainbow.bridge.targetcore.factory.targetsource.TargetFactory;
import com.rainbow.bridge.targetcore.factory.taskrule.TaskRuleFactory;
import com.rainbow.bridge.targetcore.handler.AbsEntryHandlerFactory;
import freemarker.template.Configuration;

/**
 * 目标源类型es的同步对象
 * @author gujiachun
 */
public class EsEntryHandlerFactory extends AbsEntryHandlerFactory {

    public EsEntryHandlerFactory(String targetType){
        super(targetType);
    }

    @Override
    public EntryHandler buildEntryHandler(String targetType, String taskId, TaskRuleFactory taskRuleFactory,
                                          TargetFactory targetFactory, Configuration freeMarkerConfiguration) {
        EntryHandler entryHandler = new EsEntryHandler(taskId,taskRuleFactory,targetFactory,
                freeMarkerConfiguration,targetType,new EsBridgeAdapter());

        return entryHandler;
    }
}
