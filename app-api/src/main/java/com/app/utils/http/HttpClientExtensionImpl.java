package com.app.utils.http;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

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

    @Override
    public Map<String, String> getHeaders(String url) {
        Connection conn = Jsoup.connect(url);
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
        Connection.Response resp;
        try {
            resp = conn.method(Connection.Method.GET).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>(16);
        }
        return resp.cookies();
    }
}
