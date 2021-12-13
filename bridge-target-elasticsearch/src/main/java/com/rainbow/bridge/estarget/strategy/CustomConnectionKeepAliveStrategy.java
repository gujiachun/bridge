package com.rainbow.bridge.estarget.strategy;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.protocol.HttpContext;

import java.util.concurrent.TimeUnit;


/**
 * @author gujiachun
 */
public class CustomConnectionKeepAliveStrategy extends DefaultConnectionKeepAliveStrategy {
    public static CustomConnectionKeepAliveStrategy INSTANCE;

    private static long maxKeepAliveMinutes;

    // 带参数构造器
    private CustomConnectionKeepAliveStrategy(long maxKeepAliveMinutes) {
        super();
        this.maxKeepAliveMinutes = maxKeepAliveMinutes;
    }

    // 为了传参数进来写了个单例
    public static CustomConnectionKeepAliveStrategy getInstance(long maxKeepAliveMinutes) {
        if (INSTANCE == null) {
            synchronized (CustomConnectionKeepAliveStrategy.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CustomConnectionKeepAliveStrategy(maxKeepAliveMinutes);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 最大keep alive的时间（分钟）
     * 这里默认为10分钟，可以根据实际情况设置。
     * 可以观察客户端机器状态为TIME_WAIT的TCP连接数，如果太多，可以增大此值。
     */
    @Override
    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
        long keepAliveDuration = super.getKeepAliveDuration(response, context);
        // <0 为无限期keepalive
        // 将无限期替换成一个默认的时间
        if (keepAliveDuration < 0) {
            return TimeUnit.MINUTES.toMillis(maxKeepAliveMinutes);
        }
        return keepAliveDuration;
    }
}