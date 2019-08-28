package app.miniprogram.application.express.service.impl;

import app.miniprogram.application.constant.Constants;
import app.miniprogram.application.express.entity.ExpressTypeEntity;
import app.miniprogram.application.express.entity.TrajectoryEntity;
import app.miniprogram.application.express.service.Express;
import app.miniprogram.http.HttpProxyClient;
import app.miniprogram.utils.CommonUtil;
import app.miniprogram.utils.JsonUtil;
import com.alibaba.fastjson.JSONObject;
import com.app.redis.RedisClient;
import com.app.utils.http.*;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * 防止并线程出现问题，使用ThreadLocal
     */
    private ThreadLocal<HttpClientExtension> httpClientExtensionThreadLocal = new ThreadLocal<>();

    @Override
    public TrajectoryEntity queryExpress(String postId, String type) throws Exception {
        httpClientExtensionThreadLocal.set(httpProxyClient.getHttpProxy());
        try {
            log.info("====> 快递查询开始");
            return doQuery(postId, type);
        } catch (Exception e) {
            try {
                // 失败情况清除redis token再查询一次
                return queryAgain(postId, type);
            } catch (Exception ex) {
                // 如果再失败，则移除此代理
                httpProxyClient.removeTargetProxy(httpClientExtensionThreadLocal.get().getInUseProxy());
                throw new Exception();
            }
        } finally {
            log.info("<==== 快递查询结束");
            httpClientExtensionThreadLocal.remove();
        }
    }

    /**
     * 获取快递类型
     */
    @Override
    public List<ExpressTypeEntity> getExpressTypeList(String postId) throws IOException {

        log.info("查询快递类型");
        Map<String, String> param1 = new HashMap<>(4);
        param1.put("resultv2", "1");
        param1.put("text", postId);
        String ret = httpClientExtensionThreadLocal.get().post(this.apiExpressTypeUrl, param1);

        Map<String, Object> map = JSONObject.parseObject(ret);

        if (!map.containsKey("auto")) {
            throw new IllegalArgumentException("获取快递类型失败 结果：" + map);
        }
        ret = JSONObject.toJSONString(map.get("auto"));
        log.info("快递类型结果：" + ret);
        return new JsonUtil().getCustomObjectMapper().readValue(ret, new TypeReference<List<ExpressTypeEntity>>() {
        });
    }

    /**
     * 首次执行查询
     *
     * @param postId 快递单号
     * @param type   快递类型
     * @return 查询结果
     * @throws Exception 异常
     */
    private TrajectoryEntity doQuery(String postId, String type) throws Exception {
        // 如果用户选择了快递类型，不再去查询快递类型
        TrajectoryEntity result = StringUtils.isEmpty(type) ? queryAndGetType(postId) : queryByWeb(postId, type);
        // 校验是否查询成功
        checkResult(result);
        return result;
    }

    /**
     * 清除缓存再次查询
     *
     * @param postId 快递单号
     * @param type   快递类型
     * @return 查询结果
     * @throws Exception 异常
     */
    private TrajectoryEntity queryAgain(String postId, String type) throws Exception {
        log.info("首次查询失败，清除缓存再次查询");
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
    private TrajectoryEntity queryAndGetType(String postId) throws IOException {
        List<ExpressTypeEntity> typeList = getExpressTypeList(postId);
        return queryByWeb(postId, typeList.get(0).getComCode());
    }

    /**
     * 从web直接查询
     */
    private TrajectoryEntity queryByWeb(String postId, String type) throws IOException {
        log.info("执行查询 参数==> postId：" + postId + " type: " + type);

        Map<String, String> headers = new HashMap<>(16);
        headers.put("Cookie", getCookie(url));
        headers.put("Referer", url);
        headers.put("User-Agent", apiAgent);

        Map<String, String> param = new HashMap<>(16);
        param.put("postid", postId);
        param.put("type", type);
        param.put("temp", String.valueOf(Math.random()));
        param.put("phone", "");

        String ret = httpClientExtensionThreadLocal.get().get(apiUrl, param, headers);
        TrajectoryEntity trajectoryEntity = new JsonUtil().getCustomObjectMapper().readValue(ret, new TypeReference<TrajectoryEntity>() {
        });
        log.debug("查询结果为==> " + ret);
        return trajectoryEntity;
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
    private void checkResult(TrajectoryEntity result) throws Exception {
        if (result == null) {
            throw new Exception("查询失败 返回结果为空");
        }
        if (result.getData() == null || result.getData().size() == 0) {
            throw new Exception("查询结果异常，可能需要重新获取cookie 结果==> " + result);
        }
        if (!Constants.HTTP_OK.equals(result.getStatus())) {
            throw new Exception("查询结果异常，状态为 结果==> " + result);
        }

        boolean noResult = result.getData()
                .stream()
                .filter(event -> !StringUtils.isEmpty(event.getContext()))
                .map(TrajectoryEntity.DataBean::getContext)
                .collect(Collectors.joining())
                .contains("查无结果");
        if (noResult) {
            throw new Exception("查无结果 ==> " + result.toString());
        }
    }

    /**
     * 获取web的cookie
     */
    private String getCookiesInWeb(String url) {
        Map<String, String> cookiesMap = httpClientExtensionThreadLocal.get().getCookies(url);
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
