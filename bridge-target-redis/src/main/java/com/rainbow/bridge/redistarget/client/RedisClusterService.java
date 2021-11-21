package com.rainbow.bridge.redistarget.client;

/**
 * @author yunlong.liu
 * @date 2020-07-16 11:22:27
 */

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.rainbow.bridge.redistarget.client.prop.RedisClusterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import redis.clients.jedis.*;
import redis.clients.jedis.params.SetParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis集群操作服务类
 *
 * @author LIUYL
 */
public class RedisClusterService implements RedisService {


    public static final Logger logger = LoggerFactory.getLogger(RedisClusterService.class);


    /**
     * 给批量执行的key打标签，
     * 防止集群设置多个key时，因为key的slots分布不同，造成无法设置的问题
     */
    public static final String KEY_TAG = "{CLUSTER_TAG}";


    private JedisCluster jedisCluster;

    /**
     * <p>Description: 初始化方法，当依赖的属性都注入完成后，开始调用</p>
     */

    public RedisClusterService(RedisClusterProperties properties) {

        logger.info("nodes={}", properties.getNodes());
        // ## 注册redis连接节点
        Set<HostAndPort> clusterNodes = Sets.newHashSet();
        if (!StringUtils.isEmpty(properties.getNodes())) {
            // 以“；”分割成"ip:post"
            String[] ipAndPostes = properties.getNodes().split(",");
            // 以“：”分割成 “ip”，“post”
            if (ipAndPostes != null) {
                for (String ipAndPost : ipAndPostes) {
                    //ipAndPost 值：(ip：端口)
                    String[] ipAndPostArray = ipAndPost.split(":");
                    clusterNodes.add(new HostAndPort(ipAndPostArray[0], Integer.parseInt(ipAndPostArray[1])));
                }
            }
        }

        logger.info("redis链接节点个数(clusterNodes)：{}", clusterNodes.size());

        checkHostAndPost(clusterNodes);

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMinIdle(properties.getMinIdle());
        jedisPoolConfig.setMaxTotal(properties.getMaxActive());
        jedisPoolConfig.setMaxIdle(properties.getMaxIdle());
        jedisPoolConfig.setMaxWaitMillis(properties.getMaxWait());

        jedisCluster = new JedisCluster(clusterNodes, properties.getConnectTimeout(), properties.getSoTimeout(), properties.getMaxRedirects(), properties.getPassword(), jedisPoolConfig);

    }

    /**
     * 检查节点的ip和端口是否开通
     */
    public void checkHostAndPost(Set<HostAndPort> clusterNodes) {
        // Set<HostAndPort> clusterNodes
        for (HostAndPort hostAndPort : clusterNodes) {
            try (Connection connection = new Connection()) {
                connection.setPort(hostAndPort.getPort());
                connection.setHost(hostAndPort.getHost());
                connection.connect();
            } catch (Exception e) {
                throw new RuntimeException("redis cluster[" + hostAndPort.getHost() + ":" + hostAndPort.getPort()
                        + "],Exception is " + e.getMessage());
            }
        }

    }

    /**
     * <p>Description: 调用key进行自增操作</p>
     *
     * @param key redis中检索的key
     * @return key不存在，会自动创建，返回1 ，否则加1后返回
     */
    @Override
    public Long inCr(String key) {

        try {
            return jedisCluster.incr(key);
        } catch (Exception e) {
            if ("ERR increment or decrement would overflow".equals(e.getMessage())) {
                logger.error("执行incr方法key={},已经到达了最大数据范围 {}", key, Long.MAX_VALUE);
            }
            logger.error("执行inCr方法key={},出现异常", key, e);
            return 0L;
        }
    }

    /**
     * <p>Description: 执行lua脚本</p>
     *
     * @param script 脚本内容
     * @param keys   redis的key参数列表
     * @param args   其他的值参列表
     * @return 执行完lua脚本的实际返回值
     */
    @Override
    public Object evalScript(String script, List<String> keys, List<String> args) {

        if (keys != null) {
            int keySize = keys.size();

            if (keySize > 1) {
                for (int i = 0; i < keySize; i++) {
                    keys.set(i, KEY_TAG + keys.get(i));
                }
            }

        }

        try {
            return jedisCluster.eval(script, keys, args);
        } catch (Exception e) {
            logger.error("执行evalScript方法keys={},args={},出现异常", JSON.toJSONString(keys), JSON.toJSONString(args), e);
            return null;
        }
    }

    /**
     * <p>Description: 以k-v形式保存</p>
     *
     * @param key   redis的键
     * @param value 需要保存的值
     * @return 1-key不存在，保存成功 0-key存在，保存失败 -1-出现异常
     */
    @Override
    public Long setNx(String key, String value) {
        try {
            return jedisCluster.setnx(key, value);
        } catch (Exception e) {
            logger.error("执行setNx方法，保存key={}，出现异常", key, e);
        }
        return -1L;
    }

