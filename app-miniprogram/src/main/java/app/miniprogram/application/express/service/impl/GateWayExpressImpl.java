package app.miniprogram.application.express.service.impl;

import app.miniprogram.application.constant.Constants;
import app.miniprogram.application.express.service.Express;
import app.miniprogram.http.HttpProxyClient;
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
 * 通过网关查询快递
 *
 * @author :wkh.
 * @date :2019/5/25.
 */
@Service("gateWayExpressImpl")
@Slf4j
public class GateWayExpressImpl implements Express {

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

    private final RedisClient redisClient;

    @Autowired
    private HttpProxyClient httpProxyClient;

    private HttpClient httpClient;

    @Override
    public Map<String, Object> queryExpress(String postId, String type) throws Exception {

        httpClient = httpProxyClient.getHttpProxy();
        try {
            return doQuery(postId, type);
        } catch (Exception e) {
            // 失败情况清除redis token再查询一次
            return queryAgain(postId, type);
        }
    }

    /**
     * 获取快递类型
     */
    @Override
    public Map<String, Object> getExpressTypeMap(String postId) {
        Map<String, String> param1 = new HashMap<>(16);
        param1.put("resultv2", "1");
        param1.put("text", postId);
        String ret = httpClient.post(this.apiExpressTypeUrl, param1);
        Map<String, Object> map = JSONObject.parseObject(ret);
        if (!map.containsKey("auto")) {
            throw new IllegalArgumentException("获取快递类型失败，请使用api方式获取 结果：" + map);
        }
        return map;
    }

    /**
     * 首次执行查询
     *
     * @param postId 快递单号
     * @param type   快递类型
     * @return 查询结果
     * @throws Exception 异常
     */
    private Map<String, Object> doQuery(String postId, String type) throws Exception {
        // 如果用户选择了快递类型，不再去查询快递类型
        Map<String, Object> resultMap = StringUtils.isEmpty(type) ? queryAndGetType(postId) : queryByWeb(postId, type);
        // 校验是否查询成功
        checkResult(resultMap);

        return resultMap;
    }

    /**
     * 清除缓存再次查询
     *
     * @param postId 快递单号
     * @param type   快递类型
     * @return 查询结果
     * @throws Exception 异常
     */
    private Map<String, Object> queryAgain(String postId, String type) throws Exception {
        log.info("=== 清除缓存再次查询");
        // 清除key再查询
        redisClient.remove(expressRedisKey);
        return doQuery(postId, type);
    }

    /**
     * 执行查询，并且获取快递公司类型集合
     *
     * @param postId 快递单号
     * @return 查询结果
     */
    private Map<String, Object> queryAndGetType(String postId) {
        log.info("=== 查询快递类型");
        Map<String, Object> typeMap = getExpressTypeMap(postId);
        String type = getExpressType(typeMap);
        Map<String, Object> result = queryByWeb(postId, type);
        // 返回快递类型集合到前台
        result.put("typeList", typeMap.get("auto"));
        return result;
    }

    /**
     * 从web直接查询
     */
    private Map<String, Object> queryByWeb(String postId, String type) {
        log.info("== web快递查询开始 参数==> postId：" + postId + " type: " + type);
        Map<String, Object> webResult;

        Map<String, String> headers = new HashMap<>(16);
        headers.put("Cookie", getCookie(url));
        headers.put("Referer", url);
        headers.put("User-Agent", apiAgent);

        Map<String, String> param = new HashMap<>(16);
        param.put("postid", postId);
        param.put("type", type);
        param.put("temp", String.valueOf(Math.random()));
        param.put("phone", "");

        String ret = httpClient.get(apiUrl, param, headers);
        log.debug("查询结果为==> " + ret);
        webResult = JSONObject.parseObject(ret);
        webResult.put("org", ret);
        return webResult;
    }

    private String getCookie(String url) {
        return Optional.ofNullable(getCookiesInRedis()).orElseGet(() -> getCookiesInWeb(url));
    }

    /**
     * 验证查询结果
     *
     * @param result 查询结果
     * @throws Exception 异常
     */
    private void checkResult(Map<String, Object> result) throws Exception {
        // TODO 需要完善
        if (result == null || result.size() == 0) {
            throw new Exception("查询失败 返回结果为空");
        }
        if (result.get("data") == null) {
            throw new Exception("查询结果异常，可能需要重新获取cookie 结果==> " + result);
        }
        if (!Constants.HTTP_OK.equals(result.get("status"))) {
            throw new Exception("查询结果异常，状态为 结果==> " + result);
        }
        if (String.valueOf(result.get("org")).contains("查无结果")) {
            throw new Exception("查无结果 ==> " + result);
        }
    }

    /**
     * 获取快递类型
     */
    @SuppressWarnings("unchecked")
    private String getExpressType(Map<String, Object> map) {

        JSONArray jsonArray = (JSONArray) map.get("auto");
        Map<String, String> map1 = (Map<String, String>) jsonArray.get(0);
        return map1.get("comCode");
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
            throw new IllegalArgumentException("cookie获取失败，请使用api方式获取");
        }

        return this.csrfTokenKey + "=" + csrfToken + ";" + this.wwwIdKey + "=" + wwwId;
    }

    @Autowired
    public GateWayExpressImpl(RedisClient redisClient) {
        this.redisClient = redisClient;
    }
}
