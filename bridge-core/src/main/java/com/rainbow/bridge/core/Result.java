package com.rainbow.bridge.core;


import com.rainbow.bridge.core.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Optional;

/**
 * 统一的restful返回值
 * @author gujiachun
 * @date 2021-07-02
 */
public class Result<T> implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(Result.class);

    private static final long serialVersionUID = 2745191259961841129L;
    /**
     * 表示业务请求是否成功
     */
    private Boolean success;

    /**
     * 请求返回码
     */
    private Integer code;

    /**
     * 返回的错误信息
     */
    private String message;

    /**
     * 返回前端需要的json数据
     */
    private T data;

    public Result() {
        this.code = ResultCode.SUCCESS.getCode();
        this.message = ResultCode.SUCCESS.getMessage();
        this.success = true;
    }

    public Result(final Integer code, final String message, final T data,final Boolean success) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
    }

    public static <T> Result<T> success() {
        return success(ResultCode.SUCCESS, null, null);
    }

    public static <T> Result<T> success(T data) {
        return success(ResultCode.SUCCESS, null, data);
    }

    public static <T> Result<T> success(ResultCode rc, T data) {
        return success(rc, null, data);
    }

    public static <T> Result<T> success(ResultCode rc, String message, T data) {
        return new Result<T>(rc.getCode(), Optional.ofNullable(message).orElse(rc.getMessage()), data,Boolean.TRUE);
    }

    public static <T> Result<T> fail(BusinessException e) {
        return fail(e.getCode(), e.getMessage(), null);
    }

    public static <T> Result<T> fail(ResultCode rc) {
        return fail(rc.getCode(), rc.getMessage(), null);
    }

    public static <T> Result<T> fail(Integer code, String message) {
        return fail(code, message, null);
    }

    public static <T> Result<T> fail(ResultCode rc, String message) {
        return fail(rc.getCode(), message, null);
    }

    public static <T> Result<T> fail(Integer code, String message, T data) {
        return new Result<T>(code, message, data,Boolean.FALSE);
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public T getData() {
        return this.data;
    }

    public void setCode(final Integer code) {
        this.code = code;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
