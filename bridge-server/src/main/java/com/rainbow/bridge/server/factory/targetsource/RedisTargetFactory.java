package com.rainbow.bridge.server.factory.targetsource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rainbow.bridge.biz.entity.SyncRedisTargetEntity;
import com.rainbow.bridge.biz.entity.SyncTaskRuleRedisEntity;
import com.rainbow.bridge.core.constant.RedisCons;
import com.rainbow.bridge.core.enums.StatusEnum;
import com.rainbow.bridge.core.utils.PropertiesUtil;
import com.rainbow.bridge.server.model.targetsource.RedisTargetConn;
import com.rainbow.bridge.server.model.targetsource.TargetConn;
import com.rainbow.bridge.server.redis.RedisClusterService;
import com.rainbow.bridge.server.redis.RedisSentinelService;
import com.rainbow.bridge.server.redis.RedisService;
import com.rainbow.bridge.server.redis.RedisSingleNodeService;
import com.rainbow.bridge.server.redis.prop.RedisClusterProperties;
import com.rainbow.bridge.server.redis.prop.RedisProperties;
import com.rainbow.bridge.server.redis.prop.RedisSentinelProperties;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * redis目标源类型 构建工厂
 * @author gujiachun
 */
public class RedisTargetFactory extends AbsTargetFactory {

    //这个对象 看看 将来能不能去掉，target中 尽量 不要和 taskrule 有耦合
    private IService redisTaskRuleService;

    private IService redisTargetService;

    public RedisTargetFactory(IService redisTaskRuleService, IService redisTargetService ,String targetType){
        this.redisTaskRuleService = redisTaskRuleService;
        this.redisTargetService = redisTargetService;
        this.targetType = targetType;
    }

    @Override
    protected Object initTargetConn(TargetConn targetConn) {
        Map<String, String> map = PropertiesUtil.stringToMap(targetConn.getProps());
        try {
            Integer mode = ((RedisTargetConn) targetConn).getMode();
            //单机
            if (RedisCons.single.equals(mode)){
                return createSingle(map);
            }else if (RedisCons.sentinel.equals(mode)){
                return createSentinel(map);
            }else if (RedisCons.cluster.equals(mode)){
                return createCluster(map);
            }
            logger.info("初始redis目标client成功,目标数据源:{}",targetConn.getTargetId());
        } catch (Exception e) {
            logger.error("初始redis目标client发生异常，目标数据源:{},异常:{}",targetConn.getTargetId(),e.getMessage());
        }
        return null;
    }

    private RedisService createSingle(Map<String, String> map){
        RedisProperties prop = new RedisProperties();
        if (map.containsKey(RedisProperties.REDIS_PREFIX + ".database")){
            prop.setDatabase(Integer.valueOf(map.get(RedisProperties.REDIS_PREFIX + ".database")));
        }

        if (map.containsKey(RedisProperties.REDIS_PREFIX + ".connectTimeout")){
            prop.setConnectTimeout(Integer.valueOf(map.get(RedisProperties.REDIS_PREFIX + ".connectTimeout")));
        }

        if (map.containsKey(RedisProperties.REDIS_PREFIX + ".host")){
            prop.setHost(map.get(RedisProperties.REDIS_PREFIX + ".host"));
        }

        if (map.containsKey(RedisProperties.REDIS_PREFIX + ".maxActive")){
            prop.setMaxActive(Integer.valueOf(map.get(RedisProperties.REDIS_PREFIX + ".maxActive")));
        }

        if (map.containsKey(RedisProperties.REDIS_PREFIX + ".maxIdle")){
            prop.setMaxIdle(Integer.valueOf(map.get(RedisProperties.REDIS_PREFIX + ".maxIdle")));
        }

        if (map.containsKey(RedisProperties.REDIS_PREFIX + ".maxWait")){
            prop.setMaxWait(Long.valueOf(map.get(RedisProperties.REDIS_PREFIX + ".maxWait")));
        }

        if (map.containsKey(RedisProperties.REDIS_PREFIX + ".minIdle")){
            prop.setMinIdle(Integer.valueOf(map.get(RedisProperties.REDIS_PREFIX + ".minIdle")));
        }

        if (map.containsKey(RedisProperties.REDIS_PREFIX + ".password")){
            prop.setPassword(map.get(RedisProperties.REDIS_PREFIX + ".password"));
        }

        if (map.containsKey(RedisProperties.REDIS_PREFIX + ".port")){
            prop.setPort(Integer.valueOf(map.get(RedisProperties.REDIS_PREFIX + ".port")));
        }

        if (map.containsKey(RedisProperties.REDIS_PREFIX + ".soTimeout")){
            prop.setSoTimeout(Integer.valueOf(map.get(RedisProperties.REDIS_PREFIX + ".soTimeout")));
        }

        return new RedisSingleNodeService(prop);
    }

