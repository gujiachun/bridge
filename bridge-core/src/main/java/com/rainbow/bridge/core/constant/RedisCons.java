package com.rainbow.bridge.core.constant;

/**
 * redis常量
 * @author gujiachun
 */
public class RedisCons {

    public static final String set = "set";

    public static final String hset = "hset";

    public static final String hmset = "hmset";

    public static final String incr = "incr";

    public static final String delhKeys = "delhKeys";

    public static final String delete = "delete";

    /** 单机 */
    public static final Integer single = 0;

    /** 哨兵 */
    public static final Integer sentinel = 1;

    /** 集群 */
    public static final Integer cluster = 2;
}
