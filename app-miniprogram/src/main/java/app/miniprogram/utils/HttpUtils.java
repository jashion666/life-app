package app.miniprogram.utils;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.Args;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author :wkh.
 * @date :2019/5/17.
 */
@Slf4j
public class HttpUtils {

    public String doGet(String url) {
        try {
            return Request.Get(url).execute().returnContent().asString(Consts.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String doGet(String url, Map<String, String> paramMap) {
        return doGet(url, null, null, null, paramMap);
    }

    public String doGet(String url, String hostName, Integer port, String schemeName, Map<String, String> paramMap) {
        return executeGet(url, hostName, port, schemeName, paramMap);
    }

    private String executeGet(String url, String hostName, Integer port, String schemeName, Map<String, String> paramMap) {
        Args.notNull(url, "url");

        url = buildGetParam(url, paramMap);
        Request request = Request.Get(url);
        request = buildProxy(request, hostName, port, schemeName);
        try {
            return request.execute().returnContent().asString(Consts.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage(), e.toString());
        }
        return null;
    }

    private String buildGetParam(String url, Map<String, String> paramMap) {
        Args.notNull(url, "url");
        if (MapUtils.isNotEmpty(paramMap)) {
            List<NameValuePair> paramList = Lists.newArrayListWithCapacity(paramMap.size());
            for (String key : paramMap.keySet()) {
                paramList.add(new BasicNameValuePair(key, paramMap.get(key)));
            }
            //拼接参数
            url += "?" + URLEncodedUtils.format(paramList, Consts.UTF_8);
        }
        return url;
    }

    public String doPost(String url) {
        return doPost(url, null);
    }

    public String doPost(String url, List<NameValuePair> nameValuePairs) {
        return doPost(url, null, null, null, nameValuePairs, null);
    }

    public void doPost(String url, List<NameValuePair> nameValuePairs, List<File> files) {
        doPost(url, null, null, null, nameValuePairs, files);
    }

    public String doPost(String url, String hostName, Integer port, String schemeName, List<NameValuePair> nameValuePairs, List<File> files) {
        return executePost(url, hostName, port, schemeName, nameValuePairs, files);
    }

    private String executePost(String url, String hostName, Integer port, String schemeName, List<NameValuePair> nameValuePairs, List<File> files) {
        Args.notNull(url, "url");
        HttpEntity entity = buildPostParam(nameValuePairs, files);
        Request request = Request.Post(url).body(entity);
        request = buildProxy(request, hostName, port, schemeName);
        try {
            return request.execute().returnContent().asString(Consts.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage(), e.toString());
        }
        return null;
    }

    /**
     * 构建POST方法请求参数
     */
    private HttpEntity buildPostParam(List<NameValuePair> nameValuePairs, List<File> files) {
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
     * 设置代理
     */
    private Request buildProxy(Request request, String hostName, Integer port, String schemeName) {
        if (StringUtils.isNotEmpty(hostName) && port != null) {
            //设置代理
            if (StringUtils.isEmpty(schemeName)) {
                schemeName = HttpHost.DEFAULT_SCHEME_NAME;
            }
            request.viaProxy(new HttpHost(hostName, port, schemeName));
        }
        return request;
    }
}