    private RedisService createSentinel(Map<String, String> map){
        RedisSentinelProperties prop = new RedisSentinelProperties();
        if (map.containsKey(RedisSentinelProperties.SENTINEL_PREFIX + ".masterName")){
            prop.setMasterName(map.get(RedisProperties.REDIS_PREFIX + ".masterName"));
        }

        if (map.containsKey(RedisSentinelProperties.SENTINEL_PREFIX + ".nodes")){
            prop.setNodes(map.get(RedisProperties.REDIS_PREFIX + ".nodes"));
        }

        if (map.containsKey(RedisSentinelProperties.SENTINEL_PREFIX + ".database")){
            prop.setDatabase(Integer.valueOf(map.get(RedisSentinelProperties.SENTINEL_PREFIX + ".database")));
        }

        if (map.containsKey(RedisSentinelProperties.SENTINEL_PREFIX + ".connectTimeout")){
            prop.setConnectTimeout(Integer.valueOf(map.get(RedisProperties.REDIS_PREFIX + ".connectTimeout")));
        }

        if (map.containsKey(RedisSentinelProperties.SENTINEL_PREFIX + ".maxActive")){
            prop.setMaxActive(Integer.valueOf(map.get(RedisSentinelProperties.SENTINEL_PREFIX + ".maxActive")));
        }

        if (map.containsKey(RedisSentinelProperties.SENTINEL_PREFIX + ".maxIdle")){
            prop.setMaxIdle(Integer.valueOf(map.get(RedisSentinelProperties.SENTINEL_PREFIX + ".maxIdle")));
        }

        if (map.containsKey(RedisSentinelProperties.SENTINEL_PREFIX + ".maxWait")){
            prop.setMaxWait(Long.valueOf(map.get(RedisSentinelProperties.SENTINEL_PREFIX + ".maxWait")));
        }

        if (map.containsKey(RedisSentinelProperties.SENTINEL_PREFIX + ".minIdle")){
            prop.setMinIdle(Integer.valueOf(map.get(RedisSentinelProperties.SENTINEL_PREFIX + ".minIdle")));
        }

        if (map.containsKey(RedisSentinelProperties.SENTINEL_PREFIX + ".password")){
            prop.setPassword(map.get(RedisSentinelProperties.SENTINEL_PREFIX + ".password"));
        }

        if (map.containsKey(RedisSentinelProperties.SENTINEL_PREFIX + ".soTimeout")){
            prop.setSoTimeout(Integer.valueOf(map.get(RedisSentinelProperties.SENTINEL_PREFIX + ".soTimeout")));
        }

        return new RedisSentinelService(prop);
    }

