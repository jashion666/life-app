package app.miniprogram.application.express.service.impl;

import app.miniprogram.application.constant.Constants;
import app.miniprogram.application.express.service.ExpressService;
import app.miniprogram.redis.RedisClient;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.app.utils.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author :wkh.
 * @date :2019/5/25.
 */
@Service
@Slf4j
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

    @Value("${kuaidi100.redis.key}")
    private String expressRedisKey;

    @Value("${kuaidi100.redis.expire.time}")
    private Long redisExpireTime;

    @Autowired
    private RedisClient redisClient;

    @Override
    public Map<String, Object> queryExpressByGateWay(String postId, String type) {

        // TODO 需要申请api； 先调用快递web接口，如果失败去调用 申请的快递100api
        log.info("== web快递查询开始");
        log.info("=== 快递单号" + postId);
        try {
            // 获取快递公司
            Map<String, Object> typeMap = getExpressTypeMap(postId);
            // 用户没有选择默认快递时，快递公司为第一个
            if (StringUtils.isEmpty(type)) {
                type = getExpressType(typeMap);
            }
            Map<String, Object> result = queryByWeb(postId, type);
            result.put("type", typeMap.get("auto"));
            log.info("=== 查询成功");
            log.info("== web快递查询结束");
            return result;
        } catch (Exception e) {
            log.info("=== web查询状态失败，查询结果为 ==" + e.getMessage());
        }
        // 调用api方式
        return queryByApi(postId);
    }

    /**
     * 获取快递类型
     */
    @Override
    public Map<String, Object> getExpressTypeMap(String postId) {
        HttpClient httpClient = new HttpClientImpl();
        Map<String, String> param1 = new HashMap<>(16);
        param1.put("resultv2", "1");
        param1.put("text", postId);
        String ret = httpClient.post(this.apiExpressTypeUrl, param1);
        return JSONObject.parseObject(ret);
    }

    private Map<String, Object> queryByApi(String postId) {
        log.info("== api查询开始");
        log.info("== api快递查询结束");
        return null;
    }

    /**
     * 从web直接查询
     */
    private Map<String, Object> queryByWeb(String postId, String type) throws Exception {

        Map<String, Object> webResult;
        HttpClient httpClient = new HttpClientImpl();

        Map<String, String> headers = new HashMap<>(16);
        headers.put("Cookie", getCookie(this.url));
        headers.put("Referer", this.url);
        headers.put("User-Agent", this.apiAgent);

        Map<String, String> param = new HashMap<>(16);
        param.put("postid", postId);
        param.put("type", type);
        param.put("temp", String.valueOf(Math.random()));
        param.put("phone", "");

        String ret = httpClient.get(this.apiUrl, param, headers);
        webResult = JSONObject.parseObject(ret);

        checkResult(webResult);

        return webResult;
    }

    private void checkResult(Map<String, Object> result) throws Exception {
        if (result == null || result.size() == 0) {
            throw new Exception("返回结果为空");
        }
        if (!Constants.EXPRESS_CHECK_OK.equals(result.get("ischeck"))) {
            throw new Exception("查询结果异常，可能需要重新获取cookie 结果==> " + result);
        }
        if (!Constants.HTTP_OK.equals(result.get("status"))) {
            throw new Exception("查询结果异常，状态为 结果==> " + result);
        }
    }

    /**
     * 获取快递类型
     */
    @SuppressWarnings("unchecked")
    private String getExpressType(Map<String, Object> map) {

        if (!map.containsKey("auto")) {
            throw new IllegalArgumentException("获取快递类型失败，使用api方式获取 ==>" + map);
        }
        JSONArray jsonArray = (JSONArray) map.get("auto");
        Map<String, String> map1 = (Map<String, String>) jsonArray.get(0);
        return map1.get("comCode");
    }

    private String getCookie(String url) {
        return Optional.ofNullable(getCookiesInRedis()).orElseGet(() -> getCookiesInWeb(url));
    }

    /**
     * 获取web的cookie
     */
    private String getCookiesInWeb(String url) {
        HttpClientExtension httpClientExtension = new HttpClientExtensionImpl();
        Map<String, String> cookiesMap = httpClientExtension.getCookies(url);
        String cookie = joinCookie(cookiesMap);
        log.info("redis 内的cookie过期 添加cookie的值到Redis中。");
        // redis存放时间为1小时
        this.redisClient.set(expressRedisKey, cookiesMap, redisExpireTime);
        return cookie;
    }

    /**
     * 获取web的cookie
     */
    @SuppressWarnings("unchecked")
    private String getCookiesInRedis() {
        Map<String, String> cookiesMap = (Map<String, String>) redisClient.get(expressRedisKey);
        if (cookiesMap == null || cookiesMap.size() == 0) {
            return null;
        }
        return joinCookie(cookiesMap);
    }

    private String joinCookie(Map<String, String> cookies) {
        String csrfToken = cookies.get(this.csrfTokenKey);
        String wwwId = cookies.get(this.wwwIdKey);

        if (StringUtils.isEmpty(csrfToken) || StringUtils.isEmpty(wwwId)) {
            throw new IllegalArgumentException("cookie获取失败,请通知管理员，或者使用api方式获取");
        }

        return this.csrfTokenKey + "=" + csrfToken + ";" + this.wwwIdKey + "=" + wwwId;
    }
}
