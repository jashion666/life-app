package app.miniprogram.application.express.service.impl;

import app.miniprogram.application.express.entity.ExpressTypeEntity;
import app.miniprogram.utils.JsonUtil;
import com.alibaba.fastjson.JSONObject;
import com.app.redis.RedisClient;
import com.app.utils.http.HttpClient;
import com.app.utils.http.HttpClientExtension;
import com.app.utils.http.HttpClientExtensionImpl;
import com.app.utils.http.HttpClientImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author :wkh.
 * @date :2019/8/31.
 */
@Slf4j
@Service
public class ExpressUtil {

    @Value("${kuaidi100.url}")
    private String url;

    @Value("${kuaidi100.user.agent}")
    private String apiAgent;

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
    public ExpressUtil(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    List<ExpressTypeEntity> getTypeListByKd100(String postId) throws IOException {
        Map<String, String> param1 = new HashMap<>(4);
        param1.put("resultv2", "1");
        param1.put("text", postId);
        Map<String, String> headers = new HashMap<>(4);
        headers.put("Cookie", getCookie(url));
        headers.put("Referer", url);
        headers.put("User-Agent", apiAgent);
        HttpClient httpClient = new HttpClientImpl();
        String ret = httpClient.post(this.apiExpressTypeUrl, param1, headers);

        Map<String, Object> map = JSONObject.parseObject(ret);

        // 如果快递类型查询失败，直接返回
        if (!map.containsKey("auto")) {
            log.error("获取快递类型失败 结果：" + map);
            throw new IllegalArgumentException();
        }
        ret = JSONObject.toJSONString(map.get("auto"));
        log.info("快递类型结果：" + ret);
        return new JsonUtil().getCustomObjectMapper().readValue(ret, new TypeReference<List<ExpressTypeEntity>>() {
        });
    }

    String getCookie(String url) {
        return Optional.ofNullable(getCookiesInRedis()).orElseGet(() -> getCookiesInWeb(url));
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
            throw new IllegalArgumentException("cookie获取失败");
        }

        return this.csrfTokenKey + "=" + csrfToken + ";" + this.wwwIdKey + "=" + wwwId;
    }

    /**
     * 获取web的cookie
     */
    private String getCookiesInWeb(String url) {
        HttpClientExtension httpClient = new HttpClientExtensionImpl();
        Map<String, String> cookiesMap = httpClient.getCookies(url);
        String cookie = joinCookie(cookiesMap);
        log.info("redis 内的cookie过期 添加cookie的值到Redis中。");
        // redis存放时间为1小时
        this.redisClient.set(expressRedisKey, cookiesMap, redisExpireTime);
        return cookie;
    }


}
