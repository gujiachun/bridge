package com.rainbow.bridge.redistarget.client;

import java.util.List;
import java.util.Map;

/**
 * Redis服务操作上层接口，具体是单节点操作类还是集群操作类，根据配置文件进行自动注入
 * @author LIUYL
 *
 */
public interface RedisService  {
	

	/**
	 * 
	 * <p>Description: 调用key进行自增操作</p>
	 * @param key  redis中检索的key
	 * @return  key不存在，会自动创建，返回1 ，否则加1后返回
	 */
	public Long inCr(String key);
	

	/**
	 * 
	 * <p>Description: 以k-v形式保存</p>
	 * @param key redis的键
	 * @param value 需要保存的值
	 * @return 1-key不存在，保存成功 0-key存在，保存失败 -1-出现异常
	 */
	public Long setNx(String key, String value);
	
	
	/**
	 * <p>Description: 以k-v形式保存</p>
	 * @param key      键
	 * @param value    值
	 * @param expireTime  超时时间(秒)
	 * @return
	 */
	public String setNx(String key, String value, int expireTime);
	
	

	/**
	 * 设置key的值为value并设置过期时间
	 * @param key  
	 * @param value
	 * @param expireTime 单位秒
	 * @return
	 */
	public boolean set(String key, String value, int expireTime);
	
	
	/**
	 * 设置key的值为value，永久保存
	 * @param key
	 * @param value 
	 * @return
	 */
	public boolean set(String key, String value);
	
	/**
	 * 
	 * <p>Description: 将value设置到key中</p>
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public <T> boolean set(String key, T value);

	/**
	 * 
	 * <p>Description: 向指定key中的hash结构中存入键为field，值为value</p>
	 * @param key redis检索的key
	 * @param field 字段
	 * @param value 值
	 * @return true-成功 false-失败
	 */
	public boolean hset(String key, String field, String value);

	
	/**
	 * 
	 * <p>Description: 根据一个key存入一个hash结构的数据</p>
	 * @param key  redis检索的key
	 * @param hash map结构
	 * @return  true-保存成功 false-失败 
	 */
		public boolean hmset(String key, Map<String, String> hash);

	
	/**
	 * 
	 * <p>Description: 将value设置到key中</p>
	 * @param key 键
	 * @param value 值
	 * @param expireTime 过期时间（秒）
	 * @return
	 */
	public <T> boolean set(String key, T value, int expireTime);
	

	/**
	 * 
	 * <p>Description: 判断key是否存在</p>
	 * @param key 
	 * @return
	 */
	public boolean isExists(String key);
	
	

	/**
	 * 
	 * <p>Description: 判断指定key下面的hash结构中是否存键为field的条目</p>
	 * @param key redis检索的key
	 * @param field 字段
	 * @return true-存在 false-不存在
	 */
	public boolean existshKey(String key, String field);
	
	
	
	
	/**
	 * 根据key获取的value
	 * @param key
	 * @return
	 */
	public String get(String key);
	
	
	/**
	 * 根据key获取value并根据类休息完成反序列化返回
	 * @param key
	 * @param clz
	 * @return
	 */
	public <T> T get(String key, Class<T> clz);
	
	/**
	 * 
	 * <p>Description: 根据redis的key检索hash结构中键为field的值</p>
	 * @param key redis的key
	 * @param field 字段名
	 * @return key对应的hash结构中 field对应的值
	 */
	public String hget(String key, String field);
	
	

	/**
	 * 获取的key下面的hash结构
	 * @param key 
	 * @return
	 */
	public Map<String, String> hgetAll(String key);
	
	
	/**
	 * 
	 * <p>Title: 根据key获取value，通过类对象完成反序列化成列表</p> 
	 * <p>Description: </p> 
	 * @param key
	 * @param clz
	 * @return
	 */
	public <T> List<T> getList(String key, Class<T> clz);
	
	
	/**
	 * 
	 * <p>Description: 根据hash中key和field获取value,并指定value的类型，自动完成反序列化返回</p>
	 * @param key 键
	 * @param field 字段
	 * @param clazz value的类对象
	 * @return
	 */
	public <T> T hget(String key, String field, Class<T> clazz) ;
	
	

	/**
	 * 
	 * <p>Description: 根据key删除值</p>
	 * @param key
	 * @return
	 */
	public boolean delete(String key);
	
	
	/**
	 * 
	 * <p>Description: 删除key下面的hash结构中指定键的条目</p>
	 * @param key redis检索的key
	 * @param fields 需要删除的键
	 * @return true-成功 false-失败
	 */
	public boolean delhKeys(String key, String... fields);
	
	
	/**
	 * 
	 * <p>Description: 执行lua脚本</p>
	 * @param script 脚本内容
	 * @param keys redis的key参数列表
	 * @param args 其他的值参列表
	 * @return 执行完lua脚本的实际返回值
	 */
	public Object evalScript(String script, List<String> keys, List<String> args);
	
	/**
	 * 
	 * <p>Description: 尝试获取分布式锁</p>
	 * @param lockKey 锁
	 * @param requestId 请求标识
	 * @param expireTime 过期时间
	 * @return true-成功 false-失败
	 */
	public boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime);
	
	/**
	 * 
	 * <p> Description: 释放分布式锁</p>
	 * @param lockKey 锁
	 * @param requestId 请求标识
	 * @return true-成功 false-失败
	 */
	public boolean releaseDistributedLock(String lockKey, String requestId);
	

	/**
	 * 
	 * @Title 设置过期时间
	 * @Description  
	 * @param key
	 * @param seconds
	 * @return
	 */
	public boolean expire(String key, int seconds);
	
	
	
	/**
	 * <p>Description: 获取list长度</p>
	 * @param key  list key
	 * @return     list 长度
	 */
	public long lsize(String key);
	
	
	/**
	 * 在 list 指定位置 插入值，覆盖原有的值
	 * @param key      list key
	 * @param index    指定位置
	 */
	public String lset(String key, int index, String value);
	
	
	/**
	 * List头部追加记录
     * 如果list不存在，则创建list
	 * @return  list长度
	 */
	public long lpush(String key, String... value);
	
	
	/**
     * List尾部追加记录
     * 如果list不存在，则创建list
     * @return  list长度
     */
    public long rpush(String key, String... value);
    
    
    /**
     * 获取list指定位置的值
     * @param index 指定位置
     */
    public String lindex(String key, int index);
    
    
    /**
     * 获取指定范围的记录
     * 0:第一个记录, -1:最后一个记录, -2:倒数第二条记录 ...
     * @param start 起始位置 
     * @param end   结束位置
     */
    public List<String> lrange(String key, int start, int end);
    
    
    /**
     * 获取list列表数据
     * @param key
     * @return
     */
    public List<String> lrangeAll(String key);
    
    
    /**
     * 移除并返回列表 key 的 第一个值
     * 当key不存在时，返回null
     */
    public String lpop(String key);
    
    
    /**
     * 移除并返回列表 key 的 最后一个值
     * 当key不存在时，返回null
     */
    public String rpop(String key);
    
    
    /**
     * 删除list数据，保留指定范围数据，start和end为0时，即清空list
     * @param start 起始位置 
     * @param end   结束位置
     */
    public String ltrim(String key, int start, int end);
    
    
    /**
     * 清空list数据
     */
    public String ltrimAll(String key);
	
}