package com.rainbow.bridge.redistarget.config;

import com.rainbow.bridge.biz.service.RedisTargetService;
import com.rainbow.bridge.biz.service.TaskRedisRuleService;
import com.rainbow.bridge.core.constant.TargetType;
import com.rainbow.bridge.redistarget.RedisTargetFactory;
import com.rainbow.bridge.redistarget.RedisTaskRuleFactory;
import com.rainbow.bridge.redistarget.handler.RedisEntryHandlerFactory;
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
public class RedisTargetAutoConfig implements TargetType {

    @Override
    public String getTargetType() {
        return "redis";
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
    @Bean
    public TargetFactory redisTargetFactory(TaskRedisRuleService taskRedisRuleService, RedisTargetService redisTargetService){
        return new RedisTargetFactory(taskRedisRuleService,redisTargetService, getTargetType());
    }

    /**
     * redis目标源类型 对应的任务规则 创建工厂
     *@author gujiachun
     *@date 2021/11/19 10:40 上午
     *@param taskRedisRuleService
     *@return com.rainbow.bridge.targetcore.factory.taskrule.TaskRuleFactory
    */
    @Bean
    public TaskRuleFactory redisTaskRuleFactory(TaskRedisRuleService taskRedisRuleService){
        return new RedisTaskRuleFactory(getTargetType(),taskRedisRuleService);
    }

    /**
     * 目标源redis的 同步对象 创建工厂
     *@author gujiachun
     *@date 2021/11/20 10:53 上午
     *@param
     *@return com.rainbow.bridge.targetcore.handler.EntryHandlerFactory
    */
    @Bean
    public EntryHandlerFactory redisEntryHandlerFactory(){
        return new RedisEntryHandlerFactory(getTargetType());
    }
}
