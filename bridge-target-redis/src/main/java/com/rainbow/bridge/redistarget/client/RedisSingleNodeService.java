package com.rainbow.bridge.redistarget.client;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.rainbow.bridge.redistarget.client.prop.RedisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * <p>ClassName: RedisSingleNodeService</p>
 * <p>Description: Redis单节点操作类</p>
 */
public class RedisSingleNodeService implements RedisService {

    public static final Logger logger = LoggerFactory.getLogger(RedisSingleNodeService.class);

    JedisPool jedisPool = null;

    public RedisSingleNodeService(RedisProperties redisProperties) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(redisProperties.getMaxActive());
        config.setMaxIdle(redisProperties.getMaxIdle());
        config.setMaxWaitMillis(redisProperties.getMaxWait());
        config.setMinIdle(redisProperties.getMinIdle());
        config.setTestOnBorrow(false);

        jedisPool = new JedisPool(config, redisProperties.getHost(), redisProperties.getPort(),
                redisProperties.getConnectTimeout(), redisProperties.getSoTimeout(), redisProperties.getPassword(), redisProperties.getDatabase(),
                null, false, null,
                null, null);


    }

    /**
     * <p>Description: 调用key进行自增操作</p>
     *
     * @param key redis中检索的key
     * @return key不存在，会自动创建，返回1 ，否则加1后返回
     */
    @Override
    public Long inCr(String key) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.incr(key);
        } catch (Exception e) {
            if ("ERR increment or decrement would overflow".equals(e.getMessage())) {
                logger.error("执行inCr方法，key={}，已经到达了最大数据范围 {}", key, Long.MAX_VALUE);
            }
            logger.error("执行inCr方法，key={}，出现异常", key, e);
            return 0L;
        } finally {
            if (jedis != null) {
                jedis.close();
            }

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
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return jedis.eval(script, keys, args);
        } catch (Exception e) {
            logger.error("执行evalScript方法，keys={},args={},出现异常", JSON.toJSONString(keys), JSON.toJSONString(args), e);
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
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

        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return jedis.setnx(key, value);
        } catch (Exception e) {
            logger.error("执行setNx方法，key={}，出现异常", key, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return -1L;
    }


    /**
     * <p>Description: 以k-v形式保存</p>
     *
     * @param key     键
     * @param value   值
     * @param expireTime 超时时间(秒)
     * @return
     */
    @Override
    public String setNx(String key, String value, int expireTime) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.set(key, value, new SetParams().nx().ex(expireTime));
        } catch (Exception e) {
            logger.error("执行带超时时间的setNx方法，key={}，出现异常", key, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
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

        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.hmset(key, hash);
        } catch (Exception e) {
            logger.error("执行hmset保存key={}，hash={}，出现异常", key, JSON.toJSONString(hash), e);
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return true;
    }

    @Override
    public Map<String, String> hgetAll(String key) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.hgetAll(key);
        } catch (Exception e) {
            logger.error("执行hgetAll方法，key={}，出现异常", key,e);
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
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

        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.hset(key, field, value);
        } catch (Exception e) {
            logger.error("执行hset方法key={} filed={}出现异常 ", key, field, e);
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
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
        Jedis jedis = null;
        String result = null;
        try {
            jedis = jedisPool.getResource();
            result = jedis.hget(key, field);
        } catch (Exception e) {
            logger.error("执行hget方法，key={},filed={}出现异常", key, field, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;


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
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return jedis.hexists(key, field);
        } catch (Exception e) {
            logger.error("执行existshKey方法，key={},filed={} 出现异常", key, field, e);
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
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

        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.hdel(key, fields);
        } catch (Exception e) {
            logger.error("执行delhKeys方法，key={}，fields={} 出现异常", key, fields, e);
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return true;

    }

    @Override
    public boolean set(String key, String value, int seconds) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.setex(key, seconds, value);
        } catch (Exception e) {
            logger.error("执行带超时set方法 key={} ,value={} 出现异常", key, value, e);
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return true;

    }

    /**
     * <p>Description: 根据key删除值</p>
     *
     * @param key
     * @return
     */
    @Override
    public boolean delete(String key) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
        } catch (Exception e) {
            logger.error("执行del方法 key={} 出现异常", key, e);
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return true;
    }

    /**
     * <p>Description: 判断是否存在</p>
     *
     * @param key
     * @return
     */
    @Override
    public boolean isExists(String key) {
        Jedis jedis = null;
        boolean exists = false;
        try {
            jedis = jedisPool.getResource();
            exists = jedis.exists(key);
        } catch (Exception e) {
            logger.error("执行exists方法 key={} 出现异常", key, e);
            return exists;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return exists;
    }

    @Override
    public boolean set(String key, String value) {
        Jedis jedis = null;
        boolean exists = false;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            return true;
        } catch (Exception e) {
            logger.error("执行set方法 key={} 出现异常", key, e);
            return exists;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("执行get方法 key={} 出现异常", key, e);
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public <T> boolean set(String key, T value, int expireTime) {
        String jsonData = JSON.toJSONString(value);
        if (jsonData != null) {
            return set(key, jsonData, expireTime);
        }
        return false;
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {

        Jedis jedis = null;
        String jsonData = null;
        try {
            jedis = jedisPool.getResource();
            jsonData = jedis.get(key);
        } catch (Exception e) {
            logger.error("执行get方法 key={} 出现异常", key, e);

        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return jsonData != null ?
                JSON.parseObject(jsonData, clazz) : null;
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
    public <T> T hget(String key, String feild, Class<T> clazz) {
        String jsonData = hget(key, feild);
        if (jsonData != null) {
            return JSON.parseObject(jsonData, clazz);
        }

        return null;
    }

    @Override
    public boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime) {
        Jedis jedis = null;
        String result;
        try {
            jedis = jedisPool.getResource();
            result = jedis.set(lockKey, requestId,SetParams.setParams().nx().ex(expireTime));
        } catch (Exception e) {
            logger.error("执行tryGetDistributedLock方法,lockKey={},requestId={} 出现异常", lockKey, requestId, e);
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return "OK".equals(result);
    }

    @Override
    public boolean releaseDistributedLock(String lockKey, String requestId) {

        List<String> keys = new ArrayList<String>();
        keys.add(lockKey);
        List<String> args = new ArrayList<String>();
        args.add(requestId);
        String script = "\r\n"
                + "local lockKey   = KEYS[1]\r\n"
                + "local requestId  = ARGV[1]\r\n" + "\r\n"
                + "  local value= redis.call('get', lockKey)\r\n"
                + "\r\n"
                + "  if(value==requestId) \r\n"
                + "	  then \r\n"
                + "		return redis.call('del', lockKey)\r\n"
                + "	  else\r\n"
                + "		return 0\r\n" + "end";

        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return (Long) jedis.eval(script, keys, args) == 1L ? true : false;
        } catch (Exception e) {
            logger.error("执行releaseDistributedLock方法 lockKey={},requestId={} 出现异常", lockKey, requestId, e);
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public <T> boolean set(String key, T value) {
        Jedis jedis = null;
        String json = JSON.toJSONString(value);
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, json);
        } catch (Exception e) {
            logger.error("执行set方法 key={} ,value={} 出现异常", key, json, e);
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return true;
    }

    @Override
    public boolean expire(String key, int seconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.expire(key, seconds);
        } catch (Exception e) {
            logger.error("执行expire方法 key={} ,seconds={} 出现异常", key, seconds, e);
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return true;
    }


    /**
     * <p>Description: 获取list长度</p>
     *
     * @param key list key
     * @return list 长度
     */
    @Override
    public long lsize(String key) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return jedis.llen(key);
        } catch (Exception e) {
            logger.error("执行llen方法 key={}，出现异常", key, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return -1L;
    }


    /**
     * 在 list 指定位置 插入值，覆盖原有的值
     *
     * @param key   list key
     * @param index 指定位置
     */
    @Override
    public String lset(String key, int index, String value) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return jedis.lset(key, index, value);
        } catch (Exception e) {
            logger.error("执行lset方法 key={}, index={}, value={}，出现异常", key, index, value, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }


    /**
     * List头部追加记录
     * 如果list不存在，则创建list
     *
     * @return list长度
     */
    @Override
    public long lpush(String key, String... value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("执行lpush方法 key={}, value={}，出现异常", key, value, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return -1;
    }


    /**
     * List尾部追加记录
     * 如果list不存在，则创建list
     *
     * @return list长度
     */
    @Override
    public long rpush(String key, String... value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.rpush(key, value);
        } catch (Exception e) {
            logger.error("执行rpush方法 key={}, value={}，出现异常", key, value, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return -1;
    }


    /**
     * 获取list指定位置的值
     *
     * @param index 指定位置
     */
    @Override
    public String lindex(String key, int index) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return jedis.lindex(key, index);
        } catch (Exception e) {
            logger.error("执行lindex方法 key={}, index={}，出现异常", key, index, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }


    /**
     * 获取指定范围的记录
     * 0:第一个记录, -1:最后一个记录, -2:倒数第二条记录 ...
     *
     * @param start 起始位置
     * @param end   结束位置
     */
    @Override
    public List<String> lrange(String key, int start, int end) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            logger.error("执行lrange方法 key={}, start={}, end={}，出现异常", key, start, end, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }


    /**
     * 获取list列表数据
     *
     * @param key
     * @return
     */
    @Override
    public List<String> lrangeAll(String key) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            return jedis.lrange(key, 0, -1);
        } catch (Exception e) {
            logger.error("执行lrange方法 key={}, start=0, end=-1，出现异常", key, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }


    /**
     * 移除并返回列表 key 的 第一个值
     * 当key不存在时，返回null
     */
    @Override
    public String lpop(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.lpop(key);
        } catch (Exception e) {
            logger.error("执行lpop方法 key={}，出现异常", key, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }


    /**
     * 移除并返回列表 key 的 最后一个值
     * 当key不存在时，返回null
     */
    @Override
    public String rpop(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.rpop(key);
        } catch (Exception e) {
            logger.error("执行rpop方法 key={}，出现异常", key, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 删除list数据，保留指定范围数据，start和end为0时，即清空list
     *
     * @param start 起始位置
     * @param end   结束位置
     */
    @Override
    public String ltrim(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.ltrim(key, start, end);
        } catch (Exception e) {
            logger.error("执行ltrim方法 key={}, start={}, end={}，出现异常", key, start, end, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }


    /**
     * 清空list数据
     */
    @Override
    public String ltrimAll(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.ltrim(key, 0, 0);
        } catch (Exception e) {
            logger.error("执行ltrim方法 key={}, start=0, end=0，出现异常", key, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

}