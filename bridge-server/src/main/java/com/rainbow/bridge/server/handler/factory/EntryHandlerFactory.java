package com.rainbow.bridge.server.handler.factory;

import com.rainbow.bridge.handler.EntryHandler;
import com.rainbow.bridge.server.factory.targetsource.TargetFactory;
import com.rainbow.bridge.server.factory.taskrule.TaskRuleFactory;
import freemarker.template.Configuration;

/**
 * @author gujiachun
 */
public interface EntryHandlerFactory {

    /**
     * 构建具体同步的EntryHandler
     *@author gujiachun
     *@date 2021/11/20 9:30 上午
     *@param targetType 目标源类型
     *@param taskId 任务ID
     *@param taskRuleFactory 任务规则工厂
     *@param targetFactory 目标源工厂
     *@param freeMarkerConfiguration freeMarker引擎配置对象
     *@return com.rainbow.bridge.handler.EntryHandler
    */
    EntryHandler buildEntryHandler(String targetType, String taskId, TaskRuleFactory taskRuleFactory,
                                   TargetFactory targetFactory, Configuration freeMarkerConfiguration);
}
