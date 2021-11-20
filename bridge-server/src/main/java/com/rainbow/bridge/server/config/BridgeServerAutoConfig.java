package com.rainbow.bridge.server.config;

import com.rainbow.bridge.biz.service.MysqlTargetService;
import com.rainbow.bridge.biz.service.RedisTargetService;
import com.rainbow.bridge.biz.service.TaskMysqlRuleService;
import com.rainbow.bridge.biz.service.TaskRedisRuleService;
import com.rainbow.bridge.core.constant.CommonCons;
import com.rainbow.bridge.core.constant.TargetTypeCons;
import com.rainbow.bridge.server.factory.targetsource.MysqlTargetFactory;
import com.rainbow.bridge.server.factory.targetsource.RedisTargetFactory;
import com.rainbow.bridge.server.factory.targetsource.TargetFactory;
import com.rainbow.bridge.server.factory.taskrule.MysqlTaskRuleFactory;
import com.rainbow.bridge.server.factory.taskrule.RedisTaskRuleFactory;
import com.rainbow.bridge.server.factory.taskrule.TaskRuleFactory;
import com.rainbow.bridge.server.handler.factory.EntryHandlerFactory;
import com.rainbow.bridge.server.handler.factory.MysqlEntryHandlerFactory;
import com.rainbow.bridge.server.handler.factory.RedisEntryHandlerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 彩虹桥服务 配置
 * @author gujiachun
 */
@Configuration
public class BridgeServerAutoConfig {

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
    @Bean(CommonCons.TARGET_PREFIX + TargetTypeCons.MYSQL)
    public TargetFactory mysqlTargetFactory(TaskMysqlRuleService taskMysqlRuleService, MysqlTargetService mysqlTargetService){
        return new MysqlTargetFactory(taskMysqlRuleService,mysqlTargetService,TargetTypeCons.MYSQL);
    }

    /**
     * 目标源类型redis，初始化工厂
     * @Bean(CommonCons.TARGET_PREFIX + TargetTypeCons.REDIS)中的REDIS 是对应目标源的类型
     * 这个是为了可扩展，可以自行定义 TargetType类型，可以定义redis1，另一个mysql目标类型
     * 只要和数据库中的目标源类型对应就可以
     *@author gujiachun
     *@date 2021/11/19 10:18 上午
     *@param taskRedisRuleService
     *@return com.rainbow.bridge.server.factory.target.TargetFactory
     */
    @Bean(CommonCons.TARGET_PREFIX + TargetTypeCons.REDIS)
    public TargetFactory redisTargetFactory(TaskRedisRuleService taskRedisRuleService, RedisTargetService redisTargetService){
        return new RedisTargetFactory(taskRedisRuleService,redisTargetService,TargetTypeCons.REDIS);
    }

    /**
     * mysql目标源类型 对应的任务规则 创建工厂
     *@author gujiachun
     *@date 2021/11/19 10:39 上午
     *@param taskMysqlRuleService
     *@return com.rainbow.bridge.server.factory.taskrule.TaskRuleFactory
    */
    @Bean(CommonCons.TASK_RULE_PREFIX + TargetTypeCons.MYSQL)
    public TaskRuleFactory mysqlTaskRuleFactory(TaskMysqlRuleService taskMysqlRuleService){
        return new MysqlTaskRuleFactory(taskMysqlRuleService);
    }

    /**
     * redis目标源类型 对应的任务规则 创建工厂
     *@author gujiachun
     *@date 2021/11/19 10:40 上午
     *@param taskRedisRuleService
     *@return com.rainbow.bridge.server.factory.taskrule.TaskRuleFactory
    */
    @Bean(CommonCons.TASK_RULE_PREFIX + TargetTypeCons.REDIS)
    public TaskRuleFactory redisTaskRuleFactory(TaskRedisRuleService taskRedisRuleService){
        return new RedisTaskRuleFactory(taskRedisRuleService);
    }

    /**
     * 目标源mysql的 同步对象 创建工厂
     *@author gujiachun
     *@date 2021/11/20 10:52 上午
     *@param
     *@return com.rainbow.bridge.server.handler.factory.EntryHandlerFactory
    */
    @Bean(CommonCons.ENTRY_HANDLER_FACTORY_PREFIX + TargetTypeCons.MYSQL)
    public EntryHandlerFactory mysqlEntryHandlerFactory(){
        return new MysqlEntryHandlerFactory();
    }

    /**
     * 目标源redis的 同步对象 创建工厂
     *@author gujiachun
     *@date 2021/11/20 10:53 上午
     *@param
     *@return com.rainbow.bridge.server.handler.factory.EntryHandlerFactory
    */
    @Bean(CommonCons.ENTRY_HANDLER_FACTORY_PREFIX + TargetTypeCons.REDIS)
    public EntryHandlerFactory redisEntryHandlerFactory(){
        return new RedisEntryHandlerFactory();
    }
}
