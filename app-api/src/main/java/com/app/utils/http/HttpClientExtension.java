package com.app.utils.http;

import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * http扩展接口
 *
 * @author :wkh.
 * @date :2019/5/25.
 */
public interface HttpClientExtension extends HttpClient {

    /**
     * 获取请求头
     *
     * @param url 地址
     * @return 所有请求头
     */
    Map<String, String> getHeaders(String url);

    /**
     * 获取cookie
     *
     * @param url 地址
     * @return cookie
     */
    Map<String, String> getCookies(String url);

    /**
     * RestTemplate 方式发送请求
     * @param url 地址
     * @param params 参数
     * @return 报文
     */
    String sendPostByRestTemplate(String url, MultiValueMap<String, String> params);

}
