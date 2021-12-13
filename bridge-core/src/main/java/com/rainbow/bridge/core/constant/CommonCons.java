package com.rainbow.bridge.core.constant;

/**
 * 常用 常量
 * @author gujiachun
 */
public class CommonCons {

    public static final String rocketmq = "rocketmq";

    public static final String kafka = "kafka";

    public static final Integer enable = 1;

    public static final Integer disable = 0;

    /**
     * 分隔符
     */
    public final static String split = "#@#";

    public final static String DATA_FORMAT = "YYYY-MM-dd'T'HH:mm:ss.SSSZ";

    // ES字段自定义类型枚举
    public final static class EsFieldType{

        public final static String INT = "int";

        public final static String DATE = "date";

        public final static String STRING = "string";

        public final static String ARRAY = "array";

        public final static String JSON = "json";

        public final static String DECIMAL = "decimal";

        //删除策略: 0:根据index和id模板删除索引，1：执行sql模板，更新索引
        public final static Integer delete_strategy_0 = 0;
        //删除策略: 0:根据index和id模板删除索引，1：执行sql模板，更新索引
        public final static Integer delete_strategy_1 = 1;

        //父文档
        public final static Integer relation_parent = 1;

        //子文档
        public final static Integer relation_child = 2;
    }

    // ES字段自定义类型枚举
    public final static class EsIndexType{

        /** 索引部分 更新 */
        public final static Integer part_type = 1;

        public final static String type = "type";

        public final static String type_array = "array";

        public final static String data = "data";

    }
}
