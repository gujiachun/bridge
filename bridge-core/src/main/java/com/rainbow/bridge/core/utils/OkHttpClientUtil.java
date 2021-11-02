package com.rainbow.bridge.core.utils;

import cn.hutool.core.map.MapUtil;
import com.rainbow.bridge.core.Result;
import com.rainbow.bridge.core.ResultEnum;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * ok http 请求工具类
 * @author gujiachun
 */
public class OkHttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(OkHttpClientUtil.class);

    public static OkHttpClient client = null;

    private static String ENCODING = "UTF-8";

    static {
        TrustManager[] trustManagers = buildTrustManagers();
        client = new OkHttpClient.Builder()
                .connectTimeout(3L, TimeUnit.SECONDS)
                .readTimeout(5L, TimeUnit.SECONDS)
                .writeTimeout(5L, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(20, 5, TimeUnit.MINUTES))
                .sslSocketFactory(createSSLSocketFactory(trustManagers), (X509TrustManager) trustManagers[0])
                .retryOnConnectionFailure(true)
                .build();
    }

    /**
     * 生成安全套接字工厂，用于https请求的证书跳过
     *
     * @return
     */
    private static SSLSocketFactory createSSLSocketFactory(TrustManager[] trustAllCerts) {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssfFactory;
    }

    private static TrustManager[] buildTrustManagers() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
    }

    /**
     * 根据map获取get请求参数
     * @param queries
     * @return
     */
    public static StringBuffer getQueryString(String url,Map<String,String> queries){
        StringBuffer sb = new StringBuffer(url);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            Iterator iterator = queries.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry<String, String>) iterator.next();
                if (firstFlag) {
                    sb.append("?" + entry.getKey() + "=" + entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }
        return sb;
    }

    /**
     * 调用okhttp的newCall方法
     * @param request
     * @return
     */
    private static Result<String> execNewCall(Request request){
        Response response = null;
        try {
            response = client.newCall(request).execute();
            int status = response.code();
            if (response.isSuccessful()) {
                return Result.success(response.body().string());
            }else{
                logger.error("请求URI:{},请求方式:{},请求参数:{},请求失败 status:{},e:{}",request.url().toString(),request.method(),request.body(),status,response.message());
                return Result.fail(status,response.message());
            }
        } catch (Exception e) {
            logger.error("请求异常 >> ex = {}", ExceptionUtils.getStackTrace(e));
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return Result.fail(ResultEnum.HTTP_CLIENT_ERROR,"调用接口异常:" + request.url().toString());
    }

    /**
     * Set Header
     * @param headers 请求头数据
     */
    private static Headers getHeaders(Map<String, String> headers){
        if(headers != null && headers.keySet().size() >0){
            return Headers.of(headers);
        }
        return  new Headers.Builder().build();
    }

    /**
     * Content-Type: application/x-www-from-urlencoded
     *
     * Description: 封装请求参数
     * @param params 参数
     *
     */
    private FormBody getFormBody(Map<String, String> params) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder(Charset.forName(ENCODING));
        // 封装请求参数
        if (MapUtil.isNotEmpty(params)) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        return  formBodyBuilder.build();
    }

    /**
     * get请求
     *@author gujiachun
     *@date 2021/9/7 5:06 下午
     *@param url
     *@param headers 请求头
     *@param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     *@return com.lixiang.common.core.Result<java.lang.String>
     */
    public static Result<String> get(String url, Map<String, String> headers, Map<String, String> queries) {
        StringBuffer sb = getQueryString(url,queries);
        Request request = new Request.Builder()
                .url(sb.toString())
                .headers(getHeaders(headers))
                .build();
        return execNewCall(request);
    }

    /**
     * post form 提交的参数
     *@author gujiachun
     *@date 2021/9/7 5:06 下午
     *@param url 请求url
     *@param headers 请求头
     *@param params post form 提交的参数
     *@return com.lixiang.common.core.Result<java.lang.String>
     */
    public static Result<String> postFormParams(String url,Map<String, String> headers, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .headers(getHeaders(headers))
                .post(builder.build())
                .build();
        return execNewCall(request);
    }


    /**
     * Post请求发送JSON数据....{"name":"zhangsan","pwd":"123456"}
     *@author gujiachun
     *@date 2021/9/7 5:05 下午
     *@param url 请求url
     *@param headers 请求头
     *@param jsonParams 请求参数 json字符串 {"name":"zhangsan","pwd":"123456"}
     *@return com.lixiang.common.core.Result<java.lang.String>
     */
    public static Result<String> postJsonParams(String url, Map<String, String> headers,String jsonParams) {
        RequestBody requestBody;
        if (StringUtils.isNotBlank(jsonParams)){
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);
        }else{
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "");
        }
        Request request = new Request.Builder()
                .url(url)
                .headers(getHeaders(headers))
                .post(requestBody)
                .build();
        return execNewCall(request);
    }

    /**
     * Put请求发送JSON数据....{"name":"zhangsan","pwd":"123456"}
     *@author gujiachun
     *@date 2021/9/7 5:04 下午
     *@param url 请求url
     *@param headers 请求头
     *@param jsonParams 请求参数，json字符串 {"name":"zhangsan","pwd":"123456"}
     *@return com.lixiang.common.core.Result<java.lang.String>
     */
    public static Result<String> putJsonParams(String url, Map<String, String> headers,String jsonParams) {
        RequestBody requestBody;
        if (StringUtils.isNotBlank(jsonParams)){
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);
        }else{
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "");
        }
        Request request = new Request.Builder()
                .url(url)
                .headers(getHeaders(headers))
                .put(requestBody)
                .build();
        return execNewCall(request);
    }

    /**
     * delete请求
     *@author gujiachun
     *@date 2021/9/7 5:04 下午
     *@param url 请求url
     *@param headers 请求头
     *@return com.lixiang.common.core.Result<java.lang.String>
     */
    public static Result<String> delete(String url,Map<String, String> headers) {
        Request request = new Request.Builder()
                .url(url)
                .headers(getHeaders(headers))
                .delete()
                .build();
        return execNewCall(request);
    }

    /**
     * Post请求发送xml数据....
     * 参数一：请求Url
     * 参数二：请求的xmlString
     * 参数三：请求回调
     */
    public static Result<String> postXmlParams(String url, Map<String, String> headers,String xml) {
        RequestBody requestBody;
        if (StringUtils.isNotBlank(xml)){
            requestBody = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), xml);
        }else{
            requestBody = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), "");
        }
        Request request = new Request.Builder()
                .url(url)
                .headers(getHeaders(headers))
                .post(requestBody)
                .build();
        return execNewCall(request);
    }
}
