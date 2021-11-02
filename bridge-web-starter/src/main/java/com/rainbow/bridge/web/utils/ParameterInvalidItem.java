package com.rainbow.bridge.web.utils;

/**
 * @desc 参数无效项
 * 
 */
public class ParameterInvalidItem {

	/**
	 * 无效字段的名称
	 */
	private String fieldName;

	/**
	 * 错误信息
	 */
	private String message;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
