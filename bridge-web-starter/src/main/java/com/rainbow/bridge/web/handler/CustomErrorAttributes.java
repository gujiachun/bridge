package com.rainbow.bridge.web.handler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义错误返回属性
 * @author gujiachun
 */
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String,Object> map = super.getErrorAttributes(webRequest, options);
        Integer status = Integer.valueOf(map.get("status").toString());
        Map<String,Object> map1 = new HashMap<>(5);
        map1.put("code",status);
        map1.put("success",false);

        String msg;
        if (status.equals(HttpStatus.UNAUTHORIZED.value())) {
            msg = "未认证，请先登录";
        }else if (status.equals(HttpStatus.FORBIDDEN.value())) {
            msg = "没有相关权限";
        }else if (status.equals(HttpStatus.NOT_FOUND.value())) {
            msg = "请求资源不存在";
        }else {
            msg = map.get("message").toString();
            if (StringUtils.isBlank(msg)){
                msg = map.get("error").toString();
            }
        }
        map1.put("message",msg);
        return map1;
    }
}
