package com.rainbow.bridge.core.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * RMB转换
 *
 * @author pengke
 * @date 2020年4月20日17:04:13
 */
public class RMBFormatUtil {
    private static final BigDecimal DIVIDENUM = new BigDecimal(100);

    private static final String DEFAULT_VALUE = "0";

    /**
     * 转换成元
     *
     * @param num
     * @param point
     * @return
     */
    public static BigDecimal convertTenYuan(BigDecimal num, int point) {
        BigDecimal decimal = new BigDecimal(0);
        if (num != null) {
            decimal = num.divide(new BigDecimal(100), point, RoundingMode.CEILING);
        }
        return decimal;
    }

    /**
     * 将分转换成元
     *
     * @param num
     * @param point
     * @return
     */
    public static BigDecimal convertTenYuan(Integer num, int point) {
        BigDecimal numBigDecimal = new BigDecimal(num == null ? 0 : num);
        return convertTenYuan(numBigDecimal, point);
    }

}
