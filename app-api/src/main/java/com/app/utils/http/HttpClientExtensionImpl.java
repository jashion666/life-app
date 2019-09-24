package com.app.utils.http;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * http扩展类
 *
 * @author :wkh.
 * @date :2019/5/25.
 */
public class HttpClientExtensionImpl extends HttpClientImpl implements HttpClientExtension {

    public HttpClientExtensionImpl() {
    }

    public HttpClientExtensionImpl(ProxyInfo proxyInfo) {
        super(proxyInfo);
    }

    @Override
    public Map<String, String> getHeaders(String url) {
        Connection conn = Jsoup.connect(url);
        if (proxyInfo != null) {
            conn.proxy(proxyInfo.getIp(), proxyInfo.getPort());
        }
        Connection.Response resp;
        try {
            resp = conn.method(Connection.Method.GET).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>(16);
        }
        return resp.headers();
    }

    @Override
    public Map<String, String> getCookies(String url) {
        Connection conn = Jsoup.connect(url);
        if (proxyInfo != null) {
            conn.proxy(proxyInfo.getIp(), proxyInfo.getPort());
        }
        Connection.Response resp;
        try {
            resp = conn.method(Connection.Method.GET).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>(16);
        }
        return resp.cookies();
    }

    /**
     * 向目的URL发送post请求
     * @param url       目的url
     * @param params    发送的参数
     * @return  AdToutiaoJsonTokenData
     */
    @Override
    public String sendPostByRestTemplate(String url, MultiValueMap<String, String> params){
        RestTemplate client = new RestTemplate();
        //新建Http头，add方法可以添加参数
        HttpHeaders headers = new HttpHeaders();
        //设置请求发送方式
        HttpMethod method = HttpMethod.POST;
        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用String 类格式化（可设置为对应返回值格式的类）
        ResponseEntity<String> response = client.exchange(url, method, requestEntity,String .class);

        return response.getBody();
    }

}