    @Override
    public String setNx(String key, String value, int expireTime) {
        SetParams setParams=new SetParams().nx().ex(expireTime);
        return jedisCluster.set(key, value, setParams);

    }

    /**
     * <p>Description: 根据一个key存入一个hash结构的数据</p>
     *
     * @param key  redis检索的key
     * @param hash map结构
     * @return true-保存成功 false-失败
     */
    @Override
    public boolean hmset(String key, Map<String, String> hash) {
        try {
            jedisCluster.hmset(key, hash);
        } catch (Exception e) {
            logger.error("执行hmset方法，key={}，hash={}，出现异常", key, JSON.toJSONString(hash), e);
            return false;
        }
        return true;
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        try {
            return jedisCluster.hgetAll(key);
        } catch (Exception e) {
            logger.error("执行hgetAll方法， key={}，出现异常", key);
            return null;
        }
    }

    /**
     * <p>Description: 向指定key中的hash结构中存入键为field，值为value</p>
     *
     * @param key   redis检索的key
     * @param field 字段
     * @param value 值
     * @return true-成功 false-失败
     */
    @Override
    public boolean hset(String key, String field, String value) {
        try {
            jedisCluster.hset(key, field, value);
        } catch (Exception e) {
            logger.error("执行hset方法，key={},filed={},value={}，出现异常", key, field,value, e);
            return false;
        }
        return true;
    }

    /**
     * <p>Description: 根据redis的key检索hash结构中键为field的值</p>
     *
     * @param key   redis的key
     * @param field 字段名
     * @return key对应的hash结构中 field对应的值
     */
    @Override
    public String hget(String key, String field) {
        try {
            return jedisCluster.hget(key, field);
        } catch (Exception e) {
            logger.error("执行hget方法，key={},filed={} ，出现异常", key, field, e);
            return null;
        }

    }

    /**
     * <p>Description: 判断指定key下面的hash结构中是否存键为field的条目</p>
     *
     * @param key   redis检索的key
     * @param field 字段
     * @return true-存在 false-不存在
     */
    @Override
    public boolean existshKey(String key, String field) {
        try {
            return jedisCluster.hexists(key, field);
        } catch (Exception e) {
            logger.error("执行existshKey方法，key={} filed={} 出现异常", key, field, e);
            return false;
        }
    }

    /**
     * <p>Description: 删除key下面的hash结构中指定键的条目</p>
     *
     * @param key    redis检索的key
     * @param fields 需要删除的键
     * @return true-成功 false-失败
     */
    @Override
    public boolean delhKeys(String key, String... fields) {
        try {
            jedisCluster.hdel(key, fields);
        } catch (Exception e) {
            logger.error("执行delhKeys方法，key={}，fields={}，出现异常", key, fields, e);
            return false;
        }
        return true;

    }

    @Override
    public boolean set(String key, String value, int seconds) {
        try {
            jedisCluster.setex(key,seconds,value);
        } catch (Exception e) {
            logger.error("执行set方法 key={},value={},expireTime={} 出现异常", key, value, seconds, e);
            return false;
        }
        return true;

    }

    /**
     * <p>Description: 根据key获取值</p>
     *
     * @param key
     * @return
     */
    @Override
    public String get(String key) {
        try {
            return jedisCluster.get(key);
        } catch (Exception e) {
            logger.error("执行get方法 key={} 出现异常", key, e);
            return null;
        }
    }

    /**
     * <p>Description: 判断是否存在</p>
     *
     * @param key
     * @return
     */
    @Override
    public boolean isExists(String key) {

        try {
            return jedisCluster.exists(key);
        } catch (Exception e) {
            logger.error("执行isExists方法 key={} 出现异常", key, e);
            return false;
        }
    }

    @Override
    public boolean delete(String key) {
        try {
            jedisCluster.del(key);
            return true;
        } catch (Exception e) {
            logger.error("执行delete方法，删除key={}出现异常", key, e);
            return false;
        }
    }

    @Override
    public boolean set(String key, String value) {
        try {
            jedisCluster.set(key, value);
            return true;
        } catch (Exception e) {
            logger.error("执行set方法，key={},value={}出现异常", key, value, e);
            return false;
        }
    }

    @Override
    public <T> boolean set(String key, T value, int expireTime) {

        String jsonData =  JSON.toJSONString(value);
        if (jsonData != null) {
            return set(key, jsonData, expireTime);
        }
        return false;
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        String jsonData = get(key);
        if (jsonData != null) {
            return JSON.parseObject(jsonData, clazz);
        }
        return null;
    }

    @Override
    public <T> List<T> getList(String key, Class<T> clazz) {
        String jsonData = get(key);
        if (jsonData != null) {
            return JSON.parseArray(jsonData,clazz);
        }
        return Lists.newArrayList();
    }

