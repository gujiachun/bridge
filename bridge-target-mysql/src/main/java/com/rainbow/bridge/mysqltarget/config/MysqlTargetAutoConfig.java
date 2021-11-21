package com.rainbow.bridge.mysqltarget.config;

import com.rainbow.bridge.biz.service.MysqlTargetService;
import com.rainbow.bridge.biz.service.TaskMysqlRuleService;
import com.rainbow.bridge.core.constant.CommonCons;
import com.rainbow.bridge.core.constant.TargetType;
import com.rainbow.bridge.mysqltarget.MysqlTargetFactory;
import com.rainbow.bridge.mysqltarget.MysqlTaskRuleFactory;
import com.rainbow.bridge.mysqltarget.handler.MysqlEntryHandlerFactory;
import com.rainbow.bridge.targetcore.factory.targetsource.TargetFactory;
import com.rainbow.bridge.targetcore.factory.taskrule.TaskRuleFactory;
import com.rainbow.bridge.targetcore.handler.EntryHandlerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 彩虹桥服务 配置
 * @author gujiachun
 */
@Configuration
public class MysqlTargetAutoConfig implements TargetType {

    @Override
    public String getTargetType() {
        return "mysql";
    }

    /**
     * 目标源类型mysql，初始化工厂
     * @Bean(CommonCons.TARGET_PREFIX + TargetTypeCons.MYSQL)中的MYSQL 是对应目标源的类型
     * 这个是为了可扩展，可以自行定义 TargetType类型，可以定义mysql1，另一个mysql目标类型
     * 只要和数据库中的目标源类型对应就可以
     *@author gujiachun
     *@date 2021/11/19 10:18 上午
     *@param taskMysqlRuleService
     *@return com.rainbow.bridge.server.factory.target.TargetFactory
    */
    @Bean
    public TargetFactory mysqlTargetFactory(TaskMysqlRuleService taskMysqlRuleService, MysqlTargetService mysqlTargetService){
        return new MysqlTargetFactory(taskMysqlRuleService,mysqlTargetService, getTargetType());
    }

    /**
     * mysql目标源类型 对应的任务规则 创建工厂
     *@author gujiachun
     *@date 2021/11/19 10:39 上午
     *@param taskMysqlRuleService
     *@return com.rainbow.bridge.targetcore.factory.taskrule.TaskRuleFactory
    */
    @Bean
    public TaskRuleFactory mysqlTaskRuleFactory(TaskMysqlRuleService taskMysqlRuleService){
        return new MysqlTaskRuleFactory(getTargetType(),taskMysqlRuleService);
    }

    /**
     * 目标源mysql的 同步对象 创建工厂
     *@author gujiachun
     *@date 2021/11/20 10:52 上午
     *@param
     *@return com.rainbow.bridge.targetcore.handler.EntryHandlerFactory
    */
    @Bean
    public EntryHandlerFactory mysqlEntryHandlerFactory(){
        return new MysqlEntryHandlerFactory(getTargetType());
    }

}
