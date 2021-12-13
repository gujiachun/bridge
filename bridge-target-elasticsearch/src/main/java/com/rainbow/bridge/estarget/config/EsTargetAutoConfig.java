package com.rainbow.bridge.estarget.config;

import com.rainbow.bridge.biz.service.es.EsTargetService;
import com.rainbow.bridge.biz.service.es.TaskEsRuleService;
import com.rainbow.bridge.core.constant.TargetType;
import com.rainbow.bridge.estarget.EsTargetFactory;
import com.rainbow.bridge.estarget.EsTaskRuleFactory;
import com.rainbow.bridge.estarget.SourceDsCache;
import com.rainbow.bridge.estarget.handler.EsEntryHandlerFactory;
import com.rainbow.bridge.targetcore.factory.targetsource.TargetFactory;
import com.rainbow.bridge.targetcore.factory.taskrule.TaskRuleFactory;
import com.rainbow.bridge.targetcore.handler.EntryHandlerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gujiachun
 */
@Configuration
public class EsTargetAutoConfig implements TargetType {
    @Override
    public String getTargetType() {
        return "es";
    }


    /**
     * 目标源类型es，初始化工厂
     * 这个是为了可扩展，可以自行定义 TargetType类型，可以定义es1
     * 只要和数据库中的目标源类型对应就可以
     *@author gujiachun
     *@date 2021/11/19 10:18 上午
     *@param taskEsRuleService
     *@return com.rainbow.bridge.server.factory.target.TargetFactory
     */
    @Bean
    public TargetFactory esTargetFactory(TaskEsRuleService taskEsRuleService, EsTargetService esTargetService){
        return new EsTargetFactory(taskEsRuleService,esTargetService, getTargetType());
    }

    /**
     * es目标源类型 对应的任务规则 创建工厂
     *@author gujiachun
     *@date 2021/11/19 10:40 上午
     *@param taskEsRuleService
     *@return com.rainbow.bridge.targetcore.factory.taskrule.TaskRuleFactory
     */
    @Bean
    public TaskRuleFactory esTaskRuleFactory(TaskEsRuleService taskEsRuleService){
        return new EsTaskRuleFactory(getTargetType(),taskEsRuleService);
    }

    /**
     * 目标源es的 同步对象 创建工厂
     *@author gujiachun
     *@date 2021/11/20 10:53 上午
     *@param
     *@return com.rainbow.bridge.targetcore.handler.EntryHandlerFactory
     */
    @Bean
    public EntryHandlerFactory esEntryHandlerFactory(){
        return new EsEntryHandlerFactory(getTargetType());
    }

    /**
     * 源链接 缓存
     *@author gujiachun
     *@date 2021/12/8 4:52 下午
     *@param
     *@return com.rainbow.bridge.mysqltarget.SourceDsCache
     */
    @Bean
    public SourceDsCache sourceDsCache(){
        return new SourceDsCache();
    }
}
