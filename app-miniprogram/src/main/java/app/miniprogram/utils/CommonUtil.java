package app.miniprogram.utils;

import app.miniprogram.application.express.entity.TrajectoryEntity;
import app.miniprogram.enums.HttpEnums;
import app.miniprogram.redis.RedisClient;
import com.alibaba.fastjson.JSONObject;
import com.app.utils.http.ProxyInfo;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author :wkh.
 * @date :2019/8/9.
 */
public class CommonUtil {

    /**
     * 获取redis 代理ip集合
     */
    @SuppressWarnings("unchecked")
    public static List<ProxyInfo> getProxyInfoList(RedisClient redisClient) {
        Object proxyInfoObject = Optional.ofNullable(redisClient.get(HttpEnums.PROXY_KEY.getValue())).orElse(new ArrayList<>());
        return (List<ProxyInfo>) proxyInfoObject;
    }

    /**
     * 将物流轨迹转换成字符串
     *
     * @param entity 实体类
     * @return 字符串
     */
    public static String formatExpressData(TrajectoryEntity entity) {
        return Optional.ofNullable(entity).map(e -> JSONObject.toJSONString(e.getData())).orElse("");
    }

    /**
     * 将物流轨迹转换成实体类
     *
     * @param trajectory 字符串
     * @return 实体类
     */
    public static List<TrajectoryEntity.DataBean> parseExpressData(String trajectory) {
        try {
            return new JsonUtil().getCustomObjectMapper().readValue(trajectory, new TypeReference<List<TrajectoryEntity.DataBean>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("类型转换异常");
        }
    }
}
