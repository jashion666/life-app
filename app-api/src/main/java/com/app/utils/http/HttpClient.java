package com.app.utils.http;

import java.util.Map;

/**
 * http 工具类接口
 *
 * @author :wkh.
 * @date :2019/5/21.
 */
public interface HttpClient {

    /**
     * 无参数get请求
     *
     * @param url 请求地址
     * @return 报文
     */
    String get(String url);

    /**
     * 有参数get请求
     *
     * @param url      请求地址
     * @param paramMap 请求参数
     * @return 报文
     */
    String get(String url, Map<String, String> paramMap);

    /**
     * 有参数get请求
     *
     * @param url        请求地址
     * @param paramMap   请求参数
     * @param headersMap 请求头
     * @return 报文
     */
    String get(String url, Map<String, String> paramMap, Map<String, String> headersMap);

    /**
     * 无参数post请求
     *
     * @param url 请求地址
     * @return 报文
     */
    String post(String url);

    /**
     * 有参数post请求
     *
     * @param url      请求地址
     * @param paramMap 请求参数
     * @return 报文
     */
    String post(String url, Map<String, String> paramMap);

    /**
     * 有参数post请求
     *
     * @param url        请求地址
     * @param paramMap   请求参数
     * @param headersMap 请求头
     * @return 报文
     */
    String post(String url, Map<String, String> paramMap, Map<String, String> headersMap);

}