    private RedisService createCluster(Map<String, String> map){
        RedisClusterProperties prop = new RedisClusterProperties();

        if (map.containsKey(RedisClusterProperties.CLUSTER_PREFIX + ".nodes")){
            prop.setNodes(map.get(RedisClusterProperties.CLUSTER_PREFIX + ".nodes"));
        }

        if (map.containsKey(RedisClusterProperties.CLUSTER_PREFIX + ".database")){
            prop.setDatabase(Integer.valueOf(map.get(RedisClusterProperties.CLUSTER_PREFIX + ".database")));
        }

        if (map.containsKey(RedisClusterProperties.CLUSTER_PREFIX + ".connectTimeout")){
            prop.setConnectTimeout(Integer.valueOf(map.get(RedisClusterProperties.CLUSTER_PREFIX + ".connectTimeout")));
        }

        if (map.containsKey(RedisSentinelProperties.SENTINEL_PREFIX + ".maxActive")){
            prop.setMaxActive(Integer.valueOf(map.get(RedisSentinelProperties.SENTINEL_PREFIX + ".maxActive")));
        }

        if (map.containsKey(RedisSentinelProperties.SENTINEL_PREFIX + ".maxIdle")){
            prop.setMaxIdle(Integer.valueOf(map.get(RedisSentinelProperties.SENTINEL_PREFIX + ".maxIdle")));
        }

        if (map.containsKey(RedisClusterProperties.CLUSTER_PREFIX + ".maxWait")){
            prop.setMaxWait(Long.valueOf(map.get(RedisClusterProperties.CLUSTER_PREFIX + ".maxWait")));
        }

        if (map.containsKey(RedisClusterProperties.CLUSTER_PREFIX + ".minIdle")){
            prop.setMinIdle(Integer.valueOf(map.get(RedisClusterProperties.CLUSTER_PREFIX + ".minIdle")));
        }

        if (map.containsKey(RedisClusterProperties.CLUSTER_PREFIX + ".password")){
            prop.setPassword(map.get(RedisClusterProperties.CLUSTER_PREFIX + ".password"));
        }

        if (map.containsKey(RedisClusterProperties.CLUSTER_PREFIX + ".soTimeout")){
            prop.setSoTimeout(Integer.valueOf(map.get(RedisClusterProperties.CLUSTER_PREFIX + ".soTimeout")));
        }

        if (map.containsKey(RedisClusterProperties.CLUSTER_PREFIX + ".maxRedirects")){
            prop.setMaxRedirects(Integer.valueOf(map.get(RedisClusterProperties.CLUSTER_PREFIX + ".maxRedirects")));
        }

        return new RedisClusterService(prop);
    }

    @Override
    protected List<Integer> getTargetsByTaskId(String taskId) {
        QueryWrapper<SyncTaskRuleRedisEntity> wrapper = new QueryWrapper();
        wrapper.eq("task_id",taskId);
        wrapper.eq("status", StatusEnum.valid.getStatus());

        List<SyncTaskRuleRedisEntity> syncTaskRuleRedisEntityList = redisTaskRuleService.list(wrapper);

        if (CollectionUtils.isEmpty(syncTaskRuleRedisEntityList)){
            return null;
        }

        List<Integer> targetIds = new ArrayList<>();

        for (SyncTaskRuleRedisEntity syncTaskRuleRedisEntity : syncTaskRuleRedisEntityList){
            if (!targetIds.contains(syncTaskRuleRedisEntity.getTargetId())){
                targetIds.add(syncTaskRuleRedisEntity.getTargetId());
                logger.info("任务id:{},需要同步到 目标源targetId:{}",taskId,syncTaskRuleRedisEntity.getTargetId());
            }
        }

        return targetIds;
    }

    @Override
    protected List<TargetConn> getTargetConnByTaskId(String taskId) {
        List<Integer> targetIds = getTargetsByTaskId(taskId);

        //没有找到 任务规则
        if (targetIds == null || targetIds.isEmpty()){
            return null;
        }

        List<TargetConn> targetCons = new ArrayList<>();

        for (Integer targetId : targetIds){
            QueryWrapper<SyncRedisTargetEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("target_id",targetId);

            SyncRedisTargetEntity syncRedisTargetEntity = (SyncRedisTargetEntity)redisTargetService.getOne(wrapper);

            if (syncRedisTargetEntity == null || StringUtils.isBlank(syncRedisTargetEntity.getProps())){
                logger.error("目标源targetId:{} 没有找到 链接字符串属性 props");
                continue;
            }

            RedisTargetConn targetConn = new RedisTargetConn();
            targetConn.setTargetId(targetId);
            targetConn.setProps(syncRedisTargetEntity.getProps());
            targetConn.setMode(syncRedisTargetEntity.getMode());

            targetCons.add(targetConn);
        }

        return targetCons;
    }
}
