package com.app.utils.http;

import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author :wkh.
 * @date :2019/5/21.
 */
public class HttpClientImpl implements HttpClient {

    private String proxyHostName;
    private String proxySchemeName;
    private Integer proxyPort;

    public HttpClientImpl() {
    }

    /**
     * 构建代理参数
     *
     * @param hostName   代理host
     * @param port       代理端口
     * @param schemeName schemeName 默认为 http
     */
    public HttpClientImpl(String hostName, Integer port, String schemeName) {
        this.proxyHostName = hostName;
        this.proxyHostName = schemeName;
        this.proxyPort = port;
    }

    @Override
    public String get(String url) {
        Request request = buildRequest(Request.Get(url), null, null);
        return doExecute(request);
    }

    @Override
    public String get(String url, Map<String, String> paramMap) {
        Request request = buildRequest(Request.Get(url), paramMap, null);
        return doExecute(request);
    }

    @Override
    public String get(String url, Map<String, String> paramMap, Map<String, String> headersMap) {
        Request request = buildRequest(Request.Get(url), paramMap, headersMap);
        return doExecute(request);
    }

    @Override
    public String post(String url) {
        Request request = buildRequest(Request.Post(url), null, null);
        return doExecute(request);
    }

    @Override
    public String post(String url, Map<String, String> paramMap) {
        Request request = buildRequest(Request.Post(url), paramMap, null);
        return doExecute(request);
    }

    @Override
    public String post(String url, Map<String, String> paramMap, Map<String, String> headersMap) {
        Request request = buildRequest(Request.Post(url), paramMap, headersMap);
        return doExecute(request);
    }

    private Request buildRequest(Request request, Map<String, String> paramMap, Map<String, String> headersMap) {

        // 构建请求参数
        List<NameValuePair> params = buildParam(paramMap);
        // 构建请求头
        Header[] headers = buildHeaders(headersMap);
        // 设置代理
        buildProxy(request);

        request.setHeaders(headers).bodyForm(params);

        return request;
    }

    private String doExecute(Request request) {
        HttpResponse response;
        try {
            response = execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
        return HttpEnums.getValue(response.getStatusLine().getStatusCode());
    }

    private HttpResponse execute(Request request) throws IOException {
        return request.execute().returnResponse();
    }

    /**
     * 构建请求参数
     */
    private List<NameValuePair> buildParam(Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return null;
        }
        List<NameValuePair> paramList = new ArrayList<>();
        map.forEach((key, value) -> paramList.add(new BasicNameValuePair(key, value)));
        return paramList;
    }

    /**
     * 构建POST方法请求参数
     */
    private HttpEntity buildParam(List<NameValuePair> nameValuePairs, List<File> files) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        for (File file : files) {
            builder.addBinaryBody(file.getName(), file, ContentType.APPLICATION_OCTET_STREAM, file.getName());
        }
        for (NameValuePair nameValuePair : nameValuePairs) {
            //设置ContentType为UTF-8,默认为text/plain; charset=ISO-8859-1,传递中文参数会乱码
            builder.addTextBody(nameValuePair.getName(), nameValuePair.getValue(), ContentType.create("text/plain", Consts.UTF_8));
        }
        return builder.build();
    }

    /**
     * 构建请求头
     */
    private Header[] buildHeaders(Map<String, String> map) {

        if (map == null || map.size() == 0) {
            return null;
        }
        Header[] headers = new Header[map.size()];
        List<Header> list = new ArrayList<>();
        map.forEach((k, v) -> list.add(new BasicHeader(k, v)));
        return list.toArray(headers);
    }

    /**
     * 设置代理
     */
    private void buildProxy(Request request) {
        if (StringUtils.isEmpty(proxyHostName) || proxyPort == null) {
            return;
        }
        if (StringUtils.isEmpty(proxySchemeName)) {
            proxySchemeName = HttpHost.DEFAULT_SCHEME_NAME;
        }
        request.viaProxy(new HttpHost(proxyHostName, proxyPort, proxySchemeName));
    }
}
