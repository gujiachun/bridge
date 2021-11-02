package com.rainbow.bridge.server.redis.prop;

/**
 * @author gujiachun
 * @date 2020-07-14 11:09:13
 */
public class RedisProperties {

    public static final String REDIS_PREFIX = "redis.single";

    /**
     * 连接的主机地址
     */
    private String host;

    /**
     * 连接的端口号 (默认6379)
     */
    private int port = 6379;

    /**
     * 操作的database下标，默认是0
     */
    private int database=0;

    /**
     * 连接的超时时间（毫秒）
     */
    private int connectTimeout=1000;

    /**
     * 请求的响应超时时间（3000）毫秒
     */
    private int soTimeout=3000;

    /**
     * 创建连接的密码
     */
    private String password;
    /**
     * 资源池中的最大连接数（默认20）
     */
    private int maxActive = 20;

    /**
     * 资源池允许的最大空闲连接数（默认10）
     */
    private int maxIdle = 10;

    /**
     * 资源池确保的最少空闲连接数（默认5）
     */
    private int minIdle = 5;

    /**
     * 当资源池连接用尽后，调用者的最大等待时间（单位为毫秒）默认1000
     */
    private long maxWait=1000;



    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
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

    public void setPort(int port) {
        this.port = port;
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


}
