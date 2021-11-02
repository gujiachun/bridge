package com.rainbow.bridge.core;

/**
 * 结果响应码顶层接口，你可以实现此接口来自定义业务{@code ResultCode}
 *
 * @author fonlin
 * @date 2020/11/24
 */
public interface ResultCode {

    /**
     * 获得编码
     *
     * @return Integer
     */
    Integer getCode();

    /**
     * 获得编码
     *
     * @return String
     */
    String getMessage();

    ResultCode SUCCESS = new ResultCode() {
        @Override
        public Integer getCode() {
            return 0;
        }

        @Override
        public String getMessage() {
            return "SUCCESS";
        }
    };
}
