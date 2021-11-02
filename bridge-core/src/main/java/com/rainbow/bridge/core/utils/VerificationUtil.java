package com.rainbow.bridge.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gujiachun
 * 验证工具类
 */
public class VerificationUtil {
	
	/**
     * 手机号码验证规则
     */
    public static final String PHONE_REGEXP = "^((\\+86)|(86))?[1][3456789][0-9]{9}$";

    /**
     * 姓名验证规则
     */
    public static final String NAME_REGEXP = "^[\\u4e00-\\u9fa5]*$";

    /**
     * 昵称验证规则
     */
    public static final String NICK_REGEXP = "^[\\u4e00-\\u9fa5_a-zA-Z0-9]*$";

    /**
     * 固定号码验证规则
     */
    public static final String FIXED_PHONE_REGEXP = "^0\\d{2,3}-[1-9]\\d{6,7}$";

    /**
     * 密码正则校验
     */
    public static final String PWD_REGEXP = "^[A-Za-z0-9.]{6,15}$";

    /**
     * 车牌号
     */
    public static final String CAR_NUMBER =
        "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{1}(([A-HJ-Z]{1}[A-HJ-NP-Z0-9]{5})|([A-HJ-Z]{1}(([DF]{1}[A-HJ-NP-Z0-9]{1}[0-9]{4})|([0-9]{5}[DF]{1})))|" + "([A-HJ-Z" + "]{1}[A-D0-9]{1}[0-9]{3}警)))|" +
            "([0-9]{6}使)|((([沪粤川云桂鄂陕蒙藏黑辽渝]{1}A)|鲁B|闽D|蒙E|蒙H)[0-9]{4}领)|(WJ[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼·•]{1}[0-9]{4}[TDSHBXJ0-9]{1})|" + "([VKHBSLJNGCE]{1}[A-DJ-PR" + "-TVY]{1}[0-9]{5})";

    /**
     * 日期年月日校验 yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_TIME = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9" +
        "]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s+([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";

    /**
     * 日期校验 yyyy-MM-dd
     */
    public static final String DATE = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))"
        + "|(02-(0[1-9]|[1][0-9]|2[0-8])))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)" + "([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9"
        + "][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|(" + "(([0-9]{2})(0[48]|[2468][048]|[13579][26])|(" + "(0[48" + "]|[2468][048]|[3579][26])00))-02-29)";

    /**
     * 年月校验 例: 2019-10
     */
    public static final String YEAR_MONTH = "^\\d{4}-((0([1-9]))|(1(0|1|2)))$";


    /**
     * 时间区间验证 10:23-19:00
     */
    public static final String TIME_SECTION= "^(0[0-9]|1[0-9]|2[0-3]):(0[0-9]|[1-5][0-9])-(0[0-9]|1[0-9]|2[0-3]):(0[0-9]|[1-5][0-9])$";

    /**
     * 时间验证 10:23
     */
    public static final String TIME = "^(0[0-9]|1[0-9]|2[0-3]):(0[0-9]|[1-5][0-9])$";

    /**
     * 身份证号
     */
    public static final String ID_CARD = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";

    /**
     * URL
     */
    public static final String URL = "[a-zA-z]+://[^\\s]*";

    /**
     * 邮箱
     */
    public static final String EMAIL = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";

    /**
     * 整数
     */
    public static final String INTEGER = "^-?[1-9]\\d*$";

    /**
     * 小数
     */
    public static final String DOUBLE = "^-?[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";
    
    /**
     * 验证方法
     * @param pattern 正则表达式
     * @param v 传入的值
     * @return 是否匹配
     */
    public static boolean valid(String pattern,String v) {
    	Pattern p= Pattern.compile(pattern);
    	Matcher m = p.matcher(v);
    	return m.matches();
    }
}
