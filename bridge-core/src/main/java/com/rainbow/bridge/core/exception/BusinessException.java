package com.rainbow.bridge.core.exception;


import com.rainbow.bridge.core.ResultCode;

/**
 * 业务异常类
 *
 * @author fonlin
 * @date 2020/11/24
 */
public class BusinessException extends RuntimeException {

    private Integer code;

    public BusinessException(ResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getMessage(), null);
    }

    public BusinessException(ResultCode resultCode, Throwable cause) {
        this(resultCode.getCode(), resultCode.getMessage(), cause);
    }

    public BusinessException(ResultCode resultCode, String message) {
        this(resultCode.getCode(), message, null);
    }

    public BusinessException(ResultCode resultCode, String message, Throwable cause) {
        this(resultCode.getCode(), message, cause);
    }

    public BusinessException(Integer code, String message) {
        this(code, message, null);
    }

    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
