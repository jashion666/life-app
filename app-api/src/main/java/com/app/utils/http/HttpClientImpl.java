package com.app.utils.http;

import com.app.enums.HttpStateEnums;
import org.apache.http.*;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.app.constant.CommonConstant.URL_PARAMETER_SEPARATOR;

/**
 * @author :wkh.
 * @date :2019/5/21.
 */
public class HttpClientImpl implements HttpClient {

    ProxyInfo proxyInfo;

    public HttpClientImpl() {
    }

    /**
     * 构建代理参数
     *
     * @param proxyInfo 代理实体类
     */
    public HttpClientImpl(ProxyInfo proxyInfo) {
        this.proxyInfo = proxyInfo;
    }

    @Override
    public String get(String url) {
        Request request = buildGet(url, null, null);
        return doExecute(request);
    }

    @Override
    public String get(String url, Map<String, String> paramMap) {
        Request request = buildGet(url, paramMap, null);
        return doExecute(request);
    }

    @Override
    public String get(String url, Map<String, String> paramMap, Map<String, String> headersMap) {
        Request request = buildGet(url, paramMap, headersMap);
        return doExecute(request);
    }

    @Override
    public String post(String url) {
        Request request = buildPost(url, null, null);
        return doExecute(request);
    }

    @Override
    public String post(String url, Map<String, String> paramMap) {
        Request request = buildPost(url, paramMap, null);
        return doExecute(request);
    }

    @Override
    public String post(String url, Map<String, String> paramMap, Map<String, String> headersMap) {
        Request request = buildPost(url, paramMap, headersMap);
        return doExecute(request);
    }

    @Override
    public ProxyInfo getInUseProxy() {
        return proxyInfo;
    }

    private Request buildGet(String url, Map<String, String> paramMap, Map<String, String> headersMap) {

        if (paramMap != null) {
            url = buildGetParam(url, paramMap);
        }
        Request request = Request.Get(url);
        // 设置代理
        buildProxy(request);
        // 构建请求头
        if (headersMap != null) {
            request.setHeaders(buildHeaders(headersMap));
        }
        return request;
    }

    private Request buildPost(String url, Map<String, String> paramMap, Map<String, String> headersMap) {

        Request request = Request.Post(url);
        // 设置代理
        buildProxy(request);
        // 构建请求头
        if (headersMap != null) {
            request.setHeaders(buildHeaders(headersMap));
        }
        // 构建请求参数
        if (paramMap != null) {
            request.bodyForm(buildPostParam(paramMap));
        }
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
        throw new IllegalArgumentException(HttpStateEnums.getValue(response.getStatusLine().getStatusCode()));
    }

    private HttpResponse execute(Request request) throws IOException {
        // 超时时间为4秒
        return request.connectTimeout(4000).execute().returnResponse();
    }

    /**
     * 构建请求参数
     */
    private List<NameValuePair> buildPostParam(Map<String, String> map) {
        Args.notNull(map, "params");
        List<NameValuePair> paramList = new ArrayList<>();
        map.forEach((key, value) -> paramList.add(new BasicNameValuePair(key, value)));
        return paramList;
    }

    /**
     * 构建请求参数
     */
    private String buildGetParam(String url, Map<String, String> map) {
        Args.notNull(map, "params");
        StringBuilder ret = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            ret.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        if (url.contains(URL_PARAMETER_SEPARATOR)) {
            return url + ret.toString();
        }
        return url + URL_PARAMETER_SEPARATOR + ret.toString();
    }

    /**
     * 构建POST方法请求参数
     */
    private HttpEntity buildFileParam(List<NameValuePair> nameValuePairs, List<File> files) {
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
        Args.notNull(map, "headers");
        Header[] headers = new Header[map.size()];
        List<Header> list = new ArrayList<>();
        map.forEach((k, v) -> list.add(new BasicHeader(k, v)));
        return list.toArray(headers);
    }

    /**
     * 设置代理
     */
    private void buildProxy(Request request) {
        if (proxyInfo == null) {
            return;
        }
        request.viaProxy(new HttpHost(proxyInfo.getIp(), proxyInfo.getPort(), proxyInfo.getScheme()));
    }

}
