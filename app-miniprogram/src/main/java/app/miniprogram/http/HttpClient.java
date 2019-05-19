package app.miniprogram.http;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.http.*;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URLEncodedUtils;
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

/**
 * @author :wkh.
 * @date :2019/5/17.
 */
@Slf4j
public class HttpClient {

    public String doGet(String url) {
        return doGet(url, null);
    }

    public String doGet(String url, Map<String, String> paramMap) {
        return doGet(url, null, null, null, paramMap);
    }

    /**
     * 通过代理访问
     */
    public String doGet(String url, String hostName, Integer port, String schemeName, Map<String, String> paramMap) {
        return executeGet(url, hostName, port, schemeName, paramMap);
    }

    public String doPost(String url) {
        return executePost(url, null, null, null, null, null);
    }

    public String doPost(String url, Map<String, String> paramMap) {
        return doPost(url, null, null, null, buildPostFormParam(paramMap), null);
    }

    public String doPostForm(String url, Map<String, String> paramMap, Map<String, String> headers) {
        return executePostForm(url, buildPostFormParam(paramMap), headers);
    }

    public String doPost(String url, String hostName, Integer port, String schemeName, List<NameValuePair> nameValuePairs, List<File> files) {
        return executePost(url, hostName, port, schemeName, nameValuePairs, files);
    }

    private String executeGet(String url, String hostName, Integer port, String schemeName, Map<String, String> paramMap) {
        Args.notNull(url, "url");

        url = buildGetParam(url, paramMap);
        Request request = Request.Get(url);
        buildProxy(request, hostName, port, schemeName);
        try {
            HttpResponse response = request.execute().returnResponse();
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return HttpEnums.INTERNAL_ERROR.getValue();
        }
        return HttpEnums.PAGE_NOT_FOUND.getValue();
    }

    /**
     * 构建get参数.
     */
    private String buildGetParam(String url, Map<String, String> paramMap) {
        Args.notNull(url, "url");
        if (MapUtils.isNotEmpty(paramMap)) {
            List<NameValuePair> paramList = Lists.newArrayListWithCapacity(paramMap.size());
            for (String key : paramMap.keySet()) {
                paramList.add(new BasicNameValuePair(key, paramMap.get(key)));
            }
            url += "?" + URLEncodedUtils.format(paramList, Consts.UTF_8);
        }
        return url;
    }

    private String executePost(String url, String hostName, Integer port, String schemeName, List<NameValuePair> nameValuePairs, List<File> files) {
        Args.notNull(url, "url");
        HttpEntity entity = buildPostParam(nameValuePairs, files);
        Request request = Request.Post(url).body(entity);
        buildProxy(request, hostName, port, schemeName);
        try {
            return request.execute().returnContent().asString(Consts.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage(), e.toString());
        }
        return HttpEnums.PAGE_NOT_FOUND.getValue();
    }

    private String executePostForm(String url, List<NameValuePair> nameValuePairs, Map<String, String> headersMap) {
        Args.notNull(url, "url");

        try {
            Header[] headers = new Header[headersMap.size()];
            List<Header> list = new ArrayList<>();
            headersMap.forEach((k, v) -> list.add(new BasicHeader(k, v)));
            HttpResponse response = Request
                    .Post(url)
                    .setHeaders(list.toArray(headers))
                    .bodyForm(nameValuePairs)
                    .execute()
                    .returnResponse();
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return HttpEnums.INTERNAL_ERROR.getValue();
        }

        return HttpEnums.PAGE_NOT_FOUND.getValue();
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
     * 构建POST方法请求参数
     */
    private List<NameValuePair> buildPostFormParam(Map<String, String> paramMap) {
        List<NameValuePair> paramList = new ArrayList<>();
        paramMap.forEach((key, value) -> paramList.add(new BasicNameValuePair(key, value)));
        return paramList;
    }

    /**
     * 设置代理
     */
    private void buildProxy(Request request, String hostName, Integer port, String schemeName) {
        if (StringUtils.isNotEmpty(hostName) && port != null) {
            //设置代理
            if (StringUtils.isEmpty(schemeName)) {
                schemeName = HttpHost.DEFAULT_SCHEME_NAME;
            }
            request.viaProxy(new HttpHost(hostName, port, schemeName));
        }
    }
}
