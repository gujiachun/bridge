package com.rainbow.bridge.core.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 作用：生成短信验证码
 * 
 * @author gujiachun
 * @date 2020年2月15日 上午1:34:19
 */
public class VerificationCodeUtil {

	/**
	 * 数字
	 * */
	private static final String SYMBOLS = "0123456789";
	// 字符串
	// private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final Random RANDOM = new SecureRandom();

	/**
	 * 4位验证码
	 * @return String 验证码
	 */
	public static String getOpt4() {
		char[] nonceChars = new char[4];
		for (int index = 0; index < nonceChars.length; ++index) {
			nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
		}
		return new String(nonceChars);
	}
	
	/**
	 * 6位验证码
	 * @return String 验证码
	 */
	public static String getOpt6() {
		char[] nonceChars = new char[6];
		for (int index = 0; index < nonceChars.length; ++index) {
			nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
		}
		return new String(nonceChars);
	}
}
