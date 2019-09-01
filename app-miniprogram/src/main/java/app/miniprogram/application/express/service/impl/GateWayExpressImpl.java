package app.miniprogram.application.express.service.impl;

import app.miniprogram.application.constant.Constants;
import app.miniprogram.application.express.entity.ExpressTypeEntity;
import app.miniprogram.application.express.entity.TrajectoryEntity;
import app.miniprogram.application.express.service.Express;
import app.miniprogram.http.HttpProxyClient;
import app.miniprogram.utils.JsonUtil;
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

    @Value("${kuaidi100.redis.key}")
    private String expressRedisKey;

    private final RedisClient redisClient;

    private final ExpressUtil expressUtil;

    private final HttpProxyClient httpProxyClient;

    /**
     * 防止并线程出现问题，使用ThreadLocal
     */
    private ThreadLocal<HttpClientExtension> httpClientExtensionThreadLocal = new ThreadLocal<>();

    @Override
    public TrajectoryEntity queryExpress(String postId, String type) throws Exception {
        // 获取http工具类
        httpClientExtensionThreadLocal.set(httpProxyClient.getHttpProxy());
        try {
            log.info("====> 网关快递查询开始");
            return doQuery(postId, type);
        } catch (Exception e) {
            return queryAgain(postId, type);
        } finally {
            log.info("<==== 网关快递查询结束");
            httpClientExtensionThreadLocal.remove();
        }
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
        redisClient.remove(expressRedisKey);
        try {
            return doQuery(postId, type);
        } catch (Exception ex) {
            httpProxyClient.removeTargetProxy(httpClientExtensionThreadLocal.get().getInUseProxy());
            throw new Exception();
        }
    }

    /**
     * 执行查询，并且获取快递公司类型集合
     *
     * @param postId 快递单号
     * @return 查询结果
     */
    private TrajectoryEntity queryAndGetType(String postId) throws IOException {
        List<ExpressTypeEntity> typeList = expressUtil.getTypeListByKd100(postId);
        return queryByWeb(postId, typeList.get(0).getComCode());
    }

    /**
     * 从web直接查询
     */
    private TrajectoryEntity queryByWeb(String postId, String type) throws IOException {
        log.info("执行查询 参数==> postId：" + postId + " type: " + type);

        Map<String, String> headers = new HashMap<>(16);
        headers.put("Cookie", expressUtil.getCookie(url));
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

    @Autowired
    public GateWayExpressImpl(RedisClient redisClient,
                              ExpressUtil expressUtil,
                              HttpProxyClient httpProxyClient) {
        this.redisClient = redisClient;
        this.expressUtil = expressUtil;
        this.httpProxyClient = httpProxyClient;
    }
}
