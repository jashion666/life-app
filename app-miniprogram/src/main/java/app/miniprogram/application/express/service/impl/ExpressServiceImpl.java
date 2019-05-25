package app.miniprogram.application.express.service.impl;

import app.miniprogram.application.express.service.ExpressService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.app.utils.http.HttpClient;
import com.app.utils.http.HttpClientExtension;
import com.app.utils.http.HttpClientExtensionImpl;
import com.app.utils.http.HttpClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author :wkh.
 * @date :2019/5/25.
 */
@Service
public class ExpressServiceImpl implements ExpressService {

    @Value("${kuaidi100.url}")
    private String url;

    @Value("${kuaidi100.user.agent}")
    private String apiAgent;

    @Value("${kuaidi100.api.url}")
    private String apiUrl;

    @Value("${kuaidi100.api.type.url}")
    private String apiExpressTypeUrl;

    @Value("${kuaidi100.cookie.key1}")
    private String csrfTokenKey;

    @Value("${kuaidi100.cookie.key2}")
    private String wwwIdKey;

    @Override
    public Map<String, Object> queryExpressByGateWay(String postId) {

        // TODO 需要申请api； 先调用快递web接口，如果失败去调用 申请的快递100api
        // TODO 每一个接口 都需要判断返回code
        String type = getExpressType(postId);
        return queryByWeb(postId, type);
    }

    private String getExpressType(String postId) {

        // TODO 需要改进
        HttpClient httpClient = new HttpClientImpl();
        Map<String, String> param1 = new HashMap<>(16);
        param1.put("resultv2", "1");
        param1.put("text", postId);
        String ret = httpClient.post(this.apiExpressTypeUrl, param1);
        Map<String, Object> map = JSONObject.parseObject(ret);
        JSONArray jsonArray = (JSONArray) map.get("auto");
        Map<String, String> map1 = (Map) jsonArray.get(0);
        return map1.get("comCode");
    }

    /**
     * 从web直接查询
     */
    private Map<String, Object> queryByWeb(String postId, String type) {

        Map<String, String> headers = new HashMap<>(16);
        headers.put("Cookie", getCookies(this.url));
        headers.put("Referer", this.url);
        headers.put("User-Agent", this.apiAgent);

        Map<String, String> param = new HashMap<>(16);
        param.put("postid", postId);
        param.put("type", type);
        param.put("temp", String.valueOf(Math.random()));
        param.put("phone", "");

        HttpClient httpClient = new HttpClientImpl();
        String ret = httpClient.get(this.apiUrl, param, headers);

        return JSONObject.parseObject(ret);
    }

    /**
     * 获取web的cookie
     */
    private String getCookies(String url) {
        // TODO 放到缓存中 过期时间为1天
        HttpClientExtension httpClientExtension = new HttpClientExtensionImpl();

        Map<String, String> cookies = httpClientExtension.getCookies(url);
        String csrfToken = cookies.get(this.csrfTokenKey);
        String wwwId = cookies.get(this.wwwIdKey);

        if (StringUtils.isEmpty(csrfToken) || StringUtils.isEmpty(wwwId)) {
            throw new IllegalStateException("cookie获取失败,请专用api方式获取");
        }

        return this.csrfTokenKey + "=" + csrfToken + ";" + this.wwwIdKey + "=" + wwwId;
    }
}
