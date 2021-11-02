package com.rainbow.bridge.server.factory;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rainbow.bridge.biz.entity.SyncMysqlTargetEntity;
import com.rainbow.bridge.biz.entity.SyncRedisTargetEntity;
import com.rainbow.bridge.biz.entity.SyncTaskRuleMysqlEntity;
import com.rainbow.bridge.biz.entity.SyncTaskRuleRedisEntity;
import com.rainbow.bridge.biz.service.*;
import com.rainbow.bridge.core.enums.StatusEnum;
import com.rainbow.bridge.core.utils.PropertiesUtil;
import com.rainbow.bridge.server.redis.RedisClusterService;
import com.rainbow.bridge.server.redis.RedisSentinelService;
import com.rainbow.bridge.server.redis.RedisService;
import com.rainbow.bridge.server.redis.RedisSingleNodeService;
import com.rainbow.bridge.server.redis.prop.RedisClusterProperties;
import com.rainbow.bridge.server.redis.prop.RedisProperties;
import com.rainbow.bridge.server.redis.prop.RedisSentinelProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gujiachun
 */
@Component
public class RedisFactory {

    private static final Logger logger = LoggerFactory.getLogger(RedisFactory.class);

    private Map<Integer, RedisService> redisServiceMap;

    public RedisFactory() {
        redisServiceMap = new ConcurrentHashMap<>(10);
    }

    @Autowired
    private RedisTargetService redisTargetService;

    @Autowired
    private TaskRedisRuleService taskRedisRuleService;

    @PreDestroy
    public void destroy(){
        redisServiceMap.clear();
    }

    public void build(List<SyncTaskRuleRedisEntity> syncTaskRuleRedisEntityList){
        for (SyncTaskRuleRedisEntity syncTaskRuleRedisEntity : syncTaskRuleRedisEntityList){
            SyncRedisTargetEntity syncRedisTargetEntity = redisTargetService.getSyncRedisTargetByTargetId(syncTaskRuleRedisEntity.getTargetId());
            initTarget(syncRedisTargetEntity.getTargetId(),syncRedisTargetEntity.getMode(),syncRedisTargetEntity.getProps());
        }
    }

    /**
     * 初始化连接对象
     */
    private void initTarget(Integer targetId,Integer mode,String props) {
        if (redisServiceMap.containsKey(targetId)){
            return;
        }
        logger.info("====================================================================================================");
        logger.info("********************redis目标client id:{},初始化中···********************",targetId);
        logger.info("********************初始redis目标client id:{},props: {}  ****************",targetId,props);
        Map<String, String> map = PropertiesUtil.stringToMap(props);
        try {
            //单机
            if (mode == 0){
                redisServiceMap.put(targetId, createSingle(map));
            }else if (mode == 1){
                redisServiceMap.put(targetId, createSentinel(map));
            }else if (mode == 2){
                redisServiceMap.put(targetId, createCluster(map));
            }
            logger.info("初始redis目标client成功,目标数据源:{}",targetId);
        } catch (Exception e) {
            logger.error("初始redis目标client发生异常，目标数据源:{},异常:{}",targetId,e.getMessage());
        }
        logger.info("====================================================================================================\n");
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

    public RedisService getRedisService(Integer targetId){
        return redisServiceMap.get(targetId);
    }

    public void removeRedisService(String taskId){
        QueryWrapper<SyncTaskRuleRedisEntity> wrapper = new QueryWrapper();
        wrapper.eq("task_id",taskId);
        wrapper.eq("status", StatusEnum.valid.getStatus());

        List<SyncTaskRuleRedisEntity> syncTaskRuleRedisEntityList = taskRedisRuleService.list(wrapper);

        for (SyncTaskRuleRedisEntity syncTaskRuleRedisEntity : syncTaskRuleRedisEntityList){
            if (redisServiceMap.containsKey(syncTaskRuleRedisEntity.getTargetId())){
                RedisService redisService = redisServiceMap.get(syncTaskRuleRedisEntity.getTargetId());
                logger.info("释放redis client targetId:{}",syncTaskRuleRedisEntity.getTargetId());
                redisService = null;
                redisServiceMap.remove(syncTaskRuleRedisEntity.getTargetId());
            }
        }
    }
}
