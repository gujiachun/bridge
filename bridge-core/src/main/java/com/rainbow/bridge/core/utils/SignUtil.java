package com.rainbow.bridge.core.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

/**
 * @author gujiachun
 */
public class SignUtil {

	/**
	 * 生成签名
	 *@author gujiachun
	 *@date 2021/9/18 4:14 下午
	 *@param data 拼接成的数据
	 *@param signKey 颁发的秘钥
	 *@return java.lang.String
	*/
	public static String createSign(String data,String signKey) {
		try{
			SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(signKey),"HmacSHA256");
			Mac mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);
			byte[] bytes = mac.doFinal(data.getBytes());
			String toSign = Base64.getEncoder().encodeToString(bytes);
			return toSign;
		}catch (Exception e){

		}
		return null;
	}

	/**
	 * 对请求体 进行MD5加密
	 *@author gujiachun
	 *@date 2021/9/18 3:18 下午
	 *@param requestBody 对应@RequestBody
	 *@param requestParams 对应@RequestParams
	 *@param paths 对应@PathVariable
	 *@return java.lang.String
	*/
	public static String contentMd5(String requestBody, Map<String, String[]> requestParams, String[] paths) {
		if (StringUtils.isBlank(requestBody) && CollectionUtils.isEmpty(requestParams) && ArrayUtils.isEmpty(paths)){
			return "";
		}

		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(requestBody)) {
			sb.append(requestBody).append('#');
		}

		if (!CollectionUtils.isEmpty(requestParams)) {
			//需要根据param 中的key进行排序，在拼接字符串
			requestParams.entrySet()
					.stream()
					.sorted(Map.Entry.comparingByKey())
					.forEach(paramEntry -> {
						String paramValue = String.join(",", Arrays.stream(paramEntry.getValue()).sorted().toArray(String[]::new));
						sb.append(paramEntry.getKey()).append("=").append(paramValue).append('#');
					});
		}

		if (ArrayUtils.isNotEmpty(paths)) {
			String pathValues = String.join(",", Arrays.stream(paths).sorted().toArray(String[]::new));
			sb.append(pathValues).append('#');
		}

		return Md5Util.encode(sb.toString());
	}
}