    @Override
    public <T> T hget(String key, String field, Class<T> clazz) {
        String jsonData = hget(key, field);
        if (jsonData != null) {
            return JSON.parseObject(jsonData, clazz);
        }
        return null;
    }

    @Override
    public boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime) {

        try {
            jedisCluster.set(lockKey, requestId, new SetParams().nx().ex(expireTime));
        } catch (Exception e) {
            logger.error("执行tryGetDistributedLock方法,lockKey={},requestId={} 出现异常", lockKey, requestId, e);
            return false;
        }
        return true;
    }

    @Override
    public boolean releaseDistributedLock(String lockKey, String requestId) {
        List<String> keys = new ArrayList<>();
        keys.add(lockKey);
        List<String> args = new ArrayList<>();
        args.add(requestId);
        String script = "\r\n" + "local lockKey   = KEYS[1]\r\n" + "local requestId  = ARGV[1]\r\n" + "\r\n"
                + "  local value= redis.call('get', lockKey)\r\n" + "\r\n" + "  if(value==requestId) \r\n"
                + "	  then \r\n" + "		return redis.call('del', lockKey)\r\n" + "	  else\r\n"
                + "		return 0\r\n" + "end";

        try {

            return (long) jedisCluster.eval(script, keys, args) == 1L ? true : false;
        } catch (Exception e) {
            logger.error("执行releaseDistributedLock方法,lockKey={},requestId={} 出现异常", lockKey, requestId, e);
            return false;
        }
    }

    @Override
    public <T> boolean set(String key, T value) {
        String jsonData= JSON.toJSONString(value);
        try {
            jedisCluster.set(key,jsonData);
        } catch (Exception e) {
            logger.error("执行set方法 key={},value={} 出现异常", key, jsonData, e);
            return false;
        }
        return true;
    }

    @Override
    public boolean expire(String key, int seconds) {
        try {
            jedisCluster.expire(key, seconds);
        } catch (Exception e) {
            logger.error("执行expire方法 key={},value={} 出现异常", key, seconds, e);
            return false;
        }
        return true;
    }

    @Override
    public long lsize(String key) {
        try {
            return jedisCluster.llen(key);
        } catch (Exception e) {
            logger.error("执行llen方法 key={}，出现异常", key, e);
        }
        return -1L;
    }

    @Override
    public String lset(String key, int index, String value) {
        try {
            return jedisCluster.lset(key, index, value);
        } catch (Exception e) {
            logger.error("执行lset方法 key={}, index={}, value={}，出现异常", key, index, value, e);
            return null;
        }
    }

    @Override
    public long lpush(String key, String... value) {
        try {
            return jedisCluster.lpush(key, value);
        } catch (Exception e) {
            logger.error("执行lpush方法 key={}, value={}，出现异常", key, value, e);
        }
        return -1L;
    }

    @Override
    public long rpush(String key, String... value) {
        try {
            return jedisCluster.rpush(key, value);
        } catch (Exception e) {
            logger.error("执行rpush方法 key={}, value={}，出现异常", key, value, e);
        }
        return -1;
    }

    @Override
    public String lindex(String key, int index) {

        try {
            return jedisCluster.lindex(key, index);
        } catch (Exception e) {
            logger.error("执行lindex方法 key={}, index={}，出现异常", key, index, e);
        }
        return null;
    }

    @Override
    public List<String> lrange(String key, int start, int end) {

        try {
            return jedisCluster.lrange(key, start, end);
        } catch (Exception e) {
            logger.error("执行lrange方法 key={}, start={}, end={}，出现异常", key, start, end, e);
        }
        return null;
    }

    @Override
    public List<String> lrangeAll(String key) {

        try {
            return jedisCluster.lrange(key, 0, -1);
        } catch (Exception e) {
            logger.error("执行lrange方法 key={}, start=0, end=-1，出现异常", key, e);
        }

        return null;
    }

    @Override
    public String lpop(String key) {
        try {
            return jedisCluster.lpop(key);
        } catch (Exception e) {
            logger.error("执行lpop方法 key={}，出现异常", key, e);
        }
        return null;
    }

    @Override
    public String rpop(String key) {
        Jedis jedis = null;
        try {
            return jedisCluster.rpop(key);
        } catch (Exception e) {
            logger.error("执行rpop方法 key={}，出现异常", key, e);
        }
        return null;
    }

    @Override
    public String ltrim(String key, int start, int end) {
        try {
            return jedisCluster.ltrim(key, start, end);
        } catch (Exception e) {
            logger.error("执行ltrim方法 key={}, start={}, end={}，出现异常", key, start, end, e);
        }
        return null;
    }

    @Override
    public String ltrimAll(String key) {
        try {
            return jedisCluster.ltrim(key, 0, 0);
        } catch (Exception e) {
            logger.error("执行ltrim方法 key={}, start=0, end=0，出现异常", key, e);
        }
        return null;
    }

}
