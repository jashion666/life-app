package com.app.utils.http;

import java.io.IOException;
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

}
