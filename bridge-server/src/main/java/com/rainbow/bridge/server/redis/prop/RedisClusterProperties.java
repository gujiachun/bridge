package com.rainbow.bridge.server.redis.prop;

/**
 * @date 2020-07-16 11:24:10
 */
public class RedisClusterProperties {

    public static final String CLUSTER_PREFIX = "redis.cluster";

    /**
     * 连接的多个节点，用ip1:port1,ip2:port2
     */
    private String nodes;

    /**
     * 操作的database下标，默认是0
     */
    private int database=0;


    /**
     * 连接的超时时间（毫秒）
     */
    private int connectTimeout=1000;

    /**
     * 请求的响应超时时间（3000）
     */
    private int soTimeout=3000;

    /**
     * 创建连接的密码
     */
    private String password;


    /**
     * 资源池中的最大连接数（默认值：10）
     */
    private int maxActive=10;

    /**
     * 资源池允许的最大空闲连接数（默认值：10）
     */
    private int maxIdle=10;

    /**
     * 资源池确保的最少空闲连接数（默认值：1）
     */
    private int minIdle=1;

    /**
     * 当资源池连接用尽后，调用者的最大等待时间（单位为毫秒）（默认值：10000）
     */
    private long maxWait=10000;

    /**
     * 请求最大跳转次数（默认值：3）
     */
    private int maxRedirects=3;

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public int getMaxRedirects() {
        return maxRedirects;
    }

    public void setMaxRedirects(int maxRedirects) {
        this.maxRedirects = maxRedirects;
    }
}
