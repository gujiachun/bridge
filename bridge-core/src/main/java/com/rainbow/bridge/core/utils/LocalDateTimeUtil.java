package com.rainbow.bridge.core.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author gujiachun
 */
public class LocalDateTimeUtil {

    /**
     * 获取前一天时间
     *
     * @param timeFormat 时间格式
     * @return 昨天时间
     */
    public static String getYesterdayByFormat(String timeFormat) {
        return LocalDateTime.now(). plusDays(-1).format(DateTimeFormatter.ofPattern(timeFormat));
    }
}
