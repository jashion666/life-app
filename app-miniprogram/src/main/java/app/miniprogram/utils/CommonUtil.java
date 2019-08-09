package app.miniprogram.utils;

import app.miniprogram.enums.HttpEnums;
import app.miniprogram.redis.RedisClient;
import com.app.utils.http.ProxyInfo;

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
}
